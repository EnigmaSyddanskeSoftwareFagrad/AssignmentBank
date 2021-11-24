package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Peter Andreas Brændgaard
 * @author Julie Markersen
 */
public class loginController implements Initializable {

    @FXML
    private GridPane login_grid;
    @FXML
    private GridPane start_grid;

    @FXML
    private TextField user_textfield;
    @FXML
    private PasswordField password_passwordsFiled;
    @FXML
    private Label label_check;
    @FXML
    private VBox loadingOverlay;
    @FXML
    private Button nextButton;

    /**
     * Initializes the controller class.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        visible();
        password_passwordsFiled.setPromptText("Din adgangskode");
        user_textfield.setPromptText("Dit brugernavn");
        nextButton.requestFocus();
    }

    @FXML
    private void minimise(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Sets the initial visibility of the login menu.
     */
    public void visible() {
        start_grid.setVisible(true);
        login_grid.setVisible(false);
        loadingOverlay.setVisible(false);
    }
    
    @FXML
    private void checkKey (KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            start_button(null);
        }
    }

    @FXML
    private void buttonBack(MouseEvent event) throws IOException {
        GUIrun.changeFxml("/gui/login.fxml");
    }

    @FXML
    private void start_button(ActionEvent event) {
        visible();
        start_grid.setVisible(false);
        login_grid.setVisible(true);
    }

    private void setLoadingOverlay(boolean show) {
        if (show) {
            loadingOverlay.setVisible(true);
            login_grid.setEffect(new GaussianBlur());
            user_textfield.setDisable(true);
            password_passwordsFiled.setDisable(true);
        } else {
            loadingOverlay.setVisible(false);
            login_grid.setEffect(null);
            user_textfield.setDisable(false);
            password_passwordsFiled.setDisable(false);
        }
    }

    @FXML
    private void login_button(ActionEvent event) throws IOException, InterruptedException {
        if (user_textfield.getText().length() == 0 && password_passwordsFiled.getText().length() == 0) {
            label_check.setText("Prøvede du virkelig på det! Seriøst!");
            label_check.setTextFill(Color.rgb(210, 39, 30));
            return;
        }
        
        setLoadingOverlay(true);

        new Thread(() -> {
            if (!GUIrun.getLogic().login(user_textfield.getText(), password_passwordsFiled.getText())) {
                Platform.runLater(() -> {
                    label_check.setText("Ugyldigt brugernavn/password!");
                    label_check.setTextFill(Color.rgb(210, 39, 30));
                });
            } else {
                Platform.runLater(() -> {
                    try {
                        GUIrun.changeFxml("/gui/main.fxml");
                    } catch (IOException ex) {
                        Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                });
            
            }
            
            Platform.runLater(() -> {
                setLoadingOverlay(false);
            });
            
            
        }).start();

    }
}
