package org.dexterx.mc.townypermissions;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TownyPermissionManager {
    private final JavaPlugin plugin;
    private final LuckPerms luckPerms;
    private final Logger logger;

    public TownyPermissionManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.luckPerms = LuckPermsProvider.get();
        this.logger = plugin.getLogger();
    }

    public void updatePlayerPermissions(Player player, String townName) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());

        if (user == null) {
            logger.warning("Could not find LuckPerms user for player " + player.getName());
            return;
        }

        if (townName == null) {
            // Игрок не резидент никакого города
            removePlayerFromTownGroups(user);
            return;
        }

        String groupName = "town_" + townName;

        if (!isPlayerInGroup(user, groupName)) {
            removePlayerFromTownGroups(user);
            Group townGroup = getOrCreateGroup(groupName);
            if (townGroup != null) {
                user.data().add(Node.builder("group." + groupName).build());
                luckPerms.getUserManager().saveUser(user);
            }
        }
    }

    private boolean isPlayerInGroup(User user, String groupName) {
        return user.data().toCollection().stream()
                .anyMatch(node -> node.getKey().equals("group." + groupName));
    }

    private void removePlayerFromTownGroups(User user) {
        user.data().toCollection().stream()
                .filter(node -> node.getKey().startsWith("group.town_"))
                .collect(Collectors.toList())
                .forEach(user.data()::remove);
        luckPerms.getUserManager().saveUser(user);
    }

    private Group getOrCreateGroup(String groupName) {
        Group group = luckPerms.getGroupManager().getGroup(groupName);
        if (group == null) {
            luckPerms.getGroupManager().createAndLoadGroup(groupName).join();
            group = luckPerms.getGroupManager().getGroup(groupName);
            if (group != null) {
                logger.info("Created new group: " + groupName);
            } else {
                logger.warning("Failed to create group: " + groupName);
            }
        }
        return group;
    }

    public void addPermissionToTown(String townName, String permission) throws IOException {
        Group group = getOrCreateGroup("town_" + townName);
        if (group != null) {
            group.data().add(Node.builder(permission).build());
            luckPerms.getGroupManager().saveGroup(group);
            logger.info("Added permission " + permission + " to group " + group.getName());
        }
    }

    public void removePermissionFromTown(String townName, String permission) throws IOException {
        Group group = luckPerms.getGroupManager().getGroup("town_" + townName);
        if (group != null) {
            group.data().remove(Node.builder(permission).build());
            luckPerms.getGroupManager().saveGroup(group);
            logger.info("Removed permission " + permission + " from group " + group.getName());
        }
    }
}
