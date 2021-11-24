/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.presentation;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 * The FXML Controller class for the high score list window.
 *
 */
public class WOSHighscoreController implements Initializable {

    /**
     * The ListView in which all of the stored scores are to be added.
     */
    @FXML
    private ListView<String> scoreListView;

    /**
     * Initializes the controller class.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null
     *                  if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Fired when the close button is clicked.
     *
     * @param event The ActionEvent used to get the Stage on which the button is
     *              located.
     */
    @FXML
    private void closeButtonClicked(ActionEvent event) {
        /* Casting explained.
         * 1. The events source is an instance of Button as the node clicked is a button.
         * 2. The button is located on the Scene in the Window. Stage extends Window therefore Window can be casted to Stage as Stage is a subtype.
         * 3. The close method is located on stage which we now have an instance of.
         */
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    /**
     * Used to set the high scores retrieved from the data access layer.
     *
     * @param highscoreList The retrieved high scores.
     */
    public void setScoreList(List<String> highscoreList) {
        this.scoreListView.getItems().addAll(highscoreList);
        this.scoreListView.refresh(); // <- Refresh the ListView instead of using an ObservableList.
    }
}
