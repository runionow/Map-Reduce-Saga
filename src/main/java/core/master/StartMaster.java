package core.master;

import java.io.IOException;
import java.net.InetAddress;

public class StartMaster {

    // TODO Create master and wait to recieve jobs from the Client
    public static void main(String[] args) throws IOException, ClassNotFoundException {

//        Thread startListening = new Thread(() -> {
//            try {
//                PackageHandler.listener();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        });

        InetAddress addr = InetAddress.getLocalHost();
        System.out.println("Master Process has been initialized");
        PackageHandler.listener();


    }
}
