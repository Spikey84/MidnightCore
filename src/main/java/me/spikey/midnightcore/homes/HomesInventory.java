package me.spikey.midnightcore.homes;

import me.spikey.midnightcore.teleportcooldown.TeleportManager;
import me.spikey.midnightcore.utils.ChatUtils;
import me.spikey.midnightcore.utils.I;
import me.spikey.midnightcore.utils.Lore;
import me.spikey.midnightcore.utils.inventory.BaseInventory;
import me.spikey.midnightcore.utils.inventory.ConfirmInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class HomesInventory extends BaseInventory {
    private Plugin plugin;
    private final int[] slots = new int[]{10,11,12,13,14,15,16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34};

    private int page;
    private Player player;
    private TeleportManager teleportManager;

    public HomesInventory(Plugin plugin, Player player, HomeManager homeManager, TeleportManager teleportManager) {
        this(plugin, player, homeManager, 0, teleportManager);
    }

    private HomesInventory(Plugin plugin, Player player, HomeManager homeManager, int page, TeleportManager teleportManager) {
        super(6, plugin, "&d&lHomes");

        this.plugin = plugin;
        this.page = page;
        this.player = player;
        this.teleportManager = teleportManager;

        fillInventory(I.getVisibleFiller());

        int homeAmount = homeManager.getMaxHomeAmount(player);

        List<Home> homes = homeManager.getHomes(player.getUniqueId());

        int slot = 0;
        for (int x = page * slots.length; x < slots.length * (page + 1); x++) {

            addItem(slots[x], I.getLocked());

            if (x <= homeAmount) {
                addItem(slots[x], I.getEmpty());
            }

            if (homes == null) {
                continue;
            }
            if (homes.size() <= x) {
                continue;
            }

            Home home = homes.get(x);

            ItemStack item = new ItemStack(Material.BLUE_STAINED_GLASS_PANE);
            item = I.setName(item, ChatColor.BLUE + home.getName());
            item = I.setLore(item, Lore.createLore(ChatColor.WHITE + String.format("x: %s, y: %s, z: %s, world: %s, id: %s", home.getLocation().getBlockX(), home.getLocation().getBlockY(), home.getLocation().getBlockZ(), home.getLocation().getWorld().getName(), home.getId()), ChatColor.GREEN + "[Click to Travel]", ChatColor.RED + "[Shift Click to Delete]"));

            addItem(slots[slot], item, (clickType) -> {
                if (clickType.equals(ClickType.SHIFT_RIGHT) || clickType.equals(ClickType.SHIFT_LEFT)) {
                    new ConfirmInventory(plugin, "Delete Home", player, (bool) ->{
                        if (bool) {
                            homeManager.delHome(home);
                            new HomesInventory(plugin, player, homeManager, page, teleportManager).open(player);
                        } else {
                            new HomesInventory(plugin, player, homeManager, page, teleportManager).open(player);
                            return;
                        }
                    }).open(player);
                    return;
                }
                player.closeInventory();
                teleportManager.addTeleport(player, home.getLocation());
                player.closeInventory();
            });

            slot++;
        }

        if (page != 0) addItem(28, I.getBack(), (clickType) -> {
            new HomesInventory(plugin, player, homeManager, page-1, teleportManager).open(player);
        });

        if (page * slots.length > homes.size()) addItem(34, I.getNext(), (clickType) -> {
            new HomesInventory(plugin, player, homeManager, page+1, teleportManager).open(player);
        });
    }
}
