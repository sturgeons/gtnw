package xiaobaishushop.gtnw.biz;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import xiaobaishushop.gtnw.MainActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {
    String ACTION = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent mainActivityIntent = new Intent(context, MainActivity.class); // 要启动的Activity
            mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainActivityIntent);
        }
    }
}
