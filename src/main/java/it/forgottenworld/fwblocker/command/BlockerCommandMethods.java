package it.forgottenworld.fwblocker.command;

import it.forgottenworld.fwblocker.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class BlockerCommandMethods {

    private final Utils utils;

    protected BlockerCommandMethods(Utils utils) {
        this.utils = utils;
    }

    protected void printHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.DARK_GREEN + "/block item:" + ChatColor.GREEN + " blocks the item that you are currently holding in your hand");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block potion:" + ChatColor.GREEN + " blocks the potion that you are currently holding in your hand");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block enchant <name> <level>:" + ChatColor.GREEN + " blocks the specified enchant");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block item list:" + ChatColor.GREEN + " interactive list of all the blocked items");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block potion list:" + ChatColor.GREEN + " interactive list of all the blocked potions");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block enchant list:" + ChatColor.GREEN + " interactive list of all the blocked enchants");
    }

    protected void printBannedItemsList(CommandSender sender) {

    }

    protected void banItem(CommandSender sender){
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if(mainHand.getType() != Material.AIR){
                utils.banItem(mainHand);
            }else{
                sender.sendMessage(ChatColor.RED + "You can't ban air!");
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Only players can run this command!");
        }
    }

    protected void banPotion(CommandSender sender){
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if(utils.isPotion(mainHand)){
                utils.banPotion(mainHand);
            }else{
                sender.sendMessage(ChatColor.RED + "You must hold a potion in the main hand to ban it!");
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Only players can run this command!");
        }
    }

}
