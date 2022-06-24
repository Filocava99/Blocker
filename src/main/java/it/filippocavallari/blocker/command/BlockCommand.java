package it.filippocavallari.blocker.command;

import it.filippocavallari.blocker.util.Utils;
import it.filippocavallari.blocker.Blocker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BlockCommand implements CommandExecutor {

    private final BlockCommandMethods blockCommandMethods;

    public BlockCommand(Blocker instance) {
        blockCommandMethods = new BlockCommandMethods(new Utils(instance));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            blockCommandMethods.printHelp(sender);
        } else if (args[0].equalsIgnoreCase("item")) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("list")) {
                    blockCommandMethods.printBannedItemsList(sender, args);
                } else {
                    blockCommandMethods.printHelp(sender);
                }
            } else {
                blockCommandMethods.banItem(sender);
            }
        }else if(args[0].equalsIgnoreCase("potion")){
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("list")) {
                    blockCommandMethods.printBannedPotionsList(sender,args);
                } else {
                    blockCommandMethods.printHelp(sender);
                }
            } else {
                blockCommandMethods.banPotion(sender);
            }
        }else if(args[0].equalsIgnoreCase("enchant")){
            if(args.length > 1){
                if(args[1].equalsIgnoreCase("list")){
                    blockCommandMethods.printBannedEnchantmentsList(sender, args);
                }else if(args.length > 2){
                    blockCommandMethods.banEnchantment(sender, args);
                }else{
                    blockCommandMethods.printHelp(sender);
                }
            }else{
                blockCommandMethods.printHelp(sender);
            }
        }
        return true;
    }
}