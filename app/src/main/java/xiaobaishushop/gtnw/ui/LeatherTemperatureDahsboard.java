package xiaobaishushop.gtnw.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import xiaobaishushop.gtnw.R;
import xiaobaishushop.gtnw.tcp.TcpServer;
import xiaobaishushop.gtnw.util.Logger;

public class LeatherTemperatureDahsboard extends BaseAcctivity {
    public static final int GET_MSG = 200;
    public static final int GET_TEMP_DATA = 220;
    @BindView(R.id.bt_start_server)
    Button btStartServer;
    @BindView(R.id.bt_refresh)
    Button btRefresh;
    @BindView(R.id.bt_test)
    Button btTest;
    @BindView(R.id.tv_system_info)
    TextView tvSystemInfo;
    @BindView(R.id.tv_temperature)
    TextView tvTemperature;
    @BindView(R.id.lc_temperature)
    LineChart lcTemperature;
    @BindView(R.id.tv_bumidity)
    TextView tvBumidity;
    @BindView(R.id.lc_bumidity)
    LineChart lcBumidity;

    private TcpServer tcpServer;
    public static String buffStr;
    public static byte[] buffByte;
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    ExecutorService exec = Executors.newCachedThreadPool();
    private UpdateUncloseObservationsHandler handler = new UpdateUncloseObservationsHandler(this);
    private TimerGetDataHandler getData = new TimerGetDataHandler(this);
    private MybroadcastReceiver mybroadcastReceiver = new MybroadcastReceiver();
    public boolean isCanGetInfo = false;
    private int restSecond = 5;
    public TemperatureAndBumidityData temperatureAndBumidityData = new TemperatureAndBumidityData(0f, 0f);

    //定义 一个 Timmer
    public class timmer implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    if (tcpServer.SST.size() > 0) {
                        Logger.i(tcpServer.SST.size());
                        Thread.sleep(1000);
                        Message msg = Message.obtain();
                        getData.sendMessage(msg);
                    }
                    Logger.i("Timer 启动");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mybroadcastReceiver);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leather_temperature_dashboard);
        ButterKnife.bind(this);
        context = this;
        tcpServer = new TcpServer(2500);
        exec.execute(tcpServer);
        bindReceiver();
        initView();
    }

    private void initView() {
        lcBumidity.setDrawBorders(true);
        lcBumidity.setTouchEnabled(true);
        lcBumidity.setScaleEnabled(false);
        lcBumidity.setDrawGridBackground(false);
        lcTemperature.setDrawBorders(true);
        lcTemperature.setTouchEnabled(true);
        lcTemperature.setScaleEnabled(false);
        lcTemperature.setDrawGridBackground(false);

        YAxis temperatureChartYaxis = lcTemperature.getAxisLeft();
        LimitLine temperaturelowlimitLine = new LimitLine(48f, "低温温度");
        temperaturelowlimitLine.setTextColor(Color.RED);
        temperatureChartYaxis.addLimitLine(temperaturelowlimitLine);

        LimitLine temperatureUpperimitLine = new LimitLine(50f, "上线温度");
        temperatureUpperimitLine.setTextColor(Color.RED);
        temperatureChartYaxis.addLimitLine(temperatureUpperimitLine);
        temperatureChartYaxis.setAxisMinimum(32f);
        temperatureChartYaxis.setAxisMaximum(56f);


        YAxis bumidityChartYaxis = lcBumidity.getAxisLeft();
//        LimitLine bumiditylowlimitLine = new LimitLine(48f, "低温湿度");
//        bumiditylowlimitLine.setTextColor(Color.RED);
//        bumidityChart.addLimitLine(bumiditylowlimitLine);
//
//        LimitLine bumidityUpperimitLine = new LimitLine(50f, "上线湿度");
//        bumidityUpperimitLine.setTextColor(Color.RED);
//        bumidityChart.addLimitLine(bumidityUpperimitLine);
        bumidityChartYaxis.setAxisMaximum(80f);
        bumidityChartYaxis.setAxisMinimum(10f);


        LineData temperatureChartLD = new LineData();
        lcTemperature.setData(temperatureChartLD);

        LineData bumidityChartLD = new LineData();
        lcBumidity.setData(bumidityChartLD);

    }

    //注册服务广播
    private void bindReceiver() {
        IntentFilter intentFilter = new IntentFilter("tcpReceiver");
        intentFilter.addAction("tcpReceiverMsg");
        registerReceiver(mybroadcastReceiver, intentFilter);
    }

    // 发送请求数据 通常是0
    public void getTemperatureAndBumidity(final int i) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
