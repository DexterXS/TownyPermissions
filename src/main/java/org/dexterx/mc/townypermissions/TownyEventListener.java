package org.dexterx.mc.townypermissions;

import com.palmergames.bukkit.towny.event.TownAddResidentEvent;
import com.palmergames.bukkit.towny.event.TownRemoveResidentEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownyEventListener implements Listener {
    private final TownyPermissionManager permissionManager;

    public TownyEventListener(TownyPermissionManager permissionManager) {
        this.permissionManager = permissionManager;
    }

    @EventHandler
    public void onTownAddResident(TownAddResidentEvent event) {
        Player player = event.getResident().getPlayer();
        if (player != null) {
            permissionManager.updatePlayerPermissions(player, event.getTown().getName());
        }
    }

    @EventHandler
    public void onTownRemoveResident(TownRemoveResidentEvent event) {
        Player player = event.getResident().getPlayer();
        if (player != null) {
            permissionManager.updatePlayerPermissions(player, null);
        }
    }
}
