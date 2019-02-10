package core.master;

import common.Status;
import common.base.MapperBase;
import common.base.ReducerBase;
import common.schedulars.Job;
import common.schedulars.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class PackageHandler implements Runnable {

    protected static final int BACKLOG = 25;
    protected static final int MANAGER_LISTEN_PORT = 7777;
    protected static final int WORKER_PORT = 9080;
    public static volatile Set<Integer> nodes_in_use = new HashSet<>();


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
        Socket socket = null; // Blocking call, this will wait until a connection is attempted on this port.
        try {
            socket = ss.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Received job from client " + socket + "!");

        // Get the input stream from the connected socket
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Create a DataInputStream so we can read data from it.
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

        System.out.println("Recieved Job Defintion : " + recievedJob.getJobName());



        Class<? extends MapperBase> mapper = recievedJob.getMapper();
        Class<? extends ReducerBase> reducer = recievedJob.getReducer();


        // TODO Upon receiving the job divide and send tasks based on the worker nodes availability
        ArrayList<String> file_chunks = recievedJob.getInput();
        WorkerHandler.availableNode.putAll(WorkerHandler.workerSockets);

        // For each file chunk i am assigning a task.
        // Each task has initial status of MAP_READY
        Task[] tasks = new Task[file_chunks.size()];

        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new Task(file_chunks.get(i),
                    recievedJob.getOutput(),
                    recievedJob.getMapper(),
                    recievedJob.getReducer());
        }

        // Sending for 1 socket - testing
        while (WorkerHandler.workerSockets.size() > 0) {
            for (int keys : WorkerHandler.workerSockets.keySet()) {
                if (tasks[0].getStatus() == Status.MAP_READY) {
                    sendTask(tasks[0], keys);
                    break;
                }
            }
        }


        // Wait until all the tasks are executes

        //

        // In progress tasks
        Set<Task> task_status_map_ready = new HashSet<>();
        task_status_map_ready.addAll(Arrays.asList(tasks));

        System.out.println(task_status_map_ready.size());

        // Assigning each task to worker nodes
//        if (WorkerHandler.workerSockets.size() > 0) {
//            for(int availableWorker : WorkerHandler.availableNode.keySet()) {
//                Socket socket_task = WorkerHandler.workerSockets.get(availableWorker);
//                WorkerHandler.availableNode.remove(availableWorker);
//                nodes_in_use.add(availableWorker);
//            }
//        }


    }

    // check if all the map chunks are executed are not

    // Check if all the tasks are completed or not


    public void sendTask(Task task, int socketPort) {
        OutputStream out = null;
        ObjectOutputStream sendTask = null;
        Socket socket_task = WorkerHandler.workerSockets.get(socketPort);
        // Sending task
        try {
            out = socket_task.getOutputStream();
            sendTask = new ObjectOutputStream(out);
            sendTask.flush();
            sendTask.writeObject(task);
            out.flush();
        } catch (IOException e) {
            System.out.println("Unable to assign task to Worker Node : " + socket_task);
            e.printStackTrace();
        }

    }
}
