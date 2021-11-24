/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain;

/**
 *
 * An area that can be interactable in a few ways:
 * The area has a size and this area is impassable
 * The area potentially has a range which can have a type enabeling the game
 * mechanics based on this
 * The area can have a required item enabling the use of items based on the area
 *
 */
public class InteractableArea {

    /**
     * The size as with and height
     */
    private final int[] size;

    /**
     * The range radius
     */
    private final int range;

    /**
     * The type of range
     */
    private final String rangeType;

    /**
     * The x and y coordinates for the position
     */
    private final int[] position;

    /**
     * The required item
     */
    private final String requiredItemName;

    /**
     *
     * @param position         the position of the area
     * @param size             the size of the area
     * @param range            the range the area is detectable in
     * @param rangeType        the type of range
     * @param requiredItemName the item that is required for that area
     */
    public InteractableArea(int[] position, int[] size, int range, String rangeType, String requiredItemName) {
        this.position = position;
        this.size = size;
        this.range = range;
        this.rangeType = rangeType;
        this.requiredItemName = requiredItemName;
    }

    /**
     *
     * @param playerPosition the position of the player
     * @return true if the player is within the range of the area
     */
    public boolean isWithinRange(int[] playerPosition) {
        return (playerPosition[0] > this.position[0] - this.range && playerPosition[0] < this.position[0] + this.size[0] + this.range //within x-limit
                && playerPosition[1] > this.position[1] - this.range && playerPosition[1] < this.position[1] + this.size[1] + this.range);  //within y-limit
    }

    /**
     *
     * @param playerPosition the position of the player
     * @return true if the player is at the boundary of an area
     */
    public boolean isAtboundary(int[] playerPosition) {
        return playerPosition[0] >= this.position[0] && playerPosition[0] < this.position[0] + this.size[0] //within x-limit
                && playerPosition[1] >= this.position[1] && playerPosition[1] < this.position[1] + this.size[1];  //within y-limit
    }

    /**
     *
     * @return the type of the range
     */
    public String getRangeType() {
        return rangeType;
    }

    /**
     *
     * @param item the item that the area requires
     * @return true if the item being used is the item the area requires
     */
    public boolean isRequiredItem(Item item) {
        return item != null && item.getName().equalsIgnoreCase(requiredItemName);
    }
}
