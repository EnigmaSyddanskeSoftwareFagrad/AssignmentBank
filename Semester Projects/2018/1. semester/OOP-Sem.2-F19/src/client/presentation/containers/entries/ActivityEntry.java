/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.containers.entries;

import client.presentation.modules.dashboard.ActivityEntryPopupFXMLController;
import static client.presentation.utils.StringUtils.getBoldString;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sanitas Solutions
 */
public class ActivityEntry {

    private final String typeOfEntry;
    private final String dateOfEntryString;
    private final String specificsOfEntry;
    private final String ip;

    /**
     * Contructs an activity entry
     *
     * @param typeOfEntry      the entry type
     * @param dateOfEntry      the date
     * @param specificsOfEntry the parameters of the activity
     * @param ip               the user ip
     */
    public ActivityEntry(String typeOfEntry, Date dateOfEntry, String specificsOfEntry, String ip) {
        this.typeOfEntry = typeOfEntry;
        this.dateOfEntryString = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(dateOfEntry);
        this.specificsOfEntry = specificsOfEntry;
        this.ip = ip;
    }

    @Override
    public String toString() {
        return getBoldString(typeOfEntry) + "\n" + ip + "\n" + dateOfEntryString;
    }

    /**
     *
     */
    public void showPopup() {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/presentation/modules/dashboard/ActivityEntryPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                ActivityEntryPopupFXMLController controller = fxmlLoader.<ActivityEntryPopupFXMLController>getController();
                controller.setData(typeOfEntry, specificsOfEntry, dateOfEntryString, ip);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                root.getStylesheets().add(MessageEntry.class.getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
            }
        });
    }
}
