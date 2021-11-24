/*
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package client.presentation.containers.entries;

import client.presentation.containers.Patient;

/**
 *
 * @author Oliver
 */
public class EventDataEntry {

    private String eventID;

    /**
     * Get the value of eventID
     *
     * @return the value of eventID
     */
    public String getEventID() {
        return eventID;
    }

    private Patient[] eventParticipants;

    /**
     * Get the value of eventParticipants
     *
     * @return the value of eventParticipants
     */
    public Patient[] getEventParticipants() {
        return eventParticipants;
    }

    /**
     * Set the value of eventParticipants
     *
     * @param eventParticipants new value of eventParticipants
     */
    public void setEventParticipants(Patient[] eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

    public EventDataEntry(String eventID, Patient[] eventParticipants) {
        this.eventID = eventID;
        this.eventParticipants = eventParticipants;
    }

}
