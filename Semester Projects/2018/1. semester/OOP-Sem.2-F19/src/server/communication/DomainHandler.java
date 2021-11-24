/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.communication;

import java.util.List;
import server.domain.DomainInterface;
import server.domain.DomainInterfaceImpl;

/**
 *
 * @author Sanitas Solutions
 */
public class DomainHandler {

    private final DomainInterface domainInterface;

    /**
     * Creates an instance of the domainHandler Facade which in turn creates an
     * instance of the domainIntefaceImpl to handle queries sent
     *
     * @param ip the IP of the user interacting with the server
     */
    public DomainHandler(String ip) {
        domainInterface = new DomainInterfaceImpl(ip);
    }

    /**
     * Sends a query to the action handler in the domain layer
     *
     * @param query is the query to be sent
     * @return the return from the action handler
     */
    public List<String[]> parseQuery(String[] query) {
        return domainInterface.parseQuery(query);
    }
}
