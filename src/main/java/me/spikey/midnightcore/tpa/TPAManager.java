package me.spikey.midnightcore.tpa;

import com.google.common.collect.Maps;
import me.spikey.midnightcore.teleportcooldown.TeleportManager;
import me.spikey.midnightcore.utils.SchedulerUtils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TPAManager {
    private HashMap<UUID, TPARequest> tpas;
    private TeleportManager teleportManager;

    public TPAManager(TeleportManager teleportManager) {
        this.tpas = Maps.newHashMap();
        this.teleportManager = teleportManager;

    }

    public void addRequest(UUID sender, UUID receiver) {

        if (tpas.containsKey(sender)) {
            tpas.remove(sender);
        }

        if (Bukkit.getPlayer(receiver) == null) {
            Bukkit.getPlayer(sender).sendMessage(ChatColor.BLUE + "This player is not online.");
            return;
        }

        if (sender.equals(receiver)) {
            Bukkit.getPlayer(sender).sendMessage(ChatColor.BLUE + "You cannot tpa to yourself.");
            return;
        }

        tpas.put(sender, new TPARequest(sender, receiver));
        Bukkit.getPlayer(sender).sendMessage(ChatColor.BLUE + "TPA request sent!");

        Bukkit.getPlayer(receiver).sendMessage(ChatColor.BLUE + "%s has sent you a tpa request.".formatted(Bukkit.getPlayer(sender).getName()));
        TextComponent link = new TextComponent(ChatColor.GREEN + "Click To Accept");
        link.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        Bukkit.getPlayer(receiver).sendMessage(link);

    }

    public void acceptRequest(Player player) {
        if (getFirstRequestReceived(player) == null) {
            player.sendMessage(ChatColor.BLUE + "You do not have a current TPA request.");
            return;
        }

        TPARequest tpaRequest = getFirstRequestReceived(player);

        if (Bukkit.getPlayer(tpaRequest.getRequester()) == null) {
            player.sendMessage(ChatColor.BLUE + "Requesting player is offline.");
            tpas.remove(tpaRequest.getRequester());
            return;
        }

        Bukkit.getPlayer(tpaRequest.getRequester()).sendMessage(ChatColor.BLUE + "Teleport request accepted by %s. Teleporting in 3 seconds...".formatted(player.getName()));

        tpas.remove(tpaRequest.getRequester());

        //requester to player.getloc
        teleportManager.addTeleport(Bukkit.getPlayer(tpaRequest.getRequester()), player.getLocation());


    }

    public void denyRequest(Player player) {

        TPARequest tpaRequest = getFirstRequestReceived(player);
        TPARequest tpaRequest1 = getFirstRequestSent(player);

        if (tpaRequest != null) tpas.remove(tpaRequest.getRequester());
        if (tpaRequest1 != null) {
            tpas.remove(tpaRequest1.getRequester());
            Bukkit.getPlayer(tpaRequest.getRequester()).sendMessage(ChatColor.BLUE + "Your TPA request to %s has been denied.".formatted(player.getName()));
        }

        player.sendMessage(ChatColor.BLUE + "TPA request denied.");



    }

    public TPARequest getFirstRequestReceived(Player player) {
        for (Map.Entry<UUID, TPARequest> entry : tpas.entrySet()) {
            if (entry.getValue().getTarget() == player.getUniqueId()) return entry.getValue();
        }
        return null;
    }

    public TPARequest getFirstRequestSent(Player player) {
        for (Map.Entry<UUID, TPARequest> entry : tpas.entrySet()) {
            if (entry.getValue().getRequester() == player.getUniqueId()) return entry.getValue();
        }
        return null;
    }
}
