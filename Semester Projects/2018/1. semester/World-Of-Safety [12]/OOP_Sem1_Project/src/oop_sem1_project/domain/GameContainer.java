/* 
 * Developed by SDU OOP E18 SE/ST grp 21
 * Frederik Alexander Hounsvad, Andreas Kaer Lauritzen,  Patrick Nielsen, Oliver Lind Nordestgaard, Benjamin Eichler Staugaard
 * The use of this work is limited to educational purposes
 */

package oop_sem1_project.domain;

import java.util.ArrayList;
import java.util.List;
import oop_sem1_project.domain.popups.Popup;

/**
 *
 * A Container for values and instances 
 * 
 */
public class GameContainer {

    /**
     * A list of items 
     */
    private final List<Item> items = new ArrayList<>();

    /**
     * The current popup
     */
    private Popup popup;
    
    /**
     * The current instance of the player
     */
    private Player player;

    /**
     * Initializes the Player object
     * Initializes all rooms, areas and items
     *
     * @param playerName the Name of the player
     */
    public void inititalize(String playerName) {
        this.player = new Player(playerName, new int[]{400, 400});

        // Initializing all Rooms
        Room entrance = new Room(new String[]{"Entrance", "Entrance", "Entrance", "EntranceFire"}, 0, false);
        Room hall = new Room(new String[]{"MainHall", "MainHall", "MainHall", "MainHallFire"}, 0, false);
        Room hallway = new Room(new String[]{"Hall", "Hall", "Hall", "Hall"}, 0, false);
        Room robtek = new Room(new String[]{"Robobtek0", "Robobtek1", "Robobtek2", "Robobtek2"}, 0, true);
        Room chemlab = new Room(new String[]{"ChemLab0", "ChemLab1", "ChemLab2", "ChemLab2"}, 2, true);
        Room workshop = new Room(new String[]{"Workshop0", "Workshop1", "Workshop2", "Workshop2"}, 4, true);
        Room u55 = new Room(new String[]{"Lecture0", "Lecture1", "Lecture2", "Lecture2"}, 6, true);
        Room rallypoint = new Room(new String[]{"RallyPoint", "RallyPointFire", "RallyPointFire", "RallyPointFire"}, 10, true);

        //Entrance messages
        entrance.addMessage(0, "You decide to wander around a bit");
        entrance.addMessage(1, "Still the same, old, boring entrance");
        entrance.addMessage(8, "You should Probably call someone about this Fire!");
        entrance.addMessage(9, "Should't the university administration know that this happened?");
        entrance.addMessage(10, "Hurry to the RallyPoint or else it could have fatal consequences!");

        //Entrance interactable areas
        entrance.addInteractableArea("doorNorth", new Door("doorNorth", new int[]{400, 0}, new int[]{50, 0}, 50, "door", null, hall, true));
        entrance.addInteractableArea("doorSouth", new Door("doorSouth", new int[]{400, 450}, new int[]{50, 50}, 50, "door", null, rallypoint, true));
        entrance.addInteractableArea("flower1", new InteractableArea(new int[]{550, 350}, new int[]{50, 100}, 0, "none", null));
        entrance.addInteractableArea("flower2", new InteractableArea(new int[]{300, 350}, new int[]{50, 100}, 0, "none", null));
        entrance.addInteractableArea("wallLeft", new InteractableArea(new int[]{0, 0}, new int[]{400, 300}, 0, "none", null));
        entrance.addInteractableArea("wallRight", new InteractableArea(new int[]{500, 0}, new int[]{400, 300}, 0, "none", null));

        //Hall messages
        hall.addMessage(0, "You notice a \"Safety Point\" in the corner. You should probably check it out.\nYou also notice a faint smell of burning electronics...");
        hall.addMessage(1, "Maybe the \"safety point\" has something useful.");
        hall.addMessage(4, "You hear a bunch of loud noises as if something has gone utterly wrong in the workshop...");
        hall.addMessage(5, "You should probably go check on the workshop there is blood spilling out under the door...");
        hall.addMessage(6, "There is a drastic change in noice level from U55 something is definitely wrong.");
        hall.addMessage(7, "You should check U55 somthing is wrong in there, no one is that excitet when having a lecture.");
        hall.addMessage(8, "The smell of smoke is choking and you should definitely do something as you see fire everywhere!");
        hall.addMessage(10, "Flee you fool!");

        //Hall interactable areas
        hall.addInteractableArea("doorNorth", new Door("doorNorth", new int[]{400, 0}, new int[]{50, 0}, 50, "door", null, hallway, true));
        hall.addInteractableArea("doorEast", new Door("doorEast", new int[]{850, 200}, new int[]{50, 50}, 50, "door", null, workshop, false));
        hall.addInteractableArea("doorSouth", new Door("doorSouth", new int[]{400, 450}, new int[]{50, 50}, 50, "door", null, entrance, true));
        hall.addInteractableArea("doorWest", new Door("doorWest", new int[]{0, 200}, new int[]{0, 50}, 50, "door", null, u55, false));
        hall.addInteractableArea("safetyPoint", new InteractableArea(new int[]{650, 450}, new int[]{50, 0}, 50, "safetypoint", null));

        //Hallway messages
        hallway.addMessage(-1000, "As you walk along the empty hallway, the smell of burnt electronics gets stronger.");
        hallway.addMessage(1, "There is still a burning smell in the air. \nYou should find out where its coming from.");
        hallway.addMessage(2, "As you walk in to the hallway you hear a scream coming from the chemestry lab.");
        hallway.addMessage(3, "The screams are still ringing out of the chemestry lab.");
        hallway.addMessage(4, "The hallway appears to be empty.");

        //Hallway interactable areas
        hallway.addInteractableArea("doorEast", new Door("doorEast", new int[]{850, 200}, new int[]{50, 50}, 50, "door", null, chemlab, false));
        hallway.addInteractableArea("doorSouth", new Door("doorSouth", new int[]{400, 450}, new int[]{50, 50}, 50, "door", null, hall, true));
        hallway.addInteractableArea("doorWest", new Door("doorWest", new int[]{0, 200}, new int[]{0, 50}, 50, "door", null, robtek, false));

        //Robtek messages
        robtek.addMessage(-1000, "There is honestly no reason to ever be here");
        robtek.addMessage(0, "A burning smell enters your nose. In the corner of the room you see flames shooting out of a one-armed robot.\nOne of the engineers shouts: \"Someone please help! We are trapped behind the fire spewing robot\".");
        robtek.addMessage(1, "They really can't put out this fire on their own");
        robtek.addMessage(2, "Even with the fire gone, it still smells here \nBetter move on");

        //Robtek interactable areas
        robtek.addInteractableArea("doorEast", new Door("doorEast", new int[]{850, 200}, new int[]{50, 50}, 50, "door", null, hallway, false));
        robtek.addInteractableArea("table", new InteractableArea(new int[]{0, 100}, new int[]{250, 100}, 0, "none", null));
        robtek.addInteractableArea("robotFire", new InteractableArea(new int[]{250, 0}, new int[]{150, 250}, 50, "emergency", "fire-extinguisher"));

        //Chemlab messages
        chemlab.addMessage(-1000, "You walk up to the door of the Chemical lab, a sign on it says: \n \"Stay out, experiment in progress\"");
        chemlab.addMessage(0, "One of the chemists, screams in agony while desperatly rubbing his hands into his eyes \nYou have to help him");
        chemlab.addMessage(1, "The chemist is still screaming \nHe could use some help");
        chemlab.addMessage(2, "The sign still says \"Stay out, experiment in progress\" \n They are hopefully wearing safety glasses now");

        //Chemlab interactable areas
        chemlab.addInteractableArea("doorWest", new Door("doorWest", new int[]{0, 200}, new int[]{0, 50}, 50, "door", null, hallway, false));
        chemlab.addInteractableArea("closetWest", new InteractableArea(new int[]{0, 0}, new int[]{300, 100}, 0, "none", null));
        chemlab.addInteractableArea("tableWest", new InteractableArea(new int[]{200, 100}, new int[]{100, 250}, 0, "none", null));
        chemlab.addInteractableArea("closetEast", new InteractableArea(new int[]{400, 400}, new int[]{300, 100}, 0, "none", null));
        chemlab.addInteractableArea("tableEast", new InteractableArea(new int[]{600, 150}, new int[]{100, 250}, 0, "none", null));
        chemlab.addInteractableArea("chemStudent", new InteractableArea(new int[]{750, 350}, new int[]{100, 50}, 51, "emergency", "eyewash"));

        //Workshop messages
        workshop.addMessage(-1000, "You enter the workshop \n The air is filled with sawdust \n There is nothing to see here.");
        workshop.addMessage(0, "You notice blood pouring from one of the student's arms. \n\"Tis but a scratch!\" he claims. \nYou should probably help him anyway...");
        workshop.addMessage(1, "The Student is still calm. \nThe blood puddle on the floor seems to get larger with every second");
        workshop.addMessage(2, "The cleaning ladies are trying to get rid of all the blood on the floor \nYou dont want to bother them");

        //Workshop interactable areas
        workshop.addInteractableArea("doorWest", new Door("doorWest", new int[]{0, 200}, new int[]{0, 50}, 50, "door", null, hall, false));
        workshop.addInteractableArea("closetWest", new InteractableArea(new int[]{0, 0}, new int[]{300, 100}, 0, "none", null));
        workshop.addInteractableArea("closetEast", new InteractableArea(new int[]{350, 0}, new int[]{300, 100}, 0, "none", null));
        workshop.addInteractableArea("tableWest", new InteractableArea(new int[]{150, 150}, new int[]{100, 250}, 0, "none", null));
        workshop.addInteractableArea("tableCenter", new InteractableArea(new int[]{350, 150}, new int[]{100, 250}, 0, "none", null));
        workshop.addInteractableArea("tableEast", new InteractableArea(new int[]{550, 150}, new int[]{100, 250}, 0, "none", null));
        workshop.addInteractableArea("woodPile", new InteractableArea(new int[]{800, 0}, new int[]{100, 500}, 0, "none", null));
        workshop.addInteractableArea("workshopStudent", new InteractableArea(new int[]{650, 300}, new int[]{50, 100}, 50, "emergency", "first-aid"));

        //U55 messages
        u55.addMessage(-1000, "Someone is giving a lecture right now, you decide to stay and listen for a while \n \" ...og som i har læst i e-tivitet 4... \" \n You decide not to listen any more and decide to head out of the room.");
        u55.addMessage(0, "In the middle of all the commotion, you notice a student in the back, laying on the floor! Someone is allready doing cpr.");
        u55.addMessage(1, "The student lying on the floor doesn't seem to be responsive and its allready been a little while \nYou should act quick!");
        u55.addMessage(2, "Everyone seems to have calmed down already, some students even fell asleep...");

        //U55 interactable areas
        u55.addInteractableArea("doorEast", new Door("doorEast55", new int[]{850, 200}, new int[]{50, 50}, 50, "door", null, hall, false));
        u55.addInteractableArea("tabelNorth1", new InteractableArea(new int[]{150, 0}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("tableNorth2", new InteractableArea(new int[]{250, 0}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("tableNorth3", new InteractableArea(new int[]{350, 0}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("tableNorth4", new InteractableArea(new int[]{450, 0}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("tableNorth5", new InteractableArea(new int[]{550, 0}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("tableSouth1", new InteractableArea(new int[]{150, 300}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("tableSouth2", new InteractableArea(new int[]{250, 300}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("tableSouth3", new InteractableArea(new int[]{350, 300}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("tableSouth4", new InteractableArea(new int[]{450, 300}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("tableSouth5", new InteractableArea(new int[]{550, 300}, new int[]{50, 200}, 0, "none", null));
        u55.addInteractableArea("podium", new InteractableArea(new int[]{50, 200}, new int[]{50, 100}, 0, "none", null));
        u55.addInteractableArea("heartAttack", new InteractableArea(new int[]{600, 350}, new int[]{50, 100}, 50, "emergency", "defibrilator"));

        //Rallypoint messages
        rallypoint.addMessage(-1000, "Why would you leave already? The day isn't over");
        rallypoint.addMessage(0, "Everyone gasps at the sight of the TEK building burning to ashes. \nAt least all the students are safe \n\"Wait! Has anyone seen the Rob-tek students?\"");
        rallypoint.addMessage(-2, "Call the goddamn Fire-department!");
        rallypoint.addMessage(-1, "You should call the SDU administration aswell");

        //Rallypoint interactable areas
        rallypoint.addInteractableArea("doorNorth", new Door("doorNorth", new int[]{400, 0}, new int[]{50, 0}, 50, "door", null, entrance, true));
        rallypoint.addInteractableArea("quiz", new InteractableArea(new int[]{400, 300}, new int[]{100, 100}, 50, "quiz", null));

        this.player.setCurrentRoom(entrance);

        //Add all Items to the item list
        this.items.add(new Item("fire-extinguisher", "Fireextinguisher", 0, "*SPWOOOOOOSH!* Goes the fire-extinguisher \n\"Thank you, we would have been screwed without your help, now go\"", "Extinguisher"));
        this.items.add(new Item("eyewash", "Eyewash", 0, "*SPLASH* Goes the eyewash \nThe Student sighs in relief. \"Not worth the risk of staying here, i'll better go home and rest a couple of days\" he says", "Splaash"));
        this.items.add(new Item("defibrilator", "AED", 2, "*BZZZZZT* Goes the fancy tazer \n The student began to open his eyes again and the ambulance is on the way to the hospital with him", "Defibrilator"));
        this.items.add(new Item("first-aid", "Firstaid", 3, "As the socks are stuffed into the wound the student clenches his teeth  \n\"Thanks, i could have just waited for it to stop bleeding, but i guess now i can get back to work faster\"", "Ooof"));

    }

    /**
     *
     * @return the current Popup
     */
    public Popup getPopup() {
        return this.popup;
    }

    /**
     * Sets the current Popup
     *
     * @param popup
     */
    public void setPopup(Popup popup) {
        this.popup = popup;
    }

    /**
     *
     * @return the player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     *
     * @return a list of items
     */
    public List<Item> getItems() {
        return this.items;
    }
}
