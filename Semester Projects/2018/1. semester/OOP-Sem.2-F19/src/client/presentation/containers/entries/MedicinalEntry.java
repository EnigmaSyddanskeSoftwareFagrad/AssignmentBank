/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.containers.entries;

import client.presentation.modules.Module;
import client.presentation.modules.journal.MedicinalEntryCreationPopupFXMLController;
import client.presentation.modules.journal.MedicinalEntryShowMessagePopupFXMLController;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.text.DateFormatter;

/**
 *
 * @author Sanitas Solutions
 */
public class MedicinalEntry {

    private final String patientID;
    private final String date;
    private final String notes;
    private final String medicin;
    private final String dosage;

    /**
     * Constructs a medicinal entry for use in medicinal journal
     *
     * @param patientID patients id
     * @param date      date of entry
     * @param text      the text associated
     */
    public MedicinalEntry(String patientID, String date, String text) {
        this.patientID = patientID;
        this.date = date;
        this.notes = text.split(";:;")[2];
        this.medicin = text.split(";:;")[0];
        this.dosage = text.split(";:;")[1];

    }

    /**
     * Shows a popup containing the medicinalEntrys data
     */
    public void showPopup() {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/presentation/modules/journal/MedicinalEntryShowMessagePopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                MedicinalEntryShowMessagePopupFXMLController controller = fxmlLoader.<MedicinalEntryShowMessagePopupFXMLController>getController();
                controller.setData(notes, date, medicin, dosage);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                root.getStylesheets().add(getClass().getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
            }
        });
    }

    /**
     * Opens a popup to create a medicinalEntry
     *
     * @param moduleController the controller that started the medicinal entry
     */
    public static void showCreationPopup(Module moduleController) {
        Platform.runLater(()
                -> {
            try {
                System.out.println("happen");
                FXMLLoader fxmlLoader = new FXMLLoader(MedicinalEntry.class.getResource("/client/presentation/modules/journal/MedicinalEntryCreationPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                fxmlLoader.<MedicinalEntryCreationPopupFXMLController>getController().setModuleController(moduleController);
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initStyle(StageStyle.UNDECORATED);
                root.getStylesheets().add(MessageEntry.class.getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Formats the medicinalEntry to be show in a listview
     *
     * @return the formatted string
     */
    @Override
    public String toString() {
        try {
            DateFormatter dateFormatter = new DateFormatter(DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.MEDIUM));
            String headLine = String.format("Medicin type: %-30sDosage: %s", medicin, dosage);
            return String.format("%-40s : %s", dateFormatter.valueToString(new Date(Long.parseLong(date))), headLine);
        } catch (ParseException ex) {
            ex.printStackTrace(System.err);
            return "";
        }
    }
}
