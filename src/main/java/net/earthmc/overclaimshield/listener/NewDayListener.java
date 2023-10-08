package net.earthmc.overclaimshield.listener;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.NewDayEvent;
import com.palmergames.bukkit.towny.object.Town;
import net.earthmc.overclaimshield.Utils;
import net.earthmc.overclaimshield.manager.TownMetadataManager;
import org.bukkit.event.Listener;

import java.time.Instant;

public class NewDayListener implements Listener {
    public void onNewDay(NewDayEvent event) {
        for (Town town : TownyAPI.getInstance().getTowns()) {
            if (!TownMetadataManager.hasOverclaimShield(town))
                continue;

            Long toggledShieldOnAt = TownMetadataManager.getToggledShieldOnAt(town);
            if (toggledShieldOnAt - Instant.now().getEpochSecond() > 86400)
                continue;

            if (!town.isOverClaimed())
                continue;

            double amountOwed = Utils.getAmountOwed(town);

            if (town.getAccount().getHoldingBalance() >= amountOwed) {
                town.getAccount().withdraw(amountOwed, "Overclaim shield cost");
            } else {
                TownMetadataManager.setOverclaimShield(town, false);
                TownMetadataManager.setToggledShieldOnAt(town, null);
            }
        }
    }
}