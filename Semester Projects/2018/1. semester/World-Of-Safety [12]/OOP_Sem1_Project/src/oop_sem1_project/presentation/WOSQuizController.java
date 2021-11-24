/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.presentation;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * The FXML Controller class for the quiz window.
 * 
 */
public class WOSQuizController implements Initializable {

    /**
     * The indexes of the correct quiz answers. (0-indexed i.e. RadioButton 1 ==
     * index 0)
     */
    private final int[] correctAnswersIndex = {1, 3, 2, 3, 0};

    /**
     * A List containing all of the ToggleGroups for easy "loop-able" answer
     * checking.
     */
    private final List<ToggleGroup> toggleGroups = new ArrayList<>();

    /**
     * Instance of the controller used to resetGame the game.
     */
    private final WOSController controller;

    /**
     * The time it took the player to complete the game in milliseconds.
     */
    private final long timePassed;

    /**
     * Quiz window
     */
    @FXML
    private ScrollPane quizNode;

    /**
     * Done window
     */
    @FXML
    private VBox finishedNode;

    /**
     * The 5 ToggleGroups.
     */
    @FXML
    private ToggleGroup question1, question2, question3, question4, question5;

    /**
     * Shows the time spent
     */
    @FXML
    private TextField timeField;

    /**
     * Shows the quiz score
     */
    @FXML
    private TextField quizScoreField;

    /**
     * Shows the total score
     */
    @FXML
    private TextField totalScore;

    /**
     * Shows if you pass
     */
    @FXML
    private TextField passedField;

    /**
     * 
     * @param controller is the controller for the base game
     * @param timePassed the time passed since the player started in milliseconds
     */
    public WOSQuizController(WOSController controller, long timePassed) {
        this.controller = controller;
        this.timePassed = timePassed;
    }

    /**
     * Initializes the controller class and adds all of the ToggleGroups to the
     * list of ToggleGroups.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null
     *                  if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.toggleGroups.addAll(Arrays.asList(this.question1, this.question2, this.question3, this.question4, this.question5));
    }

    /**
     * Fired when the submit button is clicked.
     */
    @FXML
    private void onSubmitAction() {
        int correctAnswers = 0;
        for (int i = 0; i < 5; i++) {
            ToggleGroup toggleGroup = this.toggleGroups.get(i);
            // Add 1 to the correct answers if a RadioButton is selected and it is the correct one.
            correctAnswers += toggleGroup.getSelectedToggle() != null && toggleGroup.getToggles().indexOf(toggleGroup.getSelectedToggle()) == this.correctAnswersIndex[i] ? 1 : 0;
        }

        this.quizNode.setVisible(false);
        this.finishedNode.setVisible(true);

        this.timeField.setText("It took you " + this.timePassed / 1000 + " seconds to complete the game.");
        this.quizScoreField.setText("You answered " + correctAnswers + " out of 5 questions correctly.");
        int score = this.controller.getInteractionCommunicator().storeHighscore(correctAnswers);
        this.totalScore.setText("Your total score is " + score + " out of 50 possible.");
        this.controller.getLastScoreTextField().setText(String.valueOf(score));
        this.passedField.setText("You " + (correctAnswers == 5 ? "passed! :)" : "didn't pass! :("));
    }

    /**
     * upon closing the quiz it opens the menu
     */
    @FXML
    private void playAgainAction() {
        ((Stage) this.quizNode.getScene().getWindow()).close();
        this.controller.getMenu().setVisible(true);
        this.controller.getRenderer().drawMainScreen();
        this.controller.getTextArea().setText("");
    }
}
