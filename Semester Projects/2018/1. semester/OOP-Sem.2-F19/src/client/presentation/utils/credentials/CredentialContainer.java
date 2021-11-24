/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.utils.credentials;

import java.io.IOException;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Sanitas Solutions
 */
public final class CredentialContainer {

    private static CredentialContainer instance = null;

    //The stored password of the user
    private String password = null;

    //The stored hashed password of the user
    private String username = null;
    private long lastAccess = 0l;
    private boolean isGettingCredentials = false;
    private boolean firstRound = true;
    private final BooleanProperty credentialReady = new SimpleBooleanProperty(false);
    private static final String LOGIN_SCREEN_PATH = "LoginPopupFXML.fxml";
    private static final long DELAY = 3600000;

    /**
     * gets the value of the Boolean property that indicates if the credentials
     * are ready
     *
     * @return true if the credentials are ready
     */
    public BooleanProperty getCredentialReadyProperty() {
        return credentialReady;
    }

    /**
     * A constructor forced private
     */
    private CredentialContainer() {
    }

    /**
     * Gets the instance of the CredentialContainer
     *
     * @return the CredentialContainer
     */
    public static CredentialContainer getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new CredentialContainer();
        return instance;
    }

    /**
     * Opens the login window
     */
    public void openLoginWindow() {
        Platform.runLater(() -> {
            if (!isGettingCredentials) {
                isGettingCredentials = true;
                FXMLLoader loader = new FXMLLoader();
                Stage login = new Stage();
                try {
                    Parent root = loader.load(getClass().getResource(LOGIN_SCREEN_PATH));
                    Scene s = new Scene(root);
                    s.getStylesheets().add(getClass().getResource("/client/presentation/css/generalStyleSheet.css").toExternalForm());
                    login.initStyle(StageStyle.UNDECORATED);
                    login.initModality(Modality.APPLICATION_MODAL);
                    login.setTitle("Sanitas Overview - Login");
                    login.getIcons().add(new Image(getClass().getResourceAsStream("/client/presentation/resources/sanitaslogo.png")));
                    login.setScene(s);
                    login.show();
                } catch (IOException ex) {
                    ex.printStackTrace(System.err);
                }
            }
        });

    }

    /**
     * Checks if the credentials are still valid
     * If the credentials have not been accessed for one hour they will
     * be reset
     *
     * @return true if the credentials have not been accessed for an hour
     */
    public boolean checkTimeValid() {

        //Find the previous TimeChecker thread and kill it
        for (Thread t : Thread.getAllStackTraces().keySet()) {
            if (t.getName().equals("TimeChecker")) {
                t.interrupt();
            }
        }

        if (this.lastAccess > 0 && this.lastAccess > System.currentTimeMillis() - DELAY) {
            new Thread(() -> {
                try {
                    Thread.sleep(DELAY + 10); //Added 10 millis futher delay, so that i can't possibly call itself before delay
                    checkTimeValid();
                } catch (InterruptedException ex) {
                }
            }, "TimeChecker").start();
            return true;
        }
        this.password = null;
        this.lastAccess = 0;
        credentialReady.set(false);
        openLoginWindow();
        return false;
    }

    /**
     * Sets the {@link #password} and updates the system to
     * know that it has a new password
     *
     * @param password is the hashed password in a string format
     */
    protected void setPassword(String password) {
        this.password = password;
        if (this.username != null) {
            credentialReady.set(true);
        }
        this.isGettingCredentials = false;
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * Sets the {@link #username} and updates the system to
     * know that it has a new username
     *
     * @param username is the username in a string format
     */
    protected void setUsername(String username) {
        this.username = username;
        if (this.password != null) {
            credentialReady.set(true);
        }
        this.isGettingCredentials = false;
        this.lastAccess = System.currentTimeMillis();
    }

    /**
     * A getter for the {@link #password}
     *
     * @return the password stored in the container or null if the credentials
     *         are timed out
     */
    public String getPassword() {
        if (checkTimeValid()) {
            this.lastAccess = System.currentTimeMillis();
            return this.password;
        }
        return null;
    }

    /**
     * A getter for the {@link #password}
     *
     * @return the username stored in the container or null if the credentials
     *         are timed out
     */
    public String getUsername() {
        if (checkTimeValid()) {
            this.lastAccess = System.currentTimeMillis();
            return this.username;
        }
        return null;
    }

    /**
     * Checks if this is the first request for the login credentials
     *
     * @return a boolean value representing the first value
     */
    public boolean isFirst() {
        if (firstRound) {
            firstRound = false;
            return true;
        }
        return false;
    }

    /**
     * Resets the credential container
     */
    public void reset() {
        password = null;
        username = null;
        firstRound = false;
        instance = null;
    }
}
