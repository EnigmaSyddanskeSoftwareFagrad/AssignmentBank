/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.calendar;

import client.presentation.containers.Patient;
import client.presentation.containers.entries.EventDataEntry;
import client.presentation.modules.Popup;
import com.calendarfx.model.Entry;
import com.calendarfx.model.Interval;
import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import java.lang.reflect.Field;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class CalendarEventEditPopupFXMLController extends Popup {

    @FXML
    private CheckComboBox<Patient> participents;

    private Entry<EventDataEntry> entry = null;

    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    @FXML
    private JFXTextField title;
    @FXML
    private JFXTextArea details;
    @FXML
    private JFXTimePicker fromTime;
    @FXML
    private JFXTimePicker toTime;
    @FXML
    private JFXDatePicker fromDate;
    @FXML
    private JFXDatePicker toDate;
    @FXML
    private JFXButton deleteButton;

    private Entry<EventDataEntry> originalEntry = null;

    public Entry<EventDataEntry> editEvent(Entry<EventDataEntry> entry) {
        lock.lock();
        setEntry(entry);
        try {
            condition.await();
            return this.entry;
        } catch (InterruptedException ex) {
            ex.printStackTrace(System.err);
        } finally {
            lock.unlock();
        }
        return null;
    }

    public void setEntry(Entry<EventDataEntry> entry) {
        Platform.runLater(() -> {
            originalEntry = entry;
            participents.getCheckModel().clearChecks();
            Arrays.asList(entry.getUserObject().getEventParticipants()).forEach(p -> {
                if (participents.getItems().contains(p)) {
                    participents.getCheckModel().check(p);
                }
            });
            title.setText(entry.getTitle());
            details.setText(entry.getLocation());
            fromDate.setValue(entry.getStartDate());
            fromTime.setValue(entry.getStartTime());
            toDate.setValue(entry.getEndDate());
            toTime.setValue(entry.getEndTime());
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Patient> userList = new ArrayList<>();
        List<String[]> returnValue = communicationHandler.sendQuery("getPatients");
        for (String[] tuple : returnValue) {
            userList.add(new Patient(tuple[1], tuple[0]));
        }
        participents.getItems().addAll(userList);
        Platform.runLater(() -> {
            try {
                Skin<?> skin = participents.getSkin();
                Class<? extends Skin> skinCls = skin.getClass();
                Field field = skinCls.getDeclaredField("comboBox");
                field.setAccessible(true);
                ComboBox<?> internal = (ComboBox<?>) field.get(skin);
                internal.getStyleClass().add("jfx-combo-box");
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        });
        fromTime.set24HourView(true);
        fromTime.setDefaultColor(Color.web("#048BA8"));
        fromDate.setDefaultColor(Color.web("#048BA8"));
        toTime.set24HourView(true);
        toTime.setDefaultColor(Color.web("#048BA8"));
        toDate.setDefaultColor(Color.web("#048BA8"));
        deleteButton.setFocusTraversable(false);
    }

    @FXML
    private void save() {
        if (title.getText().isEmpty() || fromDate.getValue() == null || fromTime.getValue() == null || toDate.getValue() == null || toTime.getValue() == null) {
            JFXAlert alert = new JFXAlert<>(((Stage) cross.getScene().getWindow()));
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label("Please fill out all the fields"));
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.showAndWait();
            return;
        }
        if (fromDate.getValue().isAfter(toDate.getValue()) || fromTime.getValue().isAfter(toTime.getValue())) {
            JFXAlert alert = new JFXAlert<>(((Stage) cross.getScene().getWindow()));
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label("The entered time inteval is not valid"));
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.showAndWait();
            return;
        }
        if (fromDate.getValue().equals(toDate.getValue()) && fromTime.getValue().equals(toTime.getValue())) {
            JFXAlert alert = new JFXAlert<>(((Stage) cross.getScene().getWindow()));
            JFXDialogLayout layout = new JFXDialogLayout();
            layout.setBody(new Label("The entered time inteval can't be nothing"));
            alert.setOverlayClose(true);
            alert.setAnimation(JFXAlertAnimation.CENTER_ANIMATION);
            alert.setContent(layout);
            alert.initModality(Modality.NONE);
            alert.showAndWait();
            return;
        }
        lock.lock();
        try {
            entry = new Entry<>(title.getText(), new Interval(LocalDateTime.of(fromDate.getValue(), fromTime.getValue()), LocalDateTime.of(toDate.getValue(), toTime.getValue())));
            entry.setLocation(details.getText());
            List<Patient> participentsIncludingRedacted = new ArrayList(Arrays.asList(originalEntry.getUserObject().getEventParticipants()));
            participentsIncludingRedacted.removeAll(participents.getItems());
            participentsIncludingRedacted.addAll(participents.getCheckModel().getCheckedItems());
            entry.setUserObject(new EventDataEntry(originalEntry.getUserObject().getEventID(), participentsIncludingRedacted.toArray(new Patient[participentsIncludingRedacted.size()])));
            condition.signal();
            close();
        } finally {
            lock.unlock();
        }
    }

    @FXML
    private void cancel() {
        lock.lock();
        try {
            entry = null;
            condition.signal();
            close();
        } finally {
            lock.unlock();
        }
    }

    @FXML
    private void delete() {
        JFXAlert<Boolean> alert = new JFXAlert<>((Stage) cross.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        // Create the content of the JFXAlert with JFXDialogLayout
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setStyle("-fx-background-color: #2E4057;");
        Label heading = new Label("Deleting event");
        heading.setStyle("-fx-text-fill: #048BA8;");
        layout.setHeading(heading);
        Label body = new Label("This will delete the event for all participants and can't be reversed.\nAre you sure want to delete this event?");
        body.setStyle("-fx-text-fill: #048BA8;");
        layout.setBody(new VBox(body));

        // Buttons get added into the actions section of the layout.
        JFXButton yesButton = new JFXButton("Yes");
        yesButton.setFocusTraversable(false);
        yesButton.getStylesheets().add(getClass().getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
        yesButton.getStyleClass().add("delete");
        yesButton.setOnAction(addEvent -> {
            // When the button is clicked, we set the result accordingly
            alert.setResult(true);
            alert.hideWithAnimation();
        });

        JFXButton cancelButton = new JFXButton("No");
        cancelButton.getStylesheets().add(getClass().getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
        cancelButton.setDefaultButton(true);
        cancelButton.setOnAction(closeEvent -> {
            alert.setResult(false);
            alert.hideWithAnimation();
        });

        layout.setActions(yesButton, cancelButton);
        alert.setContent(layout);
        alert.setHideOnEscape(true);
        Optional<Boolean> answer = alert.showAndWait();
        if (answer.isPresent() && answer.get()) {
            lock.lock();
            try {
                entry = null;
                communicationHandler.sendQuery("removeCalendarEvent", originalEntry.getUserObject().getEventID());
                condition.signal();
                close();
            } finally {
                lock.unlock();
            }
        }
    }
}
