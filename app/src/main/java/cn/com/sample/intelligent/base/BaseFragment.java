package cn.com.sample.intelligent.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/15.
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {

    protected View contentView;
    protected Toolbar toolBar;

    protected abstract int setLayoutResourceID();

    protected abstract void initView(View contentView);

    protected abstract void bindEvent(View mContentView);

    protected abstract void initData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(setLayoutResourceID(), container, false);
        initView(contentView);
        bindEvent(contentView);
        initData();
        return contentView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View v) {

    }
}
