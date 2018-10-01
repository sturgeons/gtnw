package xiaobaishushop.gtnw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tencent.bugly.crashreport.CrashReport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xiaobaishushop.gtnw.ui.LPADashboardActivity;
import xiaobaishushop.gtnw.ui.LeatherTemperatureDahsboard;
import xiaobaishushop.gtnw.ui.SystemInfoActivity;
import xiaobaishushop.gtnw.util.Constant;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt_lpa_dashboarad)
    Button btLpaDashboarad;
    @BindView(R.id.bt_tcp)
    Button btTcp;
    @BindView(R.id.bt_system_info)
    Button btSystemInfo;
    @BindView(R.id.bt_bugly)
    Button btBugly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CrashReport.initCrashReport(getApplicationContext(), Constant.BUGLY_ID, false);
        ButterKnife.bind(this);

    }


    @OnClick({R.id.bt_lpa_dashboarad, R.id.bt_tcp, R.id.bt_system_info,R.id.bt_bugly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_lpa_dashboarad:
                startActivity(new Intent(MainActivity.this, LPADashboardActivity.class));
                break;
            case R.id.bt_tcp:
                startActivity(new Intent(MainActivity.this, LeatherTemperatureDahsboard.class));
                break;
            case R.id.bt_system_info:
                startActivity(new Intent(MainActivity.this, SystemInfoActivity.class));
                break;
            case  R.id.bt_bugly:
                CrashReport.testJavaCrash();
                break;
        }
    }

}
