package cn.com.sample.intelligent.ui.main;

import android.view.View;
import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import cn.com.sample.intelligent.R;
import cn.com.sample.intelligent.base.BaseActivity;
import cn.com.sample.intelligent.widget.AlarmRadioButton;
import cn.com.sample.intelligent.widget.CustomViewPager;

public class MainActivity extends BaseActivity {

    private CustomViewPager mViewPager;
    private MainPagerAdapter mAdapter;
    private ControlFragment controlFragment;
    private ReportFragment reportFragment;
    private MyFragment myFragment;

    private RadioGroup mMenuRg;
    private AlarmRadioButton mControlARb;
    private AlarmRadioButton mReportARb;
    private AlarmRadioButton mMyARb;

    private List<AlarmRadioButton> btnItems;
    private List<Fragment> fragments;

    private static final int VIEW_PAGER_START = 0;

    @Override
    protected int setLayoutResourceID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(View contentView) {
        mMenuRg = contentView.findViewById(R.id.radio_bar);
        mControlARb = mMenuRg.findViewById(R.id.tab_menu_control);
        mReportARb = mMenuRg.findViewById(R.id.tab_menu_report);
        mMyARb = mMenuRg.findViewById(R.id.tab_menu_my);
        mViewPager = findViewById(R.id.view_pager);

        mControlARb.dismissSign();
        mReportARb.dismissSign();
        mMyARb.dismissSign();
    }

    @Override
    protected void bindEvent(View contentView) {
        mViewPager.addOnPageChangeListener(pageChange);
        mMenuRg.setOnCheckedChangeListener(checkedChange);
    }

    @Override
    protected void initData() {
        fragments = new ArrayList<>();
        btnItems = new ArrayList<>();

        mViewPager.setOffscreenPageLimit(2);

        controlFragment = ControlFragment.newInstance();
        reportFragment = ReportFragment.newInstance();
        myFragment = MyFragment.newInstance();

        fragments.add(controlFragment);
        btnItems.add(mControlARb);
        fragments.add(reportFragment);
        btnItems.add(mReportARb);
        fragments.add(myFragment);
        btnItems.add(mMyARb);
        mAdapter = new MainPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setPagingEnabled(true);
        mViewPager.setAdapter(mAdapter);
        mMenuRg.check(btnItems.get(VIEW_PAGER_START).getId());
    }

    @Override
    public void onClick(View v) {

    }

    ViewPager.OnPageChangeListener pageChange = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_SETTLING) {
                mMenuRg.check(btnItems.get(mViewPager.getCurrentItem()).getId());
            }
        }
    };

    RadioGroup.OnCheckedChangeListener checkedChange = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            int index = 0;
            for (int i = 0; i < btnItems.size(); i++) {
                if (checkedId == btnItems.get(i).getId()) {
                    index = i;
                    break;
                }
            }
            mViewPager.setCurrentItem(index);
        }
    };
}
