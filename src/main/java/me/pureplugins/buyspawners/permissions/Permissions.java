package me.pureplugins.buyspawners.permissions;

import org.bukkit.permissions.Permission;

import java.util.HashMap;
import java.util.Map;

public class Permissions {
    private static Map<String, Permission> permissions = new HashMap<>();

    public static Permission get(String permission) {
        if (!permissions.containsKey(permission.toLowerCase())) {
            Permission perm = new Permission(permission.toLowerCase());

            permissions.put(permission.toLowerCase(), perm);
        }

        return permissions.get(permission.toLowerCase());
    }
}
