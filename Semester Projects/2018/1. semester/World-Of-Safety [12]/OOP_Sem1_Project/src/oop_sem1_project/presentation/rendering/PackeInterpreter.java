/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.presentation.rendering;

import java.util.Arrays;
import java.util.List;

/**
 * The purpose of this class is to read/translate/interpret the List of String
 * arrays returned from either a click- or keypress event. Furthermore, this
 * class has been introduced as a means of improving the readability of the data
 * received from the domain layer. E.g. "pl" on its own doesn't make much sense,
 * however, the values in the data array are assigned to variables with
 * saying/meaningful names showing the intent/use of what at first-hand looks
 * like arbitrary data.
 *
 */
class PackeInterpreter {

    /**
     * The dataList to be read.
     */
    private final List<String[]> dataList;

    /**
     * The background to be drawn.
     */
    private String background;

    /**
     * The X and Y coordinate of the player.
     */
    private int playerX, playerY;

    /**
     * The direction the player is facing.
     */
    private int playerRotation;

    /**
     * The current item the player has.
     */
    private String currentItem;

    /**
     * The current inputted phone number.
     */
    private String phoneNumber;

    /**
     * The message to be appended to the TextArea.
     */
    private String message;

    /**
     * The current popup image to be displayed.
     */
    private String popupImage;

    /**
     *
     */
    private String sound;

    /**
     * Whether or not to open the quiz a.k.a. the end of the game.
     */
    private boolean openQuiz;

    /**
     * The score in the form of time passed in milliseconds;
     */
    private long score;

    /**
     * Constructs a new PackeInterpreter and performs an initial read on the
     * data passed as in the dataList parameter.
     *
     * @param dataList The initial data list to be converted to something
     *                 understandable.
     */
    public PackeInterpreter(List<String[]> dataList) {
        this.dataList = dataList;
        readData();
    }

    /**
     * Reads the data and assigns the values to the appropriate attributes.
     */
    private void readData() {
        for (String[] data : this.dataList) {
            switch (data[0]) {
                case "bg": // Background
                    this.background = data[1];
                    break;
                case "pl": // Player
                    this.playerRotation = !Arrays.asList("W", "Up").contains(data[1]) ? !Arrays.asList("S", "Down").contains(data[1]) ? !Arrays.asList("A", "Left").contains(data[1]) ? 90 : -90 : 180 : 0;
                    this.playerX = Integer.parseInt(data[2]);
                    this.playerY = Integer.parseInt(data[3]);
                    break;
                case "ci": // Current Item
                    this.currentItem = data[1];
                    break;
                case "pn": // Phone Number
                    StringBuilder number = new StringBuilder();
                    //Add a space every 2 characters.
                    for (int i = 0; i < data[1].length(); i++) {
                        number.append(i != 0 && i % 2 == 0 ? " " : "").append(String.valueOf(data[1].charAt(i)));
                    }
                    this.phoneNumber = number.toString();
                    break;
                case "msg": // Message
                    this.message = data[1];
                    break;
                case "pu": // Popup
                    this.popupImage = data[1];
                    break;
                case "so": // Sound
                    this.sound = data[1];
                    break;
                case "oq": // Open Quiz
                    this.openQuiz = Boolean.valueOf(data[1]);
                    break;
                case "sc":
                    this.score = Long.parseLong(data[1]);
            }
        }
    }

    /**
     * @return The background.
     */
    public String getBackground() {
        return this.background;
    }

    /**
     * @return The X coordinate of the Player.
     */
    public int getPlayerX() {
        return this.playerX;
    }

    /**
     * @return The Y coordinate of the Player.
     */
    public int getPlayerY() {
        return this.playerY;
    }

    /**
     * @return The rotation of the player based on the direction. (Left, Right,
     *         Up, or Down)
     */
    public int getPlayerRotation() {
        return this.playerRotation;
    }

    /**
     * @return The current item the player has.
     */
    public String getCurrentItem() {
        return this.currentItem;
    }

    /**
     * @return The phone number.
     */
    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    /**
     * @return The message.
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * @return The current Popup.
     */
    public String getPopupImage() {
        return this.popupImage;
    }

    /**
     * @return The sound to be played if any.
     */
    public String getSound() {
        return this.sound;
    }

    /**
     * @return Whether to open the quiz or not.
     */
    public boolean isOpenQuiz() {
        return this.openQuiz;
    }

    /**
     * @return The score.
     */
    public long getScore() {
        return this.score;
    }
}
