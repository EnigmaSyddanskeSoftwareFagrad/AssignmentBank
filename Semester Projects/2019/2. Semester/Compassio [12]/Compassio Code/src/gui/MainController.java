package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;

import javafx.scene.control.Button;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mads Holm Jensen
 * @author Frederik Haagensen
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane user_menu;
    @FXML
    private ImageView menu_close;
    @FXML
    private ImageView menu_click;
    @FXML
    private VBox buttons;

    @FXML
    private Label userName;
    @FXML
    private Label userRole;
    
    @FXML
    private GridPane content;
    private Parent currentContent;
    
    @FXML
    private Label lblWelcome;

    /**
     * Initializes the controller class.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        visibleMenu();
 
        loadButtons();

        //Add first and lastname, and role to hamburger menu. 
        this.userName.setText(GUIrun.getLogic().getUserName());
        String role = GUIrun.getLogic().getUserType();
        //Capitalize first letter, lowercase the rest
        role = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
        this.userRole.setText(role);
        user_menu_open(null);
    }

    private void loadButtons () {
        try {
            //Buttons
            Button btnCreateCase = createButton("Opret sag", "createCase", "O");
            Button btnSeeCases = createButton("Se sager", "seeCases", "S");
            Button btnChangePassword = createButton("Ændre password", "changePassword", "P");
            Button btnCreateUser = createButton("Opret bruger", "createUser", "B");
            Button btnEditUsers = createButton("Rediger brugere", "editUsers", "R");
            
            //Won't do buttons
            Button btnPlanning = createButton("Planlægning", null, null);
            Button btnDiary = createButton("Dagbog", null, null);
            Button btnSeeInformation = createButton("Se Oplysninger", null, null);
            Button btnRequestCase = createButton("Anmod om sag", null, null);
            
            String type = GUIrun.getLogic().getUserType();
            
            if (type.equals("ADMIN")) {
                buttons.getChildren().addAll(btnCreateUser, btnEditUsers, btnChangePassword, btnSeeInformation);
            }
            else if (type.equals("CASEWORKER")) {
                buttons.getChildren().addAll(btnCreateCase, btnSeeCases, btnChangePassword, btnSeeInformation);
            }
            else if (type.equals("SOCIALWORKER")) {
                buttons.getChildren().addAll(btnSeeCases, btnChangePassword, btnPlanning, btnDiary, btnSeeInformation);
            }
            else if (type.equals("USER")) {
                buttons.getChildren().addAll(btnSeeInformation, btnRequestCase);
            }
            
            Button btnLogout = createButton("Logout", null, "L");
            btnLogout.setOnAction(event -> {
                GUIrun.getLogic().logout();
                try {
                    GUIrun.changeFxml("/gui/login.fxml");
                } catch (IOException ex) {
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                    Platform.exit();
                }
            });
            buttons.getChildren().add(btnLogout);
            
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            Platform.exit();
        }
    }
    
    private Button createButton (String text, String fxmlName, String shortcutKey) throws NoSuchMethodException {
            Button newBtn = new Button(text);
            newBtn.getStyleClass().add("btn");
            newBtn.setPrefWidth(130);
            newBtn.setUserData(shortcutKey);
            
            if (fxmlName != null) {
                newBtn.setOnAction(event -> {
                    try {
                        loadContent(fxmlName + ".fxml");
                    } catch (IOException ex) {
                        Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                        Platform.exit();
                    }
                });
            }
            return newBtn;
    }
    
    @FXML
    private void keyPressed (KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            if (user_menu.isVisible()) {
                user_menu_close(null);
            }
            else {
                user_menu_open(null);
            }
        }
        else {
            
            if (!user_menu.isVisible()) {
                return;
            }
            
            for (Node b : buttons.getChildren()) {
                Button btn = (Button)b;
                if (btn.getUserData() != null & KeyCode.getKeyCode((String)btn.getUserData()) == event.getCode()) {
                    btn.fire();
                }
            }
        }
    }

    @FXML
    private void user_menu_close(MouseEvent event) {
        visibleMenu();
        content.requestFocus();
    }

    @FXML
    private void user_menu_open(MouseEvent event) {
        visibleMenu();
        user_menu.setVisible(true);
        menu_click.setVisible(false);
        menu_close.setVisible(true);
        content.requestFocus();
    }

    private void visibleMenu() {
        user_menu.setVisible(false);
        menu_click.setVisible(true);
        menu_close.setVisible(false);
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
     * Changes the main content scene of the stage.
     * @param fxmlName FXML file to load as inner scene.
     * @throws IOException if the specified FXML file doesn't exist.
     */
    public void loadContent (String fxmlName) throws IOException {
        lblWelcome.setVisible(false);
        
        if (currentContent != null) {
            content.getChildren().remove(currentContent);
        }
        
        Parent root = FXMLLoader.load(getClass().getResource("innercontent/" + fxmlName));        
        content.add(root, 1, 0, 3, 3);
        currentContent = root;
        user_menu_close(null);
    }
}
