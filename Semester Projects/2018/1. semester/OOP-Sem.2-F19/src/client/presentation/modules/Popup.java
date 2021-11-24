/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules;

import client.presentation.CommunicationHandler;
import client.presentation.utils.credentials.CredentialContainer;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 *
 * @author Sanitas Solutions
 */
public abstract class Popup implements Initializable {

    @FXML
    protected FontAwesomeIconView cross;

    private Module moduleController;

    /**
     * Shared instance of the communicationHandler
     */
    protected final CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

    /**
     * Shared instance of the credential container
     */
    protected final CredentialContainer credentialContainer = CredentialContainer.getInstance();

    /**
     * Closes the popup
     */
    @FXML
    protected void close() {
        ((Stage) cross.getScene().getWindow()).close();
    }

    /**
     * @return the instance of the controller that opened the popup
     */
    protected Module getModuleController() {
        return moduleController;
    }

    /**
     * Stores the instance of the launching module controller in the
     * moduleController variable
     *
     * @param moduleController the module instance to be stored
     */
    public void setModuleController(Module moduleController) {
        this.moduleController = moduleController;
    }

}
