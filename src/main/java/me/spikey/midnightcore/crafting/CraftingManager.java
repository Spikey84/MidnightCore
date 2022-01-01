package me.spikey.midnightcore.crafting;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public class CraftingManager implements Listener {

    public CraftingManager(Plugin plugin) {

        ItemStack item = new ItemStack(Material.ENDER_EYE);

        NamespacedKey key = new NamespacedKey(plugin, "eye");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("PS ", "B  ");

        recipe.setIngredient('P', Material.ENDER_PEARL);
        recipe.setIngredient('S', Material.NETHER_STAR);
        recipe.setIngredient('B', Material.BLAZE_POWDER);

        Bukkit.addRecipe(recipe);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType().equals(Material.ENDER_EYE) && !event.getInventory().contains(Material.NETHER_STAR)) {
            event.setCancelled(true);
            event.getWhoClicked().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "You must use a netherstar to craft this item.");
        }
    }
}
