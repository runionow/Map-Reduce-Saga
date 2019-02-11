package core.worker;

import common.TaskStatus;
import common.base.MapperBase;
import common.schedulars.Task;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;


public class ManagerConnect<K, V> implements Runnable {

    @Override
    public void run() {
//        reduceExecute("D:\\arun\\output\\temp");

        /**
         * Set Maximum number of retries
         */
        int retryCount = 5;

        try {
            Socket socket = waitToConnect(retryCount);
            MapperBase map = null;

            try {
                // [START] - Receive job request from manager
                InputStream in = socket.getInputStream();
                ObjectInputStream task_to_do = new ObjectInputStream(in);
                Task task = (Task) task_to_do.readObject();
                Task task1 = task;
                TaskHandler th = new TaskHandler(socket, task1);
                System.out.println("recieved Task");
                TaskStatus status = th.runJob(); // This can be a map job or an reduce job
                System.out.println("Sending the status to Manager Node : " + status.getStatus());
                // [END] - Receive job request from manager

                // [START] sending the Response to Manager
                OutputStream out = socket.getOutputStream();
                ObjectOutputStream object = new ObjectOutputStream(out);
                object.writeObject(status);
                object.close();
                out.close();
                // [END] sending the Response to Manager

            } catch (IOException e) {
                System.out.println("Manager Disconnected");
//                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Class definition not matched");
//                e.printStackTrace();
            }
            socket.close();

        } catch (InterruptedException e) {
            System.out.println("Manager Disconnected");
//            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Restarting the node to let know the manager for new jobs");
        // Upon successful completion start a new instance for new requests
        Thread worker = new Thread(new ManagerConnect());
        worker.start();
    }

    public static int MANAGER_PORT = 9080;

    public static Socket waitToConnect(int retryCount) throws InterruptedException {
        Socket socket = null;
        System.out.println("\nLooking for a Manager");
        int count = 0;
        while (true) {
            try {
                socket = new Socket("localhost", MANAGER_PORT);
                if (socket != null) {
                    break;
                }
            } catch (IOException e) {
                Thread.sleep(5000);
                count++;
                System.out.println("Looking for Manager Node, Retrying in next 5s");
                // e.printStackTrace();
            }

            if (count > retryCount - 1) {
                System.out.println("Reached Maximum number of retries shutting down the sockets and the process");
                System.exit(0);
            }
        }

        InetSocketAddress client = (InetSocketAddress) socket.getRemoteSocketAddress();
        System.out.println("Joined Manager Node :" + client.getPort() + " Hostname :" + client.getHostName());
        return socket;
    }
}
