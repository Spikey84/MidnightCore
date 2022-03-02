package me.spikey.midnightcore.discord;

import me.spikey.midnightcore.utils.C;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        TextComponent component = new TextComponent("Click here to join our discord!");
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/EYSxBgXVu3"));
        component.setColor(C.lightBlue);
        sender.sendMessage(component);
        return true;
    }
}
