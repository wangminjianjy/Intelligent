package cn.com.sample.intelligent.manager;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;

import java.util.List;

import cn.com.sample.intelligent.manager.notch.INotchScreen;
import cn.com.sample.intelligent.manager.notch.impl.AndroidPNotchScreen;
import cn.com.sample.intelligent.manager.notch.impl.HuaweiNotchScreen;
import cn.com.sample.intelligent.manager.notch.impl.MiNotchScreen;
import cn.com.sample.intelligent.manager.notch.impl.OppoNotchScreen;
import cn.com.sample.intelligent.manager.notch.utils.RomUtils;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/15.
 */
public class NotchManager {

    private static final NotchManager instance = new NotchManager();
    private final INotchScreen notchScreen;

    private NotchManager() {
        notchScreen = getNotchScreen();
    }

    public static NotchManager getInstance() {
        return instance;
    }

    public void setDisplayInNotch(Activity activity) {
        if (notchScreen != null)
            notchScreen.setDisplayInNotch(activity);
    }

    public void getNotchInfo(final Activity activity, final INotchScreen.NotchScreenCallback notchScreenCallback) {
        final INotchScreen.NotchScreenInfo notchScreenInfo = new INotchScreen.NotchScreenInfo();
        if (notchScreen != null && notchScreen.hasNotch(activity)) {
            notchScreen.getNotchRect(activity, new INotchScreen.NotchSizeCallback() {
                @Override
                public void onResult(List<Rect> notchRects) {
                    if (notchRects != null && notchRects.size() > 0) {
                        notchScreenInfo.hasNotch = true;
                        notchScreenInfo.notchRects = notchRects;
                    }
                    notchScreenCallback.onResult(notchScreenInfo);
                }
            });
        } else {
            notchScreenCallback.onResult(notchScreenInfo);
        }
    }

    private INotchScreen getNotchScreen() {
        INotchScreen notchScreen = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            notchScreen = new AndroidPNotchScreen();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (RomUtils.isHuawei()) {
                notchScreen = new HuaweiNotchScreen();
            } else if (RomUtils.isOppo()) {
                notchScreen = new OppoNotchScreen();
            } else if (RomUtils.isVivo()) {
                notchScreen = new HuaweiNotchScreen();
            } else if (RomUtils.isXiaomi()) {
                notchScreen = new MiNotchScreen();
            }
        }
        return notchScreen;
    }
}
