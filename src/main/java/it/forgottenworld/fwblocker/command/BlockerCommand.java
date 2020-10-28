package it.forgottenworld.fwblocker.command;

import it.forgottenworld.fwblocker.FWBlocker;
import it.forgottenworld.fwblocker.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class BlockerCommand implements CommandExecutor {

    private final BlockerCommandMethods blockerCommandMethods;

    public BlockerCommand(FWBlocker instance) {
        blockerCommandMethods = new BlockerCommandMethods(new Utils(instance));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            blockerCommandMethods.printHelp(sender);
        } else if (args[0].equalsIgnoreCase("item")) {
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("list")) {
                    blockerCommandMethods.printBannedItemsList(sender);
                } else {
                    blockerCommandMethods.printHelp(sender);
                }
            } else {
                blockerCommandMethods.banItem(sender);
            }
        }else if(args[0].equalsIgnoreCase("potion")){
            if (args.length > 1) {
                if (args[1].equalsIgnoreCase("list")) {
                    blockerCommandMethods.printBannedPotionsList(sender);
                } else {
                    blockerCommandMethods.printHelp(sender);
                }
            } else {
                blockerCommandMethods.banPotion(sender);
            }
        }else if(args[0].equalsIgnoreCase("enchant")){
            if(args.length > 1){
                if(args[1].equalsIgnoreCase("list")){
                    blockerCommandMethods.printBannedEnchantmentsList(sender);
                }else if(args.length > 2){
                    blockerCommandMethods.banEnchantment(sender, args);
                }else{
                    blockerCommandMethods.printHelp(sender);
                }
            }else{
                blockerCommandMethods.printHelp(sender);
            }
        }
        return true;
    }
}