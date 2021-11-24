/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.domain;

import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import server.persistence.PersistanceInterface;
import server.persistence.PersistanceInterfaceImpl;
import server.recources.ConfigReader;

/**
 *
 * @author Sanitas Solutions
 */
public class DomainInterfaceImpl implements DomainInterface {

    private final PersistanceInterface persistenceInterface = PersistanceInterfaceImpl.getInstance();
    private String userId = null;
    private String ip = null;
    private List<String> rights = null;
    private Map<String, String> smtpConfiguration = null;
    private static final String PASS_CHARS = "ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvxyz,.-1234567890+?!@#&/";
    private String[] query;
    private static final Map<String, String> ACTIONS = new HashMap<String, String>() {
        {
            put("login", "");
            put("getCalendar", "005-001");
            put("getEventParticipants", "005-001");
            put("addCalendarEvent", "005-001");
            put("updateCalendarEvent", "005-001");
            put("removeCalendarEvent", "005-001");
            put("setRhythmHour", "005-002");
            put("getDayRhythm", "005-002");
            put("addPatient", "004-002");
            put("getPatientsByUser", "000-000");
            put("getPatients", "004-001");
            put("updatePatientAssignment", "004-005");
            put("addUser", "002-002");
            put("userListByDepartment", "000-000");
            put("userList", "002-001");
            put("alterUserFullName", "002-003");
            put("alterUserUsername", "002-003");
            put("resetUserPassword", "002-005");
            put("alterOwnPassword", "003-001");
            put("setUserDepartment", "002-003");
            put("setUserRoles", "002-006");
            put("getUserRoles", "002-006");
            put("getRoles", "002-006");
            put("getDepartments", "000-000");
            put("addJournalEntry", "001-001");
            put("getMedicinalJournal", "001-002");
            put("getJournal", "001-001");
            put("addActivity", "");
            put("getActivity", "");
            put("getUserActivity", "002-007");
            put("sendMessage", "");
            put("getMessages", "");
            put("getMenuItems", "");
            put("getPatientsByDepartment", "000-000");
        }
    };

