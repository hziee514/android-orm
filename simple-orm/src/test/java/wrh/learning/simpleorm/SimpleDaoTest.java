package wrh.learning.simpleorm;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import wrh.learning.simpleorm.app.DomainApp;
import wrh.learning.simpleorm.record.BooleanBoxRecord;
import wrh.learning.simpleorm.record.BooleanRawRecord;
import wrh.learning.simpleorm.record.EmptyRecord;
import wrh.learning.simpleorm.record.StringRecord;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
@RunWith(RobolectricTestRunner.class)
@Config(sdk = 21, constants = BuildConfig.class, application = DomainApp.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
public class SimpleDaoTest {

    @Before
    public void setUp() {
        ShadowLog.stream = System.out;
    }

    @Test
    public void save_emptyRecord() {
        assertEquals(0, SimpleDao.count(EmptyRecord.class));

        EmptyRecord r1 = new EmptyRecord();
        assertEquals(1, SimpleDao.save(r1));

        assertEquals(1, SimpleDao.count(EmptyRecord.class));

        EmptyRecord r2 = new EmptyRecord();
        assertEquals(2, SimpleDao.save(r2));

        assertEquals(2, SimpleDao.count(EmptyRecord.class));

        EmptyRecord r3 = new EmptyRecord();
        assertEquals(3, SimpleDao.save(r3));

        assertEquals(3, SimpleDao.count(EmptyRecord.class));

        assertTrue(r2.delete());

        assertEquals(2, SimpleDao.count(EmptyRecord.class));

        assertEquals(2, SimpleDao.deleteAll(EmptyRecord.class, "ID>?", "0"));

        assertEquals(0, SimpleDao.count(EmptyRecord.class));
    }

    @Test
    public void dao_StringRecord() {
        StringRecord r1 = new StringRecord();
        r1.setText("hello");
        SimpleDao.save(r1);
        assertEquals(1L, (long)r1.getId());

        assertEquals("hello", SimpleDao.fetch(StringRecord.class, 1L).getText());

        r1.setText("world");
        assertEquals(1, SimpleDao.update(r1));

        assertEquals("world", SimpleDao.fetch(StringRecord.class, 1L).getText());

        assertNull(SimpleDao.fetch(StringRecord.class, 100L));
    }

    @Test
    public void dao_BooleanRawRecord() {
        BooleanRawRecord r = new BooleanRawRecord();
        r.setBoolValue(true);
        r.save();
        assertEquals(1, (long)r.getId());

        BooleanRawRecord r1 = SimpleDao.fetch(BooleanRawRecord.class, r.getId());
        assertTrue(r1.isBoolValue());
    }

    @Test
    public void dao_BooleanBoxRecord() {
        BooleanBoxRecord r = new BooleanBoxRecord();
        r.setBoolValue(true);
        r.save();
        assertEquals(1, (long)r.getId());

        BooleanBoxRecord r1 = SimpleDao.fetch(BooleanBoxRecord.class, r.getId());
        assertTrue(r1.getBoolValue());
    }

}