package logic;

import acquaintance.ILogic;
import acquaintance.IPersistence;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Facade class for facilitating a single entry point to the logic layer.
 * @author Peter Andreas Brændgaard
 * @author Frederik Haagensen
 */
public class LogicFacade implements ILogic {

    private static IPersistence persistence;
    private User user;
    private UserType userType;

    
    @Override
    public void injectPersistence(IPersistence PersistenceLayer) {
        persistence = PersistenceLayer;
        loadUserTypes();
    }

    /**
     * Creates an instance of the different user types.
     */
    public void loadUserTypes() {
        userType = new UserType(persistence.getUserTypes());
    }
    
    @Override
    public String[] getUserTypes () {
       return userType.getTypes();
    }
    
    @Override
    public boolean createCase(String firstName, String lastName, long cprNumber,
            String type, String mainBody, LocalDate dateCreated, LocalDate dateClosed, int departmentID, String inquiry, ArrayList<String> socialWorkers) {
        Case newCase = new Case(firstName, lastName, cprNumber, type, mainBody, dateCreated, dateClosed, departmentID, inquiry);
        newCase.addPatientToDatabase();
        boolean caseSaved = newCase.saveCase();
        if (caseSaved) {
            socialWorkers.add(user.getUserID());
            persistence.saveCaseUserRelation(newCase.getCaseID(), socialWorkers);
        }
        return caseSaved;
    }

    @Override
    public ArrayList<Case> getCases() {
        ArrayList<Case> response = new ArrayList<>();
        if (this.user.getUserType() == this.userType.get("CASEWORKER")) {
            ArrayList<String[]> cases = persistence.getCasesByUserID(user.getUserID());
            while (cases.size() > 0) {
                String[] singleCase = cases.remove(cases.size() - 1);
                if (singleCase[7] != null) {
                    response.add(new Case(singleCase[0], singleCase[1], UUID.fromString(singleCase[2]), Long.parseLong(singleCase[3]),
                            singleCase[4], singleCase[5], LocalDate.parse(singleCase[6]), LocalDate.parse(singleCase[7]), Integer.parseInt(singleCase[8]), singleCase[9]));
                } else {
                    response.add(new Case(singleCase[0], singleCase[1], UUID.fromString(singleCase[2]), Long.parseLong(singleCase[3]),
                            singleCase[4], singleCase[5], LocalDate.parse(singleCase[6]), null, Integer.parseInt(singleCase[8]), singleCase[9]));
                }
            }
        } else if (this.user.getUserType() == this.userType.get("SOCIALWORKER")) {
            ArrayList<Long> departments = this.user.getDepartments();
            
            ArrayList<String[]> cases = new ArrayList<>();
            departments.forEach(d -> {
                cases.addAll(persistence.getCasesByDepartment(d));
            });
            while (cases.size() > 0) {
                String[] singleCase = cases.remove(cases.size() - 1);
                response.add(new Case(singleCase[0], singleCase[1], UUID.fromString(singleCase[2]), Long.parseLong(singleCase[3]),
                        singleCase[4], null, null, null, Integer.parseInt(singleCase[5]), singleCase[6]));
            }
        }
        return response;
    }

    /**
     * Returns the current instance of the persistence layer.
     * @return the current instance of the persistence layer.
     */
    public static IPersistence getPersistence() {
        return persistence;
    }

    @Override
    public ArrayList<String> retrieveCaseTypes() {
        return persistence.retrieveCaseTypeNames();
    }

    @Override
    public boolean login(String username, String password) {
        String[] result = this.persistence.getUser(username, password);
        
        int userType = this.userType.get("UNKNOWN");

        if (result != null) {
            if (result[4] != null) {
                userType = this.userType.get(result[4]);
            }
            this.user = new User(result[0], result[1], result[2],
                    result[3], LogicFacade.persistence.getUserDepartments(result[0]), userType);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void logout () {
        user = null;
    }
    
    @Override
    public ArrayList<String> getDepartmentInfo() {
        return persistence.getDepartments();
    }

    @Override
    public boolean checkUserID(String userID) {
        return persistence.validateUserID(userID);
    }
    
    @Override
    public boolean checkUserPassword(String password) {
        return persistence.validateUserPassword(Long.parseLong(user.getUserID()), password);
    }

    @Override
    public String getUserID() {
        return user.getUserID();
    }

    /**
     * @return user first and last name separated by a space char
     */
    @Override
    public String getUserName() {
        return this.user.getFirstName() + " " + this.user.getLastName();
    }

    /**
     * @return users given type in all uppercase
     */
    @Override
    public String getUserType() {
        return this.userType.getName(this.user.getUserType());
    }

    @Override
    public String getDepartmentNameById(int departmentId) {
        return persistence.getDepartmentNameById(departmentId);
    }

    @Override
    public Boolean changePassword(String newPassword, String oldPassword) {
        return persistence.changePassword(newPassword, oldPassword, user.getUsername());
    }

    @Override
    public void updateUserState (long userID, String newRole, boolean newInactiveState) {
        persistence.updateUserInfo(userID, userType.get(newRole), newInactiveState);
    }
    
    @Override
    public ArrayList<UserInfo> getAllUsers() {
        ArrayList<UserInfo> users = new ArrayList<>();
        ArrayList<String[]> usersInfo = persistence.getAllUsers(user.getDepartments());
        
        usersInfo.forEach((info) -> {
            users.add(new UserInfo(Long.parseLong(info[0]), info[1], info[2] + " " +  info[3], userType.getName(Integer.parseInt(info[4])), Boolean.parseBoolean(info[5])));
        });
        
        return users;
    }

    @Override
    public void createUser(String firstName, String lastName, String username, String password, String type, int departmentid) {
        try {
            int typeid = userType.get(type);
            persistence.createUser(username, firstName, lastName, password, typeid, departmentid);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            Logger.getLogger(LogicFacade.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean validateCpr(String cpr) {
        return CprValidator.validate(cpr);
    }
}