//                判断是否可以发送数据
                if (tcpServer.SST.size() > 0) {
                    byte[] sb = new byte[12];
                    sb[0] = 0x00;
                    sb[1] = 0x00;
                    sb[2] = 0x00;
                    sb[3] = 0x00;
                    sb[4] = 0x00;
                    sb[5] = 0x06;
                    sb[6] = 0x01;
                    sb[7] = 0x03;
                    sb[8] = 0x00;
                    sb[9] = 0x00;
                    sb[10] = 0x00;
                    sb[11] = 0x02;
                    tcpServer.SST.get(i).send(sb);
                }
            }
        });
    }

    private Float count = 0f;

    //更新VIEW 显示数据
    public void updateView() {
        tvBumidity.setText(temperatureAndBumidityData.getBumidityString());
        tvTemperature.setText(temperatureAndBumidityData.getTemperatureString());

        LineData temperatureChartLD = lcTemperature.getLineData();
        if (temperatureChartLD != null) {
            ILineDataSet temperatureChartDS = temperatureChartLD.getDataSetByIndex(0);
            if (temperatureChartDS == null) {
                temperatureChartDS = getNewDataSet("Temperature");
                temperatureChartLD.addDataSet(temperatureChartDS);
            }
            if (temperatureChartDS.getEntryCount() > 200) {
                temperatureChartDS.removeFirst();
            }
            temperatureChartLD.addEntry(new Entry(count, temperatureAndBumidityData.getTemperature()), 0);
            lcTemperature.notifyDataSetChanged();
            lcTemperature.invalidate();
            lcTemperature.getXAxis().setAxisMinimum(count-200);

        }

        LineData bumidityChartLD = lcBumidity.getLineData();
        if (bumidityChartLD != null) {
            ILineDataSet bumidityChartDS = bumidityChartLD.getDataSetByIndex(0);
            if (bumidityChartDS == null) {
                bumidityChartDS = getNewDataSet("Bumidity");
                bumidityChartLD.addDataSet(bumidityChartDS);
            }
            if (bumidityChartDS.getEntryCount() > 200) {
                bumidityChartDS.removeFirst();
            }
            bumidityChartLD.addEntry(new Entry(count, temperatureAndBumidityData.getBumidity()), 0);
            lcBumidity.notifyDataSetChanged();
            lcBumidity.invalidate();
            lcBumidity.getXAxis().setAxisMinimum(count-200);
        }
        count++;
    }

    //    生成dataset 图表对象
    private LineDataSet getNewDataSet(String name) {
        LineDataSet set = new LineDataSet(null, name);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(6f);
        set.setCircleRadius(4f);
        set.setDrawCircles(false);
        set.setFillAlpha(65);
        set.setFillColor(Color.rgb(144, 10, 150));
        set.setHighLightColor(Color.rgb(244, 117, 117));
        set.setDrawValues(false);
        return set;

    }


    //跟新倒计时
    public void updateTimerInfo() {
        restSecond--;
        if (restSecond <= 0) {
            getTemperatureAndBumidity(0);
            restSecond = 5;
        }
        btRefresh.setText("刷新(" + restSecond + "S)");
    }

    @OnClick({R.id.bt_start_server, R.id.bt_refresh, R.id.tv_temperature, R.id.tv_bumidity, R.id.bt_test})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_start_server:
                Logger.i("开始计时");
                new Thread(new timmer()).start();
                break;
            case R.id.bt_refresh:
                getTemperatureAndBumidity(0);
                break;
            case R.id.tv_temperature:
                break;
            case R.id.tv_bumidity:
                break;
            case R.id.bt_test:
                Toast.makeText(LeatherTemperatureDahsboard.this,"update",Toast.LENGTH_LONG).show();
                this.temperatureAndBumidityData.setTemperature((float) (Math.random()*30f+10f));
                this.temperatureAndBumidityData.setBumidity((float) (Math.random()*30f+10f));
                this.updateView();
                break;
        }
    }

    private class MybroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            Message message = Message.obtain();
            switch (mAction) {
                case "tcpReceiver":
                    Logger.i("收到：" + Arrays.toString(intent.getByteArrayExtra("tcpReceiver")));
                    message.what = GET_TEMP_DATA;
                    message.obj = buffByte;
                    break;
                case "tcpReceiverMsg":
                    message.what = GET_MSG;
                    message.obj = buffStr;
                    break;
            }
            handler.sendMessage(message);
        }
    }

    public static class UpdateUncloseObservationsHandler extends Handler {
        final WeakReference<LeatherTemperatureDahsboard> mWeakReference;

        UpdateUncloseObservationsHandler(LeatherTemperatureDahsboard mWeakReference) {
            this.mWeakReference = new WeakReference<>(mWeakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LeatherTemperatureDahsboard activity = mWeakReference.get();
//          如果队列有传感器是 打开刷新
            if (activity.tcpServer.SST.size() > 0) {
                activity.btRefresh.setEnabled(true);
                activity.isCanGetInfo = true;
            } else {
                activity.btRefresh.setEnabled(false);
                activity.isCanGetInfo = false;
            }

            switch (msg.what) {
                case GET_MSG:
//          信息加到信息面板
                    activity.tvSystemInfo.append(msg.obj.toString() + "\n");
                    break;
                case GET_TEMP_DATA:

                    byte[] buff = (byte[]) msg.obj;
                    if (buff.length > 11) {
                        try {
                            float bumidity = (buff[9] * 16f * 16f + Math.abs(buff[10])) / 100f;
                            float tempterature = (buff[11] * 16f * 16f + Math.abs(buff[12]) - 27155f) / 100f;
                            activity.temperatureAndBumidityData.setBumidity(bumidity);
                            activity.temperatureAndBumidityData.setTemperature(tempterature);
                            activity.updateView();
                        } catch (Exception e) {
                            activity.tvSystemInfo.append("INFO:" + e.toString() + "\n");
                        }

                    }

//                    activity.tvSystemInfo.append("INFO:温度" + tempterature + "湿度" + bumidity + "\n");
                    break;
            }
        }
    }

    public static class TimerGetDataHandler extends Handler {
        final WeakReference<LeatherTemperatureDahsboard> mWeakReference;

        TimerGetDataHandler(LeatherTemperatureDahsboard mWeakReference) {
            this.mWeakReference = new WeakReference<>(mWeakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            LeatherTemperatureDahsboard activity = mWeakReference.get();
            activity.updateTimerInfo();
            super.handleMessage(msg);
        }
    }

    public class TemperatureAndBumidityData {
        float Temperature;
        float Bumidity;

        public void setTemperature(float temperature) {
            Temperature = temperature;
        }

        public float getTemperature() {
            return Temperature;
        }

        public float getBumidity() {
            return Bumidity;
        }

        public void setBumidity(float bumidity) {
            Bumidity = bumidity;

        }

        public TemperatureAndBumidityData(float temperature, float bumidity) {
            Temperature = temperature;
            Bumidity = bumidity;
        }

        public String getTemperatureString() {
            return String.valueOf(Temperature) + "℃";
        }

        public String getBumidityString() {
            return String.valueOf(Bumidity) + "%";
        }
    }

}
