package net.earthmc.overclaimshield.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import net.earthmc.overclaimshield.OverclaimShield;
import net.earthmc.overclaimshield.Utils;
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
        if (!(sender instanceof Player)) {
            TownyMessaging.sendErrorMsg(sender, "Only players can use this command");
            return true;
        }

        Player player = (Player) sender;
        Resident resident = TownyAPI.getInstance().getResident(player);
        if (resident == null)
            return true;

        Town town = resident.getTownOrNull();
        if (town == null) {
            TownyMessaging.sendErrorMsg(player, "You are not part of a town");
            return true;
        }

        if (!player.hasPermission("overclaimshield.command.toggleoverclaimshield")) {
            TownyMessaging.sendErrorMsg(player, "You do not have permission to buy an overclaim shield");
            return true;
        }

        boolean isShieldEnabled = TownMetadataManager.hasOverclaimShield(town);
        if (!isShieldEnabled) {
            if (!enableOverclaimShield(player, town))
                return false;
        } else {
            disableOverclaimShield(town);
        }

        String newStatus = isShieldEnabled ? "disabled" : "enabled";
        TownyMessaging.sendMsg(player, "Overclaim shield is now " + newStatus);

        return false;
    }

    private boolean enableOverclaimShield(Player player, Town town) {
        double amountOwed = Utils.getAmountOwed(town);
        FileConfiguration config = OverclaimShield.instance.getConfig();
        AtomicBoolean success = new AtomicBoolean(true);

        Confirmation
                .runOnAccept(() -> {
                    TownMetadataManager.setOverclaimShield(town, true);
                    TownMetadataManager.setToggledShieldOnAt(town, Instant.now().getEpochSecond());

                    double amountOwedOnAccept = Utils.getAmountOwed(town);
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
                    }
                })
                .setTitle("This will cost " +
                        TownyEconomyHandler.getFormattedBalance(amountOwed) +
                        " and will cost an additional " +
                        TownyEconomyHandler.getFormattedBalance(config.getDouble("cost")) +
                        " for every " +
                        config.getInt("grouping_size") +
                        " plots over the claim limit each Towny new day")
                .sendTo(player);

        return success.get();
    }

    private void disableOverclaimShield(Town town) {
        TownMetadataManager.setOverclaimShield(town, false);
        TownMetadataManager.setToggledShieldOnAt(town, null);
    }
}
