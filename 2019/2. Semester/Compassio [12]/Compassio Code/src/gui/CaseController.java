package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
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
public class CaseController implements Initializable {

    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField caseIDField;
    @FXML
    private TextField CPRField;
    @FXML
    private ChoiceBox<String> caseTypeChoiceBox;
    @FXML
    private TextArea mainBodyArea;
    @FXML
    private DatePicker dateCreatedField;
    @FXML
    private DatePicker closedDateField;
    @FXML
    private ChoiceBox<String> departmentBox;
    private ObservableList<String> departmentTypes;

    @FXML
    private TextArea inquiryArea;
    @FXML
    private Button editButton;
    @FXML
    private Button cancelButton;
    @FXML
    private Button closeButton;

    ArrayList<Node> editableFields;
    Case currentCase;

    /**
     * Initializes the controller class.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editableFields = new ArrayList<>();
        editableFields.add(caseTypeChoiceBox);
        editableFields.add(mainBodyArea);
        editableFields.add(closedDateField);
        editableFields.add(departmentBox);
        editableFields.add(inquiryArea);
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
    private void editButton(ActionEvent event) {
        if (editButton.getText().equals("Rediger")) {
            editButton.setText("Gem");
            editableFields.forEach(nodes -> {
                nodes.setDisable(false);
            });

            cancelButton.setVisible(true);
        } else if (editButton.getText().equals("Gem")) {
            saveCase();
            if (currentCase.saveCase()) {
                editButton.setText("Rediger");
                editableFields.forEach(nodes -> {
                    nodes.setDisable(true);
                });
                cancelButton.setVisible(false);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Sagen blev gemt");
                alert.showAndWait();
            }
        }
    }

    private void saveCase() {
        currentCase.setMainBody(this.mainBodyArea.getText());
        currentCase.setInquiry(this.inquiryArea.getText());
        currentCase.setType(this.caseTypeChoiceBox.getSelectionModel().getSelectedItem());
        currentCase.setDateClosed(closedDateField.getValue());
        currentCase.setDepartmentID(Integer.parseInt(departmentBox.getValue().split(" ")[0]));
    }

    @FXML
    private void cancelButton(ActionEvent event) {
        editButton.setText("Rediger");
        this.setupCase();
        editableFields.forEach(nodes -> {
            nodes.setDisable(true);
        });
        cancelButton.setVisible(true);
    }

    @FXML
    private void closeButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
    
    /**
     * Injects the case being worked on into a different stage.
     * @param currentCase case to inject
     */
    public void injectCase(Case currentCase) {
        this.currentCase = currentCase;
        setupCase();
    }

    private void setupCase() {
        firstNameField.setText(currentCase.getFirstName());
        lastNameField.setText(currentCase.getLastName());
        caseIDField.setText(currentCase.getCaseID().toString());
        CPRField.setText(Long.toString(currentCase.getCprNumber()));
        caseTypeChoiceBox.getItems().addAll(GUIrun.getLogic().retrieveCaseTypes());
        caseTypeChoiceBox.getSelectionModel().select(currentCase.getType());
        mainBodyArea.setText(currentCase.getMainBody());
        if (currentCase.getDateCreated() != null) {
            LocalDate date = LocalDate.parse(currentCase.getDateCreated().toString());
            dateCreatedField.setValue(date);
        }
        if (currentCase.getDateClosed() != null) {     
            LocalDate date = LocalDate.parse(currentCase.getDateClosed().toString());
            closedDateField.setValue(date);
        }
        departmentTypes = FXCollections.observableArrayList(GUIrun.getLogic().getDepartmentInfo());
        departmentBox.setItems(departmentTypes);
        departmentBox.getSelectionModel().select(currentCase.getDepartmentID() - 1);
        inquiryArea.setText(currentCase.getInquiry());
    }

}
