package wrh.learning.simpleorm;

import java.util.Collections;
import java.util.List;

/**
 * @author bruce.wu
 * @date 2018/6/6
 */
public class SimpleConfiguration {

    private static final String DEFAULT_DATABASE_NAME = "simple-orm.db";

    private final String database;
    private final int version;
    private final List<Class<?>> domainClasses;
    private final boolean debugEnabled;

    public SimpleConfiguration(int version, List<Class<?>> domainClasses) {
        this(DEFAULT_DATABASE_NAME, version, domainClasses, true);
    }

    public SimpleConfiguration(String database,
                               int version,
                               List<Class<?>> domainClasses,
                               boolean debugEnabled) {
        this.database = database;
        this.version = version;
        this.domainClasses = Collections.unmodifiableList(domainClasses);
        this.debugEnabled = debugEnabled;
    }

    public static String getDefaultDatabaseName() {
        return DEFAULT_DATABASE_NAME;
    }

    public String getDatabase() {
        return database;
    }

    public int getVersion() {
        return version;
    }

    public List<Class<?>> getDomainClasses() {
        return domainClasses;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }
}
