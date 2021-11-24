/* 
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.modules;

import client.presentation.CommunicationHandler;
import client.presentation.utils.credentials.CredentialContainer;
import javafx.fxml.Initializable;

/**
 *
 * @author Sanitas Solutions
 */
public abstract class Module implements Initializable {

    /**
     * Shared instance of the communicationHandler
     *
     * @see Popup#communicationHandler
     */
    protected CommunicationHandler communicationHandler = CommunicationHandler.getInstance();

    /**
     * Shared instance of the credentialContainer
     *
     * @see Popup#credentialContainer
     */
    protected CredentialContainer credentialContainer = CredentialContainer.getInstance();

    /**
     * Clears all data in the module
     */
    abstract protected void clearAll();

    /**
     * Updates all data in the module
     */
    abstract public void updateData();
}
