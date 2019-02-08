package core.client;

import common.Transport;
import common.schedulars.Job;
import java.io.IOException;
import java.net.Socket;

public class Configuration {

    private static void Execute(Job job) throws IOException {
        Socket ss  = Transport.serveChannel(1245);

        // Spawn master
        // Spawn Client nodes

        // Pass the function
//        Socket socket = ss.accept();

//        OutputStream output = socket.getOutputStream();
//        ObjectOutputStream objectOutputStream = new ObjectOutputStream(output);

    }

}
