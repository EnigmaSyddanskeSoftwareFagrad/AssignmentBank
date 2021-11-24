/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.admin;

import client.presentation.containers.Department;
import client.presentation.containers.Patient;
import client.presentation.containers.Role;
import client.presentation.containers.User;
import client.presentation.modules.Module;
import client.presentation.utils.credentials.CredentialContainer;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class AdminFXMLController extends Module {

    @FXML
    private JFXComboBox<Department> departmentPicker;
    @FXML
    private JFXListView<Patient> assignmentView;
    @FXML
    private JFXListView<Role> roleView;
    @FXML
    private JFXTextField userId;
    @FXML
    private JFXTextField userUsername;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXComboBox<Department> userDepartment;
    @FXML
    private JFXTextField newUserUsername;
    @FXML
    private JFXTextField newUserFullName;
    @FXML
    private JFXComboBox<Department> newUserDepartment;
    @FXML
    private JFXListView<User> UserView;

    private static ChangeListener changeListener;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //Set selection modes
            UserView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            assignmentView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            roleView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            updateData();
            populateRolesList();

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        changeListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> a, Boolean b, Boolean c) {
                if (b == true && c == false) {
                    clearAll();
                } else if (b == false && c == true) {
                    updateData();
                }
            }
        };

        //Make sure that there is only one active listener from this class
        CredentialContainer.getInstance().getCredentialReadyProperty().removeListener(changeListener);
        CredentialContainer.getInstance().getCredentialReadyProperty().addListener(changeListener);
    }

    @FXML
    private void departmentPicked(ActionEvent event) {
        populateUserList();
        populatePatientList();
        userDepartment.getSelectionModel().select(departmentPicker.getSelectionModel().getSelectedIndex());
        newUserDepartment.getSelectionModel().select(departmentPicker.getSelectionModel().getSelectedIndex());
    }

    @FXML
    private void userSelected(MouseEvent event) {
        if (!UserView.getSelectionModel().isEmpty()) {
            updateUserDetailFields();
            updatePatientAssignments();
            updateRoleAssignments();
        }
    }

    @FXML
    private void saveAssignmentsClicked(MouseEvent event) {
        List<Patient> assignedPatitents = assignmentView.getSelectionModel().getSelectedItems();
        String[] query = new String[assignedPatitents.size() + 2];
        query[0] = "updatePatientAssignment";
        query[1] = UserView.getSelectionModel().getSelectedItem().getUserID();
        for (int i = 2; i < query.length; i++) {
            query[i] = assignedPatitents.get(i - 2).getPatientID();
        }
        communicationHandler.sendQuery(query);
    }

    @FXML
    private void saveRolesClicked(MouseEvent event) {
        List<Role> assignedRoles = roleView.getSelectionModel().getSelectedItems();
        String[] query = new String[assignedRoles.size() + 2];
        query[0] = "setUserRoles";
        query[1] = UserView.getSelectionModel().getSelectedItem().getUserID();
        for (int i = 2; i < query.length; i++) {
            query[i] = assignedRoles.get(i - 2).getRoleId();
        }
        communicationHandler.sendQuery(query);
    }

    @FXML
    private void saveDetailsClicked(MouseEvent event) {
        if (userDepartment.getSelectionModel().getSelectedItem() != departmentPicker.getSelectionModel().getSelectedItem()) {
            communicationHandler.sendQuery("setUserDepartment", UserView.getSelectionModel().getSelectedItem().getUserID(), userDepartment.getSelectionModel().getSelectedItem().getDepartmentId());
        }
        if (!userName.getText().equals(UserView.getSelectionModel().getSelectedItem().getUserFullName())) {
            communicationHandler.sendQuery("alterUserFullName", UserView.getSelectionModel().getSelectedItem().getUserID(), userName.getText());
        }
        populateUserList();
    }

    @FXML
    private void addNewClicked(MouseEvent event) {
        communicationHandler.sendQuery("addUser", newUserUsername.getText(), newUserFullName.getText(), newUserDepartment.getSelectionModel().getSelectedItem().getDepartmentId());
        populateUserList();
    }

    @FXML
    private void addNewPatientClicked(MouseEvent event) {
        new Thread(() -> {
            Platform.runLater(()
                    -> {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PatientCreationPopupFXML.fxml"));
                    Parent root = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initStyle(StageStyle.UNDECORATED);
                    root.getStylesheets().add(getClass().getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                    stage.setScene(new Scene(root));
                    ((PatientCreationPopupFXMLController) fxmlLoader.getController()).setModuleController(this);
                    stage.show();
                } catch (IOException e) {
                }
            });
        }, "AddPatientPopupLoader").start();
    }

    private void populateDepartmentLists() {
        new Thread(() -> {
            List<Department> departments = communicationHandler.sendQuery("getDepartments").stream().map(t -> new Department(t[0], t[1])).collect(Collectors.toList());
            Platform.runLater(() -> {
                //clear data
                clearFields();
                departmentPicker.getItems().clear();
                userDepartment.getItems().clear();
                newUserDepartment.getItems().clear();
                //populate data
                departmentPicker.getItems().addAll(departments);
                departmentPicker.getSelectionModel().selectFirst();
                userDepartment.getItems().addAll(departments);
                newUserDepartment.getItems().addAll(departments);
                userDepartment.getSelectionModel().selectFirst();
                newUserDepartment.getSelectionModel().selectFirst();
                populateUserList();
                populatePatientList();
            });
        }, "DepartmentListPopulater").start();

    }

    private void populateUserList() {
        clearFields();
        new Thread(() -> {
            List<User> users = communicationHandler.sendQuery("userListByDepartment", departmentPicker.getSelectionModel().getSelectedItem().getDepartmentId()).stream().map(t -> new User(t[0], t[1], t[2])).collect(Collectors.toList());
            Platform.runLater(() -> {
                UserView.getItems().clear();
                UserView.getItems().addAll(users);
            });
        }, "UserListPopulater").start();

    }

    /**
     *
     */
    protected void populatePatientList() {
        new Thread(() -> {
            List<Patient> patients = communicationHandler.sendQuery("getPatientsByDepartment", departmentPicker.getSelectionModel().getSelectedItem().getDepartmentId()).stream().map(t -> new Patient(t[0], t[1])).collect(Collectors.toList());
            Platform.runLater(() -> {
                assignmentView.getItems().clear();
                assignmentView.getItems().addAll(patients);
            });
        }, "PatientListPopulater(AdminPanel)").start();

    }

    private void populateRolesList() {
        new Thread(() -> {
            List<Role> roles = communicationHandler.sendQuery("getRoles").stream().map(t -> new Role(t[0], t[1])).collect(Collectors.toList());
            Collections.sort(roles);
            Platform.runLater(() -> {
                roleView.getItems().clear();
                roleView.getItems().addAll(roles);
            });
        }, "RoleListPopulator").start();

    }

    /**
     * Updates the list of patient assignemts
     */
    protected void updatePatientAssignments() {
        new Thread(() -> {
            int[] indecies = communicationHandler.sendQuery("getPatientsByUser", UserView.getSelectionModel().getSelectedItem().getUserID()).stream().map((t) -> assignmentView.getItems().indexOf(new Patient(t[1], t[0]))).collect(Collectors.toList()).stream().mapToInt(i -> i).toArray();
            Platform.runLater(() -> {
                assignmentView.getSelectionModel().clearSelection();
                if (indecies.length > 0) {
                    assignmentView.getSelectionModel().selectIndices(indecies[0], indecies);
                }
            });
        }, "PatientAssignmentSelectionUpdater").start();

    }

    private void updateRoleAssignments() {
        new Thread(() -> {
            try {
                int[] indecies = communicationHandler.sendQuery("getUserRoles", UserView.getSelectionModel().getSelectedItem().getUserID()).stream().map((t) -> roleView.getItems().indexOf(new Role(t[0], "No description"))).collect(Collectors.toList()).stream().mapToInt(i -> i).toArray();
                Platform.runLater(() -> {
                    roleView.getSelectionModel().clearSelection();
                    if (indecies.length > 0) {
                        roleView.getSelectionModel().selectIndices(indecies[0], indecies);
                    }
                });
            } catch (NullPointerException e) {
                //do nothing
            }
        }, "RoleAssignmentSelectionUpdater").start();

    }

    private void updateUserDetailFields() {
        userId.setText(UserView.getSelectionModel().getSelectedItem().getUserID());
        userUsername.setText(UserView.getSelectionModel().getSelectedItem().getUsername());
        userName.setText(UserView.getSelectionModel().getSelectedItem().getUserFullName());
        updateRoleAssignments();
        updatePatientAssignments();
    }

    private void clearFields() {
        userId.setText("");
        userName.setText("");
        userUsername.setText("");

        //clears data about a new user
        newUserFullName.setText("");
        newUserUsername.setText("");

        //clear selections
        UserView.getSelectionModel().clearSelection();
        roleView.getSelectionModel().clearSelection();
        assignmentView.getSelectionModel().clearSelection();
    }

    @Override
    protected void clearAll() {
        //clears department pickers
        departmentPicker.getItems().clear();
        userDepartment.getItems().clear();
        newUserDepartment.getItems().clear();

        //clears user details
        clearFields();
    }

    @Override
    public void updateData() {
        populateDepartmentLists();
    }

    @FXML
    private void resetPassword(MouseEvent event) {
        if (!UserView.getSelectionModel().isEmpty()) {
            User user = UserView.getSelectionModel().getSelectedItem();
            communicationHandler.sendQuery("resetUserPassword", user.getUserID(), departmentPicker.getSelectionModel().getSelectedItem().getDepartmentId(), user.getUsername());
        }
    }
}
