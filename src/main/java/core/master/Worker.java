package core.master;

import common.Status;
import common.TaskStatus;
import common.schedulars.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Worker implements Runnable {
    private final Socket clientSocket;

    Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;

        InetSocketAddress client = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
        System.out.println("New worker Node joined from port :" + client.getPort() + " Hostname :" + client.getHostName());
        WorkerHandler.workerSockets.put(this.clientSocket.getPort(), clientSocket);
        WorkerHandler.availableNode.put(this.clientSocket.getPort(), clientSocket);
        System.out.println("Total " + WorkerHandler.workerSockets.size() + " node(s) in the orchestration");
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        /**
         * Socket Heartbeat 💕 and worker task status
         */
        boolean alive = true;
        while (alive) {
            try {
                InputStream in = clientSocket.getInputStream();
                ObjectInputStream task_to_do = new ObjectInputStream(in);
                TaskStatus status = (TaskStatus) task_to_do.readObject();
                System.out.println(status.getStatus() + " 111");
                if (status.getStatus() == Status.MAP_SUCCESS) {
                    // Update the task defintions
                    System.out.println("Task " + status.getTask_num() + "has been succesfully completed");
                    Task task = PackageHandler.task_handler.get(this.clientSocket.getPort());
                    PackageHandler.stack_in_progress.remove(status.getTask_num());
                    task.setStatus(Status.MAP_SUCCESS);
                } else {
                    System.out.println("Failed to execute task " + status.getTask_num() + " , attempting again");
                    Task task_reattempt = PackageHandler.stack_in_progress.get(status.getTask_num());
                    PackageHandler.stack_final.add(task_reattempt);
                }

                clientSocket.getInputStream().close();
                Thread.sleep(2000);
            } catch (IOException e) {
                alive = false;
            } catch (InterruptedException e) {
                alive = false;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            clientSocket.close();

            // Adjusting available nodes and worker sockets
            WorkerHandler.workerSockets.remove(clientSocket.getPort());
            WorkerHandler.availableNode.remove(clientSocket.getPort());

            System.out.println("Disconnected Worker Node at " + clientSocket + "!");
            System.out.println("Total " + WorkerHandler.workerSockets.size() + " node(s) in the orchestration");
            Task task = PackageHandler.task_handler.get(this.clientSocket.getPort());
            task.setStatus(Status.MAP_READY);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
