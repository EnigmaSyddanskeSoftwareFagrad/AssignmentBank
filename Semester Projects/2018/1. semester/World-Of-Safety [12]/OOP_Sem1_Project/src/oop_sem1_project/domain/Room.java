/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * Container for all information about a room
 * 
 */
public class Room {

    /**
     * Areas in a room with special properties
     */
    private final Map<String, InteractableArea> interactableAreas = new HashMap<>();
    
    /**
     * The minimum progress to interact with the room
     */
    private final int desiredProgress;
    
    /**
     * Map of messages with a progress indicator
     */
    private final Map<Integer, String> messages = new HashMap<>();
    
    /**
     * Whether or not the room can increase the players progress upon entering
     */
    private final boolean increasesProgress;
    
    /**
     * The images the background can have
     */
    private final String[] images;

    /**
     *
     * @param image             A String array of images being used to display
     *                          the room
     * @param desiredProgress   The progress the room desires
     * @param increasesProgress true if the progress should be increased inside
     *                          the room
     */
    public Room(String[] image, int desiredProgress, boolean increasesProgress) {
        this.images = image;
        this.desiredProgress = desiredProgress;
        this.increasesProgress = increasesProgress;
    }

    /**
     * Adds a message to the message list
     *
     * @param index   minimum progress for the message
     * @param message the message
     */
    public void addMessage(int index, String message) {
        if (this.messages.put(index, message) != null) {
            throw new IllegalArgumentException("Message index already exists");
        }
    }

    /**
     * Adds an object to the Area list
     *
     * @param key    a as a string to identify the Area
     * @param object as an interactableArea in the room
     */
    public void addInteractableArea(String key, InteractableArea object) {
        if (this.interactableAreas.put(key, object) != null) {
            throw new IllegalArgumentException("Area name already exists");
        }
    }

    /**
     * Returns the relevant message
     *
     * @param player
     * @return the relevant message
     */
    public String getMessage(Player player) {
        int roomProgress = player.getProgress() - this.desiredProgress;

        if (roomProgress == 0 && this.increasesProgress) {
            player.setProgress(player.getProgress() + 1);
        }
        return getMessageSmaller(roomProgress);
    }

    /**
     * Gets the matching message or the first message smaller than the wished
     * message. In the event that the input is smaller than the smallest index
     * then it returns null
     *
     * @param input index to be searched for
     * @return null or desired message
     */
    private String getMessageSmaller(int input) {
        int firstSmaller = -1000;
        for (Map.Entry<Integer, String> message : this.messages.entrySet()) {
            int key = message.getKey();
            if (key == input) {
                return message.getValue();
            }
            if (key < input && key > firstSmaller) {
                firstSmaller = key;
            }
        }
        return this.messages.get(firstSmaller);
    }

    /**
     *
     * @return a map of all interactableAreas in the room
     */
    public Map<String, InteractableArea> getInteractableAreas() {
        return this.interactableAreas;
    }

    /**
     *
     * @return the desired progress the room expects you to have
     */
    public int getDesiredProgress() {
        return this.desiredProgress;
    }

    /**
     * Returns the images of the room, depending on if your progress is equal,
     * higher or lower than the desired progress.
     *
     * @param player
     * @return the images used to display the current room
     */
    public String getImage(Player player) {
        int roomProgress = player.getProgress() - this.desiredProgress;

        if (roomProgress < 0) {
            return this.images[0];//Pre Traumatic
        } else if (roomProgress == 0 || roomProgress == 1) {
            return this.images[1];//Traumatic
        } else if (player.getProgress() >= 8) {
            return this.images[3];//Fire image
        }
        return this.images[2];//Post traumatic image
    }
}
