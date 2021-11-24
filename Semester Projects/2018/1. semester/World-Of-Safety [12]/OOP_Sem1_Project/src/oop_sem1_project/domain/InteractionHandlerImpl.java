/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */
package oop_sem1_project.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import oop_sem1_project.data_access.Storage;
import oop_sem1_project.data_access.StorageImpl;
import oop_sem1_project.domain.popups.PhoneMainScreenPopup;
import oop_sem1_project.domain.popups.SafetyPointClosedPopup;

/**
 *
 * A handler for the domain layer of world of safety
 * 
 */
public class InteractionHandlerImpl implements InteractionHandler {

    /**
     * Constant for how far the player should move at a time
     */
    private static final int MOVE_PIXEL = 50;

    /**
     * The rate of interaction to stop spam
     */
    private final int interactionRate = 100; //Miliseconds
    
    /**
     * The gameContainer containing the instances of the game
     */
    private final GameContainer gameContainer = new GameContainer();
    
    /**
     * The last time an interaction was made
     */
    private long lastInteractionTime = System.currentTimeMillis();
    
    /**
     * The storage class
     */
    private Storage dataAccess;
    
    /**
     * The dataPacket to send information up to the presentation layer
     */
    private DataPacket dataPacket;

    /**
     *
     */
    public InteractionHandlerImpl() {
        try {
            this.dataAccess = new StorageImpl("storage");
        } catch (IOException ex) {
        }
    }

    /**
     * Updates the game based on a key press
     * @param keyPressed as string
     * @return dataPacket as list of string arrays
     */
    @Override
    public List<String[]> update(String keyPressed) {
        if (System.currentTimeMillis() > this.lastInteractionTime + this.interactionRate && this.gameContainer.getPopup() == null) {

            this.dataPacket.setScore(System.currentTimeMillis() - this.gameContainer.getPlayer().getStartTime());
            this.lastInteractionTime = System.currentTimeMillis();

            int vertical = Arrays.asList("Up", "W").contains(keyPressed) ? -MOVE_PIXEL : Arrays.asList("Down", "S").contains(keyPressed) ? MOVE_PIXEL : 0;
            int horizontal = Arrays.asList("Left", "A").contains(keyPressed) ? -MOVE_PIXEL : Arrays.asList("Right", "D").contains(keyPressed) ? MOVE_PIXEL : 0;
            int[] newPos = {this.gameContainer.getPlayer().getPosition()[0] + horizontal, this.gameContainer.getPlayer().getPosition()[1] + vertical};
            boolean canMove = newPos[0] >= 0 && newPos[0] <= 850 && newPos[1] >= 0 && newPos[1] <= 450;
            this.dataPacket.setPlayerDirection(keyPressed);

            if (!canMove) {
                return this.dataPacket.constructPacket();
            }

            for (InteractableArea interactableArea : this.gameContainer.getPlayer().getCurrentRoom().getInteractableAreas().values()) {
                if (interactableArea.isWithinRange(newPos)) {
                    if (interactableArea.getRangeType().equalsIgnoreCase("safetypoint")) {
                        canMove = false;
                        this.gameContainer.setPopup(new SafetyPointClosedPopup(this, "SafetyPointClosed"));
                        break;
                    } else if (interactableArea.getRangeType().equalsIgnoreCase("door")) {
                        Door destination = (Door) interactableArea;

                        if (this.gameContainer.getPlayer().getProgress() >= 8 && !(Arrays.asList("doorsouth", "dooreast55").contains(destination.getName().toLowerCase()))) {
                            break;
                        }

                        if (this.gameContainer.getPlayer().getProgress() >= 8 && this.gameContainer.getPlayer().getProgress() != 10 && !destination.getName().equalsIgnoreCase("doorEast55")) {
                            this.dataPacket.setMessage("You should most definitely call someone before leaving. Take a look in the Safety Point if you can't remember the numbers!");
                            break;
                        }

                        newPos = destination.recalculatePlayerPosition(this.gameContainer.getPlayer());
                        this.dataPacket.setMessage(destination.getDestination().getMessage(this.gameContainer.getPlayer()));
                        this.dataPacket.setSound("Footstep");
                        this.gameContainer.getPlayer().setCurrentRoom(destination.getDestination());
                        break;
                    } else if (interactableArea.getRangeType().equalsIgnoreCase("quiz")) {
                        if (this.gameContainer.getPlayer().getProgress() == 11) {
                            canMove = false;
                            this.dataPacket.openQuiz(true);
                        }
                        break;
                    }
                }
                if (interactableArea.isAtboundary(newPos)) {
                    return this.dataPacket.constructPacket();
                }
            }
            if (canMove) {
                this.dataPacket.setSound("Footstep");
                this.getGameContainer().getPlayer().setPosition(newPos);
            }
            this.dataPacket.setBackground(this.gameContainer.getPlayer().getCurrentRoom().getImage(this.gameContainer.getPlayer()));
            this.dataPacket.setPopup(this.gameContainer.getPopup());
        }
        return this.dataPacket.constructPacket();
    }

