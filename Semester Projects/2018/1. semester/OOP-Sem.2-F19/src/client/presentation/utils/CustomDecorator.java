/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.utils;

import com.jfoenix.controls.JFXDecorator;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 * A Decorator class used to make the top buttons non-traversable and lets the exit button exit the system
 * @author Sanitas Solutions
 */
public class CustomDecorator extends JFXDecorator {
    
    /**
     * Constructs the CustomDecorator 
     * @param stage The stage for which the Decorator is used
     * @param node The node for which the Decorator is used
     * @param fullScreen Whether the window is in fullscreen or not
     * @param max ???
     * @param min ???
     */
    public CustomDecorator(Stage stage, Node node, boolean fullScreen, boolean max, boolean min) {
        super(stage, node, fullScreen, max, min);
        //make UnTraversable
        btnMin.setFocusTraversable(false);
        btnClose.setFocusTraversable(false);
        btnFull.setFocusTraversable(false);
        btnMax.setFocusTraversable(false);
        //Exit propperly
        btnClose.setOnAction((e) -> System.exit(0));
    }
}
