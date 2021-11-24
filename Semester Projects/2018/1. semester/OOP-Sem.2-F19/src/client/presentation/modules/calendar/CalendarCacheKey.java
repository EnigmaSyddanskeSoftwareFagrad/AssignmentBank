/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.calendar;

import java.util.Objects;

/**
 *
 * @author Sanitas Solutions
 */
public class CalendarCacheKey {

    private long patientID;
    private long epochDateStart;

    public CalendarCacheKey(long patientID, long epochDateStart) {
        this.patientID = patientID;
        this.epochDateStart = epochDateStart;
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
        final CalendarCacheKey other = (CalendarCacheKey) obj;

        return Objects.equals(this.patientID, other.patientID) && Objects.equals(this.epochDateStart, other.epochDateStart);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.patientID);
        hash = 17 * hash + Objects.hashCode(this.epochDateStart);
        return hash;
    }
}
