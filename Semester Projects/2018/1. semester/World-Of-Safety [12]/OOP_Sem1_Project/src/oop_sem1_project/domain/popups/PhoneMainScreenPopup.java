/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain.popups;

import oop_sem1_project.domain.InteractionHandlerImpl;

/**
 *
 * A class for the functionality of the phone main screen popup
 * 
 */
public class PhoneMainScreenPopup extends Popup {

    /**
     *
     * @param interactionHandler an instance of the InteractionHandler
     * @param phoneMainScreen    the name of the image used to display the Popup
     */
    public PhoneMainScreenPopup(InteractionHandlerImpl interactionHandler, String phoneMainScreen) {
        super(interactionHandler, phoneMainScreen);
        addClickableAreas("map", new int[]{390, 391, 41, 41});
        addClickableAreas("dial", new int[]{472, 391, 41, 41});
        addClickableAreas("exit1", new int[]{0, 0, 322, 500});
        addClickableAreas("exit2", new int[]{577, 0, 900, 500});
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
                case "map":
                    getInteractionHandler().getGameContainer().setPopup(new PhoneMapScreenPopup(getInteractionHandler(), "PhoneMap"));
                    break;
                case "dial":
                    getInteractionHandler().getGameContainer().setPopup(new PhoneDialScreenPopup(getInteractionHandler(), "PhoneDialScreen"));
                    break;
                case "exit1":
                case "exit2":
                    getInteractionHandler().getGameContainer().setPopup(null);
            }
        }
    }
}
