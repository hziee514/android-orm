package wrh.learning.simpleorm;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wrh.learning.simpleorm.annotation.Column;
import wrh.learning.simpleorm.annotation.MultiUnique;
import wrh.learning.simpleorm.annotation.NotNull;
import wrh.learning.simpleorm.annotation.Unique;
import wrh.learning.simpleorm.util.KeywordUtils;
import wrh.learning.simpleorm.util.MigrationParser;
import wrh.learning.simpleorm.util.NamingUtils;
import wrh.learning.simpleorm.util.TypeUtils;
import wrh.learning.simpleorm.util.ReflectionUtils;

import static wrh.learning.simpleorm.SimpleContext.TAG;

/**
 * @author bruce.wu
 * @date 2018/6/6
 */
public class SchemaBuilder {

    //TODO: Only SimpleRecord or Table annotated type supported

    private final List<Class<?>> domainClasses;

    SchemaBuilder(List<Class<?>> domainClasses) {
        this.domainClasses = Collections.unmodifiableList(domainClasses);
    }

    public void createDatabase(SQLiteDatabase db) {
        for (Class<?> domainClass : domainClasses) {
            createTable(db, domainClass);
        }
        execScript(db, "scripts/create.sql");
    }

    public void upgradeDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "select count(*) from sqlite_master where type='table' and name=?;";
        for (Class<?> domainClass : domainClasses) {
            String tableName = NamingUtils.toTableName(domainClass);
            Cursor c = db.rawQuery(sql, new String[] {tableName});
            if (c.moveToFirst() && c.getInt(0) == 0) {
                createTable(db, domainClass);
            } else {
                addColumns(db, domainClass);
            }
            c.close();
        }
        for (int i = oldVersion + 1; i <= newVersion; i++) {
            execScript(db, "scripts/" + i + ".sql");
        }
    }

    public void deleteTables(SQLiteDatabase db) {
        for (Class<?> domainClass : domainClasses) {
            deleteTable(db, domainClass);
        }
    }

    public static void deleteTable(SQLiteDatabase db, Class<?> table) {
        db.execSQL("DROP TABLE IF EXISTS " + NamingUtils.toTableName(table));
    }

    private static void execScript(SQLiteDatabase db, String scriptFile) {
        String content = readScriptFile(scriptFile);
        if (content == null) {
            return;
        }

        MigrationParser parser = new MigrationParser(content);
        for (String statement : parser.getStatements()) {
            if (!statement.isEmpty()) {
                db.execSQL(statement);
            }
        }
    }

    private static String readScriptFile(String scriptFile) {
        InputStream is = null;
        try {
            is = SimpleContext.getAssets().open(scriptFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e(TAG, "readScriptFile: " + e.getMessage());
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void addColumns(SQLiteDatabase db, Class<?> table) {
        List<Field> columnFields = ReflectionUtils.getTableFields(table);
        String tableName = NamingUtils.toTableName(table);
        List<String> existColumns = getColumnNames(db, tableName);
        List<String> alterCommands = new ArrayList<>();

        for (Field field : columnFields) {
            String columnName = NamingUtils.toColumnName(field);
            String columnType = TypeUtils.toColumnType(field.getType());

            if (existColumns.contains(columnName)) {
                continue;
            }

            boolean notNull = false;
            if (field.isAnnotationPresent(Column.class)) {
                Column annotation = field.getAnnotation(Column.class);
                notNull = annotation.notNull();
            }
            if (field.isAnnotationPresent(NotNull.class)) {
                notNull = true;
            }

            StringBuilder sb = new StringBuilder("ALTER TABLE ");
            sb.append(tableName).append(" ADD COLUMN ").append(columnName).append(" ").append(columnType);
            if (notNull) {
                sb.append(" NOT");
            }
            sb.append(" NULL");
            alterCommands.add(sb.toString());
        }

        for (String command : alterCommands) {
            db.execSQL(command);
        }
    }

    private static List<String> getColumnNames(SQLiteDatabase db, String tableName) {
        Cursor c = db.query(tableName, null, null, null, null, null, null);
        List<String> columnNames = new ArrayList<>();
        for (int i = 0; i < c.getColumnCount(); i++) {
            columnNames.add(c.getColumnName(i));
        }
        c.close();
        return columnNames;
    }

    private static void createTable(SQLiteDatabase db, Class<?> table) {
        String sql = createTableSQL(table);
        db.execSQL(sql);
    }

    static String createTableSQL(Class<?> table) {
        List<Field> columnFields = ReflectionUtils.getTableFields(table);
        String tableName = NamingUtils.toTableName(table);
        if (KeywordUtils.isKeyword(tableName)) {
            throw new InvalidNameException("Table name is keyword: " + tableName);
        }

        StringBuilder sb = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        sb.append(tableName).append(" ( ID INTEGER PRIMARY KEY AUTOINCREMENT ");
        for (Field field : columnFields) {
            String columnName = NamingUtils.toColumnName(field);
            String columnType = TypeUtils.toColumnType(field.getType());

            if ("ID".equalsIgnoreCase(columnName)) {
                continue;
            }

            boolean notNull = false;
            boolean unique = false;
            if (field.isAnnotationPresent(Column.class)) {
                Column annotation = field.getAnnotation(Column.class);
                notNull = annotation.notNull();
                unique = annotation.unique();
            }
            if (field.isAnnotationPresent(NotNull.class)) {
                notNull = true;
            }
            if (field.isAnnotationPresent(Unique.class)) {
                unique = true;
            }

            sb.append(", ").append(columnName).append(" ").append(columnType);
            if (notNull) {
                sb.append(" NOT");
            }
            sb.append(" NULL");
            if (unique) {
                sb.append(" UNIQUE");
            }
        }

        if (table.isAnnotationPresent(MultiUnique.class)) {
            String[] constraint = table.getAnnotation(MultiUnique.class).value();
            if (constraint.length > 0) {
                sb.append(", UNIQUE(");
                for (String name : constraint) {
                    sb.append(NamingUtils.toSQLName(name)).append(",");
                }
                sb.delete(sb.length() - 1, sb.length());
                sb.append(") ON CONFLICT REPLACE");
            }
        }

        sb.append(" ) ");
        return sb.toString();
    }

}
