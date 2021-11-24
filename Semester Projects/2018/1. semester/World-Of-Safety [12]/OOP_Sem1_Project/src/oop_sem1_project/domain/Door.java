/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain;

/**
 *
 * A door object
 * 
 */
public class Door extends InteractableArea {

    /**
     * Container for the room to switch to upon passing the door
     */
    private final Room destination;
    
    /**
     * Determining value for the door orientation
     */
    private final boolean vertical;
    
    /**
     * Name of the door
     */
    private final String name;

    /**
     *
     * @param name             the name of the door
     * @param position         the position of the door
     * @param size             the size of the door
     * @param range            the range of the door
     * @param rangeType        the type of range
     * @param requiredItemName the item the door requires
     * @param destination      the destination the door leads to
     * @param vertical         true if the door is located on a vertical wall
     */
    public Door(String name, int[] position, int[] size, int range, String rangeType, String requiredItemName, Room destination, boolean vertical) {
        super(position, size, range, rangeType, requiredItemName);
        this.name = name;
        this.destination = destination;
        this.vertical = vertical;
    }

    /**
     *
     * @return the destination
     */
    public Room getDestination() {
        return this.destination;
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @param player the player
     * @return the position the player is going to be after entering the door
     */
    public int[] recalculatePlayerPosition(Player player) {
        int[] location = player.getPosition();
        if (this.vertical) {
            location[1] = location[1] < 250 ? 400 : 50;
        } else {
            location[0] = location[0] < 450 ? 800 : 50;
        }
        return location;
    }
}
