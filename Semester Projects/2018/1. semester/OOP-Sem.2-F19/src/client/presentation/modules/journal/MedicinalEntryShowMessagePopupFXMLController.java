/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.journal;

import client.presentation.modules.Popup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javax.swing.text.DateFormatter;

/**
 *
 * @author Sanitas Solutions
 */
public class MedicinalEntryShowMessagePopupFXMLController extends Popup {

    @FXML
    private FontAwesomeIconView cross;
    @FXML
    private JFXTextField medicine;
    @FXML
    private JFXTextField dosage;
    @FXML
    private JFXTextField notes;
    @FXML
    private JFXButton closeButton;
    @FXML
    private Label dateLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void close(MouseEvent event) {
        close();
    }

    @FXML
    private void closeButton(ActionEvent event) {
        close();
    }

    /**
     * Sets the data for the popup
     *
     * @param notes      the data for the notes field in the medicinal popup
     * @param dateString the date as a long stored in a string
     * @param medicin    the data for the medicine field in the medicinal popup
     * @param dosage     the data for the dosage field in the medicinal popup
     */
    public void setData(String notes, String dateString, String medicin, String dosage) {
        DateFormatter dateFormatter = new DateFormatter(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM));
        this.notes.setText(notes);
        this.dosage.setText(dosage);
        this.medicine.setText(medicin);
        try {
            dateLabel.setText(dateFormatter.valueToString(new Date(Long.parseLong(dateString))));
        } catch (ParseException ex) {
            ex.printStackTrace(System.err);
        }
    }

}
