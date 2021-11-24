/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain.popups;

import oop_sem1_project.domain.InteractionHandlerImpl;

/**
 *
 * A class for the functionality of the safetypoint when it has the map screen open
 */
public class SafetyPointZoomPopup extends Popup {

    /**
     *
     * @param interactionHandler an instance of the InteractionHandler
     * @param image              the name of the image used to display the popup
     */
    public SafetyPointZoomPopup(InteractionHandlerImpl interactionHandler, String image) {
        super(interactionHandler, image);
    }

    /**
     * Does something depending on where you've clicked
     *
     * @param clickedPosition the position the mouseclick happened on
     */
    @Override
    public void onClick(int[] clickedPosition) {
        getInteractionHandler().getGameContainer().setPopup(new SafetyPointClosedPopup(getInteractionHandler(), "SafetyPointClosed"));
    }
}
