package wrh.learning.simpleorm.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import wrh.learning.simpleorm.InvalidColumnTypeException;

import static org.junit.Assert.*;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class TypeUtilsTest {

    @Test
    public void toColumnType_Boolean() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(Boolean.class));
    }

    @Test
    public void toColumnType_boolean() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(Boolean.TYPE));
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(boolean.class));
    }

    @Test
    public void toColumnType_Short() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(Short.class));
    }

    @Test
    public void toColumnType_short() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(short.class));
    }

    @Test
    public void toColumnType_Integer() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(Integer.class));
    }

    @Test
    public void toColumnType_int() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(int.class));
    }

    @Test
    public void toColumnType_Long() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(Long.class));
    }

    @Test
    public void toColumnType_long() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(long.class));
    }

    @Test
    public void toColumnType_Date() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(Date.class));
    }

    @Test
    public void toColumnType_Calendar() {
        assertEquals(ColumnType.INTEGER, TypeUtils.toColumnType(Calendar.class));
    }

    @Test
    public void toColumnType_byteArray() {
        assertEquals(ColumnType.BLOB, TypeUtils.toColumnType(byte[].class));
    }

    @Test
    public void toColumnType_Float() {
        assertEquals(ColumnType.REAL, TypeUtils.toColumnType(Float.class));
    }

    @Test
    public void toColumnType_float() {
        assertEquals(ColumnType.REAL, TypeUtils.toColumnType(float.class));
    }

    @Test
    public void toColumnType_Double() {
        assertEquals(ColumnType.REAL, TypeUtils.toColumnType(Double.class));
    }

    @Test
    public void toColumnType_double() {
        assertEquals(ColumnType.REAL, TypeUtils.toColumnType(double.class));
    }

    @Test
    public void toColumnType_String() {
        assertEquals(ColumnType.TEXT, TypeUtils.toColumnType(String.class));
    }

    @Test
    public void toColumnType_BigDecimal() {
        assertEquals(ColumnType.TEXT, TypeUtils.toColumnType(BigDecimal.class));
    }

    @Test(expected = InvalidColumnTypeException.class)
    public void toColumnType_Object() {
        TypeUtils.toColumnType(Object.class);
    }

}