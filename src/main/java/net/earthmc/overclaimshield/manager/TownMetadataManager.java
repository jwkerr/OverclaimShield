package net.earthmc.overclaimshield.manager;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.LongDataField;

public class TownMetadataManager {
    private static final String hasShield = "os_hasshield";
    private static final String toggledShieldOnAt = "os_toggledshieldonat";

    public static Boolean hasOverclaimShield(Town town) {
        BooleanDataField bdf = (BooleanDataField) town.getMetadata(hasShield);
        if (bdf == null)
            return false;

        return bdf.getValue();
    }

    public static void setOverclaimShield(Town town, boolean value) {
        if (!town.hasMeta(hasShield))
            town.addMetaData(new BooleanDataField(hasShield, null));

        BooleanDataField bdf = (BooleanDataField) town.getMetadata(hasShield);
        if (bdf == null)
            return;

        bdf.setValue(value);
        town.addMetaData(bdf);
    }

    public static Long getToggledShieldOnAt(Town town) {
        LongDataField ldf = (LongDataField) town.getMetadata(toggledShieldOnAt);
        if (ldf == null)
            return null;

        return ldf.getValue();
    }

    public static void setToggledShieldOnAt(Town town, Long value) {
        if (!town.hasMeta(toggledShieldOnAt))
            town.addMetaData(new LongDataField(toggledShieldOnAt, null));

        LongDataField ldf = (LongDataField) town.getMetadata(toggledShieldOnAt);
        if (ldf == null)
            return;

        ldf.setValue(value);
        town.addMetaData(ldf);
    }
}
