package core.master;

import common.Tuple;
import common.base.MapperBase;
import common.base.ReducerBase;
import common.collectors.Collector;
import common.schedulars.Job;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class PackageHandler implements Runnable {

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

    @Override
    public void run() {
        // don't need to specify a hostname, it will be the current machine
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(LISTEN_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nManager Node awaiting for jobs...");
        Socket socket = null; // blocking call, this will wait until a connection is attempted on this port.
        try {
            socket = ss.accept();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connection from " + socket + "!");

        // get the input stream from the connected socket
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create a DataInputStream so we can read data from it.
        ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Job recievedJob = null;
        try {
            recievedJob = (Job) objectInputStream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Class<? extends MapperBase> mapper = recievedJob.getMapper();
        Class<? extends ReducerBase> reducer = recievedJob.getReducer();

        System.out.println("Recieved Job Defintion : " + recievedJob.getJobName());

        MapperBase map = null;
        ReducerBase reduce = null;

        try {
            Constructor conMap = mapper.getConstructor();
            Constructor conRed = reducer.getConstructor();
            map = (MapperBase) conMap.newInstance();
            reduce = (ReducerBase) conRed.newInstance();

        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Unable to instantiate mapper class (or) Reducer class");
            //            e.printStackTrace();
            return;
        }

        map.map(new Tuple("String", 1), new Collector());
        reduce.reduce(new Collector(), new Collector());
    }
}
