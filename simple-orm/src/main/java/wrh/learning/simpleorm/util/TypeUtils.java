package wrh.learning.simpleorm.util;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import wrh.learning.simpleorm.InvalidColumnTypeException;
import wrh.learning.simpleorm.InvalidObjectException;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public final class TypeUtils {

    /**
     * convert java type to column type
     *
     * @param type java type
     * @return column type
     */
    public static String toColumnType(Class<?> type) {
        if (type.equals(Boolean.class) || type.equals(boolean.class)
                || type.equals(Short.class) || type.equals(short.class)
                || type.equals(Integer.class) || type.equals(int.class)
                || type.equals(Long.class) || type.equals(long.class)) {
            return ColumnType.INTEGER;
        }
        if (type.equals(Date.class) || type.equals(Calendar.class) || type.equals(Timestamp.class)) {
            return ColumnType.INTEGER;
        }
        if (type.equals(String.class) || type.equals(BigDecimal.class) || type.isEnum()) {
            return ColumnType.TEXT;
        }
        if (type.equals(Float.class) || type.equals(float.class)
                || type.equals(Double.class) || type.equals(double.class)) {
            return ColumnType.REAL;
        }
        if (type.equals(byte[].class)) {
            return ColumnType.BLOB;
        }
        throw new InvalidColumnTypeException(type);
    }

    /**
     * add column value to save or update
     *
     * @param values content values to save or update
     * @param o object
     * @param column column field
     */
    public static void addFieldValueToColumn(ContentValues values, Object o, Field column) {
        try {
            column.setAccessible(true);
            Class<?> columnType = column.getType();
            String columnName = NamingUtils.toColumnName(column);
            Object columnValue = column.get(o);
            if (columnType.equals(Boolean.class) || columnType.equals(boolean.class)) {
                values.put(columnName, (Boolean)columnValue);
            } else if (columnType.equals(Short.class) || columnType.equals(short.class)) {
                values.put(columnName, (Short)columnValue);
            } else if (columnType.equals(Integer.class) || columnType.equals(int.class)) {
                values.put(columnName, (Integer)columnValue);
            } else if (columnType.equals(Long.class) || columnType.equals(long.class)) {
                values.put(columnName, (Long)columnValue);
            } else if (columnType.equals(Float.class) || columnType.equals(float.class)) {
                values.put(columnName, (Float)columnValue);
            } else if (columnType.equals(Double.class) || columnType.equals(double.class)) {
                values.put(columnName, (Double)columnValue);
            } else if (columnType.equals(String.class)) {
                values.put(columnName, (String)columnValue);
            } else if (columnType.equals(BigDecimal.class)) {
                if (columnValue != null) {
                    values.put(columnName, columnValue.toString());
                }
            } else if (columnType.equals(Timestamp.class)) {
                if (columnValue != null) {
                    values.put(columnName, ((Timestamp)columnValue).getTime());
                }
            } else if (columnType.equals(Date.class)) {
                if (columnValue != null) {
                    values.put(columnName, ((Date)columnValue).getTime());
                }
            } else if (columnType.equals(Calendar.class)) {
                if (columnValue != null) {
                    values.put(columnName, ((Calendar)columnValue).getTimeInMillis());
                }
            } else if (columnType.equals(byte[].class)) {
                if (columnValue != null) {
                    values.put(columnName, (byte[])columnValue);
                }
            } else if (columnType.isEnum()) {
                values.put(columnName, ((Enum)columnValue).name());
            }
            //TODO: throw new InvalidColumnTypeException
        } catch (Exception e) {
            throw new InvalidObjectException(o.getClass().getName(), e);
        }
    }


    public static void getFieldValueFromColumn(Cursor cursor, Field field, Object instance) {
        String columnName = NamingUtils.toColumnName(field);
        int columnIndex = cursor.getColumnIndex(columnName);

        if (columnIndex < 0) {
            //TODO: no such column
            return;
        }

        if (cursor.isNull(columnIndex)) {
            return;
        }

        Class<?> fieldType = field.getType();
        try {
            field.setAccessible(true);
            if (fieldType.equals(Boolean.class) || fieldType.equals(boolean.class)) {
                field.set(instance, "1".equals(cursor.getString(columnIndex)));
            } else if (fieldType.equals(Short.class) || fieldType.equals(short.class)) {
                field.set(instance, cursor.getShort(columnIndex));
            } else if (fieldType.equals(Integer.class) || fieldType.equals(int.class)) {
                field.set(instance, cursor.getInt(columnIndex));
            } else if (fieldType.equals(Long.class) || fieldType.equals(long.class)) {
                field.set(instance, cursor.getLong(columnIndex));
            } else if (fieldType.equals(Float.class) || fieldType.equals(float.class)) {
                field.set(instance, cursor.getFloat(columnIndex));
            } else if (fieldType.equals(Double.class) || fieldType.equals(double.class)) {
                field.set(instance, cursor.getDouble(columnIndex));
            } else if (fieldType.equals(String.class)) {
                field.set(instance, cursor.getString(columnIndex));
            } else if (fieldType.equals(BigDecimal.class)) {
                field.set(instance, new BigDecimal(cursor.getString(columnIndex)));
            } else if (fieldType.equals(Timestamp.class)) {
                field.set(instance, new Timestamp(cursor.getLong(columnIndex)));
            } else if (fieldType.equals(Date.class)) {
                field.set(instance, new Date(cursor.getLong(columnIndex)));
            } else if (fieldType.equals(Calendar.class)) {
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(cursor.getLong(columnIndex));
                field.set(instance, c);
            } else if (fieldType.equals(byte[].class)) {
                field.set(instance, cursor.getBlob(columnIndex));
            } else if (fieldType.isEnum()) {
                Method valueOf = fieldType.getDeclaredMethod("valueOf", String.class);
                Object enumVal = valueOf.invoke(fieldType, cursor.getString(columnIndex));
                field.set(instance, enumVal);
            }
            //TODO: throw new InvalidColumnTypeException
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
