package com.gmail.llmdlio.townyflight.listeners;

import io.canvasmc.canvas.event.EntityPostTeleportAsyncEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import com.gmail.llmdlio.townyflight.TownyFlightAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Resident;

public class PlayerTeleportListener implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	private void playerTeleports(EntityPostTeleportAsyncEvent event) {
        if (!(event.getEntity() instanceof Player player))
            return;

		if (!aTeleportCauseThatMatters(event.getCause())) {
            return;
        }

		if (player.hasPermission("townyflight.bypass") 
				|| !player.getAllowFlight()
				|| flightAllowedDestination(player, event.getTo())) {
			return;
		}

		TownyFlightAPI.getInstance().removeFlight(player, false, true, "");
	}

	private boolean aTeleportCauseThatMatters(TeleportCause teleportCause) {
		return teleportCause == TeleportCause.PLUGIN || teleportCause == TeleportCause.COMMAND ||
				teleportCause == TeleportCause.ENDER_PEARL || teleportCause == TeleportCause.CONSUMABLE_EFFECT; // TODO: change over when 1.21.4 and older support is dropped.
	}

	private boolean flightAllowedDestination(Player player, Location loc) {
		Resident resident = TownyUniverse.getInstance().getResident(player.getUniqueId());
		return resident != null && TownyFlightAPI.allowedLocation(player, loc, resident);
	}

}
