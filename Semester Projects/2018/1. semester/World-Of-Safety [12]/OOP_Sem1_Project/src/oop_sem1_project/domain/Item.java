/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain;

/**
 *
 * Class containing logic for items
 * 
 */
public class Item {

    /**
     * The progress needed to use the item
     */
    private final int desiredProgress;
    
    /**
     * The name of the item
     */
    private final String name;
    
    /**
     * The name of the image file
     */
    private final String image;
    
    /**
     * Message displayed upon use of the item
     */
    private final String useMessage;
    
    /**
     * The sound played upon use
     */
    private final String sound;

    /**
     * Creates a new Item
     *
     * @param name            The name of the item
     * @param image           The path to its image
     * @param desiredProgress The items desired progress.
     * @param useMessage      The message displayed when the item gets used
     * @param sound
     */
    public Item(String name, String image, int desiredProgress, String useMessage, String sound) {
        this.name = name;
        this.desiredProgress = desiredProgress;
        this.image = image;
        this.useMessage = useMessage;
        this.sound = sound;
    }

    /**
     *
     * @param progress
     * @return true if the desiredProgress equals the parameter
     */
    public boolean isDesiredProgress(int progress) {
        return this.desiredProgress == progress;
    }

    /**
     *
     * @return The items name
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return The items image name
     */
    public String getImage() {
        return this.image;
    }

    /**
     *
     * @return The message that gets displayed
     */
    public String getUseMessage() {
        return this.useMessage;
    }

    /**
     * 
     * @return 
     */
    public String getSound() {
        return this.sound;
    }

}
