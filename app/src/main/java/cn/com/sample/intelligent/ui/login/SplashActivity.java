package cn.com.sample.intelligent.ui.login;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseActivity;
import cn.com.sample.intelligent.manager.NotchManager;
import cn.com.sample.intelligent.manager.SharedPreferencesManager;
import cn.com.sample.intelligent.ui.main.MainActivity;

public class SplashActivity extends BaseActivity {

    private TextView mSplashTv;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView(View contentView) {
        mSplashTv = contentView.findViewById(R.id.tv_second);

        // 支持显示到刘海区域
        NotchManager.getInstance().setDisplayInNotch(this);
    }

    @Override
    protected void bindEvent(View contentView) {
        mSplashTv.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        countDownTimer.start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_second) {
            countDownTimer.onFinish();
        }
    }

    CountDownTimer countDownTimer = new CountDownTimer(1000 * 4, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mSplashTv.setText(getString(R.string.hint_splash_time, (millisUntilFinished / 1000) + "s"));
        }

        @Override
        public void onFinish() {
            String user = SharedPreferencesManager.getUser();
            if (TextUtils.isEmpty(user)) {
                startActivity(LoginActivity.class, true);
            } else {
                startActivity(MainActivity.class, true);
            }
            cancel();
        }
    };
}
