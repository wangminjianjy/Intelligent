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

    private UBaseRequest getRequest(String funcName, String key, ArrayMap<String, Object> params) {
        String url = BaseConfig.HOST_URL + "/AppInterface/" + funcName;
        UBaseRequest baseRequest = new UBaseRequest();
        baseRequest.setKey(key);
        baseRequest.setParams(params);
        baseRequest.setUrl(url);
        return baseRequest;
    }

    public void postNoParams(String funcName, StringCallback callback) {
        ArrayMap<String, Object> params = new ArrayMap<>();

        UBaseRequest baseRequest = getRequest(funcName, "post", params);
        getData(baseRequest, callback);
    }

    public void binding(String funcName, String phoneCode, String keyCode, String factoryID, StringCallback callback) {
        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("PhoneCode", phoneCode);
        params.put("RegCode", keyCode);
//        params.put("ID_Factory", factoryID);

        UBaseRequest baseRequest = getRequest(funcName, "binding", params);
        getData(baseRequest, callback);
    }

    public void untying(String funcName, String phoneCode, StringCallback callback) {
        ArrayMap<String, Object> params = new ArrayMap<>();
        params.put("PhoneCode", phoneCode);

        UBaseRequest baseRequest = getRequest(funcName, "untying", params);
        getData(baseRequest, callback);
    }
}
