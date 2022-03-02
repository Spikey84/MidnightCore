package me.spikey.midnightcore;

import me.spikey.midnightcore.utils.C;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class RulesCommand implements CommandExecutor {
    //https://pastebin.com/raw/5JRsUXDU
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        TextComponent component = new TextComponent("Click here to view our rules!");
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://pastebin.com/raw/5JRsUXDU"));
        component.setColor(C.lightRed);
        sender.sendMessage(component);
        return true;
    }
}
