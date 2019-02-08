package core.master;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class WorkerHandler implements Runnable {
    private static final int WORKER_PORT = 9080;
    private static final int BUFFER_SIZE = 2044;


    @Override
    public void run() {

        ServerSocket ss = null;
        {
            try {
                ss = new ServerSocket(WORKER_PORT);
            } catch (IOException e) {
                System.out.println("Socket already in use");
                e.printStackTrace();
            }
        }
        System.out.println("\nWaiting for worker nodes to join the Manager....");
        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = ss.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            InetSocketAddress client = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
            System.out.println("New worker Node joined from port :" + client.getPort() + "Hostname :" + client.getHostName());
            Socket finalClientSocket = clientSocket;
            new Thread(() -> {
                InputStream in;
                try {
                    in = finalClientSocket.getInputStream();
                    OutputStream out = finalClientSocket.getOutputStream();
                    byte[] buffer = new byte[BUFFER_SIZE];

                    while (finalClientSocket.isConnected()) {
                        int len = in.read(buffer);
                        String data = new String(buffer);
                        System.out.println(data);
                        if (len > 0) {
                            out.write(buffer, 0, len);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
