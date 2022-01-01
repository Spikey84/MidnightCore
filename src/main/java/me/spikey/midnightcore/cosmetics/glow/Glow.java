package me.spikey.midnightcore.cosmetics.glow;

import me.MrGraycat.eGlow.API.EGlowAPI;
import me.MrGraycat.eGlow.API.Enum.EGlowColor;
import me.MrGraycat.eGlow.API.Enum.EGlowEffect;
import me.clip.placeholderapi.PlaceholderAPI;
import me.spikey.midnightcore.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


public class Glow {

    private byte id;
    private Material material;
    private String name;
    private EGlowColor color;
    private EGlowEffect eGlowEffect;

    public Glow(EGlowEffect effect, Material material, String name, byte id) {
        this.name = name;
        this.eGlowEffect = effect;
        this.id = id;
        this.material = material;
    }

    public Glow(EGlowColor color, Material material, String name, byte id) {
        this.name = name;
        this.color = color;
        this.id = id;
        this.material = material;
    }

    public ItemStack getItem(Player player) {
        ItemStack item = new ItemStack(material);
        ItemMeta iM = item.getItemMeta();
        iM.setDisplayName(name);
        item.setItemMeta(iM);
        return item;
    }

    public ItemStack getItemStackDenied(Player player) {
        ItemStack i = getItem(player);
        i.setType(Material.BARRIER);
        return i;
    }

    public EGlowColor getColor() {
        return color;
    }

    public EGlowEffect eGlowEffect() {
        return eGlowEffect;
    }

    public byte getId() {
        return id;
    }

    public boolean canBeUsed(Player player) {

        return (player.hasPermission("cosmetics.glow.%s".formatted(getId())) || player.hasPermission("cosmetics.glow.*"));
    }

    public String getName() {
        return name;
    }
}
