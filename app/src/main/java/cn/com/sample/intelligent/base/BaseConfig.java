package cn.com.sample.intelligent.base;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/15.
 */
public class BaseConfig {

    public static final String HOSTURL = "http://192.168.43.16:59949";

    // 数据返回值
    public static final String CODE = "Code";                           // 返回数据值
    public static final String MSG = "ResultMsg";                       // 返回数据信息
    public static final String RESULT = "Result";                       // 返回数据结果

    public static final int NETWORK_READ_TIMEOUT = 30 * 1000;            // 读取超时时间
    public static final int NETWORK_CONNECT_TIMEOUT = 30 * 1000;         // 连接超时时间
    public static final int NETWORK_WRITE_TIMEOUT = 30 * 1000;           // 写入超时时间
}
