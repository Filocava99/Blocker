package it.forgottenworld.fwblocker.listener;

import it.forgottenworld.fwblocker.FWBlocker;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

import java.util.HashSet;
import java.util.Set;

public class PlayerListener implements Listener {

    private final FWBlocker instance;

    public PlayerListener(FWBlocker instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerEnchant(EnchantItemEvent event) {
        Set<Enchantment> enchantmentsToBeRemoved = new HashSet<>();
        event.getEnchantsToAdd().forEach((key, value) -> {
            ConfigurationSection enchantmentsSection = instance.getPluginConfig().getConfig().getConfigurationSection("enchantments");
            assert enchantmentsSection != null;
            if (enchantmentsSection.contains(key.toString())) {
                int enchantmentLevel = enchantmentsSection.getInt(key.toString());
                if (value >= enchantmentLevel) {
                    enchantmentsToBeRemoved.add(key);
                }
            }
        });
        enchantmentsToBeRemoved.forEach(enchantment -> event.getItem().removeEnchantment(enchantment));
    }

    @EventHandler
    public void onPotionBrewed(BrewEvent event) {
        for (ItemStack itemStack : event.getContents().getContents()) {
            if (itemStack.getType() == Material.POTION || itemStack.getType() == Material.LINGERING_POTION || itemStack.getType() == Material.SPLASH_POTION) {
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();
                ConfigurationSection potionsSection = instance.getPluginConfig().getConfig().getConfigurationSection("potions");
                PotionData potionData = potionMeta.getBasePotionData();
                assert potionsSection != null;
                if (potionsSection.contains(potionData.getType().toString())) {
                    ConfigurationSection potionSection = potionsSection.getConfigurationSection(potionData.getType().toString());
                    assert potionSection != null;
                    if (!potionSection.getBoolean("extendable") && potionData.isExtended()) {
                        event.setCancelled(true);
                        return;
                    }
                    if (!potionSection.getBoolean("upgradable") && potionData.isUpgraded()) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerCrafting(CraftItemEvent event){
        ItemStack itemStack = event.getRecipe().getResult();
        String materialName = itemStack.getType().toString();
        if(instance.getPluginConfig().getConfig().getStringList("items").contains(materialName)){
            event.setCancelled(true);
        }
    }
}
