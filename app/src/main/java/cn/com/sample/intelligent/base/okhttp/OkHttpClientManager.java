package cn.com.sample.intelligent.base.okhttp;

import android.os.Handler;
import android.os.Looper;

import androidx.collection.ArrayMap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.com.sample.intelligent.base.BaseConfig;
import cn.com.sample.intelligent.base.okhttp.callback.DownloadCallback;
import cn.com.sample.intelligent.base.okhttp.callback.StringCallback;
import cn.com.sample.intelligent.base.okhttp.callback.UploadCallback;
import cn.com.sample.intelligent.base.okhttp.upload.ProgressRequestBody;
import cn.com.sample.intelligent.base.okhttp.upload.UploadProgressListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dzl on 2017/3/28.
 */

public class OkHttpClientManager {

    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;

    private Handler mDelivery;

    //提交json数据
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串数据
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient.Builder()
//              .addInterceptor(loggingInterceptor)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .build();
        mDelivery = new Handler(Looper.getMainLooper());
    }

    private OkHttpClientManager(long connectTimeout, long readTimeout, long writeTimeout) {
        mOkHttpClient = new OkHttpClient.Builder()
//              .addInterceptor(loggingInterceptor)
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .build();

        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager(
                            BaseConfig.NETWORK_READ_TIMEOUT,
                            BaseConfig.NETWORK_CONNECT_TIMEOUT,
                            BaseConfig.NETWORK_WRITE_TIMEOUT);
                }
            }
        }
        return mInstance;
    }

    /**********************************************************************/
    /**
     * 获取上传文件的请求
     *
     * @param files    文件
     * @param fileKeys 文件标识
     * @return
     */
    public Request getPostRequest(String url, ArrayMap<String, String> params, File[] files, String[] fileKeys) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            builder.addFormDataPart(fileKeys[i], file.getName(), fileBody);
        }
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                builder.addFormDataPart(params.keyAt(i), params.valueAt(i));
            }
        }
        MultipartBody multipartBody = builder.build();
        return getPostRequest(url, multipartBody);
    }

    public Request getPostRequest(String url, ArrayMap<String, String> params) {
//        FormBody.Builder formBodyBuilder = new FormBody.Builder();
//        if (params != null) {
//            for (int i = 0; i < params.size(); i++) {
//            formBodyBuilder.add(params.keyAt(i), params.valueAt(i));
////                formBodyBuilder.add(params.keyAt(i), params.valueAt(i).getBytes("UTF-8"));
//        }
//    }
//        return getPostRequest(url, formBodyBuilder.build());

        try {
            // 处理参数
            StringBuilder tempParams = new StringBuilder();
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    tempParams.append(String.format("%s=%s", params.keyAt(i), URLEncoder.encode(params.valueAt(i), "utf-8")));
//                formBodyBuilder.add(params.keyAt(i), params.valueAt(i).getBytes("UTF-8"));
                }
            }
            // 创建一个请求实体对象 RequestBody
            RequestBody body = RequestBody.create(MEDIA_TYPE_JSON, tempParams.toString());
            return getPostRequest(url, body);
        } catch (Exception e) {
            return null;
        }
    }

    public Request getPostRequest(String url, String key, String value) {
//        FormBody.Builder formBodyBuilder = new FormBody.Builder();
//        formBodyBuilder.add(key, value);
        RequestBody body = FormBody.create(MediaType.parse("application/json"), value);
        return getPostRequest(url, body);
    }

    public Request getPostRequest(String url, RequestBody requestBody) {
        return new Request.Builder().url(url).post(requestBody).build();
    }

    public Request getGetRequest(String url) {
        return new Request.Builder().url(url).get().build();
    }

    /**
     * 获取上传文件的请求
     *
     * @param url      路径
     * @param params   参数
     * @param files    文件
     * @param fileKeys 文件标识
     * @return
     */
    @SuppressWarnings("UnChecked")
    private Request _uploadFilesAsyn(String url, ArrayMap<String, String> params, File[] files, String[] fileKeys) {
        return getPostRequest(url, params, files, fileKeys);
    }

    /**
     * 获取带监听进度的上传文件的请求
     *
     * @param url      上传网络路径
     * @param params   上传参数
     * @param files    上传文件
     * @param fileKeys 上传文件的key
     * @param listener 上传监听
     * @return
     */
    @SuppressWarnings("UnChecked")
    private Request _uploadFilesWithCallBackAsyn(String url, final ArrayMap<String, String> params, File[] files,
                                                 String[] fileKeys, UploadProgressListener listener) {
        ProgressRequestBody.Builder builder = new ProgressRequestBody.Builder();
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                builder.add(params.keyAt(i), params.valueAt(i));
            }
        }
        if (fileKeys != null) {
            for (int i = 0; i < fileKeys.length; i++) {
                builder.add(fileKeys[i], fileKeys[i]);
            }
        }
        builder.addUploadProgressListener(listener);
        ProgressRequestBody requestBody = builder.build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**********************************************************************/
    /**
     * 同步请求
     *
     * @return Response
     */
    public Response getAsyn(Request request) throws IOException {
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    /**
     * 异步的post请求
     *
     * @param callback
     */
    public Call postAsyn(Request request, final StringCallback callback) {
        return _deliveryResult(request, callback);
    }

    /**
     * 下载文件
     *
     * @param request  请求
     * @param destDir  下载到本地路径
     * @param fileName 文件名
     * @param callback 下载回调
     */
    public void downloadAsyn(Request request, String destDir, String fileName, DownloadCallback callback) {
        getInstance()._downloadAsyn(request, destDir, fileName, callback);
    }

    /**
     * 上传多文件
     *
     * @param url      请求路径
     * @param files    需要上传的文件
     * @param fileKeys 文件标识
     */
    public Call uploadFilesAsyn(String url, File[] files, String[] fileKeys, StringCallback callback) {
        return uploadFilesAsyn(url, null, files, fileKeys, callback);
    }

    /**
     * 上传多文件
     *
     * @param url      请求路径
     * @param files    需要上传的文件
     * @param fileKeys 文件标识
     */
    public Call uploadFilesAsyn(String url, ArrayMap<String, String> params, File[] files, String[] fileKeys, StringCallback callback) {
        Request request = getInstance()._uploadFilesAsyn(url, params, files, fileKeys);
        return _deliveryResult(request, callback);
    }

    /**
     * 上传多文件
     *
     * @param url      请求路径
     * @param files    需要上传的文件
     * @param fileKeys 文件标识
     */
    //TODO 把listener 转换成callback 然后 在弄断点续传
    public Call uploadFilesAsyn(String url, ArrayMap<String, String> params, File[] files, String[] fileKeys,
                                final UploadCallback callback, final StringCallback feedbackCallback) {
        if (callback != null) callback.onStart();
        UploadProgressListener listener = new UploadProgressListener() {
            @Override
            public void onUploadContent(final List<String> names, final List<String> values, final long byteCount) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onUploadContent(names, values, byteCount);
                    }
                });
            }

            @Override
            public void onProgress(final File file, final String key, final long totalLength, final long uploadLength, final boolean isFileNull) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onProgress(file, key, totalLength, uploadLength, isFileNull);
                    }
                });
            }
        };
        Request request = getInstance()._uploadFilesWithCallBackAsyn(url, params, files, fileKeys, listener);
        return _deliveryResult(request, feedbackCallback);
    }

    /**********************************************************************/
    private Call _deliveryResult(final Request request, final StringCallback callback) {
        Call call = mOkHttpClient.newCall(request);
        if (callback != null) callback.onStart(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onFailure(call, request, e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null)
                                callback.onResponse(call, response, string);
                        }
                    });
                } catch (final IOException e) {
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null)
                                callback.onResponseError(call, response, e);
                        }
                    });
                }
            }
        });
        return call;
    }

    /**
     * 异步下载文件
     *
     * @param destFileDir 本地文件存储的文件夹
     * @param callback
     */
    private Call _downloadAsyn(final Request request, final String destFileDir,
                               final String fileName, final DownloadCallback callback) {
        if (callback != null) callback.onStart(request);
        Call call = mOkHttpClient.newCall(request);
        Callback callback1 = new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                mDelivery.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null)
                            callback.onFailure(call, request, e);
                    }
                });
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, fileName);
                    fos = new FileOutputStream(file);
                    long total = response.body().contentLength();
                    long temp = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        temp += len;
                        int progress = (int) (temp * 1.0f / total * 100);
                        callback.onDownLoadProgress(call, response, progress);
                    }
                    fos.flush();
                    // 如果下载文件成功，第一个参数为文件的绝对路径
                    final String path = file.getAbsolutePath();
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null)
                                callback.onDownLoadFinish(call, response, path);
                        }
                    });
                } catch (final IOException e) {
                    mDelivery.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null)
                                callback.onResponseError(call, response, e);
                        }
                    });
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        };
        call.enqueue(callback1);
        return call;
    }
}
