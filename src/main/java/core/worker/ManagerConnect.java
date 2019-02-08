package core.worker;

// TODO find for any manager and connect to the managers
// TODO Send the heart beat to the manager

import java.io.IOException;
import java.net.Socket;

public class ManagerConnect implements Runnable {

    @Override
    public void run() {
        // Wait until manager is available
        Socket socket = null;
        int retryCount = 0;
        try {
            waitToConnect(retryCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        try {
//            Socket socket = new Socket("localhost", 9080);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Connected!");
        while (true) {

        }
    }

    public static void waitToConnect(int retryCount) throws InterruptedException {
        Socket socket = null;
        System.out.println("Looking for a Manager");
        while (true) {
            try {
                socket = new Socket("localhost", 9080);
                if (socket != null || retryCount == 5) {
                    break;
                }
            } catch (IOException e) {
                Thread.sleep(5000);
                retryCount++;
                System.out.println("Looking for Manager Node, Retrying in next 5s");
                // e.printStackTrace();
            }
        }
    }
}
