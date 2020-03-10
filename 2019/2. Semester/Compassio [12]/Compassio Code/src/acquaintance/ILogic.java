package acquaintance;

import java.time.LocalDate;
import java.util.ArrayList;
import logic.Case;
import logic.UserInfo;

/**
 * Defines methods that the logic layer must implement in order to function with
 * the GUI layer.
 * @author Peter Brændgaard
 * @author Frederik Haagensen
 * @author Julie Markersen
 */
public interface ILogic {

    /**
     * Injects the persistence object into the logic layer
     * @param PersistenceLayer The persistence object
     */
    public void injectPersistence(IPersistence PersistenceLayer);
    
    /**
     * Retrieves all the cases, that are avaiable for the current user
     * @return A list of all cases available to the user
     */
    public ArrayList<Case> getCases();

    /**
     * Retrieves all the names of the types of cases
     * @return A list of names
     */
    public ArrayList<String> retrieveCaseTypes();

    /**
     * Tries to login a user, with a username and password
     * @param username The username of the user
     * @param password The password of the user
     * @return <code>True</code> if the login is successful, else <code>false</code>
     */
    public boolean login(String username, String password);
    
    /**
     * Logs out the user
     */
    public void logout();

    /**
     * Creates a new case from the given parameters
     * @param firstName The firstname of the patient
     * @param lastName The lastname of the patient
     * @param cprNumber The CPR number of the patient
     * @param type The type of the case
     * @param mainBody The main body of the case
     * @param dateCreated The date at which the case was created
     * @param dateClosed The date at which the case was closed. <code>Null</code> if not closed
     * @param departmentID The id of the department, to which the case belong
     * @param inquiry The inquiry of the case
     * @param socialWorkers A list of the casewokers, who are working on the case
     * @return <code>True</code> if the case is saved successful, else <code>false</code>
     */
    public boolean createCase(String firstName, String lastName, long cprNumber, String type, String mainBody,
            LocalDate dateCreated, LocalDate dateClosed, int departmentID, String inquiry, ArrayList<String> socialWorkers);

    /**
     * Gets a list of department names, with their respective id
     * @return A list of department info
     */
    public ArrayList<String> getDepartmentInfo();

    /**
     * Checks if the given user id exists
     * @param userID The user id to check
     * @return <code>True</code> if the id exists, else <code>false</code>
     */
    public boolean checkUserID(String userID);
    
    /**
     * Used for validating a entered password
     * @param password The password to validate
     * @return <code>True</code> if the password is correct and <code>false</code> if it is wrong
     */
    public boolean checkUserPassword(String password);

    /**
     * Gets the current users id
     * @return the users id
     */
    public String getUserID();

    /**
     * Gets the current users name
     * @return The current users name
     */
    public String getUserName();

    /**
     * Gets the current users type
     * @return The current users type
     */
    public String getUserType();
    
    /**
     * Gets an array of all possible user types
     * @return an array of user types
     */
    public String[] getUserTypes ();
    
    /**
     * Returns a list of all users within the users departments
     * @return a list of all users
     */
    public ArrayList<UserInfo> getAllUsers ();

    /**
     * Gets the name of a department from the id
     * @param departmentId The id of the department
     * @return The name of the department
     */
    public String getDepartmentNameById(int departmentId);


    /**
     * Method to change a users password
     *
     * @param newPassword The password the user wants to change to
     * @param oldPassword The users old password
     * @return <code>True</code> if the password was change and <code>false</code> if the old password is wrong
     */
    public Boolean changePassword(String newPassword, String oldPassword);
    
    /**
     * Method to update the role and inactive state of a user
     * @param userID The id of the user to update
     * @param newRole The new role of the user
     * @param newInactiveState the state of the user
     */
    public void updateUserState (long userID, String newRole, boolean newInactiveState);
    
    /**
     * Method for creating a new user in the system
     * @param firstName The first name of the user
     * @param lastName The last name of the user
     * @param username The username of the user
     * @param password The password of the user
     * @param type The type of the user
     * @param departmentid The department of the user
     */
    public void createUser(String firstName, String lastName, String username, String password, String type, int departmentid);
    
    /**
     * Returns <code>true</code> on valid number. <code>false</code> on invalid
     * number.
     * @param cpr cpr number to test on.
     * @return <code>true</code> on valid. <code>false</code> on invalid
     */
    public boolean validateCpr(String cpr);
}
