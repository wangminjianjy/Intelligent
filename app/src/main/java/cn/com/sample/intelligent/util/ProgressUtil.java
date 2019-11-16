package cn.com.sample.intelligent.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;

import com.maning.mndialoglibrary.MProgressDialog;

import cn.com.sample.intelligent.R;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/10/8.
 */
public class ProgressUtil {

    private MProgressDialog mProgressDialog;

    public void showProgress(Context context, String msg, boolean cancelFlag) {
        if (mProgressDialog == null) {
            int color = Color.TRANSPARENT;
            int bgColor = context.getResources().getColor(R.color.colorTextGray);
            mProgressDialog = new MProgressDialog.Builder(context)
                    //点击外部是否可以取消
                    .isCanceledOnTouchOutside(cancelFlag)
                    .setBackgroundWindowColor(color)
                    .setBackgroundViewColor(bgColor)
                    .build();
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show(msg);
        }
    }

    public void hideProgress(Context context) {
        if (context != null && !((Activity) context).isDestroyed() && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
