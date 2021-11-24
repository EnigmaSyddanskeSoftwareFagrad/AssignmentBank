package persistence.dataaccessobjects;

import java.util.List;

/**
 * The purpose of this interface is to provide access to the persistent storage.
 * The methods provided in this interface are for creating, retrieving,
 * updating, and deleting data.
 *
 * @author Morten Kargo Lyngesen
 */
public interface DataAccessObject {
    

    /**
     * Returns the entry that matches the given ID. If none exists returns
     * <code>null</code>.
     *
     * @param id id of the element to get
     * @return the element with specified id.
     */
    String[] get(String... id);

    /**
     * Returns all entries that satisfy the condition(s)
     *
     * @param cond condition(s) that entries must satisfy
     * @return all entries that satisfy the conditions
     */
    List<String[]> getAll(String... cond);

    /**
     *
     * @param args
     * @return
     */
    boolean create(String... args);

    /**
     *
     * @param id
     * @param args
     * @return
     */
    boolean update(long id, String... args);

    /**
     *
     * @param id
     * @return
     */
    boolean delete(long id);
}
