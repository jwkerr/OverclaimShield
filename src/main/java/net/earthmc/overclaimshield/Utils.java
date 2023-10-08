package net.earthmc.overclaimshield;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.file.FileConfiguration;

public class Utils {
    public static double getAmountOwed(Town town) {
        FileConfiguration config = OverclaimShield.instance.getConfig();

        int townBlocksOverLimit = town.getNumTownBlocks() - town.getMaxTownBlocks();
        int payableGroups = Math.round((float) townBlocksOverLimit / config.getInt("grouping_size"));

        return payableGroups * config.getDouble("cost");
    }
}
