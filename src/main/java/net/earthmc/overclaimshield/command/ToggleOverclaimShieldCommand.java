package net.earthmc.overclaimshield.command;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyEconomyHandler;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.confirmations.Confirmation;
import com.palmergames.bukkit.towny.confirmations.ConfirmationTransaction;
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
            enableOverclaimShield(player, town);
        } else {
            disableOverclaimShield(player, town);
        }

        return false;
    }

    private void enableOverclaimShield(Player player, Town town) {
        double amountOwed = Utils.getAmountOwed(town);
        FileConfiguration config = OverclaimShield.instance.getConfig();

        Confirmation
                .runOnAccept(() -> {
                    boolean currentValue = TownMetadataManager.hasOverclaimShield(town);

                    TownMetadataManager.setOverclaimShield(town, !currentValue);
                    TownMetadataManager.setToggledShieldOnAt(town, Instant.now().getEpochSecond());

                    String newStatus = currentValue ? "disabled" : "enabled";
                    TownyMessaging.sendMsg(player, "Overclaim shield is now " + newStatus);
                })
                .setTitle("This will cost " + TownyEconomyHandler.getFormattedBalance(amountOwed) + " and will cost an additional " + TownyEconomyHandler.getFormattedBalance(config.getDouble("cost")) + " for every " + config.getInt("grouping_size") + " plots over the claim limit each Towny new day")
                .setCost(new ConfirmationTransaction(() -> amountOwed, town.getAccount(), "Cost of enabling overclaim shield"))
                .sendTo(player);
    }

    private void disableOverclaimShield(Player player, Town town) {
        boolean currentValue = TownMetadataManager.hasOverclaimShield(town);

        TownMetadataManager.setOverclaimShield(town, !currentValue);
        TownMetadataManager.setToggledShieldOnAt(town, null);

        String newStatus = currentValue ? "disabled" : "enabled";
        TownyMessaging.sendMsg(player, "Overclaim shield is now " + newStatus);
    }
}
