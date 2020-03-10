
package persistence;

import logic.LoginTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author bentw
 */


@RunWith(Suite.class)

@Suite.SuiteClasses({
   ChangePasswordTest.class,
   SaveCaseTest.class
})

public class PersistenceSuite {
    
}
