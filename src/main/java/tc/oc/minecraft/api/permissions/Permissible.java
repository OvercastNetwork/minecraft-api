package tc.oc.minecraft.api.permissions;

public interface Permissible {

    /**
     * Checks if this user has the specified permission node.
     *
     * @param permission the node to check
     * @return whether they have this node
     */
    boolean hasPermission(String permission);
}
