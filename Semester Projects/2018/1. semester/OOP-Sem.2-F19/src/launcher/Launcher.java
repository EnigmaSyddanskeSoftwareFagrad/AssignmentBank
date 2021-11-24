/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package launcher;

import client.presentation.Presentation;
import javafx.application.Application;
import javafx.stage.Stage;
import server.communication.ServerController;

/**
 *
 * @author Sanitas Solutions
 */
public class Launcher extends Application {

    public static void main(String[] args) {
        new Thread(() -> Presentation.main(args), "ClientThread").start();
        new Thread(new ServerController(), "ServerThread").start();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Downside of creating a JavaFX project
    }
}
