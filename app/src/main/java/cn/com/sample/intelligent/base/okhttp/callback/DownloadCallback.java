package cn.com.sample.intelligent.base.okhttp.callback;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by dzl on 2017/3/29.
 */
public interface DownloadCallback extends BaseCallBack {

    void onStart(Request request);

    void onDownLoadProgress(Call call, Response response, int progress);

    void onDownLoadFinish(Call call, Response response, String absolutePath);

    void onResponseError(Call call, Response response, IOException e);

    void onFailure(Call call, Request request, IOException e);
}