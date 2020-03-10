package logic;

import java.util.ArrayList;

/**
 * The User class is a representation of the signed in user. A user object is to
 * be be instantiated when a user logs in. 
 * 
 * @author Morten Kargo Lyngesen
 */
public class User {
    private final String userID;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final int userType;
    private final ArrayList<Long> departments;

    /**
     * Class constructor.
     * @param userID userID of the current user.
     * @param username username of the current user.
     * @param firstName current users first name.
     * @param lastName current users last name.
     * @param departments departments that the current user is associated with.
     * @param userType current users type; admin, user etc...
     */
    public User (String userID, String username, String firstName, 
                    String lastName, ArrayList<Long> departments, int userType) {
        this.username = username;
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userType = userType;
        this.departments = departments;
    }

    /**
     * @return the users id
     */
    public String getUserID() {
        return this.userID;
    }

    /**
     * @return the users username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * @return the users first name
     */
    public String getFirstName() {
        return this.firstName;
    }

    /**
     * @return the users last name
     */
    public String getLastName() {
        return this.lastName;
    }
    
    /**
     * @return the users assigned type
     */
    public int getUserType() {
        return this.userType;
    }
    
    /**
     * @return all the departments the current user is a member of.
     */
    public ArrayList<Long> getDepartments(){
        return departments;
    }
}
