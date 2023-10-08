package net.earthmc.overclaimshield;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import net.earthmc.overclaimshield.command.ToggleOverclaimShieldCommand;
import net.earthmc.overclaimshield.listener.NewDayListener;
import net.earthmc.overclaimshield.listener.TownPreClaimListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class OverclaimShield extends JavaPlugin {
    public static JavaPlugin instance;

    @Override
    public void onEnable() {
        instance = this;

        getServer().getPluginManager().registerEvents(new TownPreClaimListener(), this);
        getServer().getPluginManager().registerEvents(new NewDayListener(), this);

        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN_TOGGLE, "overclaimshield", new ToggleOverclaimShieldCommand());

        getLogger().info("OverclaimShield initialised");
    }

    @Override
    public void onDisable() {
        getLogger().info("OverclaimShield disabled");
    }
}
