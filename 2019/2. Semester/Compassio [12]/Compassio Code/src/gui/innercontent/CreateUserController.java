package gui.innercontent;

import gui.GUIrun;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author madsh
 */
public class CreateUserController implements Initializable {

    @FXML
    private TextField txtFirstName;
    @FXML
    private TextField txtLastName;
    @FXML
    private TextField txtUsername;
    @FXML
    private TextField txtPassword;
    @FXML
    private ChoiceBox<String> chbType;
    private ObservableList<String> userTypes;
    @FXML
    private ChoiceBox<String> chbDepartment;
    private ObservableList<String> departments;
    private HashMap<String, Integer> departmentMap;
    @FXML
    private Label errorLabel;

    private Dialog<String> confirmDialog;
    private PasswordField password;
    
    /**
     * Initializes the controller class.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userTypes = FXCollections.observableArrayList();
        departments = FXCollections.observableArrayList();
        chbType.setItems(userTypes);
        chbDepartment.setItems(departments);
        departmentMap = new HashMap<>();
        
        confirmDialog = createConfirmDialog();
        
        String[] types = GUIrun.getLogic().getUserTypes();
        for (String t : types) {
            userTypes.add(t);
        }
        
        ArrayList<String> deps = GUIrun.getLogic().getDepartmentInfo();
        
        for (String d : deps) {
            String[] tokens = d.split(" ");
            departments.add(tokens[1]);
            departmentMap.put(tokens[1], Integer.parseInt(tokens[0]));
        }
        
        chbType.getSelectionModel().selectFirst();
        chbDepartment.getSelectionModel().selectFirst();
    }    

    @FXML
    private void createUser(ActionEvent event) {
    
        if (validateInput()) {
            //Create user - everything is good
            Optional<String> result = confirmDialog.showAndWait();
            String pass = (result.isPresent() ? result.get() : null);

            while (result.isPresent() && (pass == null || !GUIrun.getLogic().checkUserPassword(pass))) {
                confirmDialog.setHeaderText("FORKERT ADGANGSKODE! Indtast administrator adgangskode, for at oprette bruger!");
                result = confirmDialog.showAndWait();
                pass = (result.isPresent() ? result.get() : null);
            }
            
            if (result.isPresent()) {
                GUIrun.getLogic().createUser(txtFirstName.getText(), txtLastName.getText(), txtUsername.getText(), txtPassword.getText(), chbType.getSelectionModel().getSelectedItem(), departmentMap.get(chbDepartment.getSelectionModel().getSelectedItem()));
                clear();
            }
        }
    }
    
    private boolean validateInput () {
        if (txtFirstName.getText().length() == 0 || txtLastName.getText().length() == 0 || txtUsername.getText().length() == 0 || txtPassword.getText().length() == 0) {
            errorLabel.setText("Alle felter skal være udfyldt!");
            errorLabel.setTextFill(Color.RED);
            return false;
        }
        
        if (txtUsername.getText().length() < 4) {
            errorLabel.setText("Brugernavnet skal minimum være 4 tegn!");
            errorLabel.setTextFill(Color.RED);
            return false;
        }
        
        if (txtPassword.getText().length() < 8) {
            errorLabel.setText("Koden skal minimum være 8 tegn!");
            errorLabel.setTextFill(Color.RED);
            return false;
        }
        
        return true;
    }
    
    private void clear () {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        chbDepartment.getSelectionModel().selectFirst();
        chbType.getSelectionModel().selectFirst();
        
        errorLabel.setTextFill(Color.GREEN);
        errorLabel.setText("Bruger oprettet!");
    }
    
    private Dialog<String> createConfirmDialog () {
        Dialog<String> dia = new Dialog<>();
        dia.setTitle("Bekræft adgangskode");
        dia.setHeaderText("Indtast administrator adgangskode, for at oprette bruger!");
        
        ButtonType confirmBtn = new ButtonType("Bekræft", ButtonBar.ButtonData.OK_DONE);
        dia.getDialogPane().getButtonTypes().addAll(confirmBtn, ButtonType.CANCEL);
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        password = new PasswordField();
        password.setPromptText("Adgangskode");
        grid.add(new Label("Adgangskode:"), 0, 1);
        grid.add(password, 1, 1);
        
        dia.getDialogPane().setContent(grid);
        
        dia.setResultConverter(button -> {
            if (button == confirmBtn) {
                return password.getText();
            }
            return null;
        });
        
        return dia;
    }
}
