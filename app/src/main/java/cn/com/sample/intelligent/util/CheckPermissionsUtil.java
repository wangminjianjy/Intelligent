package cn.com.sample.intelligent.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Creator: wangminjian
 * Create time: 2018/5/3.
 */

public class CheckPermissionsUtil {

    private Context mContext;
    private String[] needPermissions;
    private static volatile CheckPermissionsUtil mInstance; // 单利引用
    private static final int PERMISSION_REQUEST_CODE = 0;

    /**
     * 获取单例引用
     */
    public static CheckPermissionsUtil getInstance(Context context, String[] needPermissions) {
        CheckPermissionsUtil inst = mInstance;
        if (inst == null) {
            synchronized (CheckPermissionsUtil.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new CheckPermissionsUtil(context, needPermissions);
                    mInstance = inst;
                }
            }
        }
        return inst;
    }

    private CheckPermissionsUtil(Context context, String[] needPermissions) {
        this.mContext = context;
        this.needPermissions = needPermissions;
    }

    // 对权限的申请
    public void requestPermission() {
        checkPermissions(needPermissions);
    }

    /**
     * @since 2.5.0
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissionList = findDeniedPermissions(permissions);
        if (null != needRequestPermissionList && needRequestPermissionList.size() > 0) {
            ActivityCompat.requestPermissions((Activity) mContext,
                    needRequestPermissionList.toArray(new String[needRequestPermissionList.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(mContext, perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, perm)) {
                needRequestPermissionList.add(perm);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 检测是否所有的权限都已经授权
     */
    public boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断是否缺少权限
     */
    public static boolean lacksPermission(Context context, String[] permissions) {
        boolean isAllAllow = true;
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(context, perm) == PackageManager.PERMISSION_DENIED) {
                isAllAllow = false;
            }
        }
        return isAllAllow;
    }
}
