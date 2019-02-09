package core.master;

import java.net.Socket;

public class Worker implements Runnable {
    private final Socket clientSocket;

    Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
        WorkerHandler.workerSockets.put(this.clientSocket.getPort(), clientSocket);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        System.out.println("New worker now available");
    }
}
