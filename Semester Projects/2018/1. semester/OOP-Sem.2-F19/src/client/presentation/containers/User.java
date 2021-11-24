/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.containers;

/**
 *
 * @author Sanitas Solutions
 */
public class User {

    private final String userName;
    private final String fullName;
    private final String userID;

    /**
     *
     * @param userName the unique username of the user
     * @param userID   the unique identifier of the user
     * @param fullName the full name of the user
     */
    public User(String userName, String userID, String fullName) {
        this.userName = userName;
        this.fullName = fullName;
        this.userID = userID;
    }

    /**
     * Formats the information for the user to be displayed in a listView
     *
     * @return a string formatted to be viewed in a listView
     */
    @Override
    public String toString() {
        return fullName + " (" + userName + ")";
    }

    /**
     *
     * @return the unique id associated with the user
     */
    public String getUserID() {
        return userID;
    }

    /**
     *
     * @return the unique of the user
     */
    public String getUsername() {
        return userName;
    }

    /**
     *
     * @return the full name of the user
     */
    public String getUserFullName() {
        return fullName;
    }

}