    /**
     * Constructs a new Action handler that handles predefined actions
     *
     * @param ip the IP that should be logged on specific actions
     */
    public DomainInterfaceImpl(String ip) {
        this.ip = ip;
        try {
            smtpConfiguration = new ConfigReader("SMTP").getProperties();
        } catch (IllegalArgumentException | NullPointerException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Parses the query by passing an altered version to the domain layer
     *
     * @param query is the query received from the client
     * @return the expected result based on the parseble quarries
     * <p>
     * <p>
     * </p>
     * <pre>
     * Actions:                     Param 1:            Param 2:        Param 3:    Param 4:    Param 5:        Param 6:
     *
     * "login"                      Username            HashedPassword
     * "getCalendar"                Patient-id          Date_min        Date_max
     * "getEventParticipants"       eventId
     * "addCalendarEvent"           date-start          date_end        name        details     participants…
     * "updateCalendarEvent"        eventId             date-start      date_end    name        details         participants…
     * "removeCalendarEvent"        eventId
     * "setRhythmHour"              Patient-id          hour            icon        title
     * "getDayRhythm"               Patient-id
     * "addPatient"                 full_name
     * "getPatientsByDepartment"    department
     * "getPatientsByUser"          userId
     * "getPatients"
     * "updatePatientAssignment"    userId              patient…
     * "addUser"                    username            fullname        department
     * "userListBydepartment"       department
     * "userList"
     * "alterUserfullName"          userId              full_Name
     * "resetUserPassword"          userId              department      username
     * "alterOwnPassword"           newHashedPassword
     * "setUserDepartment"          userId              departmentID
     * "setUserRoles"               userId              roles…
     * "getUserRoles"               userId
     * "getRoles"
     * "getDepartments"
     * "addJournalEntry"            Patient-id          entrytype       contents
     * "getMedicinalJournal"        Patient-id
     * "getJournal"                 Patient-id
     * "getActivity"
     * "getUserActivity"            userId
     * "getMessages"
     * "sendMessage"                recieverUserName    header          contents
     * "getMenuItems"
     *
     * </pre>
     * In the event that the system does not return data from the database a
     * message is returned in the form of {@literal List<String[]>} with the
     * first index in the first array being a single word descriptor and the
     * second index being a message associated
     * <p>
     * </p>
     * <pre>
     * Descriptors:
     *  * Error - In the event of errors
     *  * Success - In The event of success
     * </pre>
     */
    @Override
    public List<String[]> parseQuery(String[] query) {
        this.query = query;
        try {
            List<String[]> data = persistenceInterface.parseQuery("checkCredentials", query[1], query[2]);

            if (!data.isEmpty()) {
                userId = data.get(0)[1];
                rights = persistenceInterface.parseQuery("getUserRoles", userId).stream().map(t -> t[0]).collect(Collectors.toList());

                if (hasRights(query[0])) {
                    switch (query[0]) {
                        case "login":
                            addActivity();
                            return data;

                        case "getCalendar":
                            if (query[3].equals(userId) || isAssignedPatient(query[3])) {
                                addActivity();
                                return persistenceInterface.parseQuery("getCalendar", query[3], query[4], query[5]);
                            } else {
                                return constructReturn("Error", "Patient not assigned");
                            }

                        case "getEventParticipants":
                            return persistenceInterface.parseQuery("getEventParticipants", query[3]);

                        case "addCalendarEvent":
                            addActivity();
                            persistenceInterface.parseQuery("addCalendarEvent", query[3], query[4], query[5], query[6]);
                            List<String[]> eventId = persistenceInterface.parseQuery("maxEventId");
                            for (int i = 7; i < query.length; i++) {
                                persistenceInterface.parseQuery("addEventParticipant", eventId.get(0)[0], query[i]);
                            }
                            return eventId;

                        case "updateCalendarEvent":
                            addActivity();
                            persistenceInterface.parseQuery("updateCalendarEvent", query[3], query[4], query[5], query[6], query[7]);
                            List<String> participants = persistenceInterface.parseQuery("getEventParticipants", query[3]).stream().map(t -> t[0]).collect(Collectors.toList());
                            for (int i = 8; i < query.length; i++) {
                                if (!participants.contains(query[i])) {
                                    persistenceInterface.parseQuery("addEventParticipant", query[3], query[i]);
                                } else {
                                    participants.remove(participants.indexOf(query[i]));
                                }
                            }
                            participants.forEach((t) -> {
                                persistenceInterface.parseQuery("removeEventParticipant", query[3], t);
                            });
                            return constructReturn("Success", "Event successfully updated");

                        case "removeCalendarEvent":
                            addActivity();
                            persistenceInterface.parseQuery("removeCalendarEvent", query[3]);
                            return constructReturn("Success", "Event Removed");

                        case "setRhythmHour":
                            addActivity();
                            List<String> rhythmHours = persistenceInterface.parseQuery("getDayRhythm", query[3]).stream().map(t -> t[0]).collect(Collectors.toList());
                            if (rhythmHours.contains(query[4])) {
                                persistenceInterface.parseQuery("updateRhythmHour", query[3], query[4], query[5], query[6]);
                            } else {
                                persistenceInterface.parseQuery("setRhythmHour", query[3], query[4], query[5], query[6]);
                            }
                            return constructReturn("Success", "Patient rhytm updated");

                        case "getDayRhythm":
                            return persistenceInterface.parseQuery("getDayRhythm", query[3]);

                        case "addPatient":
                            addActivity();
                            persistenceInterface.parseQuery("addPatient", query[3], query[4]);
                            return constructReturn("Success", "Patienty");

                        case "getPatientsByDepartment":
                            addActivity();
                            return persistenceInterface.parseQuery("getPatientsByDepartment", query[3]);

                        case "getPatientsByUser":
                            return persistenceInterface.parseQuery("getPatients", query[3]);

                        case "getPatients":
                            return persistenceInterface.parseQuery("getPatients", userId);

                        case "updatePatientAssignment":
                            addActivity();
                            List<String> assignments = persistenceInterface.parseQuery("getPatients", query[3]).stream().map(t -> t[0]).collect(Collectors.toList());
                            for (int i = 4; i < query.length; i++) {
                                if (!assignments.contains(query[i])) {
                                    persistenceInterface.parseQuery("addAssignedPatient", query[3], query[i]);
                                } else {
                                    assignments.remove(assignments.indexOf(query[i]));
                                }
                            }
                            assignments.forEach((t) -> {
                                persistenceInterface.parseQuery("removeAssignedPatient", query[3], t);
                            });
                            return constructReturn("Success", "Patients updated");

                        case "addUser":
                            addActivity();
                            String password = generatePassword();
                            persistenceInterface.parseQuery("addUser", query[3], query[4], Hashing.sha256().hashString("sanitasoverviewsalt" + query[3] + password, Charset.forName("UTF-8")).toString(), query[5]);
                            List<String[]> result = persistenceInterface.parseQuery("getMailDomainByDepartment", query[5]);
                            sendPassword(query[3], result.get(0)[0], password);
                            return result;

                        case "userListByDepartment":
                            addActivity();
                            return persistenceInterface.parseQuery("getUsersByDepartment", query[3]);

                        case "userList":
                            addActivity();
                            List<String[]> department = persistenceInterface.parseQuery("getUserDepartment", userId);
                            return persistenceInterface.parseQuery("getUsersByDepartment", department.get(0)[0]);

                        case "alterUserFullName":
                            addActivity();
                            persistenceInterface.parseQuery("alterUserFullName", query[3], query[4]);
                            return constructReturn("Success", "Full name of user altered");

                        case "resetUserPassword":
                            addActivity();
                            String newPassword = generatePassword();
                            persistenceInterface.parseQuery("setUserPassword", query[3], Hashing.sha256().hashString("sanitasoverviewsalt" + query[5] + newPassword, Charset.forName("UTF-8")).toString());
                            String domain = persistenceInterface.parseQuery("getMailDomainByDepartment", query[4]).get(0)[0];
                            sendPassword(query[5], domain, newPassword);
                            return constructReturn("Success", "Password updated");

                        case "alterOwnPassword":
                            addActivity();
                            persistenceInterface.parseQuery("setUserPassword", userId, query[3]);
                            return constructReturn("Success", "Password succesfully updated");

                        case "setUserDepartment":
                            persistenceInterface.parseQuery("setUserDepartment", query[3], query[4]);
                            return constructReturn("Success", "Patients updated");

                        case "setUserRoles":
                            addActivity();
                            List<String> roles = persistenceInterface.parseQuery("getUserRoles", query[3]).stream().map(t -> t[0]).collect(Collectors.toList());
                            for (int i = 4; i < query.length; i++) {
                                if (!roles.contains(query[i])) {
                                    persistenceInterface.parseQuery("addUserRole", query[3], query[i]);
                                } else {
                                    roles.remove(roles.indexOf(query[i]));
                                }
                            }
                            roles.forEach((t) -> {
                                persistenceInterface.parseQuery("removeUserRole", query[3], t);
                            });
                            return constructReturn("Success", "roles successfully updated");

                        case "getUserRoles":
                            return persistenceInterface.parseQuery("getUserRoles", query[3]);

                        case "getRoles":
                            return persistenceInterface.parseQuery("getRoles");

                        case "getDepartments":
                            return persistenceInterface.parseQuery("getDepartments");

                        case "addJournalEntry":
                            addActivity();
                            persistenceInterface.parseQuery("addJournalEntry", query[3], query[4], query[5], Long.toString(System.currentTimeMillis()), userId);
                            return constructReturn("Success", "Entry added");

                        case "getMedicinalJournal":
                            addActivity();
                            return persistenceInterface.parseQuery("getMedicalJournal", query[3]);

                        case "getJournal":
                            addActivity();
                            return persistenceInterface.parseQuery("getJournal", query[3]);

                        case "getActivity":
                            return persistenceInterface.parseQuery("getActivity", userId);

                        case "getUserActivity":
                            addActivity();
                            return persistenceInterface.parseQuery("getActivity", query[3]);

                        case "getMessages":
                            return persistenceInterface.parseQuery("getMessages", userId);

                        case "sendMessage":
                            addActivity();
                            return persistenceInterface.parseQuery("sendMessage", userId, query[3], query[4], query[5]);

                        case "getMenuItems":
                            return persistenceInterface.parseQuery("getMenuItems", userId);
                        default:
                            return constructReturn("Error", "The action: " + query[0] + " Does not exist");
                    }
                } else {
                    return constructReturn("Error", "Missing required roles");
                }
            } else {
                return constructReturn("Error", "Credentials invalid");
            }
        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
            e.printStackTrace();
        }
        return constructReturn("Error", "Unpredicted error found in domain");
    }

    private List<String[]> constructReturn(String... input) {
        return new ArrayList<>(Arrays.asList(new String[][]{new String[]{input[0], input[1]}}));
    }

    private void addActivity() {
        StringBuilder specifics = new StringBuilder();
        if (query.length > 3) {
            for (int i = 3; i < query.length; i++) {
                specifics.append(query[i]).append(";:;");
            }
            specifics.delete(specifics.length() - 3, specifics.length());
        }
        persistenceInterface.parseQuery("addActivity", query[0], specifics.toString(), this.ip, this.userId);
    }

    private boolean hasRights(String action) {
        if (ACTIONS.get(action) == null) {
            System.err.printf("Role '%s' non existant", action);

        }
        return action != null && (ACTIONS.get(action).isEmpty() || rights.contains(ACTIONS.get(action)) || rights.contains("000-000"));
    }

    private boolean isAssignedPatient(String id) {
        return persistenceInterface.parseQuery("getPatients", userId).stream().map(t -> t[0]).collect(Collectors.toList()).contains(id) || rights.contains("000-000");
    }

    /**
     * Sends an email containing the username and password of the user
     *
     * @param username is the username of the user
     * @param domain   is the email domain of the user
     * @param password is the un-encrypted password of the user
     */
    private void sendPassword(String username, String domain, String password) {
        new EmailHandler(smtpConfiguration.get("host"), smtpConfiguration.get("port"), smtpConfiguration.get("username"), smtpConfiguration.get("password")).sendMail(username + "@" + domain, password);
    }

    /**
     * Generates a 16 char random password containing random characters from the
     * following list
     * <pre>
     *Pass chars: {@value DomainInterfaceImpl#PASS_CHARS}
     * </pre>
     *
     * @return random 16 char password
     */
    private String generatePassword() {
        StringBuilder passwordBuilder = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            passwordBuilder.append(PASS_CHARS.charAt((int) (Math.random() * 70)));
        }
        return passwordBuilder.toString();
    }
}
