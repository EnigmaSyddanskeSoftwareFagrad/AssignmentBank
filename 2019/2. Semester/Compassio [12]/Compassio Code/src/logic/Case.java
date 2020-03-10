package logic;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Represents a social case created by a case worker
 *
 * @author Frederik Haagensen
 * @author Bent Wilhelmsen
 */
public class Case {

    private String firstName;

    private String lastName;

    private UUID caseID;

    private long cprNumber;

    private String type;

    private String mainBody;

    private LocalDate dateCreated;

    private LocalDate dateClosed;

    private int departmentID;

    private String inquiry;

    /**
     * Creates a case in specified cprNumber, a random ID, on current date.
     *
     * @param cprNumber cpr of citizen associated with the case.
     */
    public Case(long cprNumber) {
        this.cprNumber = cprNumber;
        this.dateCreated = LocalDate.now();
        this.caseID = UUID.randomUUID();
    }

    /**
     * Creates a case in specified cprNumber, specified ID, on the specified
     * date.
     *
     * @param cprNumber cpr of citizen associated with the case.
     * @param caseID id of the case.
     * @param dateCreated date the case was created.
     */
    public Case(long cprNumber, UUID caseID, LocalDate dateCreated) {
        this.cprNumber = cprNumber;
        this.dateCreated = dateCreated;
        this.caseID = caseID;
    }

    /**
     * Creates a case in specified citizens cprNumber, first name, last name, of
     * type with details listed in mainBody on specified creation date, with an
     * inquiry to specified department. Must specify ID.
     *
     * @param firstName first name of citizen.
     * @param lastName last name of citizen.
     * @param caseID id of the case.
     * @param cprNumber cpr of citizen associated with the case.
     * @param type case type.
     * @param mainBody case workers notes on the case.
     * @param dateCreated date the case was created.
     * @param dateClosed date the case was closed.
     * @param departmentID id of the department inquiry is to be sent to.
     * @param inquiry inquiry to the specified department.
     */
    public Case(String firstName, String lastName, UUID caseID, long cprNumber, String type, String mainBody, LocalDate dateCreated, LocalDate dateClosed, int departmentID, String inquiry) {
        this(cprNumber, caseID, dateCreated);
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.mainBody = mainBody;
        this.dateClosed = dateClosed;
        this.departmentID = departmentID;
        this.inquiry = inquiry;
    }

    /**
     * Creates a case in specified citizens cprNumber, first name, last name, of
     * type with details listed in mainBody on specified creation date, with an
     * inquiry to specified department. ID is automatically generated.
     *
     * @param firstName first name of citizen.
     * @param lastName last name of citizen.
     * @param cprNumber cpr of citizen associated with the case.
     * @param type case type.
     * @param mainBody case workers notes on the case.
     * @param dateCreated date the case was created.
     * @param dateClosed date the case was closed.
     * @param departmentID id of the department inquiry is to be sent to.
     * @param inquiry inquiry to the specified department.
     */
    public Case(String firstName, String lastName, long cprNumber, String type, String mainBody, LocalDate dateCreated, LocalDate dateClosed, int departmentID, String inquiry) {
        this(cprNumber, UUID.randomUUID(), dateCreated);
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.mainBody = mainBody;
        this.dateClosed = dateClosed;
        this.departmentID = departmentID;
        this.inquiry = inquiry;
    }

    /**
     * Returns the case id
     *
     * @return id of the case
     */
    public UUID getCaseID() {
        return caseID;
    }

    /**
     * Returns cpr-number of the citizen.
     *
     * @return cpr-number of the citizen.
     */
    public long getCprNumber() {
        return cprNumber;
    }

    /**
     * Returns the case type.
     *
     * @return case type.
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the main body of the case.
     *
     * @return main body of case
     */
    public String getMainBody() {
        return mainBody;
    }

    /**
     * Returns the date the case was created.
     *
     * @return date case was created
     */
    public LocalDate getDateCreated() {
        return dateCreated;
    }

    /**
     * Returns the date the case was closed.
     *
     * @return date case was closed
     */
    public LocalDate getDateClosed() {
        return dateClosed;
    }

    /**
     * Returns the id of associated department.
     *
     * @return id of associated department.
     */
    public int getDepartmentID() {
        return departmentID;
    }

    /**
     * Returns the inquiry associated with the case.
     *
     * @return inquiry message of case.
     */
    public String getInquiry() {
        return inquiry;
    }

    /**
     * Returns the first name of the citizen associated with the case.
     *
     * @return first name of citizen.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the last name of the citizen associated with the case.
     *
     * @return first name of citizen.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the first name of the citizen associated with the case.
     *
     * @param firstName the first name of the citizen.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the case ID of the case.
     *
     * @param caseID the id of the case.
     */
    public void setCaseID(UUID caseID) {
        this.caseID = caseID;
    }

    /**
     * Sets case type.
     *
     * @param type type of case.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the main body text of the case.
     *
     * @param mainBody main body text of case.
     */
    public void setMainBody(String mainBody) {
        this.mainBody = mainBody;
    }

    /**
     * Sets the date the case was closed. Returns <code>true</code> on success.
     * Returns <code>false</code> if closed date is before the creation date.
     *
     * @param dateClosed
     * @return Returns <code>true</code> on success. <code>false</code> false on
     * failure.
     */
    public boolean setDateClosed(LocalDate dateClosed) {
        if (dateClosed == null) {
            this.dateClosed = dateClosed;
            return true;
        } else if (dateClosed.compareTo(this.dateCreated) > 0) {
            this.dateClosed = dateClosed;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Sets the department the cases is associated with.
     *
     * @param departmentID department to associate the case with.
     */
    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    /**
     * Sets the case inquiry message.
     *
     * @param inquiry inquiry message to department.
     */
    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }

    /**
     * Saves the case to persistent storage.
     *
     * @return <code>true</code> if save was successful. <code>false</code> on
     * failure
     */
    public boolean saveCase() {
        return LogicFacade.getPersistence().saveCase(caseID, cprNumber, type, mainBody, dateCreated, dateClosed, departmentID, inquiry);
    }

    /**
     * Adds the specified person to the CPR table. This is for demonstration
     * purposes.
     */
    public void addPatientToDatabase() {
        LogicFacade.getPersistence().insertNewPatient(cprNumber, firstName, lastName);
    }
    
    /**
     * Compares two cases to see if they are the same.
     * @param c case to compare to.
     * @return <code>true</code> if the cases are the same.
     */
    public boolean compareTo(Case c) {
        boolean mainBodyEquals = (mainBody != null ? mainBody.equals(c.mainBody) : mainBody == c.mainBody);
        boolean dateClosedEquals = (dateClosed != null ? dateClosed.equals(c.dateClosed) : dateClosed == c.dateClosed);
        boolean inquiryEquals = (inquiry != null ? inquiry.equals(c.inquiry) : inquiry == c.inquiry);

        return firstName.equals(c.firstName) && lastName.equals(c.lastName) && caseID.equals(c.caseID) && cprNumber == c.cprNumber && type.equals(c.type) && mainBodyEquals && dateCreated.equals(c.dateCreated) && dateClosedEquals && departmentID == c.departmentID && inquiryEquals;
    }
}
