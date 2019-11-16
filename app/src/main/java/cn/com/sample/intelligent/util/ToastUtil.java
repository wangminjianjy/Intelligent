package cn.com.sample.intelligent.util;

import android.content.Context;
import android.widget.Toast;

import cn.com.sample.intelligent.base.CustomApplication;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/10/8.
 */
public class ToastUtil {

    private static Toast mToast;
    private static Context context = CustomApplication.getContext();

    // 非连续弹出的Toast
    public static void showSingleToast(int resId, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, context.getResources().getText(resId).toString(), duration);
        } else {
            mToast.setText(context.getResources().getText(resId).toString());
        }
        mToast.show();
    }

    public static void showSingleToast(String text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    // 连续弹出的Toast
    public static void showToast(int resId, int duration) {
        Toast.makeText(context, context.getResources().getText(resId).toString(), duration).show();
    }

    public static void showToast(String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }
}
