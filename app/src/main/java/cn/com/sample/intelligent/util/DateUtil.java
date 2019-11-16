package cn.com.sample.intelligent.util;

import android.annotation.SuppressLint;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/10/8.
 */
public class DateUtil {

    @SuppressLint("SimpleDateFormat")
    public static String getNowDateTime() {
        SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMddHHmmss");
        return s_format.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateTime() {
        SimpleDateFormat s_format = new SimpleDateFormat("yyyyMMdd");
        return s_format.format(new Date());
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateTime(String formatStr) {
        String format = formatStr;
        if (format == null || format.length() <= 0) {
            format = "yyyyMMddHHmmss";
        }
        SimpleDateFormat s_format = new SimpleDateFormat(format);
        return s_format.format(new Date());
    }

    // 将时间戳变化为yyyy-MM-dd
    @SuppressLint("SimpleDateFormat")
    public static String changeDateTime(String dateTime) {
        String time = dateTime.substring(6, dateTime.length() - 7);
        Date date = new Date(Long.parseLong(time));
        SimpleDateFormat s_format = new SimpleDateFormat("yyyy-MM-dd");
        return s_format.format(date);
    }

    // 将时间戳变化为yyyy-MM-dd 例：2018-04-17T20:15:45.253
    public static String parseCSharpTime(String dateTime) {
        String time = dateTime.substring(0, dateTime.indexOf("T"));
        return time;
    }

    // 将时间戳变化为yyyy-MM-dd hh:mm 例：2018-04-17T20:15:45.253
    public static String parseDeleteTTime(String dateTime) {
        String time = dateTime.substring(0, 16);
        String newTime = time.replace('T', ' ');
        return newTime;
    }

    // 获取网络时间
    public static String getNetworkTime() {
//        String webUrl1 = "http://www.bjtime.cn";        // bjTime
        String webUrl2 = "http://www.baidu.com";        // 百度
//        String webUrl3 = "http://www.taobao.com";       // 淘宝
//        String webUrl4 = "http://www.ntsc.ac.cn";       // 中国科学院国家授时中心
//        String webUrl5 = "http://www.360.cn";           // 360
//        String webUrl6 = "http://www.beijing-time.org"; // beijing-time

//        System.out.println(getWebsiteDatetime(webUrl1) + " [bjtime]");
//        System.out.println(getWebsiteDatetime(webUrl2) + " [百度]");
//        System.out.println(getWebsiteDatetime(webUrl3) + " [淘宝]");
//        System.out.println(getWebsiteDatetime(webUrl4) + " [中国科学院国家授时中心]");
//        System.out.println(getWebsiteDatetime(webUrl5) + " [360安全卫士]");
//        System.out.println(getWebsiteDatetime(webUrl6) + " [beijing-time]");

        return getWebsiteDatetime(webUrl2);
    }

    // 获取指定网站的日期时间
    private static String getWebsiteDatetime(String webUrl) {
        try {
            URL url = new URL(webUrl);                  // 取得资源对象
            URLConnection uc = url.openConnection();    // 生成连接对象
            uc.connect();                               // 发出连接
            long ld = uc.getDate();                     // 读取网站日期时间
            Date date = new Date(ld);                   // 转换为标准时间对象
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);   // 输出北京时间
            return sdf.format(date);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
