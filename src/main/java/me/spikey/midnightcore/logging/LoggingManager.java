package me.spikey.midnightcore.logging;

import me.spikey.midnightcore.discord.DiscordManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.spi.ErrorHandler;
import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;
import org.bukkit.Bukkit;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class LoggingManager implements Appender {
    private DiscordManager discordManager;

    public LoggingManager(DiscordManager discordManager) {
        this.discordManager = discordManager;
    }

    @Override
    public void addFilter(Filter filter) {

    }

    @Override
    public Filter getFilter() {
        return null;
    }

    @Override
    public void clearFilters() {

    }

    @Override
    public void close() {

    }

    @Override
    public void doAppend(LoggingEvent loggingEvent) {

        Guild guild = discordManager.getJda().getGuildById("939591492270452776");
        if (guild == null) {
            Bukkit.getLogger().info("null");
            return;
        }
        TextChannel textChannel = guild.getTextChannelById("939591543063453736");
        if (textChannel == null) {
            Bukkit.getLogger().info("null");
            return;
        }

        textChannel.sendMessage(loggingEvent.getMessage().toString()).queue();
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void setErrorHandler(ErrorHandler errorHandler) {

    }

    @Override
    public ErrorHandler getErrorHandler() {
        return null;
    }

    @Override
    public void setLayout(Layout layout) {

    }

    @Override
    public Layout getLayout() {
        return null;
    }

    @Override
    public void setName(String s) {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }


//    @Override
//    public void publish(LogRecord record) {
//        Guild guild = discordManager.getJda().getGuildById("939591492270452776");
//        if (guild == null) {
//            return;
//        }
//        TextChannel textChannel = guild.getTextChannelById("939591543063453736");
//        if (textChannel == null) {
//            return;
//        }
//
//        textChannel.sendMessage(record.getMessage()).queue();
//    }


}
