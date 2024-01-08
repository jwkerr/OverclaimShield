package net.earthmc.overclaimshield.listener;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.event.TownPreClaimEvent;
import com.palmergames.bukkit.towny.object.Town;
import net.earthmc.overclaimshield.manager.TownMetadataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownPreClaimListener implements Listener {

    @EventHandler
    public void onTownPreClaim(TownPreClaimEvent event) {
        if (!event.isOverClaim())
            return;

        Town overclaimingTown = event.getTown();
        int overclaimsRemainingToday = TownMetadataManager.getOverclaimsRemainingToday(overclaimingTown);
        if (overclaimsRemainingToday <= 0) {
            event.setCancelled(true);
            TownyMessaging.sendErrorMsg(event.getPlayer(), "Could not overclaim this plot as your town has no remaining overclaims today");
            return;
        }

        Town townGettingOverclaimed = event.getTownBlock().getTownOrNull();
        if (townGettingOverclaimed == null)
            return;

        if (TownMetadataManager.hasOverclaimShield(townGettingOverclaimed)) {
            event.setCancelled(true);
            TownyMessaging.sendErrorMsg(event.getPlayer(), "Could not overclaim this plot as the town has purchased an overclaim shield");
        }
    }
}
