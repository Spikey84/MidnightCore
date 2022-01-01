package me.spikey.midnightcore.cosmetics;

import me.spikey.midnightcore.cosmetics.chatcolor.ChatColorInventory;
import me.spikey.midnightcore.cosmetics.chatcolor.ChatColorManager;
import me.spikey.midnightcore.cosmetics.glow.GlowManager;
import me.spikey.midnightcore.cosmetics.tags.TagsManager;
import me.spikey.midnightcore.utils.C;
import me.spikey.midnightcore.utils.Lore;
import me.spikey.midnightcore.utils.inventory.BaseInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.Callable;
import java.util.function.Consumer;


public class CosmeticManager {
    public static Plugin plugin;
    public static CosmeticManager cosmeticManager;
    private ChatColorManager chatColorManager;
    private TagsManager tagsManager;
    private GlowManager glowManager;

    public CosmeticManager(Plugin plugin) {
        CosmeticManager.plugin = plugin;
        CosmeticManager.cosmeticManager = this;
        this.chatColorManager = new ChatColorManager(plugin);
        this.tagsManager = new TagsManager(plugin);
        this.glowManager = new GlowManager(plugin);
    }

    public ChatColorManager getChatColorManager() {
        return chatColorManager;
    }

    public TagsManager getTagsManager() {
        return tagsManager;
    }

    public GlowManager getGlowManager() {
        return glowManager;
    }
}

enum CosmeticType {
    chatColor((byte) 0, Material.BLUE_DYE, C.lightBlue + "Chat Colors", Lore.createLore(ChatColor.GRAY + "Click here to change your chat message color.")),
    tag((byte) 1, Material.NAME_TAG, C.lightOrange + "Chat Tags", Lore.createLore(ChatColor.GRAY + "Click here to change your chat tag color.")),
    glow((byte) 2, Material.GLOW_INK_SAC, C.lightPink + "Glow Effects", Lore.createLore(ChatColor.GRAY + "Click here to change glow effect.")),
    soon((byte) 3, Material.STRUCTURE_BLOCK, C.lightRed + "Coming Soon", Lore.createLore(ChatColor.GRAY + "More content coming soon.")),
    coming((byte) 4, Material.STRUCTURE_BLOCK, C.lightRed + "Coming Soon", Lore.createLore(ChatColor.GRAY + "More content coming soon."));

    private byte id;
    private Material material;
    private String name;
    private Lore lore;
    CosmeticType(byte id, Material material, String name, Lore lore) {
        this.id = id;
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    public byte getId() {
        return id;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public Lore getLore() {
        return lore;
    }
}
