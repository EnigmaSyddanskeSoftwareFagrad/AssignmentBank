/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.recources;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *
 * @author Hounsvad
 */
public final class ConfigReader {

    private final String configName;
    private final Map<String, String> properties;

    /**
     * Loads the configuration file in the same folder as the ConfigReaderClass
     *
     * @param configName is the name without the extension ie with a file called
     *                   block.config the name would be block
     * @throws IllegalArgumentException if the file is non-existent
     */
    public ConfigReader(String configName) {
        this.configName = configName;
        this.properties = new TreeMap<>();
        reload();
    }

    /**
     * Reloads the configuration file
     */
    public void reload() {
        properties.clear();
        new Scanner(getClass().getResourceAsStream("/server/recources/" + configName + ".config")).useDelimiter("\r\n").forEachRemaining((s) -> properties.put(s.split(" := ")[0], s.split(" := ")[1]));
    }

    /**
     *
     * @return the properties read from file
     */
    public Map<String, String> getProperties() {
        return new TreeMap<>(properties);
    }

}
