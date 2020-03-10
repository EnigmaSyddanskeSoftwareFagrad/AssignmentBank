package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import acquaintance.IGUI;
import acquaintance.ILogic;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

/**
 * Starts the GUI thread and instantiates the initial controller class.
 *
 * @author Peter Brændgaard
 * @author Julie Markersen 
 */
public class GUIrun extends Application implements IGUI {

    private static ILogic logic;

    private static IGUI guiRun;
    
    private static GUIHandler guih;

    @Override
    public void injectLogic(ILogic LogicLayer) {
        logic = LogicLayer;
    }
    
    /**
     * Returns a singleton instance of the class.
     * @return a singleton instance of the class.
     */
    public static IGUI getInstance() {
        return guiRun;
    }
    
    /**
     * Returns the logic layer associated with the class.
     * @return the logic layer associated with the class.
     */
    public static ILogic getLogic() {
        return logic;
    }
    
    /**
     * Changes the scene based on a specified FXML file.
     * @param fxml FXML file to set the scene as.
     * @throws IOException if the specified file does not exist.
     */
    public static void changeFxml (String fxml) throws IOException {
        guih.changeFXMLAction(fxml);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
        Parent root = loader.load();
        
        loginController controller = loader.getController();
        
        stage.initStyle(StageStyle.TRANSPARENT);
        
        Image img = new Image("/gui/img/logo.png");
        stage.getIcons().add(img);

        Scene scene = new Scene(root);

        guih = new GUIHandler(stage);
        guih.moves(stage, scene);
        
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void startApplication(String[] args) {
        guiRun = this;
        launch(args);
    }
}
