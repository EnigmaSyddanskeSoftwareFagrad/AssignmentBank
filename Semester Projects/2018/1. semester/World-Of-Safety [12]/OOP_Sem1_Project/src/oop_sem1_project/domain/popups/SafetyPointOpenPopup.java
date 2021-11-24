/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain.popups;

import oop_sem1_project.domain.InteractionHandlerImpl;

/**
 *
 * A class for the functionality of the safetypoint when it is "open"
 */
public class SafetyPointOpenPopup extends Popup {

    /**
     *
     * @param interactionHandler an instance of the InteractionHandler
     * @param image              the name of the image used to display the Popup
     */
    public SafetyPointOpenPopup(InteractionHandlerImpl interactionHandler, String image) {
        super(interactionHandler, image);

        addClickableAreas("zoom", new int[]{366, 117, 163, 97});
        addClickableAreas("fire-extinguisher", new int[]{363, 318, 68, 153});
        addClickableAreas("defibrilator", new int[]{461, 340, 70, 60});
        addClickableAreas("first-aid", new int[]{458, 403, 66, 59});
        addClickableAreas("eyewash", new int[]{458, 297, 70, 32});
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
                case "fire-extinguisher":
                    getInteractionHandler().getGameContainer().getPlayer().setItem(getInteractionHandler().getGameContainer().getItems().get(0));
                    break;
                case "eyewash":
                    getInteractionHandler().getGameContainer().getPlayer().setItem(getInteractionHandler().getGameContainer().getItems().get(1));
                    break;
                case "defibrilator":
                    getInteractionHandler().getGameContainer().getPlayer().setItem(getInteractionHandler().getGameContainer().getItems().get(2));
                    break;
                case "first-aid":
                    getInteractionHandler().getGameContainer().getPlayer().setItem(getInteractionHandler().getGameContainer().getItems().get(3));
                    break;
                case "exit1":
                case "exit2":
                    getInteractionHandler().getGameContainer().setPopup(null);
            }
        }
    }
}
