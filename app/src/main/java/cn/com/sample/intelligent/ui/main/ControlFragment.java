package cn.com.sample.intelligent.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseFragment;

public class ControlFragment extends BaseFragment {

    private TextView controlBack;
    private TextView controlTitle;
    private TextView controlStorage;
    private TextView controlTem;
    private TextView controlRH;
    private TextView controlJQ;
    private TextView controlBen;
    private TextView controlPM;
    private TextView controlCO;
    private ImageView controlAlert;

    public static ControlFragment newInstance() {
        Bundle args = new Bundle();
        ControlFragment fragment = new ControlFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_control;
    }

    @Override
    protected void initView(View contentView) {
        controlBack = contentView.findViewById(R.id.custom_back);
        controlTitle = contentView.findViewById(R.id.custom_title);
        controlStorage = contentView.findViewById(R.id.control_storage);
        controlTem = contentView.findViewById(R.id.param_wendu);
        controlRH = contentView.findViewById(R.id.param_rh);
        controlJQ = contentView.findViewById(R.id.param_jiaquan);
        controlBen = contentView.findViewById(R.id.param_ben);
        controlPM = contentView.findViewById(R.id.param_pm);
        controlCO = contentView.findViewById(R.id.param_co);
        controlAlert = contentView.findViewById(R.id.control_alert);
    }

    @Override
    protected void bindEvent(View mContentView) {
        controlStorage.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        controlBack.setVisibility(View.GONE);
        controlTitle.setText(getString(R.string.control_title));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.control_storage:
                getStorageData();
                break;
            default:
                break;
        }
    }

    private void getStorageData() {

    }
}
