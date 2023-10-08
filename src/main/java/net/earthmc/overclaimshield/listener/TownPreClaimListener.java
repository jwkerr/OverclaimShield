package net.earthmc.overclaimshield.listener;

import com.palmergames.bukkit.towny.event.TownPreClaimEvent;
import com.palmergames.bukkit.towny.object.Town;
import net.earthmc.overclaimshield.manager.TownMetadataManager;
import org.bukkit.event.Listener;

public class TownPreClaimListener implements Listener {
    public onTownPreClaim(TownPreClaimEvent event) {
        if (!event.isOverClaim())
            return;

        Town town = event.getTown();
        if (TownMetadataManager.hasOverclaimShield(town)) {
            event.setCancelled(true);
            event.setCancelMessage("Could not overclaim this plot as the town has purchased an overclaim shield");
        }
    }
}
