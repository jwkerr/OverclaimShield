package net.earthmc.overclaimshield.manager;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.object.metadata.LongDataField;
import net.earthmc.overclaimshield.OverclaimShield;

public class TownMetadataManager {
    private static final String hasShield = "os_hasShield";
    private static final String toggledShieldOnAt = "os_toggledShieldOnAt";
    private static final String overclaimsRemainingToday = "os_overclaimsRemainingToday";

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

    public static int getOverclaimsRemainingToday(Town town) {
        IntegerDataField idf = (IntegerDataField) town.getMetadata(overclaimsRemainingToday);
        if (idf == null) {
            int configuredOverclaims = OverclaimShield.INSTANCE.getConfig().getInt("overclaims_per_day");
            setOverclaimsRemainingToday(town, configuredOverclaims);
            return configuredOverclaims;
        }

        return idf.getValue();
    }

    public static void setOverclaimsRemainingToday(Town town, int value) {
        if (!town.hasMeta(overclaimsRemainingToday))
            town.addMetaData(new IntegerDataField(overclaimsRemainingToday, null));

        IntegerDataField idf = (IntegerDataField) town.getMetadata(overclaimsRemainingToday);
        if (idf == null)
            return;

        idf.setValue(value);
        town.addMetaData(idf);
    }
}
