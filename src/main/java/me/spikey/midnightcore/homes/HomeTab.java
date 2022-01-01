package me.spikey.midnightcore.homes;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HomeTab implements TabCompleter {
    private HomeManager homeManager;

    public HomeTab(HomeManager homeManager) {
        this.homeManager = homeManager;
    }



    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        List<String> homes = Lists.newArrayList();

        if (!(commandSender instanceof Player)) {
            return null;
        }

        Player player = (Player) commandSender;
        for (Home home : homeManager.getHomes(player.getUniqueId())) {
            homes.add(home.getName());
        }

        if (args.length > 1) return Lists.newArrayList();
        return homes;
    }
}
