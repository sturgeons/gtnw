package xiaobaishushop.gtnw.tcp;

import java.net.Socket;

public class ClientTask extends Thread {
    private Socket socket;

    public ClientTask(Socket socket) {
        this.socket = socket;
    }

}
