package me.spikey.midnightcore.cosmetics.glow;

import me.spikey.midnightcore.cosmetics.CosmeticManager;
import me.spikey.midnightcore.cosmetics.MasterInventory;
import me.spikey.midnightcore.cosmetics.tags.Tag;
import me.spikey.midnightcore.cosmetics.tags.TagsManager;
import me.spikey.midnightcore.utils.I;
import me.spikey.midnightcore.utils.inventory.BaseInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class GlowInventory extends BaseInventory {
    private List<Glow> glowTypes;
    private Player player;
    private GlowManager glowManager;

    public GlowInventory(CosmeticManager cosmeticManager, Player player) {
        super(3, CosmeticManager.plugin, "ChatColor");

        glowManager = cosmeticManager.getGlowManager();
        glowTypes = glowManager.getGlowTypes();

        this.player = player;

        fillInventory(I.getFiller());

        addItem(18, I.getBack(), (clickType -> {
            player.openInventory(new MasterInventory(CosmeticManager.plugin, player, cosmeticManager).getInventory());
        }));


        for (Glow glow: glowTypes) {
            if (glow.canBeUsed(player)) {
                addItem(glow.getId(), glow.getItem(player), (clickType -> {
                    cosmeticManager.getGlowManager().setGlow(glow.getId(), player.getUniqueId());
                    player.sendMessage(String.format(ChatColor.GRAY + "You glow has been set to %s" + ChatColor.GRAY + ".", glow.getName()));
                    player.sendMessage("In order for this effect to be visible, please leave and rejoin the server.");
                }));
            } else {
                addItem(glow.getId(), glow.getItemStackDenied(player), (clickType -> {
                    player.sendMessage(String.format(ChatColor.GRAY + "Visit our store to gain access to %s" + ChatColor.GRAY + " as your glow color.", glow.getName()));
                }));
            }
        }
    }


    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}