package logic;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class, for representing all user types in the database, 
 * converting between id and name
 * 
 * @author Morten Kargo Lyngesen
 * @author Mads Holm Jensen
 */
public class UserType {
    
        private HashMap<Integer, String> types;
    
    /**
     * Class constructor. Takes an array of user types.
     * @param names names and ID's of user types.
     */
    public UserType (String[] names) {
            types = new HashMap<>();
            
            for (String n : names) {
                String[] tokens = n.split(",");
                
                types.put(Integer.parseInt(tokens[0]), tokens[1]);
            }
        }
        
        /**
         * Gets the id of a type from the type name
         * @param type The name of the type
         * @return The id of the type
         */
        public int get (String type) {
            for (Map.Entry<Integer, String> ent : types.entrySet() ) {
                if (ent.getValue().equals(type)) {
                    return ent.getKey();
                }
            }
            
            return -1;
        }
        
        /**
         * Gets the name of a type from an id
         * @param index The id of the type
         * @return The name of the type
         */
        public String getName (int index) {
            return types.get(index);
        }
        
        /**
         * Gets an array of all the types
         * @return An array of all types
         */
        public String[] getTypes () {
            String[] allTypes = new String[types.size()];
            int count = 0;
            
            for (Map.Entry<Integer, String> ent : types.entrySet() ) {
                allTypes[count++] = ent.getValue();
            } 
            
            return allTypes;
        }
}
