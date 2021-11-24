/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.domain;

import java.util.List;

/**
 *
 * @author Sanitas Solutions
 */
public interface DomainInterface {
    
    /**
     * Parse the query by the command in the query
     * @param query The query for the database
     * @return The data from the database
     */
    public List<String[]> parseQuery(String[] query);
    
}
