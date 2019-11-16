package cn.com.sample.intelligent.net;

import androidx.collection.ArrayMap;

/**
 * Description ：net 请求体
 * creator ：wangminjian
 * Create time : 2018/4/16
 */

public abstract class BaseRequest implements Cloneable {

    protected Object key;
    protected String url;
    protected ArrayMap<String, Object> params;

    public BaseRequest() {
    }

    public BaseRequest clone() throws CloneNotSupportedException {
        BaseRequest requestInfo = (BaseRequest) super.clone();
        return requestInfo;
    }

    public Object getKey() {
        return this.key;
    }

    public void setKey(Object key) {
        this.key = key;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayMap<String, Object> getParams() {
        return this.params;
    }

    public void setParams(ArrayMap<String, Object> params) {
        this.params = params;
    }
}
