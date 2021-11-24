/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.containers;

import java.util.Objects;

/**
 *
 * @author Sanitas Solutions
 */
public class Patient implements Comparable {

    private final String fullName;
    private final String patientID;

    /**
     *
     * @param fullName  is the full name of the patient
     * @param patientID is the unique id of the patient
     */
    public Patient(String fullName, String patientID) {
        this.fullName = fullName;
        this.patientID = patientID;
    }

    /**
     * Formats the information about the patient to be displayed in a listView
     *
     * @return the formatted string
     */
    @Override
    public String toString() {
        return fullName;
    }

    /**
     *
     * @return the full name of the patient
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @return the patients unique identifier
     */
    public String getPatientID() {
        return patientID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.fullName);
        hash = 17 * hash + Objects.hashCode(this.patientID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Patient other = (Patient) obj;
        return this.patientID.equals(other.patientID);
    }

    @Override
    public int compareTo(Object o) {
        return Integer.parseInt(this.patientID) - Integer.parseInt(((Patient) o).getPatientID());
    }

}
