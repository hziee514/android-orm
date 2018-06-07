package wrh.learning.simpleorm;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import wrh.learning.simpleorm.util.SimpleCursorFactory;

/**
 * @author bruce.wu
 * @date 2018/6/6
 */
public class SimpleDb extends SQLiteOpenHelper {

    static SimpleDb create(SimpleConfiguration configuration) {
        return new SimpleDb(configuration);
    }

    private final SchemaBuilder schemaBuilder;
    private int openedRef = 0;
    private SQLiteDatabase database = null;

    private SimpleDb(SimpleConfiguration configuration) {
        super(SimpleContext.getApplicationContext(),
                configuration.getDatabase(),
                new SimpleCursorFactory(),
                configuration.getVersion());
        schemaBuilder = new SchemaBuilder(configuration.getDomainClasses());
    }

    public synchronized SQLiteDatabase open() {
        if (database == null) {
            database = getWritableDatabase();
        }
        return database;
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        openedRef++;
        return super.getReadableDatabase();
    }

    @Override
    public synchronized void close() {
        openedRef--;
        if (openedRef <= 0) {
            super.close();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        schemaBuilder.createDatabase(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        schemaBuilder.upgradeDatabase(db, oldVersion, newVersion);
    }

}
