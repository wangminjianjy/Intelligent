package cn.com.sample.intelligent.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import cn.com.sample.intelligent.util.LogUtil;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/15.
 */
public class CustomApplication extends Application {

    private static final String TAG = "CustomApplication";
    private static Context context;
    public Activity mCurrentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        this.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public static Context getContext() {
        return context;
    }

    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            LogUtil.d(TAG, "...onActivityCreated..." + activity.getLocalClassName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            LogUtil.d(TAG, "...onActivityStarted..." + activity.getLocalClassName());
            mCurrentActivity = activity;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            LogUtil.d(TAG, "...onActivityResumed..." + activity.getLocalClassName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LogUtil.d(TAG, "...onActivityPaused..." + activity.getLocalClassName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            LogUtil.d(TAG, "...onActivityStopped..." + activity.getLocalClassName());
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            LogUtil.d(TAG, "...onActivitySaveInstanceState..." + activity.getLocalClassName());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            LogUtil.d(TAG, "...onActivityDestroyed..." + activity.getLocalClassName());
        }
    };
}
