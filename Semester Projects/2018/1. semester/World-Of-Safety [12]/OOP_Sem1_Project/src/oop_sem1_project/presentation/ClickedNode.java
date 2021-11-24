/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.presentation;

/**
 * An enum class containing the valid areas a click event can occur in our game.
 *
 */
public enum ClickedNode {
    /**
     * A click corresponding to a click on the game/main Canvas.
     */
    GAME_CANVAS,
    /**
     * A click corresponding to a click on the phone Canvas/ImageView.
     */
    PHONE_CANVAS,
    /**
     * A click corresponding to a click on the item Canvas/ImageView.
     */
    ITEM_CANVAS
}
