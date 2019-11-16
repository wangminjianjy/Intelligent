package cn.com.sample.intelligent.manager;

import android.content.Context;
import android.content.SharedPreferences;

import cn.com.sample.intelligent.base.CustomApplication;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/10/9.
 */
public class SharedPreferencesManager {

    public static String COMMON_SETTING = "COMMON_SETTING";

    /**
     * 保存用户名
     *
     * @param user
     */
    public static void saveUser(String user) {
        SharedPreferences sharedPreferences = CustomApplication.getContext().getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("common_user", user).commit();
    }

    /**
     * 获取用户名
     */
    public static String getUser() {
        SharedPreferences sharedPreferences = CustomApplication.getContext().getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        return sharedPreferences.getString("common_user", "");
    }

    /**
     * 保存验证码
     *
     * @param code
     */
    public static void saveCode(String code) {
        SharedPreferences sharedPreferences = CustomApplication.getContext().getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("common_code", code).commit();
    }

    /**
     * 获取验证码
     */
    public static String getCode() {
        SharedPreferences sharedPreferences = CustomApplication.getContext().getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        return sharedPreferences.getString("common_code", "");
    }

    /**
     * 保存验证码
     *
     * @param phoneCode
     */
    public static void savePhoneCode(String phoneCode) {
        SharedPreferences sharedPreferences = CustomApplication.getContext().getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("common_phoneCode", phoneCode).commit();
    }

    /**
     * 获取验证码
     */
    public static String getPhoneCode() {
        SharedPreferences sharedPreferences = CustomApplication.getContext().getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        return sharedPreferences.getString("common_phoneCode", "");
    }

    /**
     * 获取apk url
     */
    public static String getAppPath(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        return sharedPreferences.getString("common_version_url", "");
    }

    /**
     * 保存apk url
     */
    public static void saveAppPath(Context context, String appVersionFromNet) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("common_version_url", appVersionFromNet).commit();
    }

    /**
     * 获取apk本地名称
     */
    public static String getAppName(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        return sharedPreferences.getString("common_version_url", "");
    }

    /**
     * 保存apk本地名称
     */
    public static void saveAppName(Context context, String appVersionFromNet) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(COMMON_SETTING, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("common_version_url", appVersionFromNet).commit();
    }
}
