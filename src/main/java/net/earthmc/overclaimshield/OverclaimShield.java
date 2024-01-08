package net.earthmc.overclaimshield;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import net.earthmc.overclaimshield.command.ToggleOverclaimShieldCommand;
import net.earthmc.overclaimshield.config.Config;
import net.earthmc.overclaimshield.listener.NewDayListener;
import net.earthmc.overclaimshield.listener.TownClaimListener;
import net.earthmc.overclaimshield.listener.TownPreClaimListener;
import net.earthmc.overclaimshield.listener.TownStatusScreenListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class OverclaimShield extends JavaPlugin {
    public static OverclaimShield INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        Config.init(getConfig());
        saveConfig();

        registerListeners();
        registerCommands();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new NewDayListener(), this);
        getServer().getPluginManager().registerEvents(new TownPreClaimListener(), this);
        getServer().getPluginManager().registerEvents(new TownClaimListener(), this);
        getServer().getPluginManager().registerEvents(new TownStatusScreenListener(), this);
    }

    private void registerCommands() {
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN_TOGGLE, "overclaimshield", new ToggleOverclaimShieldCommand());
    }
}
