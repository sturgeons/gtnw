package xiaobaishushop.gtnw.tcp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class MsgPool {
    private static MsgPool sInstance = new MsgPool();
    private LinkedBlockingDeque<String> mQueue = new LinkedBlockingDeque<>();

    public static MsgPool getsInstance() {
        return sInstance;
    }

    public void start() {
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String str = mQueue.take();
                        notifyMsgComing(str);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private void notifyMsgComing(String str) {
        for (MsgComingListener msgComingListener : mListeners) {
            msgComingListener.onMsgComing(str);
        }
    }

    public interface MsgComingListener {
        void onMsgComing(String str);
    }

    private List<MsgComingListener> mListeners = new ArrayList<>();

    public void addMsgListener(MsgComingListener msgComingListener) {
        mListeners.add(msgComingListener);
    }

    private MsgPool() {

    }

    public void sendMsg(String msg) {
        try {
            mQueue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
