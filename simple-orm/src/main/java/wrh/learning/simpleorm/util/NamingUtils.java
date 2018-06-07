package wrh.learning.simpleorm.util;

import java.lang.reflect.Field;

import wrh.learning.simpleorm.InvalidNameException;
import wrh.learning.simpleorm.annotation.Column;
import wrh.learning.simpleorm.annotation.Table;

/**
 * @author bruce.wu
 * @date 2018/6/6
 */
public final class NamingUtils {

    /**
     * convert java name to sql name
     *
     * @param camelCased java name
     * @return sql name
     */
    public static String toSQLName(String camelCased) {
        if ("".equals(camelCased)) {
            throw new IllegalArgumentException();
        }

        StringBuilder sb = new StringBuilder();
        char[] buf = camelCased.toCharArray();

        for (int i = 0; i < buf.length; i++) {
            char prev = (i > 0) ? buf[i - 1] : 0;
            char c = buf[i];
            char next = (i < (buf.length - 1)) ? buf[i + 1] : 0;

            if ((i == 0) || Character.isLowerCase(c) || Character.isDigit(c)) {
                sb.append(Character.toUpperCase(c));
            } else if (Character.isUpperCase(c)) {
                if (Character.isLowerCase(prev)) {
                    sb.append("_").append(c);
                } else if (next > 0 && Character.isLowerCase(next)) {
                    sb.append("_").append(c);
                } else {
                    sb.append(c);
                }
            } else if (c == '_') {
                sb.append(c);
            } else {
                throw new InvalidNameException(camelCased);
            }
        }
        return sb.toString();
    }

    /**
     * get table name from model type
     *
     * @param table model type
     * @return table name
     */
    public static String toTableName(Class<?> table) {
        if (table.isAnnotationPresent(Table.class)) {
            Table annotation = table.getAnnotation(Table.class);
            if (!"".equals(annotation.name())) {
                return annotation.name();
            }
        }
        return toSQLName(table.getSimpleName());
    }

    /**
     * get column name from model field
     *
     * @param column model field
     * @return column name
     */
    public static String toColumnName(Field column) {
        if (column.isAnnotationPresent(Column.class)) {
            Column annotation = column.getAnnotation(Column.class);
            if (!"".equals(annotation.name())) {
                return annotation.name();
            }
        }
        return toSQLName(column.getName());
    }

}
