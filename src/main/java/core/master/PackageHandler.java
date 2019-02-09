package core.master;

import common.Tuple;
import common.base.MapperBase;
import common.base.ReducerBase;
import common.collectors.Collector;
import common.schedulars.Job;
import common.schedulars.Task;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;



public class PackageHandler implements Runnable {

    protected static final int BACKLOG = 25;
    protected static final int MANAGER_LISTEN_PORT = 7777;
    protected static final int WORKER_PORT = 9080;

    @Override
    public void run() {
        // don't need to specify a hostname, it will be the current machine
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(MANAGER_LISTEN_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nManager Node awaiting for jobs...");
        Socket socket = null; // blocking call, this will wait until a connection is attempted on this port.
        try {
            socket = ss.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Received job from client " + socket + "!");

        // get the input stream from the connected socket
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Job recievedJob = null;
        try {
            recievedJob = (Job) objectInputStream.readObject();
            objectInputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Class<? extends MapperBase> mapper = recievedJob.getMapper();
        Class<? extends ReducerBase> reducer = recievedJob.getReducer();

        System.out.println("Recieved Job Defintion : " + recievedJob.getJobName());

        MapperBase map = null;
        ReducerBase reduce = null;

        try {
            Constructor conMap = mapper.getConstructor();
            Constructor conRed = reducer.getConstructor();

            map = (MapperBase) conMap.newInstance();
            reduce = (ReducerBase) conRed.newInstance();

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Unable to instantiate mapper class (or) Reducer class");
            //            e.printStackTrace();
            return;
        }

        // Map and reduce Tester
        map.map(new Tuple("String", 1), new Collector());
        reduce.reduce(new Collector(), new Collector());

        // TODO Upon receiving the job divide and the send tasks based on the worker nodes availability


        sendTask(recievedJob);

    }


    public void sendTask(Job job) {
        ArrayList<String> files = job.getInput();

        OutputStream out = null;
        ObjectOutputStream sendTask = null;

        for (int port : WorkerHandler.workerSockets.keySet()) {
            Task mapTask = new Task(files.get(0), job.getMapper());
            Socket socket_task = WorkerHandler.workerSockets.get(port);

            try {
                out = socket_task.getOutputStream();
                sendTask = new ObjectOutputStream(out);
                sendTask.flush();
                sendTask.writeObject(mapTask);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
