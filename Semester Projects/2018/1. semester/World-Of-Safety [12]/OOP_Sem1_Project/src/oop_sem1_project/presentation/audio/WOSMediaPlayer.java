/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.presentation.audio;

import java.net.URISyntaxException;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class is in charge of playing game music and individual sounds.
 *
 */
public class WOSMediaPlayer {

    /**
     * If the MediaPlayer instance is declared locally it is garbage collected
     * and the music is stopped. Declaring it as an instance variable prevents
     * it from being garbage collected. The variable is not used again.
     */
    private MediaPlayer mainTrackMediaPlayer;

    /**
     * Constructs a new WOSMediaPlayer and starts the game music in an infinite
     * loop.
     */
    public WOSMediaPlayer() {
        try {
            this.mainTrackMediaPlayer = new MediaPlayer(new Media(getClass().getResource("MainTrack.mp3").toURI().toString()));
            this.mainTrackMediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            this.mainTrackMediaPlayer.setVolume(0.1);
            this.mainTrackMediaPlayer.play();
        } catch (URISyntaxException ex) {
        }
    }

    /**
     * Plays the specified sound once.
     *
     * @param sound The name of the sound to be played.
     */
    public void playSound(String sound) {
        try {
            new MediaPlayer(new Media(getClass().getResource(sound + ".mp3").toURI().toString())).play();
        } catch (URISyntaxException ex) {
        }
    }
}
