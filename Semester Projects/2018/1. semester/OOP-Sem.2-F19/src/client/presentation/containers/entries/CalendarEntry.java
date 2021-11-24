/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.containers.entries;

import client.presentation.utils.StringUtils;

/**
 *
 * @author Sanitas Solutions
 */
public class CalendarEntry {

    private final String title;
    private final String description;

    public CalendarEntry(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Override
    public String toString() {
        return StringUtils.getBoldString(title) + "\n" + description;
    }
}
