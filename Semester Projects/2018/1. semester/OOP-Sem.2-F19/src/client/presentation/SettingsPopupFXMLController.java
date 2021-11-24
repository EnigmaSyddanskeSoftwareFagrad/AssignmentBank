/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation;

import client.presentation.utils.credentials.CredentialContainer;
import com.google.common.hash.Hashing;
import com.jfoenix.controls.JFXPasswordField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class SettingsPopupFXMLController implements Initializable {

    @FXML
    private FontAwesomeIconView cross;
    @FXML
    private JFXPasswordField password;
    @FXML
    private JFXPasswordField repeatPassword;

    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Do nothing
    }

    @FXML
    private void close() {
        ((Stage) cross.getScene().getWindow()).close();
    }

    @FXML
    private void saveOwnPassword(ActionEvent event) {
        //Saves the password to the database if valid
        if (validate()) {
            communicationHandler.sendQuery("alterOwnPassword", Hashing.sha256().hashString(CredentialContainer.getInstance().getUsername() + password.getText(), Charset.forName("UTF-8")).toString());
            CredentialContainer.getInstance().openLoginWindow();
            close();
        }
    }

    @FXML
    private void updatePassword(KeyEvent event) {
        //updates the css of the password field based on the password
        System.out.println("PASSWPRD");
        if (validatePassword()) {
            if (password.getStyleClass().contains("wrong-credentials")) {
                password.getStyleClass().remove("wrong-credentials");
            }
        }
    }

    @FXML
    private void updateRepeat(KeyEvent event) {
        ////updates the css of the repeatPassword field based on the if the passwords are equal
        System.out.println("REAOKROPAÅKSDOKAPOÅSKD");
        if (validateEqual()) {
            if (repeatPassword.getStyleClass().contains("wrong-credentials")) {
                repeatPassword.getStyleClass().remove("wrong-credentials");
            }
        }
    }

    private boolean validate() {
        //Checks if the password is valid and equal
        return validatePassword() && validateEqual();
    }

    private boolean validatePassword() {
        if (!password.getStyleClass().contains("wrong-credentials")) {
            password.getStyleClass().add("wrong-credentials");
        }
        //Checks if the password is valid
        return password.getText().length() >= 8;
    }

    private boolean validateEqual() {
        if (!repeatPassword.getStyleClass().contains("wrong-credentials")) {
            repeatPassword.getStyleClass().add("wrong-credentials");
        }
        //Checks if the password fields are equal
        return password.getText().equals(repeatPassword.getText());
    }
}
