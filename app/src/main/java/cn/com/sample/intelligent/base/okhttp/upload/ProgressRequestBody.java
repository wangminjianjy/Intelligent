package cn.com.sample.intelligent.base.okhttp.upload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by dzl on 2017/3/29.
 */
//TODO dont test
public class ProgressRequestBody extends RequestBody {

    private static final MediaType CONTENT_TYPE = MediaType.parse("multipart/form-data");

    private final List<String> names;
    private final List<String> values;
    private final List<File> filelist;
    private final List<String> fileKeylist;

    private UploadProgressListener mListener;

    ProgressRequestBody(List<String> names, List<String> values,
                        List<File> filelist, List<String> fileKeylist,
                        UploadProgressListener mListener) {
        this.names = Util.immutableList(names);
        this.values = Util.immutableList(values);
        this.filelist = Util.immutableList(filelist);
        this.fileKeylist = Util.immutableList(fileKeylist);
        this.mListener = mListener;
    }

    @Override
    public MediaType contentType() {
        return CONTENT_TYPE;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long byteCount = 0L;
        boolean countBytes = true;
        Buffer buffer;
        if (countBytes) {
            buffer = new Buffer();
        } else {
            buffer = sink.buffer();
        }
        for (int i = 0, size = names.size(); i < size; i++) {
            if (i > 0) buffer.writeByte('&');
            buffer.writeUtf8(names.get(i));
            buffer.writeByte('=');
            buffer.writeUtf8(values.get(i));
        }
        if (countBytes) {
            byteCount = buffer.size();
        }
        mListener.onUploadContent(names, values, byteCount);

        Source source;
        try {
            for (int i = 0; i < filelist.size(); i++) {
                //sink.writeAll(source);
                File file = filelist.get(i);
                source = Okio.source(file);
                Buffer buf = new Buffer();
                Long remaining = contentLength();
                for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                    sink.write(buf, readCount);
                    if (mListener != null) {
                        mListener.onProgress(file, fileKeylist.get(i), contentLength(), remaining -= readCount, remaining == 0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            buffer.clear();
        }
    }

    public static final class Builder {
        private final List<String> names = new ArrayList<>();
        private final List<String> values = new ArrayList<>();
        private final List<File> files = new ArrayList<>();
        private final List<String> fileKeys = new ArrayList<>();
        private UploadProgressListener mListener;

        public Builder add(File file, String fileKey) {
            files.add(file);
            fileKeys.add(fileKey);
            return this;
        }

        public Builder addEncoded(File file, String fileKey) {
            files.add(file);
            try {
                fileKeys.add(URLEncoder.encode(fileKey, Charset.defaultCharset().name()));
            } catch (UnsupportedEncodingException e) {
                fileKeys.add("");
            }
            return this;
        }

        public Builder add(String name, String value) {
            names.add(name);
            values.add(value);
            return this;
        }

        public Builder addEncoded(String name, String value) {
            try {
                names.add(URLEncoder.encode(name, Charset.defaultCharset().name()));
            } catch (UnsupportedEncodingException e) {
                names.add("");
            }
            try {
                values.add(URLEncoder.encode(value, Charset.defaultCharset().name()));
            } catch (UnsupportedEncodingException e) {
                values.add("");
            }
            return this;
        }

        public Builder addUploadProgressListener(UploadProgressListener lister) {
            this.mListener = lister;
            return this;
        }

        public ProgressRequestBody build() {
            return new ProgressRequestBody(names, values, files, fileKeys, mListener);
        }
    }
}
