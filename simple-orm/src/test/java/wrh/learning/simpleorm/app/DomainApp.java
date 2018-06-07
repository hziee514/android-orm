package wrh.learning.simpleorm.app;

import android.app.Application;

import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;
import java.util.List;

import wrh.learning.simpleorm.SimpleConfiguration;
import wrh.learning.simpleorm.SimpleContext;
import wrh.learning.simpleorm.record.BooleanBoxRecord;
import wrh.learning.simpleorm.record.BooleanRawRecord;
import wrh.learning.simpleorm.record.EmptyRecord;
import wrh.learning.simpleorm.record.StringRecord;

/**
 * @author bruce.wu
 * @date 2018/6/7
 */
public class DomainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        List<Class<?>> domainClasses = new ArrayList<>();
        domainClasses.add(EmptyRecord.class);
        domainClasses.add(StringRecord.class);
        domainClasses.add(BooleanRawRecord.class);
        domainClasses.add(BooleanBoxRecord.class);

        SimpleConfiguration configuration = new SimpleConfiguration(1, domainClasses);
        SimpleContext.init(RuntimeEnvironment.application.getApplicationContext(), configuration);
    }

    @Override
    public void onTerminate() {
        SimpleContext.term();

        super.onTerminate();
    }

}
