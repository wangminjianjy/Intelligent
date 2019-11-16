package cn.com.sample.intelligent.net;

import androidx.collection.ArrayMap;

import com.google.gson.Gson;

import cn.com.sample.intelligent.base.BaseConfig;
import cn.com.sample.intelligent.base.okhttp.OkHttpClientManager;
import cn.com.sample.intelligent.base.okhttp.callback.StringCallback;
import okhttp3.Request;

/**
 * Created by dzl on 2017/5/27.
 */

public class HttpModel {

    private static HttpModel mInstance;
    private OkHttpClientManager okHttpClientManager;

    public HttpModel() {
        okHttpClientManager = OkHttpClientManager.getInstance();
    }

    public static HttpModel getInstance() {
        if (mInstance == null) {
            synchronized (HttpModel.class) {
                if (mInstance == null) {
                    mInstance = new HttpModel();
                }
            }
        }
        return mInstance;
    }

    public void getData(UBaseRequest baseRequest, StringCallback callback) {
        Gson gson = new Gson();
        String param = gson.toJson(baseRequest.getParams());
        Request request = okHttpClientManager.getPostRequest(baseRequest.getUrl(), "", param);
        okHttpClientManager.postAsyn(request, callback);
    }

    public void postNoParams(String funcName, StringCallback callback) {
        String url = BaseConfig.HOSTURL + "/AppInterface/" + funcName;
        ArrayMap<String, Object> params = new ArrayMap<>();

        UBaseRequest baseRequest = new UBaseRequest();
        baseRequest.setKey("post");
        baseRequest.setParams(params);
        baseRequest.setUrl(url);
        getData(baseRequest, callback);
    }

    public void binding(String funcName, String phoneCode, String keyCode, String factoryID, StringCallback callback) {
        String url = BaseConfig.HOSTURL + "/AppInterface/" + funcName;
        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("PhoneCode", phoneCode);
        params.put("RegCode", keyCode);
        params.put("ID_Factory", factoryID);

        UBaseRequest baseRequest = new UBaseRequest();
        baseRequest.setKey("binding");
        baseRequest.setParams(params);
        baseRequest.setUrl(url);
        getData(baseRequest, callback);
    }

    public void untying(String funcName, String phoneCode, StringCallback callback) {
        String url = BaseConfig.HOSTURL + "/AppInterface/" + funcName;
        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("PhoneCode", phoneCode);

        UBaseRequest baseRequest = new UBaseRequest();
        baseRequest.setKey("untying");
        baseRequest.setParams(params);
        baseRequest.setUrl(url);
        getData(baseRequest, callback);
    }
}
