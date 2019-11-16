package cn.com.sample.intelligent.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseFragment;
import cn.com.sample.intelligent.base.CustomApplication;
import cn.com.sample.intelligent.manager.DataCleanManager;
import cn.com.sample.intelligent.manager.SharedPreferencesManager;
import cn.com.sample.intelligent.net.HttpCallback;
import cn.com.sample.intelligent.net.HttpModel;
import cn.com.sample.intelligent.net.HttpResult;
import cn.com.sample.intelligent.ui.login.LoginActivity;
import cn.com.sample.intelligent.util.AlertDialogUtil;
import cn.com.sample.intelligent.util.AppUtil;
import cn.com.sample.intelligent.util.ToastUtil;

public class MyFragment extends BaseFragment {

    private TextView myBack;
    private TextView myTitle;
    private TextView myName;
    private TextView myQuit;
    private TextView myCacheNo;
    private TextView myVersionNo;
    private View cacheView;
    private View versionView;

    private AlertDialogUtil alertDialog;

    public static MyFragment newInstance() {
        Bundle args = new Bundle();
        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_my;
    }

    @Override
    protected void initView(View contentView) {
        myBack = contentView.findViewById(R.id.custom_back);
        myTitle = contentView.findViewById(R.id.custom_title);
        myName = contentView.findViewById(R.id.my_name);
        myQuit = contentView.findViewById(R.id.my_quit);
        myCacheNo = contentView.findViewById(R.id.my_cache_sum);
        myVersionNo = contentView.findViewById(R.id.my_version_num);
        cacheView = contentView.findViewById(R.id.my_cache);
        versionView = contentView.findViewById(R.id.my_version);
    }

    @Override
    protected void bindEvent(View mContentView) {
        cacheView.setOnClickListener(this);
        versionView.setOnClickListener(this);
        myQuit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        myBack.setVisibility(View.GONE);
        myTitle.setText(getString(R.string.my_title));

        setMyData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_cache:
                ClearCache();
                break;
            case R.id.my_version:
                Update();
                break;
            case R.id.my_quit:
                LoginOut();
                break;
            default:
                break;
        }
    }

    private void setMyData() {
        String cache;
        try {
            cache = DataCleanManager.getTotalCacheSize(CustomApplication.getContext());
            if (cache.equals("0.0 Byte")) {
                cache = "0.00 KB";
            }
        } catch (Exception e) {
            cache = "0.00 KB";
        }

        String versionCode = AppUtil.getVersionName(CustomApplication.getContext());

        myCacheNo.setText(cache);
        myVersionNo.setText(versionCode);

        myName.setText(SharedPreferencesManager.getUser());
    }

    private void ClearCache() {
        alertDialog = new AlertDialogUtil(getContext(), getString(R.string.my_hint_clear_cache), false,
                AlertDialogUtil.REQUEST_CODE_FIRST, onDialogClick);
        alertDialog.show();
    }

    private void Update() {


    }

    private void LoginOut() {
        alertDialog = new AlertDialogUtil(getContext(), getString(R.string.my_hint_exit), false,
                AlertDialogUtil.REQUEST_CODE_SECOND, onDialogClick);
        alertDialog.show();
    }

    AlertDialogUtil.OnDialogClickListener onDialogClick = new AlertDialogUtil.OnDialogClickListener() {
        @Override
        public void onDialogClick(int requestCode, boolean isPositive) {
            if (isPositive) {
                switch (requestCode) {
                    case AlertDialogUtil.REQUEST_CODE_FIRST:
                        alertDialog.dismiss();
                        DataCleanManager.clearAllCache(CustomApplication.getContext());
                        myCacheNo.setText(String.format(Locale.CHINA, "%.2f KB", 0.00));
                        ToastUtil.showSingleToast(getString(R.string.my_hint_cache_clear), Toast.LENGTH_SHORT);
                        break;
                    case AlertDialogUtil.REQUEST_CODE_SECOND:
                        Untying();
                        break;
                    default:
                        break;
                }
            }
        }
    };

    private void Untying() {
        String phoneCode = SharedPreferencesManager.getPhoneCode();
        HttpModel.getInstance().untying("userDisBunding", phoneCode, new HttpCallback(getActivity()) {
            @Override
            public void onSuccessStr(HttpResult httpResult) {
                SharedPreferencesManager.saveUser("");
                SharedPreferencesManager.saveCode("");
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
