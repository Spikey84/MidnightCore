package me.spikey.midnightcore.cosmetics;

import me.spikey.midnightcore.cosmetics.chatcolor.ChatColorInventory;
import me.spikey.midnightcore.cosmetics.glow.GlowInventory;
import me.spikey.midnightcore.cosmetics.tags.TagsInventory;
import me.spikey.midnightcore.cosmetics.tags.TagsManager;
import me.spikey.midnightcore.utils.I;
import me.spikey.midnightcore.utils.inventory.BaseInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class MasterInventory extends BaseInventory {
    private CosmeticManager cosmeticManager;
    public MasterInventory(Plugin plugin, Player player, CosmeticManager cosmeticManager) {
        super(3, plugin, "Cosmetics!");
        this.cosmeticManager = cosmeticManager;

        fillInventory(I.getVisibleFiller());

        ItemStack item = new ItemStack(Material.BLUE_DYE);

        for (int y = 10; y < 17; y++) {
            addItem(y, I.getFiller());
        }

        int x = 11;
        for (CosmeticType cosmeticType : CosmeticType.values()) {
            addItem(x, I.setLore(I.setName(new ItemStack(cosmeticType.getMaterial()), cosmeticType.getName()), cosmeticType.getLore()), (clickType) -> {
                if (cosmeticType.getId() == 0) player.openInventory((new ChatColorInventory(this.cosmeticManager, player).getInventory()));
                if (cosmeticType.getId() == 1) player.openInventory((new TagsInventory(this.cosmeticManager, player).getInventory()));
                if (cosmeticType.getId() == 2) player.openInventory((new GlowInventory(this.cosmeticManager, player).getInventory()));
            });
            x++;
        }
    }
}
