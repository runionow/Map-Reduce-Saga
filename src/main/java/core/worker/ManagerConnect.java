package core.worker;

// TODO find for any manager and connect to the managers
// TODO Send the heart beat to the manager

import common.Tuple;
import common.base.MapperBase;
import common.collectors.Collector;
import common.schedulars.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ManagerConnect implements Runnable {

    @Override
    public void run() {
        // Wait until manager is available
        int retryCount = 0;
        try {
            waitToConnect(retryCount);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
//                retryCount++;
                System.out.println("Looking for Manager Node, Retrying in next 5s");
                // e.printStackTrace();
            }
        }

        InetSocketAddress client = (InetSocketAddress) socket.getRemoteSocketAddress();
        System.out.println("Joined Manager Node :" + client.getPort() + " Hostname :" + client.getHostName());

        MapperBase map = null;
        try {
            InputStream in = socket.getInputStream();
            ObjectInputStream task_to_do = new ObjectInputStream(in);
            Task task = (Task) task_to_do.readObject();
            in.close();

            Constructor conMap = task.getMapper().getConstructor();
            map = (MapperBase) conMap.newInstance();
            map.map(new Tuple("String", 1), new Collector());

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


    }
}
