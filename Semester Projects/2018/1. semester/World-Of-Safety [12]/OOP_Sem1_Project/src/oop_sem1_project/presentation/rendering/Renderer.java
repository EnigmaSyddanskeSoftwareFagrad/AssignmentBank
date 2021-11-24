/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.presentation.rendering;

import java.util.List;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import oop_sem1_project.presentation.WOSController;

/**
 * The class in charge of rendering/representing the data returned from a click-
 * or keypress event visually. In other words, this class translates the packet
 * interpreted by the PacketInterpreter to the corresponding currently desired
 * visual appearance of the game.
 *
 */
public class Renderer {

    /**
     * The ImageView of the player. ImageView is used instead of Image to be
     * able to rotate.
     */
    private final ImageView player = new ImageView(new Image(WOSController.class.getResourceAsStream("images/player.png")));

    /**
     * An instance of the WOSController used to access the various Nodes on the
     * Scene.
     */
    private final WOSController controller;

    /**
     * Constructs a new Renderer.
     *
     * @param controller The instance of the WOSController.
     */
    public Renderer(WOSController controller) {
        this.controller = controller;
    }

    /**
     * Updates the Scene in correspondence to the received dataList.
     *
     * @param dataList The list containing the desired visual appearance of the
     *                 Scene.
     */
    public void requestGraphicalUpdate(List<String[]> dataList) {
        // Main purpose of the PackeInterpreter is to convert the dataList to something more specific
        // than arbritray abbrevations and random array positions. Aids readability.
        PackeInterpreter packet = new PackeInterpreter(dataList);
        GraphicsContext gameCanvas = this.controller.getGameCanvas().getGraphicsContext2D();

        gameCanvas.drawImage(new Image(WOSController.class.getResourceAsStream("images/" + packet.getBackground() + ".png")), 0, 0);
        this.player.setRotate(packet.getPlayerRotation());
        SnapshotParameters snapshotParameters = new SnapshotParameters();
        snapshotParameters.setFill(Color.TRANSPARENT);
        gameCanvas.drawImage(this.player.snapshot(snapshotParameters, null), packet.getPlayerX(), packet.getPlayerY());

        if (!packet.getCurrentItem().isEmpty()) {
            this.controller.getItemImageView().setImage(new Image(WOSController.class.getResourceAsStream("images/" + packet.getCurrentItem() + ".png")));
        }

        this.controller.getTextArea().appendText(packet.getMessage().isEmpty() ? "" : "──────────────────────────────────────────────────────────────────────────────\n" + packet.getMessage() + "\n");

        if (!packet.getPopupImage().isEmpty()) {
            gameCanvas.drawImage(new Image(WOSController.class.getResourceAsStream("images/" + packet.getPopupImage() + ".png")), 0, 0);
        }

        this.controller.getPhoneNumberArea().setText(packet.getPhoneNumber());
        this.controller.getPhoneNumberArea().setVisible(!packet.getPhoneNumber().isEmpty());

        if (!packet.getSound().isEmpty()) {
            this.controller.getWOSMediaPlayer().playSound(packet.getSound());
        }

        if (packet.isOpenQuiz()) {
            this.controller.openQuiz();
        }

        this.controller.setScore(packet.getScore());
    }
    
    /**
     * Draws the main screen
     */
    public void drawMainScreen() {
        this.controller.getItemImageView().setImage(null);
        this.controller.getGameCanvas().getGraphicsContext2D().drawImage(new Image(WOSController.class.getResourceAsStream("images/MainScreen.png")), 0, 0);
    }
}
