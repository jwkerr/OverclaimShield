package net.earthmc.overclaimshield.listener;

import com.palmergames.adventure.text.Component;
import com.palmergames.adventure.text.format.NamedTextColor;
import com.palmergames.bukkit.towny.event.statusscreen.TownStatusScreenEvent;
import net.earthmc.overclaimshield.manager.TownMetadataManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TownStatusScreenListener implements Listener {

    @EventHandler
    public void onTownStatusScreen(TownStatusScreenEvent event) {
        if (!TownMetadataManager.hasOverclaimShield(event.getTown()))
            return;

        Component component = Component.empty()
                .append(Component.text("[", NamedTextColor.GRAY))
                .append(Component.text("Overclaim Shield", NamedTextColor.GREEN))
                .append(Component.text("]", NamedTextColor.GRAY))
                .hoverEvent(Component.text("This town has purchased an overclaim shield\nIts town blocks cannot be overclaimed", NamedTextColor.DARK_GREEN));

        event.getStatusScreen().addComponentOf("overclaimShield_status", component);
    }
}
