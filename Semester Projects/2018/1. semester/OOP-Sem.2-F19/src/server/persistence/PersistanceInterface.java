/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package server.persistence;

import java.util.List;

/**
 *
 * @author Sanitas Solutions
 */
public interface PersistanceInterface {

    /**
     * Parses a query based on a set of predefined actions
     * <P>
     * The first position of the query is the action to be performed and the
     * following positions are parameters
     * <P>
     *
     * In the event that the action is something other than
     * <pre>
     *    Actions:                      Param 1:    Param 2:            Param 3:    Param 4:    Param 5:
     *  * "checkCredentials"            Username    HashedPassword
     *  * "getCalendar"                 id          Date_min            Date_max
     *  * "getEventParticipants"        eventId
     *  * "addCalendarEvent"            date_start  date_end            name        details
     *  * "addEventParticipant"         eventId     participant
     *  * "removeEventParticipant"      eventId     participant
     *  * "updateCalendarEvent"         eventId     date_start          date_end    name        details
     *  * "removeCalendarEvent"         eventId
     *  * "setRhythmHour"               Patient     id                  hour        icon	title
     *  * "getDayRhythm"                patient     id
     *  * "updateRhythmHour"            patient     id                  hour        icon	title
     *  * "addPatient"                  fullname    department
     *  * "getpatientsByDepartment"     department
     *  * "getPatients"                 userid
     *  * "addAssignedPatient"          userId      PatientId
     *  * "removeAssignedPatient"       userId      PatientId
     *  * "addUser"                     username    fullname            password    department
     *  * "getUsersByDepartment"        department
     *  * "alterUserFullName"           userId      fullName
     *  * "setUserPassword"             userId      hashedPassword
     *  * "setUserDepartment"           userId      departmentId
     *  * "getUserDepartment"           userid
     *  * "addUserRole"                 userId      role
     *  * "removeUserRole"              userId      role
     *  * "getUserRoles"                userId
     *  * "getRoles"
     *  * "getDepartments"
     *  * "addJournalEntry"             Patient     id                  department  date	contents	userid      entrytype
     *  * "getJournal"                  Patient     id
     *  * "getMedicinalJournal"         Patient     id
     *  * "addActivity"                 type        specifics           ip          userId
     *  * "getActivity"                 userId
     *  * "getMessages"                 userid
     *  * "sendMessage"                 from id     recieverUserName    header      contents
     *  * "getMenuItems"                userid
     *  * "getMailDomainByDepartment"   userid
     * </pre>
     *
     * @param query is the query
     * @return the result of the query or a statement of success or error and
     *         cause
     */
    public List<String[]> parseQuery(String... query);

}
