
package logic;

import acquaintance.ILogic;
import acquaintance.IPersistence;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import persistence.PersistenceFacade;

/**
 * Checks login functionality in logic layer.
 * @author Bent Wilhelmsen
 */
public class LoginTest {

    ILogic logic = new LogicFacade();
    IPersistence persistence = new PersistenceFacade();
        
    /**
     * Injects persistence into logic layer to prepare for tests.
     */
    @Before
    public void before(){
    logic.injectPersistence(persistence);
    }
    
     /**
     * Test if a wrong password is denied.
     */
    @Test
    public void testWrongPassword(){

        System.out.println("Testing login in with wrong password");
        String username = "admin";
        String password = "";
        boolean expectedResult = false;
        boolean failedResult = logic.login(username, password);
        assertEquals(expectedResult, failedResult);
    }
    
    /**
     * Test if a valid login is accepted.
     */
    @Test
    public void testCorrectLogin() {
        System.out.println("Testing login in with correct login");
        String username = "casetest";
        String password = "password";
        boolean expResult = true;
        boolean result = logic.login(username, password);
        assertEquals(expResult, result);

    }
}
