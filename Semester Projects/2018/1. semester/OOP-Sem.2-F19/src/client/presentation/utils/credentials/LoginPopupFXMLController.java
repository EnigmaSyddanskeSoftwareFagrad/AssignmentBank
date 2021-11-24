/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.utils.credentials;

import client.presentation.CommunicationHandler;
import client.presentation.utils.CustomDecorator;
import client.presentation.utils.StringUtils;
import com.google.common.hash.Hashing;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.svg.SVGGlyph;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class LoginPopupFXMLController implements Initializable {

    /**
     * The CommunicationHandler
     */
    private final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

    /**
     * The text field for the username
     */
    @FXML
    private JFXTextField username;

    /**
     * The text field for the password
     */
    @FXML
    private JFXPasswordField password;

    /**
     * The label for the message if the password/username is wrong
     */
    @FXML
    private Label message;

    /**
     * The pane for the loading animation
     */
    @FXML
    private Pane loadpane;

    /**
     * The image view for the blurred login screen
     */
    @FXML
    private ImageView loadblur;

    /**
     * The Credential instance
     */
    private CredentialContainer containerInstance;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadpane.setVisible(false);
        loadblur.setEffect(new GaussianBlur(5));
        username.textProperty().addListener((observable, oldValue, newValue)
                -> {
            if (!newValue.isEmpty() && username.getStyleClass().contains("wrong-credentials")) {
                username.getStyleClass().remove("wrong-credentials");
                password.getStyleClass().remove("wrong-credentials");
                message.setText("");
            }
        });

        password.textProperty().addListener((observable, oldValue, newValue)
                -> {
            if (!newValue.isEmpty() && password.getStyleClass().contains("wrong-credentials")) {
                username.getStyleClass().remove("wrong-credentials");
                password.getStyleClass().remove("wrong-credentials");
                message.setText("");
            }
        });
        containerInstance = CredentialContainer.getInstance();
    }

    /**
     * Handles the login
     * Opens the main program if your login was successful
     * Tells you if your login failed
     */
    @FXML
    private void handleLoginButtonAction() {
        loadpane.setVisible(true);
        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            message.setText("Please enter a username and a password!");
            username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
            loadpane.setVisible(false);
            return;
        }
        new Thread(() -> {
            List<String[]> sqlReturn = communicationHandler.sendQuery(new String[]{"login", username.getText().toLowerCase(), StringUtils.hash(username.getText().toLowerCase() + password.getText())});
            if (sqlReturn != null && !sqlReturn.isEmpty() && !sqlReturn.get(0)[0].equalsIgnoreCase("error")) {
                CredentialContainer.getInstance().setUsername(username.getText().toLowerCase());
                CredentialContainer.getInstance().setPassword(StringUtils.hash(username.getText().toLowerCase() + password.getText()));
                Platform.runLater(() -> {
                    if (containerInstance.isFirst()) {
                        loadMain();
                    }
                    closeStage();
                });

            } else {
                Platform.runLater(()
                        -> {
                    message.setText("Wrong username or password!");
                    password.setText("");
                    username.getStyleClass().add("wrong-credentials");
                    password.getStyleClass().add("wrong-credentials");
                });

            }
            Platform.runLater(()
                    -> {
                loadpane.setVisible(false);
            });
        }, "LoginHandler").start();
    }

    /**
     * Closes the application when cancelling the login
     */
    @FXML
    private void handleCancelButtonAction() {
        System.exit(0);
    }

    /**
     * Hashes the input
     *
     * @param input The string that gets hashed
     * @return the hashed input
     */
    private String hash(String input) {
        return Hashing.sha256().hashString(input, Charset.forName("UTF8")).toString();
    }

    /**
     * Closes the stage
     */
    private void closeStage() {
        ((Stage) username.getScene().getWindow()).close();
    }

    /**
     * Initializes the main application screen
     */
    private void loadMain() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/client/presentation/MainFXML.fxml"));
            CustomDecorator decorator = new CustomDecorator(stage, root, false, true, true);
            decorator.setCustomMaximize(true);
            decorator.setGraphic(new SVGGlyph());
            Scene scene = new Scene(decorator);
            scene.getStylesheets().add(getClass().getResource("/client/presentation/css/menuStyleSheet.css").toExternalForm());
            stage.setScene(scene);
            stage.setMinHeight(845);
            stage.setMinWidth(1290);
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/client/presentation/resources/sanitaslogo.png")));
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    Platform.exit();
                    System.exit(0);
                }
            });
            stage.show();
            stage.setTitle("Sanitas Overview");

        } catch (IOException ex) {
            ex.printStackTrace(System.err);
        }

    }

}
