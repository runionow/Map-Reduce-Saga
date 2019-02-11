package core.worker;

import common.Status;
import common.TaskStatus;
import common.Tuple;
import common.base.MapperBase;
import common.base.ReducerBase;
import common.collectors.Collector;
import common.collectors.InCollector;
import common.collectors.OutCollector;
import common.schedulars.Task;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ManagerConnect<K, V> implements Runnable {

    List<String> objFile(String directory) {
        List<String> textFiles = new ArrayList<String>();
        File dir = new File(directory);
        System.out.println(dir.toString());
        for (File file : dir.listFiles()) {

            System.out.println(file.toString());
            if (file.getName().endsWith(("-obj"))) {
                System.out.println("Found : " + file);
                textFiles.add(file.toString());
            }
        }

        return textFiles;
    }

    private Collector getCollectorFromFile(String filePath) {
        try {
            FileInputStream file_input = new FileInputStream(filePath);
            ObjectInputStream objectIn = new ObjectInputStream(file_input);

            Collector obj = (Collector) objectIn.readObject();
            System.out.println("Collector now available");
            objectIn.close();

            return obj;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // For each reduce task the
    public TaskStatus reduceExecute(String filePath) {
        List<String> file_path = objFile(filePath);
        Collector c1 = new Collector();
        for (String file : file_path) {
            System.out.println(file);
            c1.add(getCollectorFromFile(file).toList());
        }


        TreeMap<?, ?> shuffling = (TreeMap<?, ?>) c1.intermediateCollectors();
        System.out.println(shuffling.size());
        for (Object key : shuffling.keySet()) {
            System.out.println(key);
        }

        // TODO Iterate through every single value in collector
        Reducer reduce = new Reducer();
        reduce.reduce((Map<String, InCollector<String, Integer>>) shuffling, new Collector<>());
        return new TaskStatus(1, Status.REDUCE_SUCCESS);
    }

    @Override
    public void run() {
        reduceExecute("D:\\arun\\output\\temp");

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

    /**
     * b. Creating your first reduce function
     * ======================================
     * 1. To create Reducer function create static Reducer Class extend MapperBase[common.base.ReducerBase] Class
     * 2. Override the base reduce function
     */
    public static class Reducer extends ReducerBase<String, Integer> {
        @Override
        public void reduce(Map<String, InCollector<String, Integer>> input, OutCollector<String, Integer> out) {
            // Sum the intermediate results and put it in out
            for (String key : input.keySet()) {
                int count = input.get(key).count(); // Summing all the intermediate values, count is in a intermediate function which has count of number of tuples
                System.out.println(key + " " + count);
                out.collect(new Tuple(key, count));
            }
        }
    }


}
