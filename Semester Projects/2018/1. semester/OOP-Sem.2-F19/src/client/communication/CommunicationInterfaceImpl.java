/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.communication;

import com.frohno.pseudossl.PseudoSSLClient;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

/**
 *
 * @author Sanitas Solutions
 */
public class CommunicationInterfaceImpl implements CommunicationInterface {

    /**
     * Sends the query to the server through a TCP connection
     *
     * @param query the query for the database
     * @return the data from the database
     */
    @Override
    public synchronized List<String[]> sendQuery(String... query) {

        try {
            Socket clientSocket = new Socket("localhost", 1025);
            PseudoSSLClient ps = new PseudoSSLClient(clientSocket);
            ps.sendObject(query);
            return (List<String[]>) ps.recieveObject();
        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }
        return new ArrayList<>(Arrays.asList(new String[][]{new String[]{"Error", "Client-side network error"}}));
    }
}
