package xiaobaishushop.gtnw.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import xiaobaishushop.gtnw.R;
import xiaobaishushop.gtnw.adapter.SystemInfoAdapter;
import xiaobaishushop.gtnw.bean.BeanKeyValue;
import xiaobaishushop.gtnw.util.SystemInfo;


public class SystemInfoActivity extends BaseAcctivity {
    @BindView(R.id.lv_system_info)
    ListView lvSystemInfo;

    private List<BeanKeyValue> beanKeyValues = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_info);
        ButterKnife.bind(this);

        initData();
        SystemInfoAdapter systemInfoAdapter = new SystemInfoAdapter(SystemInfoActivity.this, R.layout.listview_key_value_item, beanKeyValues);
        lvSystemInfo.setAdapter(systemInfoAdapter);

    }

    private void initData() {
        beanKeyValues.add(new BeanKeyValue("SDK VERSION", SystemInfo.getSystemVersion()));
        beanKeyValues.add(new BeanKeyValue("DISPLAY HEIGHT",String.valueOf( this.getBaseContext().getResources().getDisplayMetrics().heightPixels)));
        beanKeyValues.add(new BeanKeyValue("DISPLAY WIDTH",String.valueOf( this.getBaseContext().getResources().getDisplayMetrics().widthPixels)));
        beanKeyValues.add(new BeanKeyValue("WIFI IP ADRESS",SystemInfo.getLocalIpAdress(this)));
        beanKeyValues.add(new BeanKeyValue("DATE TIME",SystemInfo.getSytemInfoDateTime()));
    }
}
