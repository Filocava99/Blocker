package it.forgottenworld.fwblocker.command;

import it.forgottenworld.fwblocker.util.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BlockCommandMethods {

    private final Utils utils;
    private final int elementsPerPage = 10;

    protected BlockCommandMethods(Utils utils) {
        this.utils = utils;
    }

    protected void printHelp(CommandSender sender) {
        sender.sendMessage(org.bukkit.ChatColor.DARK_RED + "---------{ FWBlocker }---------");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block item:" + ChatColor.GREEN + " blocks the item that you are currently holding in your hand");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block potion:" + ChatColor.GREEN + " blocks the potion that you are currently holding in your hand");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block enchant <name> <level>:" + ChatColor.GREEN + " blocks the specified enchant");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block item list:" + ChatColor.GREEN + " interactive list of all the blocked items");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block potion list:" + ChatColor.GREEN + " interactive list of all the blocked potions");
        sender.sendMessage(ChatColor.DARK_GREEN + "/block enchant list:" + ChatColor.GREEN + " interactive list of all the blocked enchants");
    }

    protected void printBannedItemsList(CommandSender sender, String[] args) {
        int currentPage = args.length < 3 ? 1 : Integer.parseInt(args[2]);
        ComponentBuilder componentBuilder = new ComponentBuilder(ChatColor.GREEN + "-------------- Banned items --------------\n");
        TextComponent nextPageComponent = getArrow(">>", "/block item list " + (currentPage + 1));
        TextComponent previousPageComponent = getArrow("<<", "/block item list " + (currentPage - 1));
        List<String> bannedItemsList = utils.getBannedItems();
        int beginIndex = elementsPerPage * (currentPage - 1);
        if (beginIndex >= bannedItemsList.size()) {
            noMoreElementsToShowAlert(sender, componentBuilder, previousPageComponent);
            return;
        }
        int endIndex = beginIndex + elementsPerPage - 1;
        if (endIndex >= bannedItemsList.size()) {
            endIndex = bannedItemsList.size() - 1;
        }
        for (int i = beginIndex; i <= endIndex; i++) {
            TextComponent textComponent = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "Unban" + ChatColor.GRAY + "]");
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unblock item " + bannedItemsList.get(i)));
            textComponent.setBold(true);
            componentBuilder.append(ChatColor.YELLOW + bannedItemsList.get(i)).append("    ").append(textComponent).append("\n");
        }
        componentBuilder.append(ChatColor.GREEN + "-------------- ");
        if(currentPage > 1){
            componentBuilder.append(previousPageComponent);
        }
        componentBuilder.append(" ").append(nextPageComponent).append(ChatColor.GREEN + " --------------");
        sender.sendMessage(componentBuilder.create());
    }

    protected void printBannedPotionsList(CommandSender sender, String[] args) {
        int currentPage = args.length < 3 ? 1 : Integer.parseInt(args[2]);
        ComponentBuilder componentBuilder = new ComponentBuilder(ChatColor.GREEN + "-------------- Banned potions --------------\n");
        TextComponent nextPageComponent = getArrow(">>", "/block potion list " + (currentPage + 1));
        TextComponent previousPageComponent = getArrow("<<", "/block potion list " + (currentPage - 1));
        List<String> bannedItemsList = utils.getBannedPotions();
        int beginIndex = getPageBeginIndex(currentPage);
        if (beginIndex >= bannedItemsList.size()) {
            noMoreElementsToShowAlert(sender, componentBuilder, previousPageComponent);
            return;
        }
        int endIndex = beginIndex + elementsPerPage - 1;
        if (endIndex >= bannedItemsList.size()) {
            endIndex = bannedItemsList.size() - 1;
        }
        for (int i = beginIndex; i <= endIndex; i++) {
            TextComponent textComponent = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "Unban" + ChatColor.GRAY + "]");
            String[] potionInfo = bannedItemsList.get(i).split("-");
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unblock potion " + potionInfo[0] + " " + potionInfo[1]));
            textComponent.setBold(true);
            componentBuilder.append(ChatColor.YELLOW + potionInfo[0] + " " + potionInfo[1].toUpperCase()).append("    ").append(textComponent).append("\n");
        }
        componentBuilder.append(ChatColor.GREEN + "-------------- ");
        if(currentPage > 1){
            componentBuilder.append(previousPageComponent);
        }
        componentBuilder.append(" ").append(nextPageComponent).append(ChatColor.GREEN + " --------------");
        sender.sendMessage(componentBuilder.create());
    }

    protected void printBannedEnchantmentsList(CommandSender sender, String[] args) {
        int currentPage = args.length < 3 ? 1 : Integer.parseInt(args[2]);
        ComponentBuilder componentBuilder = new ComponentBuilder(ChatColor.GREEN + "-------------- Banned enchants --------------\n");
        TextComponent nextPageComponent = getArrow(">>", "/block enchant list " + (currentPage + 1));
        TextComponent previousPageComponent = getArrow("<<", "/block enchant list " + (currentPage - 1));
        List<String> bannedEnchantsList = utils.getBannedEnchantments();
        int beginIndex = getPageBeginIndex(currentPage);
        if (beginIndex >= bannedEnchantsList.size()) {
            noMoreElementsToShowAlert(sender, componentBuilder, previousPageComponent);
            return;
        }
        int endIndex = beginIndex + elementsPerPage - 1;
        if (endIndex >= bannedEnchantsList.size()) {
            endIndex = bannedEnchantsList.size() - 1;
        }
        for (int i = beginIndex; i <= endIndex; i++) {
            TextComponent textComponent = new TextComponent(ChatColor.GRAY + "[" + ChatColor.GOLD + "Unban" + ChatColor.GRAY + "]");
            String[] enchantInfo = bannedEnchantsList.get(i).split("-");
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/unblock enchant " + enchantInfo[0]));
            textComponent.setBold(true);
            componentBuilder.append(ChatColor.YELLOW + enchantInfo[0] + " " + enchantInfo[1].toUpperCase()).append("    ").append(textComponent).append("\n");
        }
        componentBuilder.append(ChatColor.GREEN + "-------------- ");
        if(currentPage > 1){
            componentBuilder.append(previousPageComponent);
        }
        componentBuilder.append(" ").append(nextPageComponent).append(ChatColor.GREEN + " --------------");
        sender.sendMessage(componentBuilder.create());
    }

    protected void banItem(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (mainHand.getType() != Material.AIR) {
                utils.banItem(mainHand);
                sender.sendMessage(ChatColor.YELLOW + mainHand.getType().toString() + ChatColor.GREEN + " has been banned");
            } else {
                sender.sendMessage(ChatColor.RED + "You can't ban air!");
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Only players can run this command!");
        }
    }

    protected void banPotion(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            if (utils.isPotion(mainHand)) {
                utils.banPotion(mainHand);
                sender.sendMessage(ChatColor.YELLOW + mainHand.getItemMeta().getDisplayName() + ChatColor.GREEN + " has been banned");
            } else {
                sender.sendMessage(ChatColor.RED + "You must hold a potion in the main hand to ban it!");
            }
        } else {
            sender.sendMessage(ChatColor.DARK_RED + "Only players can run this command!");
        }
    }

    protected void banEnchantment(CommandSender sender, String[] args) {
        try {
            Enchantment enchantment = Enchantment.getByKey(NamespacedKey.minecraft(args[1]));
            int level = Integer.parseInt(args[2]);
            utils.banEnchant(enchantment, level);
            sender.sendMessage(ChatColor.YELLOW + enchantment.toString() + " " + level + ChatColor.GREEN + " has been banned");
        } catch (Exception e) {
            sender.sendMessage(ChatColor.RED + "Invalid parameters!");
        }
    }

    private TextComponent getArrow(String arrowSymbol, String command) {
        TextComponent textComponent = new TextComponent(arrowSymbol);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        textComponent.setBold(true);
        textComponent.setColor(ChatColor.DARK_GREEN);
        return textComponent;
    }

    private int getPageBeginIndex(int currentPage) {
        return elementsPerPage * (currentPage - 1);
    }

    private void noMoreElementsToShowAlert(CommandSender sender, ComponentBuilder componentBuilder, TextComponent previousPageComponent) {
        componentBuilder.append(ChatColor.YELLOW + "Nothing more to display...\n");
        sender.sendMessage(componentBuilder.append(ChatColor.GREEN + "-------------- ").append(previousPageComponent).append(" --------------").create());
    }

}
