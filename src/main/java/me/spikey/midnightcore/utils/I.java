package me.spikey.midnightcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class I {
    public static ItemStack getFiller() {
        ItemStack i = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getVisibleFiller() {
        ItemStack i = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getEnabled() {
        ItemStack i = new ItemStack(Material.LIME_DYE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Enabled");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getDisabled() {
        ItemStack i = new ItemStack(Material.RED_DYE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Disabled");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getConfirm() {
        ItemStack i = new ItemStack(Material.LIME_DYE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Confirm");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getDeny() {
        ItemStack i = new ItemStack(Material.RED_DYE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Deny");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getBack() {
        ItemStack i = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Back");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getNext() {
        ItemStack i = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Next");
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getEmpty() {
        ItemStack i = new ItemStack(Material.PINK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Empty Slot");
        itemMeta.setLore(new Lore(ChatColor.WHITE + "Use /home to create a new home.").getContents());
        i.setItemMeta(itemMeta);
        return i;
    }

    public static ItemStack getLocked() {
        ItemStack i = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RED + "Locked Slot");
        itemMeta.setLore(new Lore(ChatColor.WHITE + "This slot is restricted.").getContents());
        i.setItemMeta(itemMeta);
        return i;
    }


    public static ItemStack setName(ItemStack i, String s) {
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setDisplayName(s);
        ItemStack itemStack = i.clone();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack setLore(ItemStack i, Lore s) {
        ItemMeta itemMeta = i.getItemMeta();
        itemMeta.setLore(s.getContents());
        ItemStack itemStack = i.clone();
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
