package acquaintance;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * The purpose of this interface is to provide access to the persistent storage.
 * The methods provided in this interface are for creating, retrieving,
 * updating, and deleting data related to the compassio system.
 *
 * @author Peter Andreas Brændgaard
 * @author Frederik Haagensen
 * @author Morten Kargo Lyngesen
 */
public interface IPersistence {
    
    /**
     * Get the cases connected to the userID
     *
     * @param userID The userID for which all the cases are connected to
     * @return An ArrayList with a String array containing all the attributes of
     * the case
     */
    public ArrayList<String[]> getCasesByUserID(String userID);

    /**
     * Get the cases connected to the departmentID
     *
     * @param departmentID The userID for which all the cases are connected to
     * @return An ArrayList with a String array containing all the attributes of
     * the case
     */
    public ArrayList<String[]> getCasesByDepartment(long departmentID);

    /**
     * Returns all case type names.
     *
     * @return case type names.
     */
    public ArrayList<String> retrieveCaseTypeNames();

    /**
     *
     * @param username username of the user to get
     * @param password password of the specified user
     * @return the user
     */
    public String[] getUser(String username, String password);

    /**
     * Creates a user with a hashed password
     *
     * @param userName users username. Used for login.
     * @param firstName users first name
     * @param lastName users last name
     * @param password password to hash and add to database
     * @param typeid the id of the type of the user
     * @param departmentid the id of the department to which the user is connected
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public void createUser(String userName, String firstName, String lastName, String password, int typeid, int departmentid) throws NoSuchAlgorithmException, InvalidKeySpecException;

    /**
     * Returns a list of departments associated with a user.
     *
     * @param userID user to find associated departments.
     * @return list of departments. Will return <code>null</code> if none are
     * found.
     */
    public ArrayList<Long> getUserDepartments(String userID);

    /**
     * Saves the the information associated with a case to persistent storage.
     *
     * @param caseID ID of the case
     * @param cprNumber CPR number of citizen
     * @param type case type
     * @param mainBody notes entered by the user
     * @param dateCreated date of case creation
     * @param dateClosed date of case closure
     * @param departmentID id of assigned department
     * @param inquiry message to institution
     * @return returns <code>true</code> on successful save. Returns
     * <code>false</code> on failure.
     */
    public boolean saveCase(UUID caseID, long cprNumber, String type, String mainBody,
            LocalDate dateCreated, LocalDate dateClosed, int departmentID, String inquiry);

    /**
     * Adds the assigned caseworkers to the case.
     *
     * @param caseID ID of case.
     * @param userID user ID's of assigned caseworkers.
     */
    public void saveCaseUserRelation(UUID caseID, ArrayList<String> userID);

    /**
     * Inserts a new patient into the CPR register.
     *
     * @param cpr the patients CPR number
     * @param firstName the patients first name
     * @param lastName the patients last name
     */
    public void insertNewPatient(long cpr, String firstName, String lastName);

    /**
     * Returns a list of all departments.
     *
     * @return a list of all departments.
     */
    public ArrayList<String> getDepartments();

    /**
     * Returns if the specified user is valid
     *
     * @param userID The user to check for
     * @return returns <code>true</code> if user exists or <code>false</code> if
     * they don't
     */
    public boolean validateUserID(String userID);
    
    /**
     * Returns if the specified password si valid for the user
     *
     * @param userID The user to check for
     * @param password Tha password to check
     * @return returns <code>true</code> if password is correct or <code>false</code> if
     * it isn't
     */
    public boolean validateUserPassword(long userID, String password);

    /**
     * Returns the name of name of the department
     *
     * @param departmentId the ID of the department to return the name of.
     * @return name of the department. Will return <code>null</code> if
     * department doesn't exist
     */
    public String getDepartmentNameById(int departmentId);
    
    /**
     * Returns an array of user type names
     * @return names of all user types
     */
    public String[] getUserTypes ();
    
    /**
     * Returns a list of all the users in the given departments
     * @param departmentIDs ids of all the departments to which the user is connected
     * @return info of all users
     */
    public ArrayList<String[]> getAllUsers (ArrayList<Long> departmentIDs);

    /**
     * Method to change a users password
     *
     * @param newPassword The password the user wants to change to
     * @param oldPassword The users old password
     * @param username The username of the user
     * @return True if the password was change and false if the old password is
     * wrong
     */
    public boolean changePassword(String newPassword, String oldPassword, String username);
    
    /**
     * Method to update the user info in the database
     * @param userID The user to update
     * @param role The new role
     * @param inactive The new inactive state
     */
    public void updateUserInfo(long userID, int role, boolean inactive); 
}
