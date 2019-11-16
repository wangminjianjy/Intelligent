package cn.com.sample.intelligent.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseFragment;

public class ReportFragment extends BaseFragment {

    private TextView reportBack;
    private TextView reportTitle;

    public static ReportFragment newInstance() {
        Bundle args = new Bundle();
        ReportFragment fragment = new ReportFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutResourceID() {
        return R.layout.fragment_report;
    }

    @Override
    protected void initView(View contentView) {
        reportBack = contentView.findViewById(R.id.custom_back);
        reportTitle = contentView.findViewById(R.id.custom_title);
    }

    @Override
    protected void bindEvent(View mContentView) {

    }

    @Override
    protected void initData() {
        reportBack.setVisibility(View.GONE);
        reportTitle.setText(getString(R.string.report_title));
    }
}
