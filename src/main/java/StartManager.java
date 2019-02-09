import common.MainHeader;
import core.master.PackageHandler;
import core.master.WorkerHandler;

public class StartManager {

    public static void main(String[] args) {

        // Header goes in here.
        MainHeader.title();

        // Thread for Receiving the job contents from the client
        Thread obj = new Thread(new PackageHandler());
        obj.start();

        // Thread for listening to the Worker Nodes
        Thread workers = new Thread(new WorkerHandler());
        workers.start();

    }
}
