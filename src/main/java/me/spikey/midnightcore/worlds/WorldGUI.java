package me.spikey.midnightcore.worlds;

import me.spikey.midnightcore.homes.Home;
import me.spikey.midnightcore.homes.HomesInventory;
import me.spikey.midnightcore.utils.ChatUtils;
import me.spikey.midnightcore.utils.I;
import me.spikey.midnightcore.utils.Lore;
import me.spikey.midnightcore.utils.inventory.BaseInventory;
import me.spikey.midnightcore.utils.inventory.ConfirmInventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class WorldGUI extends BaseInventory {

    private final int[] slots = new int[]{3,4,5};

    private Player player;


    public WorldGUI(Plugin plugin, Player player) {
        super(1, plugin, ChatColor.BOLD + "" + ChatColor.BLUE + "Worlds");

        int page = 0;


        fillInventory(I.getVisibleFiller());

        addItem(slots[1], I.setName(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "Staff Creative"), (clickType) -> {
            World world = Bukkit.getWorld("staff");
            player.teleport(world.getSpawnLocation());
        });

        addItem(slots[2], I.setName(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "Old Server World"), (clickType) -> {
            World world = Bukkit.getWorld("old");
            player.teleport(world.getSpawnLocation());
        });

        addItem(slots[0], I.setName(new ItemStack(Material.GREEN_STAINED_GLASS_PANE), "Main World"), (clickType) -> {
            World world = Bukkit.getWorld("world");
            player.teleport(world.getSpawnLocation());
        });
    }
}
