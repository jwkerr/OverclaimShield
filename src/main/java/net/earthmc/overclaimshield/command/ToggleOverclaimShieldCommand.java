package net.earthmc.overclaimshield.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import net.earthmc.overclaimshield.OverclaimShield;
import net.earthmc.overclaimshield.util.Util;
import net.earthmc.overclaimshield.manager.TownMetadataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicBoolean;

public class ToggleOverclaimShieldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players can use this command");
            return true;
        }

        Resident resident = TownyAPI.getInstance().getResident(player);
        if (resident == null)
            return true;

        Town town = resident.getTownOrNull();
        if (town == null) {
            TownyMessaging.sendErrorMsg(player, "You are not part of a town");
            return true;
        }

        if (!player.hasPermission("overclaimshield.command.toggle.overclaimshield")) {
            TownyMessaging.sendErrorMsg(player, "You do not have permission to buy an overclaim shield");
            return true;
        }

        boolean isShieldEnabled = TownMetadataManager.hasOverclaimShield(town);
        if (!isShieldEnabled) {
            if (!enableOverclaimShield(player, town))
                return false;
        } else {
            disableOverclaimShield(player, town);
        }

        return false;
    }

    private boolean enableOverclaimShield(Player player, Town town) {
        double amountOwed = Util.getAmountOwed(town);
        FileConfiguration config = OverclaimShield.instance.getConfig();
        AtomicBoolean success = new AtomicBoolean(true);

        Confirmation
                .runOnAccept(() -> {
                    double amountOwedOnAccept = Util.getAmountOwed(town);
                    if (amountOwed != amountOwedOnAccept) {
                        TownyMessaging.sendErrorMsg(player, "Town size changed, could not enable overclaim shield");

                        success.set(false);
                        return;
                    }

                    if (town.getAccount().getHoldingBalance() < amountOwed) {
                        TownyMessaging.sendErrorMsg(player, "Your town has insufficient funds to enable overclaim shield");

                        success.set(false);
                    } else {
                        town.getAccount().withdraw(amountOwed, "Payment to enable overclaim shield");

                        TownMetadataManager.setOverclaimShield(town, true);
                        TownMetadataManager.setToggledShieldOnAt(town, Instant.now().getEpochSecond());

                        TownyMessaging.sendMsg(player, "Overclaim shield is now enabled");
                    }
                })
                .setTitle("This will cost " + TownyEconomyHandler.getFormattedBalance(amountOwed) + " and will cost an additional " +
                        TownyEconomyHandler.getFormattedBalance(config.getDouble("cost")) + " for every " + config.getInt("grouping_size") +
                        " plots over the claim limit each Towny new day")
                .sendTo(player);

        return success.get();
    }

    private void disableOverclaimShield(Player player, Town town) {
        TownMetadataManager.setOverclaimShield(town, false);
        TownMetadataManager.setToggledShieldOnAt(town, null);

        TownyMessaging.sendMsg(player, "Overclaim shield is now disabled");
    }
}
