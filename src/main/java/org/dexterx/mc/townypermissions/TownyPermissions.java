package org.dexterx.mc.townypermissions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class TownyPermissions extends JavaPlugin {

    private TownyPermissionManager permissionManager;

    @Override
    public void onEnable() {
        this.permissionManager = new TownyPermissionManager(this);
        getServer().getPluginManager().registerEvents(new TownyEventListener(permissionManager), this);
        getCommand("tperm").setExecutor(this);
    }

    @Override
    public void onDisable() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("tperm")) {
            if (args.length != 3) {
                sender.sendMessage("Usage: /tperm <Town> <add|remove> <permission>");
                return false;
            }

            String townName = args[0];
            String action = args[1];
            String permission = args[2];

            try {
                if (action.equalsIgnoreCase("add")) {
                    permissionManager.addPermissionToTown(townName, permission);
                    sender.sendMessage("Permission added to town.");
                } else if (action.equalsIgnoreCase("remove")) {
                    permissionManager.removePermissionFromTown(townName, permission);
                    sender.sendMessage("Permission removed from town.");
                } else {
                    sender.sendMessage("Invalid action. Use add or remove.");
                    return false;
                }
            } catch (IOException e) {
                sender.sendMessage("An error occurred while modifying town permissions.");
                e.printStackTrace();
            }

            return true;
        }
        return false;
    }
}
