/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.communication;

import java.util.*;

/**
 *
 * @author Sanitas Solutions
 */
public interface CommunicationInterface {

    /**
     * Sends the query to the server
     *
     * @param query with the instructions for the database
     * @return the the data from the database
     */
    public List<String[]> sendQuery(String... query);
}
