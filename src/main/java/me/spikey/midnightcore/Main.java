package me.spikey.midnightcore;

import me.spikey.midnightcore.adverts.AdvertManager;
import me.spikey.midnightcore.adverts.AdvertsToggleCommand;
import me.spikey.midnightcore.chat.ChatManager;
import me.spikey.midnightcore.cosmetics.CosmeticManager;
import me.spikey.midnightcore.cosmetics.CosmeticsCommand;
import me.spikey.midnightcore.crafting.CraftingManager;
import me.spikey.midnightcore.discord.DiscordManager;
import me.spikey.midnightcore.discord.linking.LinkMinecraftCommand;
import me.spikey.midnightcore.discord.linking.UnlinkMinecraftCommand;
import me.spikey.midnightcore.end.EndManager;
import me.spikey.midnightcore.hardcore.BanTimeCommand;
import me.spikey.midnightcore.hardcore.HardcoreManager;
import me.spikey.midnightcore.homes.*;
import me.spikey.midnightcore.pvparea.PvpAreaManager;
import me.spikey.midnightcore.rtp.PvpCommand;
import me.spikey.midnightcore.rtp.RTPCommand;
import me.spikey.midnightcore.rtp.SpawnCommand;
import me.spikey.midnightcore.staff.CreativeCommand;
import me.spikey.midnightcore.staff.SpectatorCommand;
import me.spikey.midnightcore.staff.SurvivalCommand;
import me.spikey.midnightcore.staff.TphereCommand;
import me.spikey.midnightcore.staffChat.StaffChatCommands;
import me.spikey.midnightcore.staffChat.StaffChatManager;
import me.spikey.midnightcore.teleportcooldown.TeleportManager;
import me.spikey.midnightcore.tpa.TPACommand;
import me.spikey.midnightcore.tpa.TPAManager;
import me.spikey.midnightcore.tpa.TPAccept;
import me.spikey.midnightcore.tpa.TPDeny;
import me.spikey.midnightcore.utils.SchedulerUtils;
import me.spikey.midnightcore.worlds.WorldCommand;
import me.spikey.spikeycooldownapi.API;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private Chat chat = null;
    private Economy eco = null;

    private API api;
    private static LuckPerms luckPerms;

    private ChatManager chatManager;
    private HardcoreManager hardcoreManager;
    private AdvertManager advertManager;
    private DiscordManager discordManager;

    private HomeManager homeManager;
    private TPAManager tpaManager;
    private TeleportManager teleportManager;

    private EndManager endManager;
    private CraftingManager craftingManager;
    private StaffChatManager staffChatManager;

    private CosmeticManager cosmeticManager;
    private PvpAreaManager pvpAreaManager;

    @Override
    public void onEnable() {
        SchedulerUtils.setPlugin(this);
        saveDefaultConfig();

        setupVaultChat();
        DatabaseManager.initDatabase(this);


        api = new API(this.getName(), "core");

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            luckPerms = provider.getProvider();
        }

        setupVaultEco();

        chatManager = new ChatManager(this);
        pvpAreaManager = new PvpAreaManager(this);
        hardcoreManager = new HardcoreManager(api, this, pvpAreaManager, eco);
        advertManager = new AdvertManager(this);
        discordManager = new DiscordManager(this);

        teleportManager = new TeleportManager(this);
        homeManager = new HomeManager(this);
        tpaManager = new TPAManager(teleportManager);
        staffChatManager = new StaffChatManager(discordManager.getJda(), discordManager, this);

        endManager = new EndManager(this);

        craftingManager = new CraftingManager(this);

        cosmeticManager = new CosmeticManager(this);


        getCommand("bantime").setExecutor(new BanTimeCommand(api));

        getCommand("home").setExecutor(new HomeCommand(homeManager, teleportManager));
        getCommand("homes").setExecutor(new HomesCommand(homeManager, teleportManager, this));
        getCommand("sethome").setExecutor(new SethomeCommand(homeManager));
        getCommand("delhome").setExecutor(new DelHome(homeManager, this));
        getCommand("tpa").setExecutor(new TPACommand(tpaManager));
        getCommand("tpaccept").setExecutor(new TPAccept(tpaManager));
        getCommand("tpdeny").setExecutor(new TPDeny(tpaManager));
        getCommand("world").setExecutor(new WorldCommand(this));

        getCommand("tphere").setExecutor(new TphereCommand());
        getCommand("gmc").setExecutor(new CreativeCommand());
        getCommand("gms").setExecutor(new SurvivalCommand());
        getCommand("gmsp").setExecutor(new SpectatorCommand());

        getCommand("staffchat").setExecutor(new StaffChatCommands(staffChatManager));

        getCommand("rtp").setExecutor(new RTPCommand(api));
        getCommand("spawn").setExecutor(new SpawnCommand(api, teleportManager));

        getCommand("link").setExecutor(new LinkMinecraftCommand(discordManager.getLinkingManager()));
        getCommand("unlink").setExecutor(new UnlinkMinecraftCommand(discordManager.getLinkingManager()));

        getCommand("toggleannouncements").setExecutor(new AdvertsToggleCommand(advertManager.getToggleManager()));

        getCommand("cosmetics").setExecutor(new CosmeticsCommand(this, cosmeticManager));

        getCommand("pvp").setExecutor(new PvpCommand(api, teleportManager, pvpAreaManager));
    }

    @Override
    public void onDisable() {

    }

    private boolean setupVaultChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    private boolean setupVaultEco() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        eco = rsp.getProvider();
        return eco != null;
    }

    public Economy getEco() {
        return eco;
    }

    public Chat getChat() {
        return chat;
    }

    public API getApi() {
        return api;
    }

    public static LuckPerms getLuckPerms() {
        return luckPerms;
    }

    public CosmeticManager getCosmeticManager() {
        return cosmeticManager;
    }
}
