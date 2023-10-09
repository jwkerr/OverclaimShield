package net.earthmc.overclaimshield;

import com.palmergames.bukkit.towny.object.Town;
import org.bukkit.configuration.file.FileConfiguration;

public class Utils {
    public static double getAmountOwed(Town town) {
        FileConfiguration config = OverclaimShield.instance.getConfig();

        int townBlocksOverLimit = town.getNumTownBlocks() - town.getMaxTownBlocks();
        if (townBlocksOverLimit < 0)
            return 0;

        int payableGroups = Math.round((float) townBlocksOverLimit / config.getInt("grouping_size"));

        if (payableGroups < 1)
            payableGroups = 1;

        return payableGroups * config.getDouble("cost");
    }
}
