package it.filippocavallari.blocker.listener;

import it.filippocavallari.blocker.util.Utils;
import it.filippocavallari.blocker.Blocker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BrewingStand;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PlayerListener implements Listener {

    private final Blocker instance;
    private final Utils utils;

    public PlayerListener(Blocker instance) {
        this.instance = instance;
        this.utils = new Utils(this.instance);
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event) {
        if (!event.getEnchanter().hasPermission("fwblocker.bypass")) {
            event.setCancelled(true);
            Map<Enchantment, Integer> enchantmentsToAdd = event.getEnchantsToAdd();
            enchantmentsToAdd.entrySet().removeIf(entry -> utils.isEnchantBanned(entry.getKey(), entry.getValue()));
            event.getItem().addEnchantments(enchantmentsToAdd);
        }
    }

    @EventHandler
    public void onPotionBrewed(BrewEvent event) {
        Bukkit.getScheduler().runTaskLater(instance, () -> {
            BrewingStand brewingStand = event.getContents().getHolder();
            assert brewingStand != null;
            for (ItemStack itemStack : brewingStand.getInventory().getContents()) {
                if (itemStack != null && utils.isPotion(itemStack)) {
                    if (utils.isPotionBanned(itemStack)) {
                        brewingStand.getInventory().remove(itemStack);
                        itemStack.setType(Material.AIR);
                        utils.playBanEffect(brewingStand.getLocation());
                    }
                }
            }
        }, 1);
    }

    @EventHandler
    public void onPlayerCrafting(CraftItemEvent event) {
        if (!event.getWhoClicked().hasPermission("fwblocker.bypass")) {
            ItemStack itemStack = event.getRecipe().getResult();
            if (utils.isItemBanned(itemStack)) {
                event.setCancelled(true);
                utils.playBanEffect(event.getWhoClicked().getLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerInteraction(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("fwblocker.bypass")) {
            utils.checkHands(event.getPlayer());
            utils.checkEquippedItems(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDamaged(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (!player.hasPermission("fwblocker.bypass")) {
                utils.checkEquippedItems((Player) event.getEntity());

            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("fwblocker.bypass")) {
            ItemStack itemStack = new ItemStack(event.getBlock().getType());
            if (utils.isItemBanned(itemStack)) {
                event.setCancelled(true);
                utils.playBanEffect(event.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!event.getPlayer().hasPermission("fwblocker.bypass")) {
            ItemStack itemStack = new ItemStack(event.getBlock().getType());
            if (utils.isItemBanned(itemStack)) {
                event.setCancelled(true);
                utils.playBanEffect(event.getBlock().getLocation());
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event){
        if(utils.isItemBanned(event.getItemDrop().getItemStack())){
            event.setCancelled(true);
            event.getPlayer().getInventory().remove(event.getItemDrop().getItemStack());
        }
    }

    @EventHandler
    public void onPlayerPotionSplash(PotionSplashEvent event){
        if(utils.isPotionBanned(event.getPotion().getItem())){
            event.setCancelled(true);
        }
    }
}
