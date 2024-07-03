package org.dexterx.mc.townypermissions;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class TownyPermissionManager implements Listener {
    private final JavaPlugin plugin;
    private final LuckPerms luckPerms;
    private final Logger logger;
    private final Map<Player, Long> lastCheckTime;

    public TownyPermissionManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.luckPerms = LuckPermsProvider.get();
        this.logger = plugin.getLogger();
        this.lastCheckTime = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        updatePlayerPermissions(event.getPlayer());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        long currentTime = System.currentTimeMillis();
        long lastChecked = lastCheckTime.getOrDefault(player, 0L);

        // Проверяем права не чаще, чем раз в секунду
        if (currentTime - lastChecked > 1000) {
            updatePlayerPermissions(player);
            lastCheckTime.put(player, currentTime);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        lastCheckTime.remove(event.getPlayer());
    }

    private void updatePlayerPermissions(Player player) {
        Resident resident = TownyAPI.getInstance().getResident(player.getUniqueId());
        if (resident != null) {
            Town town = resident.getTownOrNull();
            if (town != null) {
                String groupName = "town_" + town.getName();
                User user = luckPerms.getUserManager().getUser(player.getUniqueId());
                if (user != null) {
                    if (!isPlayerInGroup(user, groupName)) {
                        removePlayerFromTownGroups(user);
                        Group townGroup = getOrCreateGroup(groupName);
                        if (townGroup != null) {
                            user.data().add(Node.builder("group." + groupName).build());
                            luckPerms.getUserManager().saveUser(user);
                        }
                    }
                } else {
                    logger.warning("Could not find LuckPerms user for player " + player.getName());
                }
            } else {
                logger.warning("Could not find town for resident " + resident.getName());
            }
        } else {
            logger.warning("Could not find resident for player " + player.getName());
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
