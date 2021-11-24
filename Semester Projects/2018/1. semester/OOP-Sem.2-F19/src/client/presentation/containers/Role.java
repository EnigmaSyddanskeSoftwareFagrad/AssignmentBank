/*
 * Developed by SI2-PRO Group 3
 * Frederik Alexander Hounsvad, Oliver Lind Nordestgaard, Patrick Nielsen, Jacob Kirketerp Andersen, Nadin Fariss
 */
package client.presentation.containers;

import java.util.Objects;

/**
 *
 * @author Sanitas Solutions
 */
public class Role implements Comparable {

    private String roleId;

    private String roleDescription;

    /**
     *
     * @param roleId          is the unique id of the role
     * @param roleDescription is the description associated with the role
     */
    public Role(String roleId, String roleDescription) {
        this.roleId = roleId;
        this.roleDescription = roleDescription;
    }

    /**
     * Get the value of roleDescription
     *
     * @return the value of roleDescription
     */
    public String getRoleDescription() {
        return roleDescription;
    }

    /**
     * Get the value of roleId
     *
     * @return the value of roleId
     */
    public String getRoleId() {
        return roleId;
    }

    @Override
    public String toString() {
        return roleId + "\n" + roleDescription;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 83 * hash + Objects.hashCode(this.roleId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Role other = (Role) obj;
        return this.roleId.equals(other.roleId);

    }

    @Override
    public int compareTo(Object o) {

        String[] oTokens = ((Role) o).getRoleId().split("-");
        String[] tTokens = this.getRoleId().split("-");
        int[] oTokensInt = new int[oTokens.length];
        int[] tTokensInt = new int[tTokens.length];
        for (int i = 0; i < oTokens.length; i++) {
            oTokensInt[i] = Integer.parseInt(oTokens[i]);
            tTokensInt[i] = Integer.parseInt(tTokens[i]);
        }
        if (oTokensInt[0] == tTokensInt[0]) {
            return tTokensInt[1] - oTokensInt[1];
        }
        return tTokensInt[0] - oTokensInt[0];
    }

}
