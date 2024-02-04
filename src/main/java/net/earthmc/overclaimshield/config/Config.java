package net.earthmc.overclaimshield.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;

public class Config {

    public static void init(FileConfiguration config) {
        config.options().setHeader(Collections.singletonList("OverclaimShield"));

        config.addDefault("cost", 1.0); config.setInlineComments("cost", Collections.singletonList("Daily upkeep price per townblock grouping size over the limit"));
        config.addDefault("grouping_size", 8); config.setInlineComments("grouping_size", Collections.singletonList("How many townblocks to group when calculating upkeep costs"));
        config.addDefault("overclaims_per_day", 8); config.setInlineComments("overclaims_per_day", Collections.singletonList("The amount of townblocks a town can overclaim in a day, resets at new day"));

        config.options().copyDefaults(true);
    }
}
