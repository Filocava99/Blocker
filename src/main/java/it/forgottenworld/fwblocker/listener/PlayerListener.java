package it.forgottenworld.fwblocker.listener;

import it.forgottenworld.fwblocker.FWBlocker;
import it.forgottenworld.fwblocker.util.Utils;
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

import java.util.Map;

public class PlayerListener implements Listener {

    private final FWBlocker instance;
    private final Utils utils;

    public PlayerListener(FWBlocker instance) {
        this.instance = instance;
        this.utils = new Utils(this.instance);
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event) {
        event.setCancelled(true);
        Map<Enchantment, Integer> enchantmentsToAdd = event.getEnchantsToAdd();
        enchantmentsToAdd.entrySet().removeIf(entry -> utils.isEnchantBanned(entry.getKey(), entry.getValue()));
        event.getItem().addEnchantments(enchantmentsToAdd);
    }

    @EventHandler
    public void onPotionBrewed(BrewEvent event) {
        for (ItemStack itemStack : event.getContents().getContents()) {
            if (utils.isPotionBanned(itemStack)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerCrafting(CraftItemEvent event) {
        ItemStack itemStack = event.getRecipe().getResult();
        if (utils.isItemBanned(itemStack)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {
        utils.checkHands(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            utils.checkEquippedItems((Player) event.getEntity());
        }
    }
}
