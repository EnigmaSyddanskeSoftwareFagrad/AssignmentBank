/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.dashboard;

import client.presentation.modules.Popup;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class MessageEntryPopupFXMLController extends Popup {

    /**
     * The icon for the cross that exits the programm
     */
    @FXML
    private FontAwesomeIconView cross;

    /**
     * The title of the MessageEntry
     */
    @FXML
    private Label title;

    /**
     * The message of the the MessageEntry
     */
    @FXML
    private Label message;

    /**
     * The date of the MessageEntry
     */
    @FXML
    private Label date;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    /**
     * Closes the popup
     *
     * @param event
     */
    @FXML
    private void close(MouseEvent event) {
        ((Stage) cross.getScene().getWindow()).close();
    }

    /**
     * Sets the data of the MessageEntry popup
     *
     * @param titleString   sets the title of the message
     * @param messageString sets the message contents
     * @param dateString    sets the date
     */
    public void setData(String titleString, String messageString, String dateString) {
        title.setText(titleString);
        message.setText(messageString);
        date.setText(dateString);
    }

}
