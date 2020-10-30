package it.forgottenworld.fwblocker.command;

import it.forgottenworld.fwblocker.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

public class UnblockCommandsMethods {

    private final Utils utils;

    public UnblockCommandsMethods(Utils utils) {
        this.utils = utils;
    }

    public void printHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_RED + "---------{ FWBlocker }---------");
        sender.sendMessage(ChatColor.DARK_GREEN + "/unblock item <itemName>:" + ChatColor.GREEN + " unblock the specified item");
        sender.sendMessage(ChatColor.DARK_GREEN + "/unblock potion <name> <type>:" + ChatColor.GREEN + " unblock the specified potion");
        sender.sendMessage(ChatColor.DARK_GREEN + "/unblock enchant <name> <level>:" + ChatColor.GREEN + " unblock the specified enchant");
    }

    public void unbanItem(CommandSender sender, String[] args) {
        Material material = Material.getMaterial(args[1]);
        if (material != null) {
            utils.unbanItem(new ItemStack(material));
            sender.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.GREEN + " has been unbanned");
        } else {
            sender.sendMessage(ChatColor.RED + "Invalid item name");
        }
    }

    public void unbanPotion(CommandSender sender, String[] args) {
        try {
            PotionType potionEffectType = PotionType.valueOf(args[1]);
            String version = args[2];
            utils.unbanPotion(potionEffectType, version);
            sender.sendMessage(ChatColor.YELLOW + args[1] + " " + args[2] + ChatColor.GREEN + " has been unbanned");
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + "Invalid potion type");
        }
    }

    public void unbanEnchantment(CommandSender sender, String[] args){
        if(args.length > 1){
            try{
                Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[1]));
                utils.unbanEnchant(enchantment);
                sender.sendMessage(ChatColor.YELLOW + args[1] + ChatColor.GREEN + " has been unbanned");
            }catch (IllegalArgumentException e){
                sender.sendMessage(ChatColor.RED + "Invalid enchantment name");
            }
        }else{
            printHelp(sender);
        }
    }
}
