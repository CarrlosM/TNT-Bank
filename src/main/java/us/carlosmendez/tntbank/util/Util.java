package us.carlosmendez.tntbank.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util {

    public static boolean isInt(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static int getTNTCount(Player player) {
        int count = 0;

        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() != Material.TNT) continue;

            count += itemStack.getAmount();
        }

        return count;
    }

    public static void subtractTNT(Player player, int amount) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack == null || itemStack.getType() != Material.TNT) continue;
            int itemAmount = itemStack.getAmount();

            if (amount > itemAmount) {
                itemStack.setAmount(itemAmount - amount);
                break;
            }

            amount -= itemAmount;
            itemStack.setType(Material.AIR);
        }

        player.updateInventory();
    }
}