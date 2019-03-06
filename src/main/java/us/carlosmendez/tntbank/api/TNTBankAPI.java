package us.carlosmendez.tntbank.api;

import com.massivecraft.factions.Faction;
import us.carlosmendez.tntbank.TNTBank;

public class TNTBankAPI {
    private TNTBank plugin = TNTBank.getInstance();

    public int getBalance(Faction faction) {
        registerFaction(faction);

        return plugin.getConfig().getInt("Factions." + faction.getId(), 0);
    }

    private void registerFaction(Faction faction) {
        if (plugin.getConfig().contains("Factions." + faction.getId())) return;

        setBalance(faction, 0);
    }

    public void setBalance(Faction faction, int amount) {
        plugin.getConfig().set("Factions." + faction.getId(), amount);
        plugin.saveConfig();
    }
}