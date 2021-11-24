/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.journal;

import client.presentation.containers.Patient;
import client.presentation.containers.entries.ManualEntry;
import client.presentation.containers.entries.MedicinalEntry;
import client.presentation.modules.Module;
import com.jfoenix.controls.JFXListView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class JournalFXMLController extends Module {

    /**
     * The list view for the MedicinalEntries
     */
    @FXML
    private JFXListView<MedicinalEntry> medicinalEntriesView;

    /**
     * The list view for the ManualEntries
     */
    @FXML
    private JFXListView<ManualEntry> manualEntriesView;

    /**
     * The list view for the Patients
     */
    @FXML
    protected JFXListView<Patient> patientView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateData();
        patientView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    }

    /**
     * Adds a {@link MedicinalEntry}
     *
     * @param event
     */
    @FXML
    private void addMedicinalEntry() {
        MedicinalEntry.showCreationPopup(this);
    }

    /**
     * adds a {@link ManualEntry}
     *
     * @param event
     */
    @FXML
    private void addManualEntry() {
        ManualEntry.showCreationPopup(this);
    }

    protected Patient getPatient() {
        return patientView.getSelectionModel().getSelectedItem();
    }

    @Override
    public void updateData() {
        clearAll();
        List<Patient> patients = new ArrayList<>();
        communicationHandler.sendQuery("getPatients").forEach((tuple) -> patients.add(new Patient(tuple[1], tuple[0])));
        patientView.getItems().addAll(patients);
        patientView.getSelectionModel().selectFirst();
        updateEntryDate();
    }

    @Override
    protected void clearAll() {
        manualEntriesView.getItems().clear();
        medicinalEntriesView.getItems().clear();
        patientView.getItems().clear();
    }

    @FXML
    private void medicinalSelected(MouseEvent event) {
        try {
            medicinalEntriesView.getSelectionModel().getSelectedItem().showPopup();
        } catch (NullPointerException e) {
            //Do nothing
        }
    }

    @FXML
    private void manualSelected(MouseEvent event) {
        try {
            manualEntriesView.getSelectionModel().getSelectedItem().showPopup();
        } catch (NullPointerException e) {
            //Do nothing
        }
    }

    @FXML
    private void patientSelected(MouseEvent event) {
        updateEntryDate();
    }

    private void updateEntryDate() {
        new Thread(() -> {
            List<ManualEntry> manualEntries = new ArrayList<>();
            List<MedicinalEntry> medicalEntries = new ArrayList<>();
            List<String[]> medicinalReturn = communicationHandler.sendQuery("getMedicinalJournal", getPatient().getPatientID());
            if (!medicinalReturn.isEmpty()) {
                if (Arrays.equals(medicinalReturn.get(0), new String[]{"Error", "Missing required roles"})) {
                    Platform.runLater(() -> ((VBox) medicinalEntriesView.getParent().getParent()).getChildren().remove(medicinalEntriesView.getParent()));
                } else {
                    medicinalReturn.forEach((tuple) -> medicalEntries.add(new MedicinalEntry(tuple[0], tuple[1], tuple[2])));
                }
            }
            communicationHandler.sendQuery("getJournal", getPatient().getPatientID()).forEach((tuple) -> manualEntries.add(new ManualEntry(tuple[1], tuple[0], tuple[2])));
            Platform.runLater(() -> {
                manualEntriesView.getItems().clear();
                medicinalEntriesView.getItems().clear();
                manualEntriesView.getItems().addAll(manualEntries);
                medicinalEntriesView.getItems().addAll(medicalEntries);
            });
        }, "Journal Updater").start();
    }
}
