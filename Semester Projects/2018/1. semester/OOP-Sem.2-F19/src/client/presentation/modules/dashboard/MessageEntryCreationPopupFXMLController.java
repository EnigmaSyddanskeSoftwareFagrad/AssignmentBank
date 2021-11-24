/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.dashboard;

import client.presentation.containers.User;
import client.presentation.modules.Popup;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class MessageEntryCreationPopupFXMLController extends Popup {

    @FXML
    private JFXComboBox<User> recipients;
    @FXML
    private JFXTextField subject;
    @FXML
    private JFXTextArea message;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        List<User> userList = new ArrayList<>();
        List<String[]> returnValue = communicationHandler.sendQuery("userList");
        for (String[] tuple : returnValue) {
            userList.add(new User(tuple[0], tuple[1], tuple[2]));
        }
        recipients.getItems().addAll(userList);
    }

    @FXML
    private void send() {
        //Check for to see if every field is selected and or filled
        if (recipients.getSelectionModel().getSelectedItem() != null && !subject.getText().isEmpty() && !message.getText().isEmpty()) {
            communicationHandler.sendQuery("sendMessage", recipients.getSelectionModel().getSelectedItem().getUserID(), subject.getText(), message.getText());
            new Thread(() -> {
                Platform.runLater(() -> ((DashboardFXMLController) getModuleController()).updateData());
            }, "MessageCreatingPopupDashboardUpdateStarter").start();
            close();
        } else {
            JFXAlert alert = new JFXAlert<>(((Stage) message.getScene().getWindow()));
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label("Please fill out all the fields"));
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.showAndWait();
        }
    }

}
