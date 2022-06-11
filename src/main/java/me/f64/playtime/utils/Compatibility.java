package me.f64.playtime.utils;

import com.google.common.primitives.Ints;
import org.bukkit.Bukkit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Compatibility {
    private static final int V1_11 = 1110;
    private static final int V1_12 = 1120;
    private static final int V1_13 = 1130;
    private static final int V1_14 = 1140;
    private static final int V1_16_5 = 1165;
    private static final int CURRENT_VERSION = getCurrentVersion();
    private static final boolean IS_PAPER = checkPaper();
    public static final boolean IS_COMPONENT_LEGACY = !IS_PAPER || CURRENT_VERSION < V1_16_5;
    public static final boolean IS_LEGACY = CURRENT_VERSION <= V1_13;
    public static final boolean IS_ITEM_LEGACY = CURRENT_VERSION < V1_13;
    public static final boolean IS_UNBREAKABLE_LEGACY = CURRENT_VERSION < V1_11;
    public static final boolean IS_PDC_VERSION = CURRENT_VERSION >= V1_14;
    public static final boolean IS_SKULL_OWNER_LEGACY = CURRENT_VERSION < V1_12;
    public static final boolean IS_CUSTOM_MODEL_DATA = CURRENT_VERSION >= V1_14;

    private static boolean checkPaper() {
        try {
            Class.forName("com.destroystokyo.paper.PaperConfig");
            return true;
        } catch (ClassNotFoundException ignored) {
            return false;
        }
    }

    private static int getCurrentVersion() {
        final Matcher matcher = Pattern.compile("(?<version>\\d+\\.\\d+)(?<patch>\\.\\d+)?")
                .matcher(Bukkit.getBukkitVersion());
        final StringBuilder stringBuilder = new StringBuilder();
        if (matcher.find()) {
            stringBuilder.append(matcher.group("version").replace(".", ""));
            final String patch = matcher.group("patch");
            if (patch == null)
                stringBuilder.append("0");
            else
                stringBuilder.append(patch.replace(".", ""));
        }
        Integer version = Ints.tryParse(stringBuilder.toString());
        if (version == null)
            throw new GuiException("Could not retrieve server version!");
        return version;
    }
}
