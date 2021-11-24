/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.calendar;

import client.presentation.containers.entries.EventDataEntry;
import client.presentation.modules.Popup;
import com.calendarfx.model.Entry;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class CalendarEventDetailsPopoverFXMLController extends Popup {

    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private Label dateStart;
    @FXML
    private Label dateEnd;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setData(Entry<EventDataEntry> entry) {
        title.setText(entry.getTitle());
        description.setText(entry.getLocation());
        dateStart.setText(LocalDateTime.of(entry.getStartDate(), entry.getStartTime()).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
        dateEnd.setText(LocalDateTime.of(entry.getEndDate(), entry.getEndTime()).format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)));
    }

}
