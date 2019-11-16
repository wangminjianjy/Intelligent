package cn.com.sample.intelligent.base.okhttp.upload;

import java.io.File;
import java.util.List;

/**
 * Created by dzl on 2017/3/29.
 */

public interface UploadProgressListener {

    void onUploadContent(List<String> names, List<String> values, long byteCount);

    void onProgress(File file, String key, long totalLength, long uploadLength, boolean isFileNull);

//    void onFinish(String s);
//
//    void onError(UploadInfo uploadInfo, String errorMsg, Exception e);
//
//    String parseNetworkResponse(Response response) throws Exception;
}
