package common;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


// Move to common package
public class Transport {
    protected static volatile Socket ss = null;
    protected int port;
    protected static final int BACKLOG = 20;

    private Transport(){
        if(ss != null) {
            throw new RuntimeException("Use getChannel() to create a transport medium");
        }
    }

    // TODO: Refactor - problem with configuring the port number not the right pattern
    public static Socket serveChannel(int port) throws IOException {
        if(ss == null){
            synchronized (Transport.class) {
                if(ss == null){
                    InetAddress addr = InetAddress.getLocalHost();
                    ss = new Socket(addr,port);
                }
            }
        }
        return ss;
    }
}
