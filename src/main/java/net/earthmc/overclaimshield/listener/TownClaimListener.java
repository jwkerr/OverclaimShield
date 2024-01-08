package net.earthmc.overclaimshield.listener;

import com.palmergames.bukkit.towny.event.TownClaimEvent;
import com.palmergames.bukkit.towny.object.Town;
import net.earthmc.overclaimshield.manager.TownMetadataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownClaimListener implements Listener {

    @EventHandler
    public void onTownClaim(TownClaimEvent event) {
        if (!event.isOverClaim())
            return;

        Town overclaimingTown = event.getTown();
        int overclaimsRemainingToday = TownMetadataManager.getOverclaimsRemainingToday(overclaimingTown);
        TownMetadataManager.setOverclaimsRemainingToday(overclaimingTown, overclaimsRemainingToday - 1);
    }
}
