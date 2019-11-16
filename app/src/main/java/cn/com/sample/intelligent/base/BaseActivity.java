package cn.com.sample.intelligent.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Description:
 * Creator : wangminjian
 * Create time : 2019/11/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    protected View contentView;
    protected Toolbar toolBar;

    // 设置布局
    protected abstract int setLayoutResourceID();

    // 初始化视图
    protected abstract void initView(View contentView);

    // 绑定事件
    protected abstract void bindEvent(View contentView);

    // 初始化数据
    protected abstract void initData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        contentView = LayoutInflater.from(this).inflate(setLayoutResourceID(), null);
        setContentView(contentView);
        initView(contentView);
        bindEvent(contentView);
        initData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentView = null;
    }

    protected void startActivity(Class activity, boolean finish) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        if (finish) {
            finish();
        }
    }
}
