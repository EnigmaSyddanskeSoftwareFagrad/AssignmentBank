package persistence.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for quick parsing of vararg/ command line arguments.
 * @author Morten Kargo Lyngesen
 */
public class ArgumentParser {

    /**
     * Parses vararg/command line arguments from string array to map with list of strings.
     * @param args the vararg/command line arguments to parse
     * @return map containing arguments as key with parameters as list of strings.
     */
    public static Map<String, List<String>> parse(String... args) {
        Map<String, List<String>> params = new HashMap<>();

        for (String arg : args) {
            if (arg.charAt(0) == '-') {
                if (arg.length() < 2) {
                    throw new IllegalArgumentException("Argument " + arg + " is not valid");
                }

                List<String> options = new ArrayList<>();
                String[] opt = arg.substring(1).split(" ");
                for (int i = 1; i < opt.length; i++) {
                    options.add(opt[i]);
                }
                params.put(arg.substring(1).split(" ")[0], options);
            } else {
                throw new IllegalArgumentException("Illegal parameter usage");
            }
        }
        return params;
    }
}
