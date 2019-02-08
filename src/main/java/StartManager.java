import core.master.PackageHandler;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class StartManager {

    // TODO Start the worker and wait for the client to pass job details
    // TODO Verify the job details and check whether the you are able to reach the mapper and reducer function
    // TODO Pass the job details to worker nodes based on the task available
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        try {
            InetAddress addr = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println("Master Process has been initialized");
        PackageHandler.listener();

    }
}
