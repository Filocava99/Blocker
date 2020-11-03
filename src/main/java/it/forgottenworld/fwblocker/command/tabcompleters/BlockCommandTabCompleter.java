package it.forgottenworld.fwblocker.command.tabcompleters;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockCommandTabCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> stringList = new ArrayList<>();
        if(args.length == 1){
            stringList.addAll(Arrays.asList("item", "potion", "enchant"));
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("item") || args[0].equalsIgnoreCase("potion") || args[0].equalsIgnoreCase("enchant")){
                stringList.add("list");
            }
            if(args[0].equalsIgnoreCase("enchant")){
                stringList.addAll(getEnchantsName());
            }
        }else if(args.length == 3 && args[0].equalsIgnoreCase("enchant")){
            stringList.addAll(Arrays.asList("1","2","3","4","5"));
        }
        return stringList;
    }

    private List<String> getEnchantsName(){
        return Arrays.stream(Enchantment.values()).map(enchantment -> enchantment.getKey().getKey()).collect(Collectors.toList());
    }
}
