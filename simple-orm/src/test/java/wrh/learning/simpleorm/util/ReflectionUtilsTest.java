package wrh.learning.simpleorm.util;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import wrh.learning.simpleorm.annotation.Ignore;

import static org.junit.Assert.*;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class ReflectionUtilsTest {

    @Test
    public void getTableFields() throws Exception {
        List<Field> fields = ReflectionUtils.getTableFields(Sub.class);
        Field[] expected = new Field[] {
                Sub.class.getDeclaredField("hello"),
                Base.class.getDeclaredField("id"),
        };
        assertArrayEquals(expected, fields.toArray());
    }

    @Test
    public void getTypeFields() throws Exception {
        List<Field> fields = new LinkedList<>();
        ReflectionUtils.getTypeFields(fields, Sub.class);
        Field[] expected = new Field[] {
                Sub.class.getDeclaredField("ignore"),
                Sub.class.getDeclaredField("st"),
                Sub.class.getDeclaredField("trans"),
                Sub.class.getDeclaredField("hello"),
                Base.class.getDeclaredField("id"),
        };
        assertArrayEquals(expected, fields.toArray());
    }

    static class Base {
        private Long id;
    }
    static class Sub extends Base {
        @Ignore
        private String ignore;
        private static String st;
        private transient String trans;
        private String hello;
    }
}