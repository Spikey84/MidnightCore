package me.spikey.midnightcore.cosmetics.tags;

import me.clip.placeholderapi.PlaceholderAPI;
import me.spikey.midnightcore.utils.C;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Tag {
    private String rawTag;
    private byte id;

    public Tag(String rawTag, byte id) {
        this.rawTag = rawTag;
        this.id = id;
    }

    public ItemStack getItem(Player player) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta iM = item.getItemMeta();
        iM.setDisplayName(getFormattedTag(player));
        item.setItemMeta(iM);
        return item;
    }

    public ItemStack getItemStackDenied(Player player) {
        ItemStack i = getItem(player);
        i.setType(Material.BARRIER);
        return i;
    }

    public String getFormattedTag(Player player) {
        String s = C.convertToColored(rawTag);
        return PlaceholderAPI.setPlaceholders(player, s);
    }

    public byte getId() {
        return id;
    }

    public String getRawTag() {
        return rawTag;
    }

    public boolean canBeUsed(Player player) {
        return player.hasPermission("cosmetics.tag.%s".formatted(getId()));
    }
}
