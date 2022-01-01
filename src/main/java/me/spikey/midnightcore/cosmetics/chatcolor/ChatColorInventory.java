package me.spikey.midnightcore.cosmetics.chatcolor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import me.spikey.midnightcore.cosmetics.CosmeticManager;
import me.spikey.midnightcore.cosmetics.MasterInventory;
import me.spikey.midnightcore.utils.I;
import me.spikey.midnightcore.utils.inventory.BaseInventory;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

public class ChatColorInventory extends BaseInventory {
    private List<ChatColorCosmetic> chatColors;
    private ChatColorManager chatColorManager;
    private Player player;

    public ChatColorInventory(CosmeticManager cosmeticManager, Player player) {
        super(3, CosmeticManager.plugin, "ChatColor");

        this.chatColors = Lists.newArrayList();
        this.player = player;
        this.chatColorManager = cosmeticManager.getChatColorManager();

        fillInventory(I.getFiller());

        addItem(18, I.getBack(), (clickType -> {
            player.openInventory(new MasterInventory(CosmeticManager.plugin, player, cosmeticManager).getInventory());
        }));

        chatColors.add(new ChatColorCosmetic(ChatColor.of("#ff5252"), "Red", Material.RED_DYE, (byte) 0));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#ff8352"), "Orange", Material.ORANGE_DYE, (byte) 1));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#fff652"), "Yellow", Material.YELLOW_DYE, (byte) 2));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#7dff52"), "Green", Material.GREEN_DYE, (byte) 3));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#52a9ff"), "Blue", Material.BLUE_DYE, (byte) 4));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#cb52ff"), "Purple", Material.PURPLE_DYE, (byte) 5));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#ff52ee"), "Pink", Material.PINK_DYE, (byte) 6));
        chatColors.add(new ChatColorCosmetic(ChatColor.GRAY, "Gray", Material.GRAY_DYE, (byte) 7));
        chatColors.add(new ChatColorCosmetic(ChatColor.WHITE, "White", Material.WHITE_DYE, (byte) 8));

        chatColors.add(new ChatColorCosmetic(ChatColor.of("#ffbfbf"), "Light Red", Material.RED_STAINED_GLASS_PANE, (byte) 9));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#ffd1bf"), "Light Orange", Material.ORANGE_STAINED_GLASS_PANE, (byte) 10));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#fffcbf"), "Light Yellow", Material.YELLOW_STAINED_GLASS_PANE, (byte) 11));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#cfffbf"), "Light Green", Material.GREEN_STAINED_GLASS_PANE, (byte) 12));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#bfdfff"), "Light Blue", Material.BLUE_STAINED_GLASS_PANE, (byte) 13));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#ecbfff"), "Light Purple", Material.PURPLE_STAINED_GLASS_PANE, (byte) 14));
        chatColors.add(new ChatColorCosmetic(ChatColor.of("#ffbff9"), "Light Pink", Material.PINK_STAINED_GLASS_PANE, (byte) 15));




        for (ChatColorCosmetic chatColorCosmetic: chatColors) {
            if (chatColorCosmetic.canBeUsed(player)) {
                addItem(chatColorCosmetic.getId(), chatColorCosmetic.getItemStack(), (clickType -> {
                    chatColorManager.setColor(chatColorCosmetic.getChatColor(), getPlayer().getUniqueId());
                    player.sendMessage(String.format(ChatColor.GRAY + "You color has been set to %sthis" + ChatColor.GRAY + ".", chatColorCosmetic.getChatColor()));
                }));
            } else {
                addItem(chatColorCosmetic.getId(), chatColorCosmetic.getItemStackDenied(), (clickType -> {
                    player.sendMessage(String.format(ChatColor.GRAY + "To access %sthis" + ChatColor.GRAY + " color please visit the shop.", chatColorCosmetic.getChatColor()));
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
