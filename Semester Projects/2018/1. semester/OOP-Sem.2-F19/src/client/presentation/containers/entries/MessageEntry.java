/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.containers.entries;

import client.presentation.modules.Module;
import client.presentation.modules.dashboard.MessageEntryCreationPopupFXMLController;
import client.presentation.modules.dashboard.MessageEntryPopupFXMLController;
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
public class MessageEntry {

    /**
     * The subject of the the entry
     */
    private final String subject;

    /**
     * The sender of the entry
     */
    private final String sender;

    /**
     * The message of the entry
     */
    private final String message;

    /**
     * The date on which the entry has been sent
     */
    private final String sentDateString;

    private final Module moduleController;

    /**
     * Constructs a new MessageEntry
     *
     * @param subject  The subject of the the entry
     * @param sender   The sender of the entry
     * @param message  The message of the entry
     * @param sentDate The date on which the entry has been sent
     */
    public MessageEntry(String subject, String sender, String message, Date sentDate) {
        this.subject = subject;
        this.sender = sender;
        this.message = message;
        this.sentDateString = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy").format(sentDate);
        this.moduleController = null;
    }

    /**
     *
     * @param moduleController the controller of the module that launched the
     *                         popup
     */
    public MessageEntry(Module moduleController) {
        this.subject = "";
        this.sender = "";
        this.message = "";
        this.sentDateString = "";
        this.moduleController = moduleController;
        showCreationPopup();
    }

    /**
     * A custom toString method
     *
     * @return The MessageEntry as a String
     */
    @Override
    public String toString() {
        return getBoldString(sender) + ": " + subject + "\n" + sentDateString;
    }

    /**
     * Opens a detailed popup of the MessageEntry
     */
    public void showPopup() {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/presentation/modules/dashboard/MessageEntryPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                MessageEntryPopupFXMLController controller = fxmlLoader.<MessageEntryPopupFXMLController>getController();
                controller.setData(sender + ": " + subject, message, sentDateString);
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
     *
     */
    public final void showCreationPopup() {
        Platform.runLater(()
                -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(MessageEntry.class.getResource("/client/presentation/modules/dashboard/MessageEntryCreationPopupFXML.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                fxmlLoader.<MessageEntryCreationPopupFXMLController>getController().setModuleController(moduleController);
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
