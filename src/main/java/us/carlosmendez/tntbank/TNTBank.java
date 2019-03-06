package us.carlosmendez.tntbank;

import org.bukkit.plugin.java.JavaPlugin;
import us.carlosmendez.tntbank.api.TNTBankAPI;
import us.carlosmendez.tntbank.commands.CmdTNT;
import us.carlosmendez.tntbank.lang.Lang;

public class TNTBank extends JavaPlugin {
    private static TNTBank instance;
    private TNTBankAPI tntBankAPI;

    @Override
    public void onEnable() {
        instance = this;
        tntBankAPI = new TNTBankAPI();

        registerConfig();
        registerCommands();
    }

    @Override
    public void onDisable() {
        instance = null;
    }

    public static TNTBank getInstance() {
        return instance;
    }

    public TNTBankAPI getTntBankAPI() {
        return tntBankAPI;
    }

    private void registerCommands() {
        new CmdTNT();
    }

    private void registerConfig() {
        saveDefaultConfig();
        Lang.setConfiguration(getConfig());

        for (Lang lang : Lang.values()) {
            if (getConfig().contains(lang.path)) continue;

            getConfig().set(lang.path, lang.def);
        }

        saveConfig();
    }
}