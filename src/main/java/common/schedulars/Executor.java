package common.schedulars;

import core.worker.ManagerConnect;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


public class Executor {

//    public void Executor(Job job) throws IOException {
//        // Check for files in the input folder
//        StringBuffer sb = new StringBuffer();
//
//        try{
//            BufferedReader textFile = new BufferedReader(new FileReader(String.valueOf(job.getInput())));
//            String line = null;
//            while((line = textFile.readLine()) != null) {
//                sb.append(line.trim()).append(" ");
//            }
//            textFile.close();
//        } catch (IOException ex) {
//
//        }
//}




        // Create an output folder
        // Segregate files
        // Run Map Job and generate intermediate files
        // Run Reduce Job and consume intermediate files
        // Show the output
        // Upon close remove temporary files


    public static void start(Job job) {

        //        TODO 1. Verify the job details
        //        TODO 2. Submit the job to the Manager

        Socket socket = null;
        // Enabling outputStream on the Socket
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;

        int retryCount = 0;

        try {
            ManagerConnect.waitToConnect(retryCount);
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
