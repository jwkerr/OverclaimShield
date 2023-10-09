package net.earthmc.overclaimshield.listener;

import com.palmergames.bukkit.towny.event.TownPreClaimEvent;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.TownBlock;
import net.earthmc.overclaimshield.manager.TownMetadataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownPreClaimListener implements Listener {
    @EventHandler
    public void onTownPreClaim(TownPreClaimEvent event) {
        if (!event.isOverClaim())
            return;

        TownBlock townBlock = event.getTownBlock();
        Town town = townBlock.getTownOrNull();

        if (town == null)
            return;

        if (TownMetadataManager.hasOverclaimShield(town)) {
            event.setCancelled(true);
            event.setCancelMessage("Could not overclaim this plot as the town has purchased an overclaim shield");
        }
    }
}
