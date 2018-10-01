package xiaobaishushop.gtnw.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SystemInfo {
    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getLocalIpAdress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return int2ip(wifiInfo.getIpAddress());

    }

    private static String int2ip(int ipInt) {
        return String.valueOf(ipInt & 0xFF) + "." +
                ((ipInt >> 8) & 0xFF) + "." +
                ((ipInt >> 16) & 0xFF) + "." +
                ((ipInt >> 24) & 0xFF);
    }
    public  static String getSytemInfoDateTime(){
        SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date= new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
