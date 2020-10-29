package it.forgottenworld.fwblocker.util;

import it.forgottenworld.fwblocker.FWBlocker;
import it.forgottenworld.fwblocker.config.Config;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Utils {

    private final FWBlocker instance;

    public Utils(FWBlocker instance) {
        this.instance = instance;
    }

    public boolean isPotion(ItemStack itemStack){
        return  itemStack.getType() == Material.POTION || itemStack.getType() == Material.LINGERING_POTION || itemStack.getType() == Material.SPLASH_POTION;
    }

    public void banPotion(ItemStack itemStack){
        Config config = instance.getPluginConfig();
        PotionMeta potionMeta = (PotionMeta)itemStack.getItemMeta();
        PotionData potionData = potionMeta.getBasePotionData();
        boolean isUpgraded = potionData.isUpgraded();
        boolean isExtended = potionData.isExtended();
        ConfigurationSection potionSection = Objects.requireNonNull(config.getConfig().getConfigurationSection("potions-banned")).createSection(potionData.getType().toString());
        if(!isExtended && !isUpgraded){
            potionSection.set("ban-only-normal",true);
        }else{
            potionSection.set("ban-only-normal",false);
        }
        potionSection.set("ban-only-extended",isExtended);
        potionSection.set("ban-only-upgraded",isUpgraded);
        config.save();
    }

    public void banItem(ItemStack itemStack){
        Config config = instance.getPluginConfig();
        List<String> bannedItems = config.getConfig().getStringList("items-banned");
        bannedItems.add(itemStack.getType().toString());
        config.getConfig().set("items-banned",bannedItems);
        config.save();
    }

    public void banEnchant(Enchantment enchantment, int level){
        Config config = instance.getPluginConfig();
        ConfigurationSection enchantSection = config.getConfig().getConfigurationSection("enchantments-banned");
        if(enchantSection != null){
            enchantSection.set(enchantment.toString(), level);
        }
        config.save();
    }

    public boolean isPotionBanned(ItemStack potion) {
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        ConfigurationSection potionsSection = instance.getPluginConfig().getConfig().getConfigurationSection("potions-banned");
        PotionData potionData = potionMeta.getBasePotionData();
        assert potionsSection != null;
        if (potionsSection.contains(potionData.getType().toString())) {
            ConfigurationSection potionSection = potionsSection.getConfigurationSection(potionData.getType().toString());
            assert potionSection != null;
            return potionSection.getBoolean("ban-only-normal") || potionSection.getBoolean("ban-only-extended") && potionData.isExtended() || potionSection.getBoolean("ban-only-upgraded") && potionData.isUpgraded();
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
        ConfigurationSection enchantmentsSection = instance.getPluginConfig().getConfig().getConfigurationSection("enchantments-banned");
        assert enchantmentsSection != null;
        if (enchantmentsSection.contains(enchantment.toString())) {
            int enchantmentLevel = enchantmentsSection.getInt(enchantment.toString());
            return value >= enchantmentLevel;
        }
        return false;
    }

    public boolean isItemBanned(ItemStack itemStack) {
        return instance.getPluginConfig().getConfig().getStringList("items-banned").contains(itemStack.getType().toString());
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
            playBanEffect(playerInventory.getHolder().getLocation());
            playerInventory.remove(itemStack);
        } else if (isPotion(itemStack)) {
            if (isPotionBanned(itemStack)) {
                playBanEffect(playerInventory.getHolder().getLocation());
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
                        playBanEffect(player.getLocation());
                        player.getInventory().remove(itemStack);
                    } else {
                        removeBannedEnchants(itemStack);
                    }
                }
            });
        }
    }

    public void playBanEffect(Location location){
        location.getWorld().playEffect(location, Effect.ANVIL_BREAK,1);
        location.getWorld().spawnParticle(Particle.BARRIER,location.add(0,1.5,0),4);
    }
}
