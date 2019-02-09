package core.worker;

import common.Tuple;
import common.base.MapperBase;
import common.collectors.Collector;
import common.schedulars.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ManagerConnect implements Runnable {

    public static Socket waitToConnect(int retryCount) throws InterruptedException {
        Socket socket = null;
        System.out.println("\nLooking for a Manager");
        int count = 0;
        while (true) {
            try {
                socket = new Socket("localhost", 9080);
                if (socket != null) {
                    socket.close();
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

    @Override
    public void run() {
        /**
         * Set Maximum number of retries
         */
        int retryCount = 5;

        try {
            Socket socket = waitToConnect(retryCount);
            MapperBase map = null;

            try {

                // [START] - Receive job request
                InputStream in = socket.getInputStream();
                ObjectInputStream task_to_do = new ObjectInputStream(in);
                Task task = (Task) task_to_do.readObject();
                Constructor conMap = task.getMapper().getConstructor();
                map = (MapperBase) conMap.newInstance();
                map.map(new Tuple("String", 1), new Collector());
                // [END] - Receive job request

                // Run the task - upon success send a message to the manager node

                // [START] Code for sending the Response to Manager
                OutputStream out = socket.getOutputStream();
                String data = "hello";
                out.write(data.getBytes(), 0, data.length());
                out.close();
                // [END] for sending the Response to manager

                // TODO Destroy thread and restart Manager Connect

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            System.out.println("Manager Disconnected");
//            e.printStackTrace();
        }
        // Keep the process alive
        while (true) {

        }
    }
}
