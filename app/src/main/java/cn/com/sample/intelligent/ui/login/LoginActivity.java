package cn.com.sample.intelligent.ui.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseActivity;
import cn.com.sample.intelligent.bean.FactoryBean;
import cn.com.sample.intelligent.manager.SharedPreferencesManager;
import cn.com.sample.intelligent.net.HttpCallback;
import cn.com.sample.intelligent.net.HttpModel;
import cn.com.sample.intelligent.net.HttpResult;
import cn.com.sample.intelligent.ui.main.MainActivity;
import cn.com.sample.intelligent.util.AlertDialogUtil;
import cn.com.sample.intelligent.util.CheckPermissionsUtil;
import cn.com.sample.intelligent.util.ListAlert.AlertListUtil;
import cn.com.sample.intelligent.util.LogUtil;
import cn.com.sample.intelligent.util.ToastUtil;

public class LoginActivity extends BaseActivity {

    private TextView tvStorage;
    private EditText etKey;
    private TextView tvBind;

    private List<FactoryBean> factoryList;
    private List<String> factoryNameList;
    private AlertListUtil alertList;
    private FactoryBean factoryBean;
    private String key;
    private String factoryID;

    private String[] needPermissions = {
            Manifest.permission.READ_PHONE_STATE
    };
    private AlertDialogUtil alertDialog;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView(View contentView) {
        tvStorage = contentView.findViewById(R.id.storage_name);
        etKey = contentView.findViewById(R.id.key_code);
        tvBind = contentView.findViewById(R.id.login_binding);
    }

    @Override
    protected void bindEvent(View contentView) {
        tvStorage.setOnClickListener(this);
        tvBind.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        factoryList = new ArrayList<>();
        factoryNameList = new ArrayList<>();

        // 调用手机权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CheckPermissionsUtil.getInstance(this, needPermissions).requestPermission();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.storage_name:
                getFactoryData();
                break;
            case R.id.login_binding:
                checkLogin();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!CheckPermissionsUtil.getInstance(this, needPermissions).verifyPermissions(grantResults)) {
            // 权限有缺失
            alertDialog = new AlertDialogUtil(this, getString(R.string.hint_permission), true, AlertDialogUtil.REQUEST_CODE_FIRST, onDialogClick);
            alertDialog.show();
        }
    }

    AlertDialogUtil.OnDialogClickListener onDialogClick = new AlertDialogUtil.OnDialogClickListener() {
        @Override
        public void onDialogClick(int requestCode, boolean isPositive) {
            if (isPositive) {
                switch (requestCode) {
                    case AlertDialogUtil.REQUEST_CODE_FIRST:
                        CheckPermissionsUtil.getInstance(LoginActivity.this, needPermissions).requestPermission();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void getFactoryData() {
        factoryList.clear();
        factoryNameList.clear();
        HttpModel.getInstance().postNoParams("getFactoryList", new HttpCallback(this) {
            @Override
            public void onSuccessStr(HttpResult httpResult) {
                try {
                    JSONArray jsonArray = new JSONArray(httpResult.getActualResult1());
                    Gson gson = new Gson();
                    if (jsonArray.length() > 0) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FactoryBean factory = gson.fromJson(jsonArray.getJSONObject(i).toString(), FactoryBean.class);
                            factoryList.add(factory);
                            factoryNameList.add(factory.getFactoryName());
                        }
                    }
                    alertList = new AlertListUtil(LoginActivity.this, getString(R.string.login_storage_title), factoryNameList, onItemClick);
                    alertList.show();
                } catch (Exception e) {
                    LogUtil.d("LoginActivity", "...onSuccessStr... exception:" + e);
                }
            }
        });
    }

    AdapterView.OnItemClickListener onItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            alertList.dismiss();
            tvStorage.setText(factoryNameList.get(position));
            factoryBean = factoryList.get(position);
        }
    };

    private void checkLogin() {
        String storage = tvStorage.getText().toString();
        key = etKey.getText().toString();
        factoryID = factoryBean.getID();
        if (TextUtils.isEmpty(storage)) {
            ToastUtil.showSingleToast(getString(R.string.login_storage_hint), Toast.LENGTH_SHORT);
        } else if (TextUtils.isEmpty(key)) {
            ToastUtil.showSingleToast(getString(R.string.login_key_hint), Toast.LENGTH_SHORT);
        } else if (!factoryBean.getRegCode().equals(key)) {
            ToastUtil.showSingleToast(getString(R.string.login_key_error), Toast.LENGTH_SHORT);
        } else {
            binding();
        }
    }

    private void binding() {
        String phoneCode = SharedPreferencesManager.getPhoneCode();
        if (TextUtils.isEmpty(phoneCode)) {
            // 调用手机权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) ==
                    PackageManager.PERMISSION_GRANTED) {
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                phoneCode = telephonyManager.getDeviceId();
                SharedPreferencesManager.savePhoneCode(phoneCode);
            } else {
                CheckPermissionsUtil.getInstance(this, needPermissions).requestPermission();
            }
        } else {
            HttpModel.getInstance().binding("userBunding", phoneCode, key, factoryID, new HttpCallback(this) {
                @Override
                public void onSuccessStr(HttpResult httpResult) {
                    SharedPreferencesManager.saveUser(factoryBean.getFactoryName());
                    SharedPreferencesManager.saveCode(factoryBean.getRegCode());
                    startActivity(MainActivity.class, true);
                }
            });
        }
    }
}
