package xiaobaishushop.gtnw.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xiaobaishushop.gtnw.R;
import xiaobaishushop.gtnw.fragment.FragmentLPAClosedDashboard;
import xiaobaishushop.gtnw.fragment.FragmentLPAUnclosedDashboard;

public class LPADashboardActivity extends BaseAcctivity {

    @BindView(R.id.mt_lpa_dashboard)
    TabLayout mtLpaDashboard;
    @BindView(R.id.vp_lpa_unclosed_dashboard)
    ViewPager vpLpaUnclosedDashboard;
//    title
    private List<String> mTitle=  new ArrayList<>();
//    fragment list
    private List<Fragment> mFragment= new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpa_dashboard);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        vpLpaUnclosedDashboard.setOffscreenPageLimit(mFragment.size());
        vpLpaUnclosedDashboard.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        mtLpaDashboard.setupWithViewPager(vpLpaUnclosedDashboard);
    }

    private void initData() {
        mTitle.add("未关闭的审核项");
        mTitle.add("已关闭的审核项");

        mFragment.add(new FragmentLPAUnclosedDashboard());
        mFragment.add(new FragmentLPAClosedDashboard());
    }


}
