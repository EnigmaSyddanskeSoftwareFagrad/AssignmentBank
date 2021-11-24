/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain;

import java.util.ArrayList;
import java.util.List;
import oop_sem1_project.domain.popups.Popup;

/**
 *
 * A class to act as an intermediary in constructing a packet of data to be sent
 * to the presentation layer
 */
public class DataPacket {

    /**
     * A container class for the current instance of the player
     */
    private final Player player;

    /**
     * A string builder used to construct the phone number shown on the screen
     */
    private final StringBuilder phoneNumber = new StringBuilder();

    /**
     * A list of string arrays to be the 'data packet' that is sent between the
     * layers
     */
    private List<String[]> packet;

    /**
     * String containing the name of the image that is the current room
     * background
     */
    private String background;

    /**
     * String representing the direction the player is facing
     */
    private String playerDirection = "Up";

    /**
     * String containing a message to be added to the text box on the display
     */
    private String message = "";

    /**
     * A container for a pointer to the current popup
     */
    private Popup popup;

    /**
     * A string containing the name of a sound to be played
     */
    private String sound = "";

    /**
     * An indicator of whether or not the quiz should be displayed
     */
    private boolean openQuiz;

    /**
     * A long integer to store the score for sending up to the display
     */
    private long score;

    /**
     * Constructs a data packet with a player and a starting background
     * @param background as a String containing the name of the image to serve as the first background
     * @param player as a Player
     */
    public DataPacket(String background, Player player) {
        this.background = background;
        this.player = player;
    }

    /**
     * Sets the string for the current background image
     * @param background as a string representing the image name
     */
    public void setBackground(String background) {
        this.background = background;
    }

    /**
     * Sets the string representing the player direction
     * @param keyPressed as a string to represent the key that has been pressed
     */
    public void setPlayerDirection(String keyPressed) {
        this.playerDirection = keyPressed;
    }

    /**
     * Returns a pointer to the StringBuilder used to construct the phone number
     * @return The string builder for the phone number
     */
    public StringBuilder getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the message in the data packet to be added to the screen
     * @param message as a string
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets the current popup
     * @param popup as a popup
     */
    public void setPopup(Popup popup) {
        this.popup = popup;
    }

    /**
     * Sets the sound to be played
     * @param sound as a string representing the name of the sound file to be played
     */
    public void setSound(String sound) {
        this.sound = sound;
    }

    /**
     * Sets the indicator for whether or not the quiz window should be open
     * @param openQuiz 
     */
    public void openQuiz(boolean openQuiz) {
        this.openQuiz = openQuiz;
    }

    /**
     * Returns the score stored in the data packet
     * @return A long integer representing the score of the player
     */
    public long getScore() {
        return this.score;
    }

    /**
     * Sets the score of the player
     * @param score as a long integer representing the score to be shown
     */
    public void setScore(long score) {
        this.score = score;
    }

    /**
     * 
     * @return a data packet containing:
     * Background   "bg", "[String image name]"
     * Player       "pl", "[String player direction]", "[int player pos x]", "[int player pos y]"
     * Current Item "ci", "[String image name]"
     * Phone number "pn", "[string phone number]"
     * Message      "msg","[String message]"
     * Popup        "pu", "[String image of popup]"
     * Sound        "so", "[String sound file name]"
     * Open Quiz    "oq", "[boolean open quiz]"
     * Score        "sc", "[long Score]"
     */
    public List<String[]> constructPacket() {
        this.packet = new ArrayList<>();
        this.packet.add(new String[]{"bg", this.background});//background
        this.packet.add(new String[]{"pl", this.playerDirection, String.valueOf(this.player.getPosition()[0]), String.valueOf(this.player.getPosition()[1])});//player
        this.packet.add(new String[]{"ci", this.player.getItem() == null ? "" : this.player.getItem().getImage()});//current item
        this.packet.add(new String[]{"pn", this.phoneNumber.toString()});//phone number
        this.packet.add(new String[]{"msg", this.message == null ? "" : this.message});//message
        this.message = ""; // Clear message.
        this.packet.add(new String[]{"pu", this.popup == null ? "" : this.popup.getImage()});//popup
        this.packet.add(new String[]{"so", this.sound});//sound
        this.sound = ""; // Clear sound.
        this.packet.add(new String[]{"oq", String.valueOf(this.openQuiz)});//open quiz
        this.packet.add(new String[]{"sc", String.valueOf(this.score)});//score
        return this.packet;
    }
}
