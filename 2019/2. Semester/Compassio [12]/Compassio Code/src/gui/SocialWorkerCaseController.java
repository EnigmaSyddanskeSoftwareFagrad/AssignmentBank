package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.Case;

/**
 * FXML Controller class
 *
 * @author Peter Andreas Brændgaard
 */
public class SocialWorkerCaseController implements Initializable {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField caseIDField;
    @FXML
    private TextField CPRField;
    @FXML
    private TextArea inquiryArea;
    @FXML
    private Button closeButton;

    Case currentCase;
    @FXML
    private TextField typeField;
    @FXML
    private TextField departmentField;

    /**
     * Initializes the controller class.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    @FXML
    private void closeButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    /**
     * Injects the current case into the controller.
     * @param currentCase case to inject.
     */
    public void injectCase(Case currentCase) {
        this.currentCase = currentCase;
        setupCase();
    }

    private void setupCase() {
        firstNameField.setText(currentCase.getFirstName());
        lastNameField.setText(currentCase.getLastName());
        caseIDField.setText(currentCase.getCaseID().toString());
        CPRField.setText("" + currentCase.getCprNumber());
        typeField.setText(currentCase.getType());
        departmentField.setText(GUIrun.getLogic().getDepartmentNameById(currentCase.getDepartmentID()));
        inquiryArea.setText(currentCase.getInquiry());
    }

}
