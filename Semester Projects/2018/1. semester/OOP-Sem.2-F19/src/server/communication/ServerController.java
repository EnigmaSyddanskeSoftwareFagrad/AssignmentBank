/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Sanitas Solutions
 */
public class ServerController implements Runnable {

    private ServerSocket serverSocket;
    private boolean run = true;

    /**
     * initialises the connection to the client
     */
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(1025);
            while (run) {
                Socket clientSocket = serverSocket.accept();
                Thread clientHandler = new ClientHandlerThread(clientSocket);
                clientHandler.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Initiates the stop function
     *
     * Note that the server wont stop before a client tries to connect
     *
     * @return the value of the run variable
     */
    public synchronized boolean stop() {
        run = false;
        return run;
    }
}
