package wrh.learning.simpleorm;

import android.app.Application;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Date;

import wrh.learning.simpleorm.annotation.Column;
import wrh.learning.simpleorm.annotation.MultiUnique;
import wrh.learning.simpleorm.annotation.NotNull;
import wrh.learning.simpleorm.annotation.Table;
import wrh.learning.simpleorm.annotation.Unique;

import static org.junit.Assert.*;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class, application = Application.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class SchemaBuilderTest {

    @Before
    public void setUp() {

    }

    @Test
    public void createDatabase() {

    }

    @Test
    public void createTableSQL() {
        String sql = SchemaBuilder.createTableSQL(Bean.class);
        System.out.println(sql);
    }

    static class Record {
        private Long id;
    }
    @Table(name = "T_BEAN")
    @MultiUnique({"ID", "NAME"})
    static class Bean extends Record {

        @Unique
        private String name;
        @NotNull
        private boolean active;
        private Date timestamp;
        @Column(name = "C_DATA", unique = true, notNull = true)
        private byte[] data;
        private double amount;

    }
}