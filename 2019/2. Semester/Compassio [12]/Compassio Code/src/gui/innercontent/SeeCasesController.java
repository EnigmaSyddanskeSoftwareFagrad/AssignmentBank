package gui.innercontent;

import gui.GUICaseCell;
import gui.GUIrun;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import logic.Case;

/**
 * FXML Controller class
 *
 * @author madsh
 */
public class SeeCasesController implements Initializable {

    @FXML
    private AnchorPane see_cases_ancher;
    @FXML
    private TextField searchField;
    @FXML
    private ChoiceBox<String> caseType;
    private ObservableList<String> caseTypes;
    private ArrayList<Case> cases;
    @FXML
    private ListView<Case> listview_cases;
    private ObservableList<Case> viewableCases;
    private FilteredList<Case> filteredCases;

    /**
     * Initializes the controller class.
     * @param rb The resources used to localize the root object, or null if the root object was not localized.
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateCases();

        listview_cases.setCellFactory(view -> new GUICaseCell());

        searchField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateCaseFilter();
            }
        });

        caseTypes = FXCollections.observableArrayList();
        caseTypes.add("All");
        caseTypes.addAll(GUIrun.getLogic().retrieveCaseTypes());
        caseType.setItems(caseTypes);
        caseType.getSelectionModel().select(0);

        caseType.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                updateCaseFilter();
            }
        });
    }

    private void updateCaseFilter() {
        Predicate<Case> text = c -> {
            String input = searchField.getText().trim().toLowerCase();
            String fullName = c.getFirstName().toLowerCase() + " " + c.getLastName().toLowerCase();
            String cpr = "" + c.getCprNumber();
            String caseNumber = "" + c.getCaseID();

            if (searchField.getText().trim().length() == 0) {
                return true;
            }

            return fullName.startsWith(input) || cpr.startsWith(input) || caseNumber.startsWith(input);
        };
        Predicate<Case> type = c -> {
            if (caseType.getSelectionModel().getSelectedIndex() == 0) {
                return true;
            }

            return c.getType().equals(caseType.getValue());
        };

        filteredCases.setPredicate(text.and(type));

    }

    private void updateCases() {
        new Thread(() -> {
            cases = GUIrun.getLogic().getCases();

            viewableCases = FXCollections.observableArrayList(cases);
            filteredCases = new FilteredList<>(viewableCases, p -> true);
            Platform.runLater(() -> {
                listview_cases.setItems(filteredCases);
            });
        }).start();
    }
}
