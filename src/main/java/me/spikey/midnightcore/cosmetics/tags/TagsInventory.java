package me.spikey.midnightcore.cosmetics.tags;

import com.google.common.collect.Lists;
import me.spikey.midnightcore.cosmetics.CosmeticManager;
import me.spikey.midnightcore.cosmetics.MasterInventory;
import me.spikey.midnightcore.cosmetics.chatcolor.ChatColorCosmetic;
import me.spikey.midnightcore.cosmetics.chatcolor.ChatColorManager;
import me.spikey.midnightcore.utils.I;
import me.spikey.midnightcore.utils.inventory.BaseInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public class TagsInventory extends BaseInventory {
    private List<Tag> tags;
    private Player player;
    private TagsManager tagsManager;

    public TagsInventory(CosmeticManager cosmeticManager, Player player) {
        super(3, CosmeticManager.plugin, "ChatColor");

        tagsManager = cosmeticManager.getTagsManager();
        tags = tagsManager.getTags();

        this.player = player;

        fillInventory(I.getFiller());

        addItem(18, I.getBack(), (clickType -> {
            player.openInventory(new MasterInventory(CosmeticManager.plugin, player, cosmeticManager).getInventory());
        }));


        for (Tag tag: tags) {
            if (tag.canBeUsed(player)) {
                addItem(tag.getId(), tag.getItem(player), (clickType -> {
                    cosmeticManager.getTagsManager().setTag(tag.getId(), player.getUniqueId());
                    player.sendMessage(String.format(ChatColor.GRAY + "You tag has been set to %s" + ChatColor.GRAY + ".", tagsManager.getTagString(player)));
                }));
            } else {
                addItem(tag.getId(), tag.getItemStackDenied(player), (clickType -> {
                    player.sendMessage(String.format(ChatColor.GRAY + "Visit our store to gain access to %s" + ChatColor.GRAY + " as your tag.", tagsManager.getTagString(player)));
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
