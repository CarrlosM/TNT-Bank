package us.carlosmendez.tntbank.commands;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.Role;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.carlosmendez.tntbank.TNTBank;
import us.carlosmendez.tntbank.api.TNTBankAPI;
import us.carlosmendez.tntbank.lang.Lang;
import us.carlosmendez.tntbank.util.Util;

import java.util.HashMap;
import java.util.Map;

public class CmdTNT implements CommandExecutor {
    private TNTBankAPI bankAPI = TNTBank.getInstance().getTntBankAPI();

    public CmdTNT() {
        TNTBank.getInstance().getCommand("tnt").setExecutor(this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Lang.ERR_NOT_PLAYER.toString());
            return true;
        }
        Player player = (Player) sender;
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction faction = fPlayer.getFaction();

        if (faction.isSafeZone() || faction.isWarZone() || faction.isWilderness()) {
            player.sendMessage(Lang.ERR_NO_FACTION.toString());
            return true;
        }

        if (args.length == 0) {
            Map<String, String> replacements = new HashMap<>();

            replacements.put("%balance%", Integer.toString(bankAPI.getBalance(faction)));
            player.sendMessage(Lang.TNT_BALANCE.toString(replacements));
            return true;
        }

        if (args[0].equalsIgnoreCase("withdraw")) {
            if (fPlayer.getRole() != Role.ADMIN && fPlayer.getRole() != Role.COLEADER && fPlayer.getRole() != Role.MODERATOR) {
                player.sendMessage(Lang.ERR_NOT_MOD.toString());
                return true;
            }
            int balance = bankAPI.getBalance(faction);
            int amount = balance;

            if (args.length > 1 && Util.isInt(args[1]) && Integer.parseInt(args[1]) <= amount) {
                amount = Integer.parseInt(args[1]);
            }
            int stacks = amount / 64;
            int remainder = amount % 64;
            ItemStack tntStack = new ItemStack(Material.TNT, 64);

            for (int i = 0; i < stacks; i++) {
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItem(player.getLocation(), tntStack);
                } else {
                    player.getInventory().addItem(tntStack);
                }
            }

            if (remainder > 0) {
                tntStack.setAmount(remainder);
                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItem(player.getLocation(), tntStack);
                } else {
                    player.getInventory().addItem(tntStack);
                }
            }
            Map<String, String> replacements = new HashMap<>();

            replacements.put("%amount%", Integer.toString(balance - amount));

            bankAPI.setBalance(faction, balance - amount);
            player.sendMessage(Lang.TNT_WITHDRAWN.toString(replacements));
            return true;
        }

        if (args[0].equalsIgnoreCase("deposit")) {
            int balance = bankAPI.getBalance(faction);
            int tntCount = Util.getTNTCount(player);
            int amount = tntCount;

            if (args.length > 1 && Util.isInt(args[1]) && Integer.parseInt(args[1]) <= tntCount) {
                amount = Integer.parseInt(args[1]);
            }
            Map<String, String> replacements = new HashMap<>();

            replacements.put("%amount%", Integer.toString(balance - amount));
            Util.subtractTNT(player, amount);
            bankAPI.setBalance(faction, balance - amount);
            player.sendMessage(Lang.TNT_DEPOSITED.toString(replacements));
            return true;
        }
        Map<String, String> replacements = new HashMap<>();

        replacements.put("%balance%", Integer.toString(bankAPI.getBalance(faction)));
        player.sendMessage(Lang.TNT_BALANCE.toString(replacements));
        return true;
    }
}