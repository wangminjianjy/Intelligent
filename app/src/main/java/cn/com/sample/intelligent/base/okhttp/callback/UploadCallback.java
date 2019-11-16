package cn.com.sample.intelligent.base.okhttp.callback;

import java.io.File;
import java.util.List;

/**
 * Created by dzl on 2017/3/29.
 */
public interface UploadCallback extends BaseCallBack {

    void onStart();

    void onUploadContent(List<String> names, List<String> values, long byteCount);

    void onProgress(File file, String key, long totalLength, long uploadLength, boolean isFileNull);
}