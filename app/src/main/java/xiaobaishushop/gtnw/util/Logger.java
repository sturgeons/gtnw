package xiaobaishushop.gtnw.util;
/**
 * 2018年7月21日21:30:46
 * log封装类
 * V0.0.1
 * **/
import android.util.Log;

public class Logger {
    private static String getObject2String(Object o) {
        return o == null ? "null" : o.toString();
    }
    /**
     * 打印正常信息
    * @param msg
    * */
    public static void i(Object msg) {
        if (Constant.DEBUG_FLAG)
        Log.i(Constant.TAG,getObject2String(msg));
    }
    /**
     * 打印警告信息
     * @param msg
     * */
    public static void w(Object msg) {
        if (Constant.DEBUG_FLAG)
            Log.w(Constant.TAG,getObject2String(msg));
    }
    /**
     * 打印调试信息
     * @param msg
     * */
    public static void d(Object msg) {
        if (Constant.DEBUG_FLAG)
            Log.d(Constant.TAG,getObject2String(msg));
    }
}
