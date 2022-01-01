package me.spikey.midnightcore.cosmetics.chatcolor;

import me.spikey.midnightcore.Main;
import net.luckperms.api.LuckPerms;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ChatColorCosmetic {
    private ChatColor chatColor;
    private ItemStack itemStack;
    private byte id;
    private String name;


    public ChatColorCosmetic(ChatColor chatColor, String name, Material material, byte id) {
        this.chatColor = chatColor;
        this.itemStack = new ItemStack(material);
        ItemMeta iM = itemStack.getItemMeta();
        iM.setDisplayName(chatColor + name);
        itemStack.setItemMeta(iM);
        this.name = name;
        this.id = id;
    }

    public byte getId() {
        return id;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ItemStack getItemStackDenied() {
        ItemStack i = itemStack;
        i.setType(Material.BARRIER);
        return itemStack;
    }

    public String getName() {
        return name;
    }

    public boolean canBeUsed(Player player) {
        if (name.toLowerCase().contains("light") && player.hasPermission("cosmetics.chatcolor.light"))  return true;
        if (getId() < 9 && player.hasPermission("cosmetics.chatcolor.basic")) return true;
        return (player.hasPermission("cosmetics.chatcolor.%s".formatted(name)) || player.hasPermission("cosmetics.chatcolor.*"));
    }
}
