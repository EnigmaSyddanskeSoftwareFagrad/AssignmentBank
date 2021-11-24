/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.containers;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Hounsvad
 */
public class Cache {

    private static Cache instance = null;
    private final Map<String, Object> cache;

    /**
     *
     * @param cache
     */
    private Cache() {
        cache = new HashMap<>();
    }

    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
        }
        return instance;
    }

    /**
     * Get the value of the cached list
     *
     * @return the value of string
     */
    public Map<String, Object> getCache() {
        return cache;
    }

    /**
     * Deletes the cache
     */
    public void reset() {
        cache.clear();
        instance = null;
    }
}
