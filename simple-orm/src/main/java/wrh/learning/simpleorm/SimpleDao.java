package wrh.learning.simpleorm;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import wrh.learning.simpleorm.util.NamingUtils;
import wrh.learning.simpleorm.util.ReflectionUtils;
import wrh.learning.simpleorm.util.TypeUtils;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class SimpleDao {

    //TODO: Only SimpleRecord or Table annotated type supported

    //region delete

    public static int deleteAll(Class<?> table, String whereClause, String...args) {
        return openDb().delete(NamingUtils.toTableName(table), whereClause, args);
    }

    public static int deleteAll(Class<?> table) {
        return deleteAll(table, null);
    }

    public static boolean delete(Class<?> table, Long id) {
        if (id != null && id > 0) {
            int count = deleteAll(table, "ID=?", Long.toString(id));
            return count > 0;
        }
        return false;
    }

    public static boolean delete(Object o) {
        return delete(o.getClass(), getId(o));
    }

    //endregion

    //region insert/update

    public static long save(Object o) {
        Class<?> table = o.getClass();
        List<Field> columns = ReflectionUtils.getTableFields(table);
        ContentValues values = new ContentValues(columns.size());
        for (Field column : columns) {
            TypeUtils.addFieldValueToColumn(values, o, column);
        }
        long id = openDb().insertWithOnConflict(NamingUtils.toTableName(table),
                null, values, SQLiteDatabase.CONFLICT_REPLACE);
        setId(o, id);
        return id;
    }

    public static long update(Object o) {
        Class<?> table = o.getClass();
        List<Field> columns = ReflectionUtils.getTableFields(table);
        ContentValues values = new ContentValues(columns.size());
        for (Field column : columns) {
            String columnName = NamingUtils.toColumnName(column);
            if (!"id".equalsIgnoreCase(columnName)) {
                TypeUtils.addFieldValueToColumn(values, o, column);
            }
        }
        long rowsAffected = openDb().update(NamingUtils.toTableName(table),
                values, "ID=?", new String[] { Long.toString(getId(o)) });
        if (rowsAffected == 0) {
            save(o);
        }
        return rowsAffected;
    }

    //endregion

    //region query

    public static <T> T fetch(Class<T> type, Long id) {
        List<T> list = find(type, "ID=?", Long.toString(id));
        if (list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static <T> List<T> query(Class<T> type, String sql, String...args) {
        Cursor cursor = openDb().rawQuery(sql, args);
        return getEntitiesFromCursor(cursor, type);
    }

    public static <T> List<T> find(Class<T> type, String whereClause, String...args) {
        return find(type, whereClause, args, null, null, null, null);
    }

    public static <T> List<T> find(Class<T> type, String whereClause, String[] whereArgs,
                                    String groupBy, String having, String orderBy, String limit) {
        String args[] = whereArgs == null ? null : replaceArgs(whereArgs);
        Cursor cursor = openDb().query(NamingUtils.toTableName(type), null,
                whereClause, args, groupBy, having, orderBy, limit);
        return getEntitiesFromCursor(cursor, type);
    }

    public static <T> List<T> getEntitiesFromCursor(Cursor cursor, Class<T> type) {
        List<T> result = new ArrayList<>();
        try {
            Constructor<T> constructor = type.getDeclaredConstructor();
            constructor.setAccessible(true);
            while (cursor.moveToNext()) {
                T entity = constructor.newInstance();
                parseEntityFromCursor(cursor, type, entity);
                result.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return result;
    }

    static <T> void parseEntityFromCursor(Cursor cursor, Class<T> type, T instance) {
        List<Field> fields = ReflectionUtils.getTableFields(type);
        for (Field field : fields) {
            TypeUtils.getFieldValueFromColumn(cursor, field, instance);
        }
    }

    //endregion

    //region stat

    public static long count(Class<?> table) {
        return count(table, null);
    }

    public static long count(Class<?> table, String whereClause, String...whereArgs) {
        String filter = TextUtils.isEmpty(whereClause) ? "" : " WHERE " + whereClause;
        SQLiteStatement statement;
        try {
            statement = openDb().compileStatement("SELECT COUNT(*) FROM " + NamingUtils.toTableName(table) + filter);
        } catch (SQLiteException e) {
            e.printStackTrace();
            return -1;
        }

        if (whereArgs != null) {
            for (int i = 0; i < whereArgs.length; i++) {
                statement.bindString(i, whereArgs[i]);
            }
        }

        try {
            return statement.simpleQueryForLong();
        } finally {
            statement.close();
        }
    }

    //endregion

    public static void exec(String sql, String...args) {
        openDb().execSQL(sql, args);
    }

    private static Long getId(Object o) {
        Class<?> type = o.getClass();

        if (SimpleRecord.class.isAssignableFrom(type)) {
            return ((SimpleRecord)o).getId();
        }

        try {
            Field field = type.getDeclaredField("id");
            field.setAccessible(true);
            return field.getLong(o);
        } catch (Exception e) {
            throw new InvalidTypeException(type, e);
        }
    }

    private static void setId(Object o, Long id) {
        Class<?> type = o.getClass();

        if (SimpleRecord.class.isAssignableFrom(type)) {
            ((SimpleRecord)o).setId(id);
            return;
        }

        try {
            Field field = type.getDeclaredField("id");
            field.setAccessible(true);
            field.set(o, id);
        } catch (Exception e) {
            throw new InvalidTypeException(type, e);
        }
    }

    private static SQLiteDatabase openDb() {
        return SimpleContext.getDb().open();
    }

    private static String[] replaceArgs(String[] args) {
        String[] replace = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            replace[i] = arg.equals("true") ? "1" : (arg.equals("false") ? "0" : arg);
        }
        return replace;
    }

    static class SimpleIterator<E> implements Iterator<E> {

        private final Class<E> type;
        private final Cursor cursor;

        SimpleIterator(Class<E> type, Cursor cursor) {
            this.type = type;
            this.cursor = cursor;
        }

        @Override
        public boolean hasNext() {
            return cursor != null && !cursor.isClosed() && !cursor.isAfterLast();
        }

        @Override
        public E next() {
            if (cursor == null || cursor.isAfterLast()) {
                throw new NoSuchElementException();
            }

            if (cursor.isBeforeFirst()) {
                cursor.moveToFirst();
            }

            try {
                E entity = type.getDeclaredConstructor().newInstance();
                parseEntityFromCursor(cursor, type, entity);
                return entity;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                cursor.moveToNext();
                if (cursor.isAfterLast()) {
                    cursor.close();
                }
            }
        }
    }

}
