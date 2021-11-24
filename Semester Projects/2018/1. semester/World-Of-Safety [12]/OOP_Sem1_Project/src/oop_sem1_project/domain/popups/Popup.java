/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain.popups;

import java.util.HashMap;
import java.util.Map;
import oop_sem1_project.domain.InteractionHandlerImpl;

/**
 *
 * A class for the functionality of the popups in the game
 * 
 */
public abstract class Popup {

    /**
     * A container variable for the pointer to the current instance of the interactionHandler
     */
    private final InteractionHandlerImpl interactionHandler;
    
    /**
     * A string to contain the name of the image file for the popup
     */
    private final String image;
    
    /**
     * A map of clickable areas
     */
    private final Map<String, int[]> clickableAreas = new HashMap<>();

    /**
     *
     * @param interactionHandler an instance of the InteractionHandler
     * @param image              the image used to display the Popup
     */
    public Popup(InteractionHandlerImpl interactionHandler, String image) {
        this.interactionHandler = interactionHandler;
        this.image = image;
    }

    /**
     *
     * @return the name of the image used to display the Popup
     */
    public String getImage() {
        return this.image;
    }

    /**
     * Adds a ClickableArea to a map of ClickableAreas
     *
     * @param identifier a unique identifier
     * @param area       the area
     */
    public final void addClickableAreas(String identifier, int[] area) {
        this.clickableAreas.put(identifier, area);
    }

    /**
     * Compares the position of the mouseclick to every ClickableArea
     *
     * @param clickedPosition the position the mouseclick happened on
     * @return return the identifier of the ClickableArea which got clicked on
     */
    public String getClickedArea(int[] clickedPosition) {
        for (Map.Entry<String, int[]> clickableArea : this.clickableAreas.entrySet()) {
            //if the clicks position from the argument is within a clickable area form the clickable areas map
            if ( //X
                    clickedPosition[0] < clickableArea.getValue()[0] + clickableArea.getValue()[2]
                    && clickedPosition[0] > clickableArea.getValue()[0]
                    //Y
                    && clickedPosition[1] < clickableArea.getValue()[1] + clickableArea.getValue()[3]
                    && clickedPosition[1] > clickableArea.getValue()[1]) {
                return clickableArea.getKey();
            }
        }
        return null;
    }

    /**
     *
     * @return the InteractionHandler
     */
    public InteractionHandlerImpl getInteractionHandler() {
        return this.interactionHandler;
    }

    public abstract void onClick(int[] clickedPosition);
}
