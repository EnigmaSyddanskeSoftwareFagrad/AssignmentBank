/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import server.recources.ConfigReader;

/**
 *
 * @author Sanitas Solutions
 */
public class PersistanceInterfaceImpl implements PersistanceInterface {

    private static PersistanceInterfaceImpl instance = null;
    private Map<String, String> configFileMap;
    private Connection conn = null;

    /**
     * Private constructor to initiate the singleton
     */
    private PersistanceInterfaceImpl() {

        //Load settings from config file
        //Initiate connection
        try {
            this.configFileMap = new ConfigReader("Database").getProperties();
            DriverManager.registerDriver(new org.postgresql.Driver());
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection("jdbc:postgresql://" + configFileMap.get("url") + ":" + configFileMap.get("port") + "/" + configFileMap.get("databaseName") + "?sslmode=require", configFileMap.get("username"), configFileMap.get("password"));
        } catch (ClassNotFoundException | SQLException | IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Returns a singleton instance of the persistence controller so as to
     * optimise speed by keeping the SQL connection open
     *
     * @return the instance of the persistence controller
     */
    public static PersistanceInterfaceImpl getInstance() {
        if (instance == null) {
            instance = new PersistanceInterfaceImpl();
        }
        return instance;
    }

    @Override
    public List<String[]> parseQuery(String... query) {
        List<String[]> output = constructReturn("Error", "Unexpected error in persistance handler");
        PreparedStatement stmt = null;
        switch (query[0]) {
            case "checkCredentials":
                try {
                    stmt = conn.prepareStatement("SELECT id.full_name, matchingId.id FROM id, (SELECT id FROM users WHERE password = ? AND username = ?) AS matchingId WHERE matchingId.id = id.id");
                    stmt.setString(1, query[2]);
                    stmt.setString(2, query[1]);
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getCalendar":
                try {
                    stmt = conn.prepareStatement("SELECT calendar.* FROM calendar, (SELECT participation.event_id FROM participation WHERE participation.id = ?) AS x WHERE (calendar.date >= ? AND calendar.date <= ?) AND calendar.event_id = x.event_id");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                    stmt.setLong(3, Long.parseLong(query[3]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getEventParticipants":
                try {
                    stmt = conn.prepareStatement("SELECT id.id, id.full_name FROM id, (SELECT id FROM participation WHERE event_id = ?) as participants WHERE id.id = participants.id");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "addCalendarEvent":
                try {
                    stmt = conn.prepareStatement("INSERT INTO calendar VALUES ((SELECT MAX(W.event_id) FROM calendar as W)+1, ?, ?, ?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setString(2, query[3]);
                    stmt.setString(3, query[4]);
                    stmt.setLong(4, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "maxEventId": {
                try {
                    stmt = conn.prepareStatement("SELECT MAX(event_id) FROM calendar");
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
            }
            break;
            case "addEventParticipant":
                try {
                    stmt = conn.prepareStatement("INSERT INTO participation VALUES (?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "removeEventParticipant":
                try {
                    stmt = conn.prepareStatement("DELETE FROM participation WHERE event_id = ? AND id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "updateCalendarEvent":
                try {
                    stmt = conn.prepareStatement("UPDATE calendar SET date = ?, event_name = ?, event_detail = ?, date_end = ? WHERE event_id = ?");
                    stmt.setLong(1, Long.parseLong(query[2]));
                    stmt.setString(2, (query[4]));
                    stmt.setString(3, (query[5]));
                    stmt.setLong(4, Long.parseLong(query[3]));
                    stmt.setLong(5, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "removeCalendarEvent":
                try {
                    stmt = conn.prepareStatement("DELETE FROM participation WHERE event_id = ?; DELETE FROM calendar WHERE event_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "setRhythmHour": {
                try {
                    stmt = conn.prepareStatement("INSERT INTO rhythm(patient_id, hour, icon, title) VALUES (?, ?, ?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setInt(2, Integer.parseInt(query[2]));
                    stmt.setString(3, query[3]);
                    stmt.setString(4, query[4]);
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            }
            case "getDayRhythm": {
                try {
                    stmt = conn.prepareStatement("SELECT hour, icon, title FROM rhythm WHERE patient_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            }
            case "updateRhythmHour": {
                try {
                    stmt = conn.prepareStatement("UPDATE rhythm SET icon = ?, title = ? WHERE patient_id = ? AND hour = ?");
                    stmt.setString(3, query[1]);
                    stmt.setInt(4, Integer.parseInt(query[2]));
                    stmt.setLong(1, Long.parseLong(query[3]));
                    stmt.setString(2, query[4]);
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            }
            case "addPatient":
                try {
                    stmt = conn.prepareStatement("INSERT INTO id VALUES ((SELECT MAX(id) FROM id WHERE id < 9000000000) + 1, ?); INSERT INTO patients VALUES((SELECT MAX(id) FROM id WHERE id < 9000000000), ?)");
                    stmt.setString(1, query[1]);
                    stmt.setString(2, query[2]);
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getPatientsByDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT id.full_name, patients.id, patients.department FROM patients, id WHERE patients.id = id.id AND patients.department = ? ORDER BY patients.id");
                    stmt.setString(1, (query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getPatients":
                try {
                    stmt = conn.prepareStatement("SELECT id.id, id.full_name FROM id, (SELECT patient_id as id FROM patient_assignment WHERE user_id = ?) as compare WHERE compare.id = id.id");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "addAssignedPatient":
                try {
                    stmt = conn.prepareStatement("INSERT INTO patient_assignment VALUES (?,?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "removeAssignedPatient":
                try {
                    stmt = conn.prepareStatement("DELETE FROM patient_assignment WHERE user_id = ? AND patient_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "addUser":
                try {
                    stmt = conn.prepareStatement("INSERT INTO id VALUES((SELECT MAX(id) FROM id)+1, ?); INSERT INTO users VALUES (?, ?, ?, (SELECT MAX(id.id)from id))");
                    stmt.setString(1, (query[2]));
                    stmt.setString(2, (query[1]));
                    stmt.setString(3, (query[3]));
                    stmt.setString(4, (query[4]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getUsersByDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT username, users.id, full_name FROM users, id WHERE id.id = users.id AND users.department = ? ORDER BY users.id");
                    stmt.setString(1, (query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;

            case "alterUserFullName":
                try {
                    stmt = conn.prepareStatement("UPDATE id SET full_name = ? WHERE id = ?");
                    stmt.setString(1, (query[2]));
                    stmt.setLong(2, Long.parseLong(query[1]));

                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "setUserPassword":
                try {
                    stmt = conn.prepareStatement("UPDATE users SET password = ? WHERE id = ?");
                    stmt.setString(1, (query[2]));
                    stmt.setLong(2, Long.parseLong(query[1]));

                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "setUserDepartment":
                try {
                    stmt = conn.prepareStatement("UPDATE users SET department=? WHERE id = ?");
                    stmt.setString(1, (query[2]));
                    stmt.setLong(2, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getUserDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT users.department FROM users WHERE users.id=?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "addUserRole":
                try {
                    stmt = conn.prepareStatement("INSERT INTO role_assignment VALUES (?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setString(2, (query[2]));

                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "removeUserRole":
                try {
                    stmt = conn.prepareStatement("DELETE FROM role_assignment WHERE user_id = ? AND role = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setString(2, (query[2]));

                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getUserRoles":
                try {
                    stmt = conn.prepareStatement("SELECT role FROM role_assignment where user_id = ?");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getRoles":
                try {
                    stmt = conn.prepareStatement("SELECT * FROM roles");
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getDepartments":
                try {
                    stmt = conn.prepareStatement("SELECT department_id, name FROM departments");
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "addJournalEntry":
                try {
                    stmt = conn.prepareStatement("INSERT INTO journal VALUES (?, ?, ?, ?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[4]));
                    stmt.setString(3, query[3]);
                    stmt.setLong(4, Long.parseLong(query[5]));
                    stmt.setString(5, query[2]);
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getJournal":
                try {
                    stmt = conn.prepareStatement("SELECT * FROM journal WHERE patient_id = ? AND entry_type = 'journal' LIMIT 30");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getMedicalJournal":
                try {
                    stmt = conn.prepareStatement("SELECT * FROM journal WHERE patient_id = ? AND entry_type = 'medicinal' LIMIT 30");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "addActivity":
                try {
                    stmt = conn.prepareStatement("INSERT INTO activity (user_id, type, specifics, ip) VALUES (?, ?, ?,?)");
                    stmt.setLong(1, Long.parseLong(query[4]));
                    stmt.setString(2, query[1]);
                    stmt.setString(3, query[2]);
                    stmt.setString(4, query[3]);
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getActivity":
                try {
                    stmt = conn.prepareStatement("SELECT date, type, specifics, ip FROM activity WHERE date > (date_part('epoch'::text, now()) * (1000)::double precision)-25922000000 AND user_id = ? ORDER BY date desc");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "sendMessage":
                try {
                    stmt = conn.prepareStatement("INSERT INTO messages(sender_id, recipient_id, title, message) VALUES (?, ?, ?, ?)");
                    stmt.setLong(1, Long.parseLong(query[1]));
                    stmt.setLong(2, Long.parseLong(query[2]));
                    stmt.setString(3, query[3]);
                    stmt.setString(4, query[4]);
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getMessages":
                try {
                    stmt = conn.prepareStatement("SELECT id.full_name, users.username, messages.title, messages.message, messages.date FROM users, id, messages WHERE id.id = sender_id AND users.id = sender_id AND recipient_id = ? ORDER BY date DESC LIMIT 30");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getMenuItems":
                try {
                    stmt = conn.prepareStatement("SELECT DISTINCT modules.name, modules.icon, modules.fxml, modules.index FROM modules, role_assignment WHERE role_assignment.user_id = ? AND (role_assignment.role = modules.role OR role_assignment.role = '000-000') ORDER BY index");
                    stmt.setLong(1, Long.parseLong(query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            case "getMailDomainByDepartment":
                try {
                    stmt = conn.prepareStatement("SELECT department_mail_domain FROM departments WHERE department_id = ?");
                    stmt.setString(1, (query[1]));
                } catch (SQLException ex) {
                    ex.printStackTrace(System.err);
                }
                break;
            default:
                return constructReturn("Error", String.format("The action \"%s\" is not defined", query[0]));
        }

        //Exceqution of the query
        try {
            ResultSet sqlReturnValues = stmt.executeQuery();
            int columnCount = sqlReturnValues.getMetaData().getColumnCount();
            output = new ArrayList<>();
            while (sqlReturnValues.next()) {
                String[] temp = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    temp[i - 1] = sqlReturnValues.getString(i);
                }
                output.add(temp);

            }
        } catch (SQLException ex) {
            if (!ex.getMessage().contains("No results")) {
                ex.printStackTrace(System.err);
            }
            output = constructReturn("Error", "Unexpected sql error");
        }
        return output;
    }

    private List<String[]> constructReturn(String... input) {
        return new ArrayList<>(Arrays.asList(new String[][]{new String[]{input[0], input[1]}}));
    }
}