    /**
     * Updates the game based on a click
     * @param clickedNode the visual node clicked
     * @param position the clicked position
     * @return dataPacket as list of string arrays
     */
    @Override
    public List<String[]> update(String clickedNode, int[] position) {
        if (clickedNode.equals("GAME_CANVAS") && this.gameContainer.getPopup() != null) {
            this.gameContainer.getPopup().onClick(position);
        } else if (clickedNode.equals("PHONE_CANVAS")) {
            this.gameContainer.setPopup(new PhoneMainScreenPopup(this, "PhoneHomeScreen"));
        } else if (clickedNode.equals("ITEM_CANVAS")) {
            for (InteractableArea interactableArea : this.gameContainer.getPlayer().getCurrentRoom().getInteractableAreas().values()) {
                if (!interactableArea.getRangeType().equalsIgnoreCase("none") && interactableArea.isWithinRange(this.gameContainer.getPlayer().getPosition())) {
                    if (interactableArea.isRequiredItem(this.gameContainer.getPlayer().getItem())) {
                        this.gameContainer.getPlayer().setProgress(this.gameContainer.getPlayer().getProgress() + 1);
                        this.dataPacket.setMessage(this.gameContainer.getPlayer().getItem().getUseMessage());
                        if (this.gameContainer.getPlayer().getItem().getSound() != null) {
                            this.dataPacket.setSound(this.gameContainer.getPlayer().getItem().getSound());
                        }
                        this.gameContainer.getPlayer().setItem(null);
                        this.dataPacket.setBackground(this.gameContainer.getPlayer().getCurrentRoom().getImage(this.gameContainer.getPlayer()));
                        break;
                    } else {
                        this.dataPacket.setMessage("This doesn't help at all");
                    }
                }
            }
        }
        this.dataPacket.setPopup(this.gameContainer.getPopup());
        return this.dataPacket.constructPacket();
    }

    /**
     * Initializes the game
     * @param playerName as string to represent the name of the player
     * @return dataPacket as list of string arrays
     */
    @Override
    public List<String[]> start(String playerName) {
        this.gameContainer.inititalize(playerName);
        this.dataPacket = new DataPacket("Entrance", this.gameContainer.getPlayer());
        return this.dataPacket.constructPacket();
    }

    /**
     * gets the list of highscores
     * @return list of string representing highscores
     */
    @Override
    public List<String> getStoredHighscores() {
        List<String> scores = new ArrayList<>();
        try {
            scores = this.dataAccess.load();
        } catch (IOException ex) {
            return scores;
        }
        Collections.sort(scores, new ScoreSorter());
        return scores;
    }

    /**
     * Stores a score
     * @param correctQuizAnswers number of correct answeres 
     * @return the score
     */
    @Override
    public int storeHighscore(int correctQuizAnswers) {
        int score = (int) ((10 - this.dataPacket.getScore() / 60000) * correctQuizAnswers);
        try {
            this.dataAccess.save(score + " " + this.gameContainer.getPlayer().getName());
        } catch (IOException ex) {
        }

        return score;
    }

    /**
     * 
     * @return the instance of the gameContainer
     */
    public GameContainer getGameContainer() {
        return this.gameContainer;
    }

    /**
     * returns the current data packet
     * @return 
     */
    public DataPacket getDataPacket() {
        return this.dataPacket;
    }
}
