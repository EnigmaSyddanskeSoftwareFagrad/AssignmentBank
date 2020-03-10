package gui.innercontent;

import gui.GUIrun;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import logic.UserInfo;

/**
 * FXML Controller class
 *
 * @author madsh
 */
public class EditUsersController implements Initializable {

    @FXML
    private AnchorPane editUsers;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<UserInfo> usersListview;
    private ObservableList<UserInfo> users;
    private FilteredList<UserInfo> usersFiltered;
    @FXML
    private Label userInformation;
    @FXML
    private ChoiceBox<String> chbUserRole;
    private ObservableList<String> roles;
    @FXML
    private Button btnUserState;
    private boolean state;
    @FXML
    private Button btnSave;
    
    private Dialog<String> confirmDialog;
    private PasswordField password;

    /**
     * Initializes the controller class.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        users = FXCollections.observableArrayList();
        usersFiltered = new FilteredList<>(users, p -> true);
        usersListview.setItems(usersFiltered);
        roles = FXCollections.observableArrayList();
        chbUserRole.setItems(roles);
        setEditDisable(true);
        
        confirmDialog = createConfirmDialog();

        String[] r = GUIrun.getLogic().getUserTypes();

        for (String role : r) {
            if (role.equals("UNKNOWN")) {
                continue;
            }
            
            roles.add(role);
        }
        chbUserRole.getSelectionModel().select(0);

        updateList();

        usersListview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<UserInfo>() {
            @Override
            public void changed(ObservableValue<? extends UserInfo> observable, UserInfo oldValue, UserInfo newValue) {
                if (newValue != null) {
                    setEditDisable(false);
                    userInformation.setText(newValue.toString());
                    chbUserRole.getSelectionModel().select(newValue.getType());

                    state = newValue.isInactive();

                    if (state) {
                        btnUserState.setText("Genaktiver bruger");
                        btnUserState.setStyle("-fx-background-color: #00cc00");
                    } else {
                        btnUserState.setText("Deaktiver bruger");
                        
                    }
                }
            }
        });
        
        searchField.textProperty().addListener((o, oldVal, newVal) -> {
            if (newVal.length() > 0) {
                usersFiltered.setPredicate(new Predicate<UserInfo>() {
                    @Override
                    public boolean test(UserInfo t) {
                        return (t.getUsername().toLowerCase().startsWith(newVal.toLowerCase()) || t.getName().toLowerCase().startsWith(newVal.toLowerCase()));
                    }
                });
            }
            else {
                usersFiltered.setPredicate(u -> true);
            }
        });
    }

    private void updateList() {
        new Thread(() -> {
            ArrayList<UserInfo> u = GUIrun.getLogic().getAllUsers();
            
            Platform.runLater(() -> {
                users.clear();
                users.addAll(u);
            });
        }).start();
    }

    private void setEditDisable(boolean state) {
        chbUserRole.setDisable(state);
        btnUserState.setDisable(state);
        btnSave.setDisable(state);
    }
    
    private Dialog<String> createConfirmDialog () {
        Dialog<String> dia = new Dialog<>();
        dia.setTitle("Bekræft adgangskode");
        dia.setHeaderText("Indtast administrator adgangskode, for at gemme ændringer");
        
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

    @FXML
    private void changeUserState(ActionEvent event) {
        state = !state;
        
        if (state) {
            btnUserState.setText("Genaktiver bruger");
            btnUserState.setStyle("-fx-background-color: #00cc00");
        } else {
            btnUserState.setText("Deaktiver bruger");
            btnUserState.setStyle("-fx-background-color: #cc0000");
        }
    }

    @FXML
    private void save(ActionEvent event) {
        Optional<String> result = confirmDialog.showAndWait();
        String pass = (result.isPresent() ? result.get() : null);
        
        while (result.isPresent() && (pass == null || !GUIrun.getLogic().checkUserPassword(pass))) {
            confirmDialog.setHeaderText("FORKERT ADGANGSKODE! Indtast administrator adgangskode, for at oprette bruger!");
            result = confirmDialog.showAndWait();
            pass = (result.isPresent() ? result.get() : null);
        }
        
        if (result.isPresent()) {
            
            GUIrun.getLogic().updateUserState(usersListview.getSelectionModel().getSelectedItem().getId(), chbUserRole.getSelectionModel().getSelectedItem(), state);
            usersListview.getSelectionModel().clearSelection();
            updateList();
            password.setText("");
            
            setEditDisable(true);
            userInformation.setText("");
            chbUserRole.getSelectionModel().select(0);
        }
    }

}
