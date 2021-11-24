/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.communication;

import org.junit.Assert;
import org.junit.Test;
import server.communication.ServerController;

/**
 *
 * @author Hounsvad
 */
public class CommunicationInterfaceImplTest {

    public CommunicationInterfaceImplTest() {
    }

    /**
     * Test of sendQuery method, of class CommunicationInterfaceImpl.
     */
    @Test
    public void testSendQuery() {
        CommunicationInterface testInstance = new CommunicationInterfaceImpl();
        ServerController serverC = new ServerController();
        Thread server = new Thread(serverC, "ServerThread");
        server.start();
        Assert.assertTrue("Sanitas Solutions".equals(testInstance.sendQuery("login", "sanitas", "9d9687a72cc7c34b5b4820a5a32e3d7a2aec24b95ed5738f76856a230916f39c").get(0)[0]));
        serverC.stop();
    }

}
