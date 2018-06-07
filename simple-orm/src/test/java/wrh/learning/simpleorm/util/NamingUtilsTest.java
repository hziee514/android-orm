package wrh.learning.simpleorm.util;

import org.junit.Test;

import java.lang.reflect.Field;

import wrh.learning.simpleorm.InvalidNameException;
import wrh.learning.simpleorm.annotation.Column;
import wrh.learning.simpleorm.annotation.Table;

import static org.junit.Assert.*;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class NamingUtilsTest {

    @Test(expected = IllegalArgumentException.class)
    public void toSQLName_empty() {
        NamingUtils.toSQLName("");
    }

    @Test
    public void toSQLName() {
        assertEquals("HELLO_WORLD", NamingUtils.toSQLName("HelloWorld"));
        assertEquals("HELLO_WORLD", NamingUtils.toSQLName("helloWorld"));
        assertEquals("H_ELLO_WORLD", NamingUtils.toSQLName("HElloWorld"));
        assertEquals("H_ELLO_WORLD", NamingUtils.toSQLName("hElloWorld"));
        assertEquals("HELLO_WO_RLD", NamingUtils.toSQLName("helloWORld"));
        assertEquals("HELLO_WORLD", NamingUtils.toSQLName("helloWORLD"));
        assertEquals("HELLO_WORLD0", NamingUtils.toSQLName("helloWorld0"));
        assertEquals("HELLO_WORLD", NamingUtils.toSQLName("hello_world"));
    }

    @Test(expected = InvalidNameException.class)
    public void toSQLName_withWhitespace() {
        NamingUtils.toSQLName("hello ");
    }

    @Test(expected = InvalidNameException.class)
    public void toSQLName_specialChar() {
        NamingUtils.toSQLName("hello$");
    }

    @Test
    public void toTableName() {
        assertEquals("hello_world_xxx", NamingUtils.toTableName(HelloWorld.class));
        assertEquals("HELLO_WORLD0", NamingUtils.toTableName(HelloWorld0.class));
    }

    @Test
    public void toColumnName_notAnnotated() throws Exception {
        Field field = HelloWorld.class.getDeclaredField("name");
        assertEquals("NAME", NamingUtils.toColumnName(field));
    }

    @Test
    public void toColumnName_annotatedNotCustomized() throws Exception {
        Field field = HelloWorld.class.getDeclaredField("name1");
        assertEquals("NAME1", NamingUtils.toColumnName(field));
    }

    @Test
    public void toColumnName_annotatedAndCustomized() throws Exception {
        Field field = HelloWorld.class.getDeclaredField("name2");
        assertEquals("hello", NamingUtils.toColumnName(field));
    }

    @Table(name = "hello_world_xxx")
    private static class HelloWorld {

        private String name;

        @Column
        private String name1;

        @Column(name = "hello")
        private String name2;

    }

    @Table
    private static class HelloWorld0 {

    }

}