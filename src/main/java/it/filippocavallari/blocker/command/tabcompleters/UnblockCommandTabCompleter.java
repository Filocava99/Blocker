package it.filippocavallari.blocker.command.tabcompleters;

import it.filippocavallari.blocker.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UnblockCommandTabCompleter implements TabCompleter {

    private final Utils utils;

    public UnblockCommandTabCompleter(Utils utils) {
        this.utils = utils;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        List<String> stringList = new ArrayList<>();
        if(args.length == 1){
            stringList.addAll(Arrays.asList("item", "potion", "enchant"));
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("item")){
                stringList.addAll(utils.getBannedItems());
            }else if(args[0].equalsIgnoreCase("potion")){
                stringList.addAll(utils.getBannedPotions().stream().map(s -> s.split("-")[0]).collect(Collectors.toList()));
            }else if(args[0].equalsIgnoreCase("enchant")){
                stringList.addAll(utils.getBannedEnchantments().stream().map(s -> s.split("-")[0]).collect(Collectors.toList()));
            }
        }else if(args.length == 3 && args[0].equalsIgnoreCase("potion")){
            stringList.addAll(utils.getBannedPotions().stream().filter(s -> s.split("-")[0].equalsIgnoreCase(args[1])).map(s -> s.split("-")[1]).collect(Collectors.toList()));
        }
        return stringList;
    }
}
