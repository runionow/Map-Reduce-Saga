package core.master;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Worker implements Runnable {
    private final Socket clientSocket;

    Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;

        InetSocketAddress client = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
        System.out.println("New worker Node joined from port :" + client.getPort() + " Hostname :" + client.getHostName());
        WorkerHandler.workerSockets.put(this.clientSocket.getPort(), clientSocket);
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
                System.out.println(clientSocket.getInputStream().read());
                System.out.println("Hello");
                // TODO HeartBeat failure and observe task status of the worker node here
                clientSocket.getInputStream().close();
                Thread.sleep(2000);
            } catch (IOException e) {
                alive = false;
            } catch (InterruptedException e) {
                alive = false;
            }
        }

        try {
            clientSocket.close();
            WorkerHandler.workerSockets.remove(clientSocket.getPort());
            System.out.println("Disconnected Worker Node" + clientSocket + "!");
            System.out.println("Total " + WorkerHandler.workerSockets.size() + " node(s) in the orchestration");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
