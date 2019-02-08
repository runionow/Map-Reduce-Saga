package core.master;

import common.schedulars.Job;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class PackageHandler {

    protected static final int BACKLOG = 25;
    protected static final int LISTEN_PORT = 7777;

    public static void listener() throws IOException, ClassNotFoundException {
        InetAddress addr = InetAddress.getLocalHost();
        ServerSocket ss = new ServerSocket(LISTEN_PORT,BACKLOG,addr);

        // Server Socket waiting for connection
        Socket socket = ss.accept();
        InputStream is = socket.getInputStream();

        ObjectInputStream objectInputStream = new ObjectInputStream(is);
        Job jobDescription = (Job) objectInputStream.readObject();

        // After recieving the package close the socket
        System.out.println("Package has been recieved" + new Date());

        // Acknowledge and close the sockets
        System.out.println("Socket connection closed");
        ss.close();
        socket.close();
    }
}
