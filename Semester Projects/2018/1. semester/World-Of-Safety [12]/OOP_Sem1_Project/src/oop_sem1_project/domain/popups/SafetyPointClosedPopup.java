/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain.popups;

import oop_sem1_project.domain.InteractionHandlerImpl;

/**
 *
 * A class for the functionality of the safetypoint when it is "closed"
 * 
 */
public class SafetyPointClosedPopup extends Popup {

    /**
     *
     * @param interactionHandler
     * @param image              the name of the image
     */
    public SafetyPointClosedPopup(InteractionHandlerImpl interactionHandler, String image) {
        super(interactionHandler, image);
        addClickableAreas("zoom", new int[]{366, 117, 163, 97});
        addClickableAreas("open", new int[]{476, 291, 79, 80});
        addClickableAreas("exit1", new int[]{0, 0, 343, 500});
        addClickableAreas("exit2", new int[]{557, 0, 343, 500});
    }

    /**
     * Does something depending on where you've clicked
     *
     * @param clickedPosition the position the mouseclick happened on
     */
    @Override
    public void onClick(int[] clickedPosition) {
        String clickedArea = getClickedArea(clickedPosition);
        if (clickedArea != null) {
            switch (clickedArea) {
                case "zoom":
                    getInteractionHandler().getGameContainer().setPopup(new SafetyPointZoomPopup(getInteractionHandler(), "SafetyPointOverlay"));
                    break;
                case "open":
                    getInteractionHandler().getGameContainer().setPopup(new SafetyPointOpenPopup(getInteractionHandler(), "SafetyPointOpen"));
                    break;
                case "exit1":
                case "exit2":
                    getInteractionHandler().getGameContainer().setPopup(null);
            }
        }
    }
}
