package cn.com.sample.intelligent.net;

/**
 * Description ： 网络请求返回code
 * creator ：但子龙
 * Create time : 2018/4/23
 */

public interface ResultCode {

    int RESULT_STATUS_SUCCESS = 200;                // 返回数据成功
    int RESULT_STATUS_ERROR_SYSTOM = 61450;         // 返回数据失败 -- 系统错误
    int RESULT_STATUS_ERROR_PARAMETER = 61451;      // 返回数据失败 -- 错误的参数
    int RESULT_STATUS_ONLY_POST = 43002;            // 返回数据失败 -- 只允许Post请求
    int RESULT_STATUS_NO_COMPANY = 61332;           // 返回数据失败 -- 没有单位
    int RESULT_STATUS_MAX_PARAMETER = 61452;        // 返回数据失败 -- 参数长度错误
    int RESULT_STATUS_ERROR_FILE = 61415;           // 返回数据失败 -- 错误的文件类型
    int RESULT_STATUS_NO_OBJECT = 61416;            // 返回数据失败 -- 找不到对象,请确认对象是否已删除！
    int RESULT_STATUS_ERROR_VALUE = 61417;          // 返回数据失败 -- 参数赋值时错误，无法保存！
    int RESULT_STATUS_UNDEFINITION = 9999;          // 返回数据失败 -- 未定义错误
    int RESULT_STATUS_WEB = 500;                    // 返回数据失败 -- 未定义错误
}
