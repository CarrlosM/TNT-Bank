package us.carlosmendez.tntbank.lang;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Map;

public enum Lang {
    ERR_NO_FACTION("Messages.Err-No-Faction", "&c&lTNT Bank &8\u00BB &7You must be in a faction to use this command"),
    ERR_NOT_MOD("Messages.Err-Not-Mod", "&c&lTNT Bank &8\u00BB &7You must be a mod in your faction to use this command"),
    ERR_NOT_PLAYER("Messages.Err-Not-Player", "&c&lTNT Bank &8\u00BB &7You must be a player to use this command"),

    TNT_BALANCE("Messages.TNT-Balance", "&c&lTNT Bank &8\u00BB &7Your faction has &d%balance% TNT&7!"),
    TNT_DEPOSITED("Messages.TNT-Withdrawn", "&c&lTNT Bank &8\u00BB &7You have deposited &d%amount% TNT&7!"),
    TNT_WITHDRAWN("Messages.TNT-Withdrawn", "&c&lTNT Bank &8\u00BB &7You have withdrawn &d%amount% TNT&7!");

    public String path;
    public String def;
    private static FileConfiguration configuration;

    Lang(String path, String def) {
        this.path = path;
        this.def = def;
    }

    public static void setConfiguration(FileConfiguration fileConfiguration) {
        Lang.configuration = fileConfiguration;
    }

    @Override
    public String toString() {
        return colorize(configuration.getString(path, def));
    }

    public String toString(Map<String, String> replacements) {
        String message = toString();

        for (String key : replacements.keySet()) {
            message = message.replace(key, replacements.get(key));
        }

        return message;
    }

    private String colorize(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}