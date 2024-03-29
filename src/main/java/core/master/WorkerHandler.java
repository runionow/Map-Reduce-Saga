package core.master;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkerHandler implements Runnable {
    private static final int WORKER_PORT = 9080;
    private static final int BUFFER_SIZE = 2044;

    public static volatile Map<Integer, Socket> workerSockets = new ConcurrentHashMap<>();
    public static volatile Map<Integer, Socket> availableNode = new ConcurrentHashMap<>();

    final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(5);

    @Override
    public void run() {

        ServerSocket ss = null;
        {
            try {
                ss = new ServerSocket(WORKER_PORT);
            } catch (IOException e) {
                System.out.println("Socket already in use");
                e.printStackTrace();
            }
        }
        System.out.println("\nWaiting for worker nodes to join the Manager....");

        while (true) {
            Socket clientSocket = null;
            try {
                clientSocket = ss.accept();
                clientProcessingPool.submit(new Worker(clientSocket));
            } catch (IOException e) {
                System.out.println("Not found");
                e.printStackTrace();
            }
        }
    }
}
