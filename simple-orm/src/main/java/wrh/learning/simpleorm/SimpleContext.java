package wrh.learning.simpleorm;

import android.content.Context;
import android.content.res.AssetManager;

/**
 * @author bruce.wu
 * @date 2018/6/6
 */
public class SimpleContext {

    public static final String SQL_TAG = "SimpleSQL";
    public static final String TAG = "SimpleORM";

    private static Context applicationContext;

    private static SimpleConfiguration configuration;

    private static SimpleDb db = null;

    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static AssetManager getAssets() {
        return applicationContext.getAssets();
    }

    public static SimpleDb getDb() {
        return db;
    }

    public static boolean isDebugEnabled() {
        return configuration.isDebugEnabled();
    }

    public static void init(Context context, SimpleConfiguration conf) {
        applicationContext = context;
        configuration = conf;
        db = SimpleDb.create(conf);
    }

    public static void term() {
        if (db != null) {
            db.close();
        }
    }

    private SimpleContext() {

    }

}
