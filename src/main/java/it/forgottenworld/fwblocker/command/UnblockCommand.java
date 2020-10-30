package it.forgottenworld.fwblocker.command;

import it.forgottenworld.fwblocker.FWBlocker;
import it.forgottenworld.fwblocker.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class UnblockCommand implements CommandExecutor {

    private final UnblockCommandsMethods unblockCommandsMethods;

    public UnblockCommand(FWBlocker plugin) {
        unblockCommandsMethods = new UnblockCommandsMethods(new Utils(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0){
            unblockCommandsMethods.printHelp(sender);
        }else if(args[0].equalsIgnoreCase("item")){
            if(args.length > 1){
                unblockCommandsMethods.unbanItem(sender, args);
            }else{
                unblockCommandsMethods.printHelp(sender);
            }
        }else if(args[0].equalsIgnoreCase("potion")){
            if(args.length > 2){
                unblockCommandsMethods.unbanPotion(sender, args);
            }else{
                unblockCommandsMethods.printHelp(sender);
            }
        }else if(args[0].equalsIgnoreCase("enchant")){
            unblockCommandsMethods.unbanEnchantment(sender, args);
        }else{
            unblockCommandsMethods.printHelp(sender);
        }
        return true;
    }
}
