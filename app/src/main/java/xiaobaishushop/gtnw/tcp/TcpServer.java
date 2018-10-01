package xiaobaishushop.gtnw.tcp;

import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import xiaobaishushop.gtnw.ui.LeatherTemperatureDahsboard;
import xiaobaishushop.gtnw.util.Logger;

public class TcpServer implements Runnable {
    public static final int SUCESS_GET_MSG_FROM_CLIENT = 500;

    public static final int SERVERSOCKET_TIMEOUT = 5000;
    private int port;
    private boolean isListen = true;
    public ArrayList<ServerSocketThread> SST = new ArrayList<>();

    public TcpServer(int port) {
        this.port = port;
    }

    public void setListen(boolean b) {
        this.isListen = b;
    }

    public void clsoeSelf() {
        isListen = false;
        for (ServerSocketThread s : SST) {
            s.isRun = false;
        }
        SST.clear();
    }

    private Socket getSocket(ServerSocket serverSocket) {
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            Logger.w("监听超时");
//            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(SERVERSOCKET_TIMEOUT);
            MsgPool.getsInstance().start();

            while (isListen) {
                Socket socket = getSocket(serverSocket);
                if (socket != null) {
                    Logger.i("RUN LISTENING");
                  ServerSocketThread st=  new ServerSocketThread(socket);
                  MsgPool.getsInstance().addMsgListener(st);
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   public class ServerSocketThread extends Thread implements MsgPool.MsgComingListener {
        public boolean isRun=true;
        private Socket socket;
        private PrintWriter pw;
        private InputStream is = null;
        private OutputStream os = null;
        private String ip;

        public ServerSocketThread(Socket socket) {
            this.socket = socket;

            ip = socket.getInetAddress().toString();
            Logger.i("Server Socket Thread Get Ip:" + ip);

            Intent intent=new Intent();
            intent.setAction("tcpReceiverMsg").putExtra("tcpReceiverMsg","连接成功");
            LeatherTemperatureDahsboard.buffStr=ip+"连接成功";
            LeatherTemperatureDahsboard.context.sendBroadcast(intent);

            try {
                socket.setSoTimeout(SERVERSOCKET_TIMEOUT);
                os = socket.getOutputStream();
                is = socket.getInputStream();
                pw = new PrintWriter(os, true);
                start();
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void send(String str) {
            pw.println(str);
            pw.flush();
        }

        public void send(byte[] bytes) {
            try {
                os.write(bytes, 0, bytes.length);
                os.flush();
//                Intent intent=new Intent();
//                intent.setAction("tcpReceiverMsg");
//                LeatherTemperatureDahsboard.buffStr=ip+"发送"+String.valueOf(bytes);
//                LeatherTemperatureDahsboard.context.sendBroadcast(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

//            BufferedReader br=new BufferedReader(new InputStreamReader(is));
//            String line;
//            try {
//               while ((line= br.readLine())!=null){
//                   Logger.i("read="+line);
////                转发消息
//                   MsgPool.getsInstance().sendMsg(line);
//               };
//            } catch (IOException e) {
//                e.printStackTrace();
//            }


            byte[] buff = new byte[18];
            SST.add(this);
            int recLen;
            while (isRun && !socket.isClosed() && !socket.isInputShutdown()) {
                try {
                    ByteArrayOutputStream baos=new ByteArrayOutputStream();
                  if (-1!=(recLen=is.read(buff))){
                      baos.write(buff,0,recLen);
                      baos.flush();
                  }
                    Intent intent=new Intent();
                    intent.setAction("tcpReceiver");
                    LeatherTemperatureDahsboard.buffByte=baos.toByteArray();
                    LeatherTemperatureDahsboard.context.sendBroadcast(intent);
                } catch (IOException e) {
                    Logger.w(ip+"还没收到信息！");
//                    e.printStackTrace();
                }
            }
            try {
                socket.close();
                SST.clear();
                Logger.i("SOCKET "+ip+" CLOSE!");
            } catch (IOException e) {
//                e.printStackTrace();
                Logger.i("SOCKET "+ip+" CLOSE!");
            }
        }

       @Override
       public void onMsgComing(String str) {
           Logger.i("收到数据"+str);
       }
   }

}
