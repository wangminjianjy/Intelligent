package cn.com.sample.intelligent.net;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.CustomApplication;
import cn.com.sample.intelligent.base.okhttp.callback.StringCallback;
import cn.com.sample.intelligent.util.LogUtil;
import cn.com.sample.intelligent.util.ProgressUtil;
import cn.com.sample.intelligent.util.ToastUtil;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:
 * Creator: wangminjian
 * Create time: 2018/4/10.
 */

public abstract class HttpCallback implements StringCallback {

    private static final String TAG = "HttpCallback";
    protected Activity context;
    private ProgressUtil progressUtil;

    public HttpCallback(Activity context) {
        this.context = context;
    }

    public boolean isShowProgressDialog() {
        return true;
    }

    public boolean isCanceledOnTouchOutsideDialog() {
        return false;
    }

    public void onStart(Request request) {
        if (isShowProgressDialog() && context != null && !context.isFinishing()) {
            progressUtil = new ProgressUtil();
            progressUtil.showProgress(context, context.getString(R.string.hint_waiting), isCanceledOnTouchOutsideDialog());
        }
    }

    @Override
    public void onResponse(Call call, Response response, String text) {
        HttpResult httpResult = null;
        try {
            httpResult = new Gson().fromJson(text, HttpResult.class);
            //result1
            try {
                JSONObject resultJson = new JSONObject(text);
                if (resultJson.has("Result")) {
                    httpResult.setActualResult1(resultJson.getString("Result"));
                } else {
                    httpResult.setActualResult1("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                httpResult.setActualResult1("");
            }
            //result2
            try {
                JSONObject resultJson = new JSONObject(text);
                if (resultJson.has("Result2")) {
                    String result2 = resultJson.getString("Result2");
                    httpResult.setActualResult2(result2);
                } else {
                    httpResult.setActualResult2("");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                httpResult.setActualResult2("");
            }

            //result3
            try {
                JSONObject resultJson = new JSONObject(text);
                if (resultJson.has("Result3")) {
                    httpResult.setActualResult3(resultJson.getString("Result3"));
                } else {
                    httpResult.setActualResult3("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                httpResult.setActualResult3("");
            }

            //result4
            try {
                JSONObject resultJson = new JSONObject(text);
                if (resultJson.has("Result4")) {
                    httpResult.setActualResult4(resultJson.getString("Result4"));
                } else {
                    httpResult.setActualResult4("");
                }
            } catch (JSONException e) {
                e.printStackTrace();
                httpResult.setActualResult4("");
            }
        } catch (Exception e) {
            httpResult = new HttpResult();
            httpResult.setCode(ResultCode.RESULT_STATUS_WEB);
            httpResult.setResultMsg("服务器错误");
            onErrorStr(httpResult);
        }
        int code = httpResult.getCode();
        if (code == ResultCode.RESULT_STATUS_SUCCESS) {
            onSuccessStr(httpResult);
        } else {
            onErrorStr(httpResult);
        }
        onFinish();

    }

    @Override
    public void onResponseError(Call call, Response response, IOException e) {

    }

    @Override
    public void onFailure(Call call, Request request, IOException e) {
        LogUtil.d("设备布控", "...onFailure...url:" + request.url() + " is error: " + e.toString());
        if (progressUtil != null) {
            progressUtil.hideProgress(context);
        }
        ToastUtil.showSingleToast(context.getString(R.string.hint_net_error), Toast.LENGTH_LONG);
    }

    public abstract void onSuccessStr(HttpResult httpResult);

    public void onErrorStr(HttpResult httpResult) {
        String msg = httpResult.getResultMsg();
        ToastUtil.showSingleToast(msg, Toast.LENGTH_LONG);
    }

    public void onFinish() {
        if (progressUtil != null) {
            progressUtil.hideProgress(context);
        }
    }
}
