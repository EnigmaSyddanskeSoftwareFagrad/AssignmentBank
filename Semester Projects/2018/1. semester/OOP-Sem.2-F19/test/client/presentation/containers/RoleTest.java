/*This is code written by Frederik Alexander Hounsvad
 * The use of this code in a non commercial and non exam environment is permitted
 */
package client.presentation.containers;

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Sanitas Solutions
 */
public class RoleTest {

    public RoleTest() {
    }

    /**
     * Test of getRoleDescription method, of class Role.
     */
    @Test
    public void testGetRoleDescription() {
        Role testInstance = new Role("001-001", "roleDescription");
        Assert.assertEquals("roleDescription", testInstance.getRoleDescription());
    }

    /**
     * Test of getRoleId method, of class Role.
     */
    @Test
    public void testGetRoleId() {
        Role testInstance = new Role("001-001", "roleDescription");
        Assert.assertEquals("001-001", testInstance.getRoleId());
    }

    /**
     * Test of toString method, of class Role.
     */
    @Test
    public void testToString() {
        Role testInstance = new Role("001-001", "roleDescription");
        Assert.assertEquals("001-001\nroleDescription", testInstance.toString());
    }

    /**
     * Test of hashCode method, of class Role.
     */
    @Test
    public void testHashCode() {
        Role testInstance1 = new Role("001-002", "roleDescription");
        Assert.assertEquals(1071344015l, 5l, (long) testInstance1.hashCode());
    }

    /**
     * Test of equals method, of class Role.
     */
    @Test
    public void testEquals() {
        Role testInstance1 = new Role("001-002", "roleDescription");
        Role testInstance2 = new Role("002-001", "roleDescription");
        Role testInstance3 = new Role("002-001", "roleDescription");

        Assert.assertTrue("001-002 != 002-001", !testInstance1.equals(testInstance2));
        Assert.assertTrue("001-002 == 001-002", testInstance1.equals(testInstance1));
        Assert.assertTrue("002-001 == 002-001", testInstance2.equals(testInstance3));
    }

    /**
     * Test of compareTo method, of class Role.
     */
    @Test
    public void testCompareTo() {
        Role testInstance1 = new Role("001-002", "roleDescription");
        Role testInstance2 = new Role("002-001", "roleDescription");
        Role testInstance3 = new Role("001-004", "roleDescription");

        Assert.assertTrue("001-002 < 002-001", testInstance1.compareTo(testInstance2) < 0);
        Assert.assertTrue("001-002 == 001-002", testInstance1.compareTo(testInstance1) == 0);
        Assert.assertTrue("002-001 > 001-002", testInstance2.compareTo(testInstance1) > 0);
        Assert.assertTrue("002-001 > 001-004", testInstance2.compareTo(testInstance3) > 0);
    }

}
