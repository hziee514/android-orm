package wrh.learning.simpleorm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import wrh.learning.simpleorm.annotation.Ignore;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public final class ReflectionUtils {

    /**
     * get fields that would map to table column
     *
     * @param type table type
     * @return column fields
     */
    public static List<Field> getTableFields(Class<?> type) {
        List<Field> typeFields = new LinkedList<>();
        getTypeFields(typeFields, type);

        List<Field> tableFields = new ArrayList<>();
        for (Field field : typeFields) {
            if (!field.isAnnotationPresent(Ignore.class)
                    && !Modifier.isStatic(field.getModifiers())
                    && !Modifier.isTransient(field.getModifiers())) {
                tableFields.add(field);
            }
        }
        return tableFields;
    }

    /**
     * get all fields of java type
     *
     * @param fields all fields, output
     * @param type java type
     */
    public static void getTypeFields(List<Field> fields, Class<?> type) {
        Collections.addAll(fields, type.getDeclaredFields());
        if (type.getSuperclass() != null) {
            getTypeFields(fields, type.getSuperclass());
        }
    }

}
