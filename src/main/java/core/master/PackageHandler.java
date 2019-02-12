package core.master;

import common.Status;
import common.base.MapperBase;
import common.base.ReducerBase;
import common.schedulars.Job;
import common.schedulars.Task;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;


public class PackageHandler implements Runnable {

    protected static final int BACKLOG = 25;
    protected static final int MANAGER_LISTEN_PORT = 7777;
    protected static final int WORKER_PORT = 9080;
    public static Stack<Task> stackTask = new Stack<>();
    public static volatile Set<Integer> nodes_in_use = new HashSet<>();
    public static volatile Stack<Task> stack_final = new Stack<>();
    public static volatile Map<Integer, Task> stack_in_progress = new HashMap<>();
    protected static Map<Integer, Task> task_handler = new HashMap<Integer, Task>();

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


        Stack<Task> stack_final = new Stack<>();
        Map<Integer, Task> stack_in_progress = new HashMap<>();

        // Execute job only when atleast one worker node is available
        HashMap<Integer, Socket> availableNodes = new HashMap<>();

        // If any new nodes joined, add it to the available nodes
        availableNodes.putAll(WorkerHandler.workerSockets);

        // Insert all the jobs into the Map
        // Every task has a default status of
        for (int i = 0; i < recievedJob.getInput().size(); i++) {
            stack_final.push(new Task(file_chunks.get(i),
                    recievedJob.getOutput(),
                    recievedJob.getMapper(),
                    recievedJob.getReducer(), i));
        }


        // Wait until all the tasks are executes
        while (!stack_final.isEmpty()) {
            while (!WorkerHandler.availableNode.isEmpty()) {
                for (Integer key : WorkerHandler.availableNode.keySet()) {
                    WorkerHandler.availableNode.remove(key);
                    if (stack_final.isEmpty() && stack_in_progress.isEmpty()) {
                        break;
                    }
                    sendTask(stack_final.pop(), key);
                }

            }
            if (WorkerHandler.availableNode.isEmpty() && !stack_final.isEmpty()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Waiting for nodes to complete the task and looking for new nodes to join the orchestration");
            } else {
                break;
            }
        }

        System.out.println("Map tasks are now completed");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Allocating the reduce tasks
        System.out.println("Starting the reduce task");

        // Creating a task for Reduce operation
        boolean availableCount = WorkerHandler.availableNode.size() > 1;
        Task reduceTask = null;
        while (availableCount) {
            reduceTask = new Task(recievedJob.getOutput(),
                    recievedJob.getOutput(),
                    recievedJob.getMapper(),
                    recievedJob.getReducer(), 9);
            reduceTask.setStatus(Status.REDUCE_READY);
            availableCount = false;
        }


        for (int i : WorkerHandler.availableNode.keySet()) {
            System.out.println(i);
            sendTask(reduceTask, i);
            break;
        }

        System.out.println("Reduce task has been completed");
    }

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
//            e.printStackTrace();
        }

//        task.setStatus(Status.MAP_TASK);
        task_handler.put(socketPort, task);
        System.out.println("New job has been added to the task_handler");

    }
}
