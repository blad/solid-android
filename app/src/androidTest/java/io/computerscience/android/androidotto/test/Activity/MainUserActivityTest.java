package io.computerscience.android.androidotto.test.Activity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.squareup.otto.Bus;

import io.computerscience.android.androidotto.Activity.MainUserActivity;
import io.computerscience.android.androidotto.Interface.Injectable;
import io.computerscience.android.androidotto.test.Application.MockApplicationInjectable;
import io.computerscience.android.androidotto.test.Module.MainUserActivityTestModule;
import io.computerscience.android.androidotto.test.Module.SingletonTestModule;

import static org.mockito.Mockito.anyObject;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MainUserActivityTest extends ActivityUnitTestCase<MainUserActivity> {

    Activity activity;
    private Application mApplication;
    private Context mContext;

    public MainUserActivityTest() {
        super(MainUserActivity.class);
    }


    @Override
    public void setUp() throws Exception {
        super.setUp();

        // See: http://stackoverflow.com/questions/12267572/mockito-dexmaker-on-android
        System.setProperty("dexmaker.dexcache",getInstrumentation().getTargetContext().getCacheDir().getPath());
        mContext = new ContextWrapper(getInstrumentation().getTargetContext()) {
            @Override
            public Context getApplicationContext() {
                return mApplication;
            }
        };

        mApplication = new MockApplicationInjectable(mContext){
            @Override
            public Object[] getModules() {
                return new Object[]{
                        new MainUserActivityTestModule(),
                        new SingletonTestModule(mContext),
                };
            }
        };

        setApplication(mApplication); // Specify modules and initialize object graph
    }

    public void testContext() {
        setActivityContext(mContext);
        startActivity(new Intent(mContext, MainUserActivity.class), null, null);
        activity = getActivity();

        assertNotNull(((MainUserActivity) activity).getContext());
        assertTrue(activity.getApplicationContext() instanceof MockApplicationInjectable);

        activity.finish();
    }


    public void testMockObject() throws Exception {
        setActivityContext(mContext);
        startActivity(new Intent(mContext, MainUserActivity.class), null, null);
        activity = getActivity();
        getInstrumentation().callActivityOnResume(activity);
        getInstrumentation().callActivityOnPause(activity);
        Bus eventBus = ((MainUserActivity) activity).getEventBus();
        Log.e("TEST", "Event Bus Object" + eventBus.toString() + eventBus.hashCode());
        verify(eventBus, times(3)).register(anyObject());
        verify(eventBus, atLeastOnce()).post(anyObject());

        activity.finish();
    }
}
