package it.forgottenworld.fwblocker.listener;

import it.forgottenworld.fwblocker.FWBlocker;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

import java.util.Arrays;
import java.util.Map;

public class PlayerListener implements Listener {

    private final FWBlocker instance;

    public PlayerListener(FWBlocker instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event) {
        event.setCancelled(true);
        Map<Enchantment, Integer> enchantmentsToAdd = event.getEnchantsToAdd();
        enchantmentsToAdd.entrySet().removeIf(entry -> isEnchantBanned(entry.getKey(), entry.getValue()));
        event.getItem().addEnchantments(enchantmentsToAdd);
    }

    @EventHandler
    public void onPotionBrewed(BrewEvent event) {
        for (ItemStack itemStack : event.getContents().getContents()) {
            if (isPotionBanned(itemStack)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerCrafting(CraftItemEvent event) {
        ItemStack itemStack = event.getRecipe().getResult();
        if (isItemBanned(itemStack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {
        checkHands(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            checkEquippedItems((Player) event.getEntity());
        }
    }

    private boolean isPotionBanned(ItemStack potion) {
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

    private boolean isEnchantBanned(Enchantment enchantment, int value) {
        ConfigurationSection enchantmentsSection = instance.getPluginConfig().getConfig().getConfigurationSection("enchantments");
        assert enchantmentsSection != null;
        if (enchantmentsSection.contains(enchantment.toString())) {
            int enchantmentLevel = enchantmentsSection.getInt(enchantment.toString());
            return value >= enchantmentLevel;
        }
        return false;
    }

    private boolean isItemBanned(ItemStack itemStack) {
        return instance.getPluginConfig().getConfig().getStringList("items").contains(itemStack.getType().toString());
    }

    private void checkHands(Player player){
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

    private void checkEquippedItems(Player player) {
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
