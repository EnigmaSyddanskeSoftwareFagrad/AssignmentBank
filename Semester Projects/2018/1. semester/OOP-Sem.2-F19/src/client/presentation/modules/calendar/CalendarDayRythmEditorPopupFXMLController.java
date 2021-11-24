/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules.calendar;

import client.presentation.modules.Popup;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.browser.MinimalGlyphsBrowser;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Sanitas Solutions
 */
public class CalendarDayRythmEditorPopupFXMLController extends Popup {

    @FXML
    private GridPane gridpane;

    private List<String[]> values;

    private String patientID;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        values = Arrays.asList(new String[]{"0", "", ""},
                new String[]{"1", "", ""},
                new String[]{"2", "", ""},
                new String[]{"3", "", ""},
                new String[]{"4", "", ""},
                new String[]{"5", "", ""},
                new String[]{"6", "", ""},
                new String[]{"7", "", ""},
                new String[]{"8", "", ""},
                new String[]{"9", "", ""},
                new String[]{"10", "", ""},
                new String[]{"11", "", ""},
                new String[]{"12", "", ""},
                new String[]{"13", "", ""},
                new String[]{"14", "", ""},
                new String[]{"15", "", ""},
                new String[]{"16", "", ""},
                new String[]{"17", "", ""},
                new String[]{"18", "", ""},
                new String[]{"19", "", ""},
                new String[]{"20", "", ""},
                new String[]{"21", "", ""},
                new String[]{"22", "", ""},
                new String[]{"23", "", ""});

        patientID = ((CalendarFXMLController) getModuleController()).getPatientID();
        communicationHandler.sendQuery("getDayRhythm", patientID).forEach(tuple -> values.set(Integer.parseInt(tuple[0]), tuple));
        addMenuItems(values);
    }

    @FXML
    private void save() {
        List<String[]> newValues = new ArrayList<>();
        IntStream.range(0, 23).forEach(i -> newValues.add(new String[]{Integer.toString(i), ((FontAwesomeIconView) getNodeByRowColumnIndex(i, 1, gridpane)).getGlyphName(), ((JFXTextField) getNodeByRowColumnIndex(i, 2, gridpane)).getText()}));
        newValues.forEach(stringArray -> {
            if (!Arrays.equals(stringArray, values.get(newValues.indexOf(stringArray)))) {
                String[] query = new String[5];
                query[0] = "setRhythmHour";
                query[1] = patientID;
                System.arraycopy(stringArray, 0, query, 2, 3);
                communicationHandler.sendQuery(query);
            }
        });
        close();
    }

    private void addMenuItems(List<String[]> input) {
        for (String[] tuple : input) {
            //Create Hour number
            Label number = new Label(String.valueOf(input.indexOf(tuple) + 1));
            number.setPrefWidth(140);
            number.setPrefHeight(60);
            number.setMaxWidth(Double.MAX_VALUE);
            number.setMaxHeight(Double.MAX_VALUE);
            number.setAlignment(Pos.CENTER_LEFT);
            number.setTextFill(Color.web("#048BA8"));
            gridpane.getChildren().add(number);
            GridPane.setColumnIndex(number, 0);
            GridPane.setRowIndex(number, input.indexOf(tuple));

            //Create icon
            CustomFontAwesomeIconView iconView = new CustomFontAwesomeIconView();

            //Disable logger wanings from FontAwesomFX because of the empty string
            Logger logger = Logger.getLogger("de.jensd.fx.glyphs.GlyphIcon");
            logger.setLevel(Level.OFF);

            iconView.setGlyphName(tuple[1]);
            iconView.glyphSizeProperty().set(30);
            if (tuple[1].length() == 0) {
                iconView.setGlyphStyle("-fx-fill : #1B2634;");
            } else {
                iconView.setGlyphStyle("-fx-fill : #048BA8;");
            }
            gridpane.getChildren().add(iconView);
            GridPane.setColumnIndex(iconView, 1);
            GridPane.setRowIndex(iconView, input.indexOf(tuple));
            iconView.setOnMouseClicked(param -> openIconChange((FontAwesomeIconView) param.getSource()));

            //Create Label
            JFXTextField label = new JFXTextField(tuple[2]);
            label.setPrefWidth(140);
            label.setPrefHeight(60);
            label.setMaxWidth(Double.MAX_VALUE);
            label.setMaxHeight(Double.MAX_VALUE);
            label.setAlignment(Pos.CENTER_LEFT);
            gridpane.getChildren().add(label);
            GridPane.setColumnIndex(label, 2);
            GridPane.setRowIndex(label, input.indexOf(tuple));
            label.setOnAction(param -> save());
        }
    }

    private void openIconChange(FontAwesomeIconView iconView) {
        new Thread(() -> {
            String glyph = MinimalGlyphsBrowser.getGlyph(iconView);
            if (glyph != null && !glyph.isEmpty() && glyph != "") {
                Platform.runLater(() -> {
                    iconView.setGlyphStyle("-fx-fill : #048BA8;");
                    iconView.setIcon(FontAwesomeIcon.valueOf(glyph));
                });
            }
        }, "GlyphUpdater").start();

    }

    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
}
