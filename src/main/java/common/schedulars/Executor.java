package common.schedulars;

import common.MainHeader;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Executor {


    // Create an output folder
    // Segregate files
    // Run Map Job and generate intermediate files
    // Run Reduce Job and consume intermediate files
    // Show the output
    // Upon close remove temporary files


    public static void start(Job job) {

        //        TODO 1. Verify the job details
        //        TODO 2. Submit the job to the Manager

        MainHeader.title();
        Socket socket = null;
        // Enabling outputStream on the Socket

        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;

        int retryCount = 0;

        try {
            System.out.println("Looking for a Manager");
            while (true) {
                try {
                    socket = new Socket("localhost", 7777);
                    if (socket != null || retryCount == 5) {
                        break;
                    }
                } catch (IOException e) {
                    Thread.sleep(5000);
                    retryCount++;
                    System.out.println("Looking for Manager Node, Retrying in next 5s");
                }
            }
            System.out.println("Connection established with manager node!");

            outputStream = socket.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(job);

        } catch (IOException | InterruptedException e) {
            System.out.println("Unable to Establish connection");
            e.printStackTrace();
        }
        System.out.println("Job has been passed to Manager");

        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Unable to close the connection");
            e.printStackTrace();
        }

    }
}
