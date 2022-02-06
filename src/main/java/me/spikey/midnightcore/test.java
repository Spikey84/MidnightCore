package me.spikey.midnightcore;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Map;

public class test implements CommandExecutor {
    public static void main(String[] args) {
        getEntityStat("minecraft.mined:minecraft.dirt", Bukkit.getPlayer("creativename11"));

    }

    public static int getEntityStat(String s, Player player) {
        String[] strings = s.replaceAll("minecraft.", "").split(":");
        if (strings[0].equals("mined")) return player.getStatistic(Statistic.MINE_BLOCK, Material.valueOf(strings[1].toUpperCase()));
        if (strings[0].equals("killed")) return player.getStatistic(Statistic.KILL_ENTITY, EntityType.valueOf(strings[1].toUpperCase()));
        return 0;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player)) {
            return true;
        }

        Player player = (Player) commandSender;

        player.sendMessage("minecraft.mined:minecraft.dirt || " + getEntityStat("minecraft.mined:minecraft.dirt", player));
        player.sendMessage("minecraft.mined:minecraft.stone || " + getEntityStat("minecraft.mined:minecraft.stone", player));

        player.sendMessage("minecraft.killed:minecraft.bat || " + getEntityStat("minecraft.killed:minecraft.bat", player));
        player.sendMessage("minecraft.killed:minecraft.zombie || " + getEntityStat("minecraft.killed:minecraft.zombie", player));
        return false;
    }
}
