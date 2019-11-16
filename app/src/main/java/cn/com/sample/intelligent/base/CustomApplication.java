package cn.com.sample.intelligent.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import cn.com.sample.intelligent.base.okhttp.WebSocketManage;
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
            LogUtil.d("设备布控", "...onActivityCreated..." + activity.getLocalClassName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            LogUtil.d("设备布控", "...onActivityStarted..." + activity.getLocalClassName());
            mCurrentActivity = activity;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            LogUtil.d("设备布控", "...onActivityResumed..." + activity.getLocalClassName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LogUtil.d("设备布控", "...onActivityPaused..." + activity.getLocalClassName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            LogUtil.d("设备布控", "...onActivityStopped..." + activity.getLocalClassName());
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            LogUtil.d("设备布控", "...onActivitySaveInstanceState..." + activity.getLocalClassName());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            LogUtil.d("设备布控", "...onActivityDestroyed..." + activity.getLocalClassName());
        }
    };
}
