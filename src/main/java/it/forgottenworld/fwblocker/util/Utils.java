package it.forgottenworld.fwblocker.util;

import it.forgottenworld.fwblocker.FWBlocker;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

import java.util.Arrays;

public class Utils {

    private final FWBlocker instance;

    public Utils(FWBlocker instance) {
        this.instance = instance;
    }

    public boolean isPotionBanned(ItemStack potion) {
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        ConfigurationSection potionsSection = instance.getPluginConfig().getConfig().getConfigurationSection("potions");
        PotionData potionData = potionMeta.getBasePotionData();
        assert potionsSection != null;
        if (potionsSection.contains(potionData.getType().toString())) {
            ConfigurationSection potionSection = potionsSection.getConfigurationSection(potionData.getType().toString());
            assert potionSection != null;
            return potionSection.getBoolean("all") || potionSection.getBoolean("extendable") && potionData.isExtended() || potionSection.getBoolean("upgradable") && potionData.isUpgraded();
        }
        return false;
    }

    private void removeBannedEnchants(ItemStack itemStack) {
        itemStack.getEnchantments().forEach((key, value) -> {
            if (isEnchantBanned(key, value)) {
                itemStack.removeEnchantment(key);
            }
        });
    }

    public boolean isEnchantBanned(Enchantment enchantment, int value) {
        ConfigurationSection enchantmentsSection = instance.getPluginConfig().getConfig().getConfigurationSection("enchantments");
        assert enchantmentsSection != null;
        if (enchantmentsSection.contains(enchantment.toString())) {
            int enchantmentLevel = enchantmentsSection.getInt(enchantment.toString());
            return value >= enchantmentLevel;
        }
        return false;
    }

    public boolean isItemBanned(ItemStack itemStack) {
        return instance.getPluginConfig().getConfig().getStringList("items").contains(itemStack.getType().toString());
    }

    public void checkHands(Player player){
        checkMainHand(player.getInventory());
        checkOffHand(player.getInventory());
    }

    private void checkMainHand(PlayerInventory playerInventory){
        checkItem(playerInventory, playerInventory.getItemInMainHand());
    }

    private void checkOffHand(PlayerInventory playerInventory){
        checkItem(playerInventory, playerInventory.getItemInOffHand());
    }

    private void checkItem(PlayerInventory playerInventory, ItemStack itemStack){
        if (isItemBanned(itemStack)) {
            playerInventory.remove(itemStack);
        } else if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.LINGERING_POTION || itemStack.getType() == Material.SPLASH_POTION) {
            if (isPotionBanned(itemStack)) {
                playerInventory.remove(itemStack);
            }
        } else {
            removeBannedEnchants(itemStack);
        }
    }

    public void checkEquippedItems(Player player) {
        ItemStack[] armor = player.getInventory().getArmorContents();
        if (armor != null) {
            Arrays.stream(armor).forEach(itemStack -> {
                if (itemStack != null) {
                    if (isItemBanned(itemStack)) {
                        player.getInventory().remove(itemStack);
                    } else {
                        removeBannedEnchants(itemStack);
                    }
                }
            });
        }
    }
}
