package persistence;

import acquaintance.ILogic;
import acquaintance.IPersistence;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import logic.Case;
import logic.LogicFacade;
import static org.junit.Assert.assertSame;
import org.junit.Before;
import org.junit.Test;

/**
 * A test class for the saving of cases to persistent storage.
 * @author Bent Wilhelmsen
 */
public class SaveCaseTest {

    ILogic logic = new LogicFacade();
    IPersistence persistence = new PersistenceFacade();

    /**
     * Logs into the system and retrieves cases to test on.
     */
    @Before
    public void before() {
        logic.injectPersistence(persistence);
    }
    
     /**
     * Test of saveCase method, of class PersistenceFacade.
     */
    @Test
    public void testSaveCase() {
        logic.login("casetest", "password");

        ArrayList<Case> cases = logic.getCases();
        Case changed = cases.get(0);
        UUID testuuid = changed.getCaseID();
        long testcpr = changed.getCprNumber();
        String testType = changed.getType();
        String testMainBody = changed.getMainBody() + " Save this";
        LocalDate testcreated = changed.getDateCreated();
        LocalDate testClosed = changed.getDateClosed();
        int testdepartmentID = changed.getDepartmentID();
        String testInquiry = changed.getInquiry();


        boolean result = persistence.saveCase(testuuid, testcpr, testType, testMainBody, testcreated, testClosed, testdepartmentID, testInquiry);
        boolean expectedResult = true;
        System.out.println("Testing save Case");
        assertSame(result, expectedResult);
    }
    
}
