package persistence;

import acquaintance.IPersistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import persistence.dataaccessobjects.*;

/**
 * Provides a facade to interact with the database through Data Access Objects.
 *
 * @author Peter Andreas Brændgaard
 * @author Frederik Haagensen
 * @author Morten Kargo Lyngesen
 */
public class PersistenceFacade implements IPersistence {

    //Data Access Objects
    private final UserDAO userDao;
    private final CaseDAO caseDao;
    private final DepartmentDAO departmentDao;
    private final CaseTypeRelationDAO caseTypeRelationDao;
    private final CprDAO cprDao;
    private final EmployeesOfDepartmentDAO employeesOfDepartmentDAO;
    private final UserTypeRelationDAO userTypeRelationDao;
    private final CaseUserRelationDAO caseUserRelationDao;

    /**
     * Initializes the class by instantiating the DAO classes.
     */
    public PersistenceFacade() {
        //Initialize DAO's
        userDao = new UserDAO();
        caseDao = CaseDAO.getInstance();
        departmentDao = new DepartmentDAO();
        caseTypeRelationDao = CaseTypeRelationDAO.getInstance();
        cprDao = new CprDAO();
        employeesOfDepartmentDAO = new EmployeesOfDepartmentDAO();
        userTypeRelationDao = UserTypeRelationDAO.getInstance();
        caseUserRelationDao = new CaseUserRelationDAO();
    }

    //==========================================================================
    // Case methods
    //==========================================================================
    @Override
    public ArrayList<String> retrieveCaseTypeNames() {
        List<String[]> list = this.caseTypeRelationDao.getAll();
        String[][] array = list.toArray(new String[][]{});
        ArrayList<String> arrayList = new ArrayList<>();
        for (String[] arr : array) {
            arrayList.add(arr[0]);
        }
        return arrayList;
    }

    @Override
    public boolean saveCase(UUID caseID, long cprNumber, String type,
            String mainBody, LocalDate dateCreated, LocalDate dateClosed, int departmentID, String inquiry) {
        String closedate = "-dateClosed null";
        if (dateClosed != null) {
            closedate = "-dateClosed " + dateClosed.toString().replace('-', '/');
        }
        return this.caseDao.create("-caseID " + caseID.toString(), "-cprNumber " + Long.toString(cprNumber), "-type " + type, "-mainBody " + mainBody, "-dateCreated " + dateCreated.toString().replace('-', '/'), closedate, "-departmentID " + Integer.toString(departmentID), "-inquiry " + inquiry);
    }

    @Override
    public ArrayList<String[]> getCasesByUserID(String userID) {
        return this.caseDao.getAll("-id " + userID, "-user");
    }

    @Override
    public void saveCaseUserRelation(UUID caseID, ArrayList<String> userID) {
        StringBuilder builder = new StringBuilder();
        userID.stream().map((String userID1) -> {
            builder.append(userID1);
            return userID1;
        }).forEachOrdered((String _item) -> {
            builder.append(" ");
        });
        this.caseUserRelationDao.create("-id " + caseID.toString(), "-users " + builder.toString());
    }

    @Override
    public void insertNewPatient(long cpr, String firstName, String lastName) {
        this.cprDao.create("-cpr " + Long.toString(cpr), "-firstname " + firstName, "-lastname " + lastName);
    }

    @Override
    public ArrayList<String[]> getCasesByDepartment(long departmentID) {
        return this.caseDao.getAll("-id " + departmentID, "-department");
    }

    //==========================================================================
    // Department methods
    //==========================================================================
    @Override
    public ArrayList<String> getDepartments() {
        return this.departmentDao.getAll();
    }

    @Override
    public String getDepartmentNameById(int departmentId) {
        return this.departmentDao.get(Integer.toString(departmentId))[1];
    }

    //==========================================================================
    // User methods
    //==========================================================================
    @Override
    public ArrayList<Long> getUserDepartments(String userID) {
        String[] dataset = this.employeesOfDepartmentDAO.get("-id " + userID);
        ArrayList<Long> longList = new ArrayList<>();
        if (dataset != null) {
            for (String dataset1 : dataset) {
                longList.add(Long.valueOf(dataset1));
            }
        }
        return longList;
    }

    @Override
    public void createUser(String userName, String firstName, String lastName, String password, int typeid, int departmentid) {
        this.userDao.create("-userName " + userName,"-firstName " + firstName,"-lastName " + lastName,"-password " + password,"-typeid " + Integer.toString(typeid),"-departmentid " + Integer.toString(departmentid));
    }

    @Override
    public String[] getUser(String username, String password) {
        return this.userDao.get(new String[]{username, password});
    }

    @Override
    public boolean validateUserID(String userID) {
        return this.userDao.validateUserID(userID);
    }

    @Override
    public boolean validateUserPassword(long userID, String password) {
        return this.userDao.validateUserPassword(userID, password);
    }

    @Override
    public String[] getUserTypes() {
        ArrayList<String[]> dataset = (ArrayList<String[]>) this.userTypeRelationDao.getAll();
        String[] types = new String[dataset.size()];
        for (int i = 0; i < dataset.size(); i++) {
            types[i] = dataset.get(i)[0];
        }
        return types;
    }

    @Override
    public ArrayList<String[]> getAllUsers(ArrayList<Long> departments) {
        Long[] array = departments.toArray(new Long[departments.size()]);
        String[] data = new String[departments.size()];
        for (int i = 0; i < departments.size(); i++) {
            data[i] = Long.toString(array[i]);
        }
        return (ArrayList<String[]>) this.userDao.getAll(data);
    }

    @Override
    public boolean changePassword(String newPassword, String oldPassword, String username) {
        if (getUser(username, oldPassword) != null) {
            return userDao.update(-1 ,"-updatePassword ","-newPassword " + newPassword,"-username " + username);
        } else {
            return false;
        }
    }

    @Override
    public void updateUserInfo(long userID, int role, boolean inactive) {
        this.userDao.update(userID, "-type " + Integer.toString(role), "-inactive " + Boolean.toString(inactive));
    }

}
