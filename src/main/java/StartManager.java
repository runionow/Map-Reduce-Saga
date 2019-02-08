import common.MainHeader;
import core.master.PackageHandler;
import core.master.WorkerHandler;

public class StartManager {

    // TODO Start the worker and wait for the client to pass job details
    // TODO Pass the job details to worker nodes based on the task available

    public static void main(String[] args) {
        // Header goes in here.
        MainHeader.title();

        // Thread for recieving the job contents from the client
        Thread obj = new Thread(new PackageHandler());
        obj.start();

        // Thread for listening to the Worker Nodes
        Thread workers = new Thread(new WorkerHandler());
        workers.start();

        // Pass Task Defintions to the worker nodes

        // Get the heart beat of the workers

        // thread for keeping track of the status pf the nodes and for keeping the process alive
        // until the job is completed.

    }
}
