import common.MainHeader;
import core.worker.ManagerConnect;

public class StartWorker {

    // TODO Stay alive
    // TODO Connect to Manager Nodes
    // TODO keep the worker alive and wait for the messages
    // TODO Implement heart beat

    public static void main(String[] args) {

        // Header
        MainHeader.title();

        // Upon starting recieve package from the Master Node
        // Do no start reducer until map job is completed
        Thread worker = new Thread(new ManagerConnect());
        worker.start();
    }
}
