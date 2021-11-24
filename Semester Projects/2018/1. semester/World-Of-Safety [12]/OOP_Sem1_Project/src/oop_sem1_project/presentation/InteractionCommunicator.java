/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.presentation;

import java.util.Arrays;
import java.util.List;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import oop_sem1_project.domain.InteractionHandler;
import oop_sem1_project.domain.InteractionHandlerImpl;
import oop_sem1_project.presentation.rendering.Renderer;

/**
 * The class in charge of communicating the click- and keypress events to the
 * domain layer. This is the only class that accesses the domain layer which
 * results in a clear separation of our layers.
 *
 */
public class InteractionCommunicator {

    /**
     * An instance of the InteractionHandler; the only "connection" to the
     * domain layer of the application.
     */
    private final InteractionHandler interactionHandler = new InteractionHandlerImpl();

    /**
     * The renderer used to update the games appearance.
     */
    private final Renderer renderer;

    /**
     * Constructs a new InteractionCommunicator.
     *
     * @param renderer An instance of the renderer to be able to update the
     *                 visual appearance based on the interactions communicated.
     */
    public InteractionCommunicator(Renderer renderer) {
        this.renderer = renderer;
    }

    /**
     * Called when any key is pressed. If the key is WASD or an arrow key it is
     * communicated to the domain layer.
     *
     * @param keycode The KeyCode of the key pressed.
     */
    public void keyEvent(KeyCode keycode) {
        if (Arrays.asList(KeyCode.W, KeyCode.A, KeyCode.S, KeyCode.D, KeyCode.UP, KeyCode.DOWN, KeyCode.LEFT, KeyCode.RIGHT).contains(keycode)) {
            this.renderer.requestGraphicalUpdate(this.interactionHandler.update(keycode.getName()));
        }
    }

    /**
     * Called when the Game-, Phone- or Item Canvas is clicked. The ClickedNode
     * enum is converted to its String value when the update is called to comply
     * with our three layer architecture design.
     *
     * @param clickedNode The clicked canvas.
     * @param mouseEvent  The MouseEvent.
     */
    public void mouseClickedEvent(ClickedNode clickedNode, MouseEvent mouseEvent) {
        this.renderer.requestGraphicalUpdate(this.interactionHandler.update(clickedNode.toString(), new int[]{(int) mouseEvent.getSceneX(), (int) mouseEvent.getSceneY()}));
    }

    /**
     * Called when the start button is clicked on the main menu.
     *
     * @param name The name of the Player.
     */
    public void startClicked(String name) {
        this.renderer.requestGraphicalUpdate(this.interactionHandler.start(name));
    }

    /**
     * Called when the highscore button is clicked.
     *
     * @return The retrieved List of highscores.
     */
    public List<String> highscoreClicked() {
        return this.interactionHandler.getStoredHighscores();
    }

    /**
     * Method for storing the highscore.
     *
     * @param correctQuizAnswers The amount of correct answers.
     * @return 
     */
    public int storeHighscore(int correctQuizAnswers) {
        return this.interactionHandler.storeHighscore(correctQuizAnswers);
    }
}
