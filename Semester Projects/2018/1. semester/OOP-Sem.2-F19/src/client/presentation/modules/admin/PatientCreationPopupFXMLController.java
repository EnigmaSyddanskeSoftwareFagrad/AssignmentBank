/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.admin;

import client.presentation.containers.Department;
import client.presentation.modules.Module;
import client.presentation.modules.Popup;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class PatientCreationPopupFXMLController extends Popup implements Initializable {

    @FXML
    private FontAwesomeIconView cross;
    @FXML
    private JFXComboBox<Department> department;
    @FXML
    private JFXTextField name;

    private AdminFXMLController adminController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        communicationHandler.sendQuery("getDepartments").forEach(t -> department.getItems().add(new Department(t[0], t[1])));
    }

    /**
     * Closes the popup
     */
    @FXML
    @Override
    public void close() {
        ((Stage) cross.getScene().getWindow()).close();
    }

    @Override
    public void setModuleController(Module moduleController) {
        super.setModuleController(moduleController);
        adminController = (AdminFXMLController) getModuleController();
    }

    /**
     * creates the user
     *
     * @param event
     */
    @FXML
    private void create(ActionEvent event) {
        communicationHandler.sendQuery("addPatient", name.getText(), department.getSelectionModel().getSelectedItem().getDepartmentId());
        adminController.populatePatientList();
        adminController.updatePatientAssignments();
        close();
    }

}
