package me.spikey.midnightcore.end;

import com.destroystokyo.paper.event.player.PlayerSetSpawnEvent;
import com.google.common.collect.Lists;
import me.spikey.midnightcore.utils.SchedulerUtils;
import me.spikey.midnightcore.utils.VectorUtils;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.List;

public class EndManager implements Listener {

    private int taskId;
    private EnderDragon enderDragon;

    private Plugin plugin;


    public EndManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        this.plugin = plugin;

    }

    @EventHandler
    public void mobSpawn(EntitySpawnEvent event) {
        if (!event.getEntity().getType().equals(EntityType.ENDER_DRAGON)) return;

        EnderDragon enderDragon = (EnderDragon) event.getEntity();

        enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1000);
        enderDragon.setHealth(1000);

        enderDragon.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(30);
        enderDragon.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).setBaseValue(20);

        World world = enderDragon.getWorld();

        this.enderDragon = enderDragon;


    }

    @EventHandler
    public void mobDamage(EntityDamageEvent event) {
        if (!event.getEntity().getType().equals(EntityType.ENDER_DRAGON)) return;

        EnderDragon enderDragon = (EnderDragon) event.getEntity();

        if (enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() == 500) {

            if (event.getEntity() instanceof Player) {
                Bukkit.broadcastMessage(ChatColor.RED + "The dragon can only be killed with projectiles in this stage!");
                event.setCancelled(true);
            }
            if (event.getCause().equals(EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
                Bukkit.broadcastMessage(ChatColor.RED + "The dragon can only be killed with projectiles in this stage!");
                event.setCancelled(true);
            }
            if (event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)) {
                Bukkit.broadcastMessage(ChatColor.RED + "The dragon can only be killed with projectiles in this stage!");
                event.setCancelled(true);
            }
        }

        if (enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() == 1024) {
            if (enderDragon.getHealth() - event.getDamage() > 250) return;
            enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(500);
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "The dragon had entered its final stage!");
            Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Only projectiles stand a chance against thicc hide");

            enderDragon.setHealth(500);

            enderDragon.getDragonBattle().getBossBar().setColor(BarColor.BLUE);
            enderDragon.getDragonBattle().getBossBar().setTitle(ChatColor.BLUE + "Ender Dragon");

            World world = enderDragon.getWorld();

            //enderDragon.setAI(false);
            //enderDragon.teleport(new Location(world, 0, 170, 0));
            enderDragon.setPhase(EnderDragon.Phase.STRAFING);

            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
                if (enderDragon == null) Bukkit.getScheduler().cancelTask(taskId);

                Player[] players = world.getNearbyPlayers(new Location(world, 0, 100, 0), 100).toArray(Player[]::new);

                Player player = players[((int) Math.ceil(Math.random()*10000)) % players.length];

                DragonFireball fireball = world.spawn(this.enderDragon.getLocation(), DragonFireball.class);
                fireball.setShooter(this.enderDragon);
                Location to = player.getLocation().clone();
                to.setX(to.getX() + (((Math.random()*1000)%10)-5));
                to.setZ(to.getZ() + (((Math.random()*1000)%10)-5));
                to.setY(player.getLocation().getY()-3);
                fireball.setIsIncendiary(true);
                fireball.setYield(1);
                fireball.setBounce(false);
                fireball.setDirection(to.toVector().subtract(fireball.getLocation().toVector()).normalize().multiply(2));

                this.enderDragon.setPhase(EnderDragon.Phase.CIRCLING);

            }, 0, 40);
        }

        if (enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() == 500) return;

        if (enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() != 1000 || enderDragon.getHealth() - event.getDamage() > 250) return;
        enderDragon.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(1024);
        Bukkit.broadcastMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "The dragon has entered STAGE TWO!!!!!!");

        enderDragon.setHealth(1000);

        World world = enderDragon.getWorld();

        List<Location> crystals = Lists.newArrayList();



        for (int x = -55; x < 55; x++) {
            if (x < 10 && x > -10) continue;
            for (int z = -55; z < 55; z++) {
                if (z < 10 && z > -10) continue;
                for (int y = 50; y < 150; y++) {
                    if (!world.getBlockAt(x, y, z).getType().equals(Material.BEDROCK)) continue;
                    crystals.add(new Location(world, x, y+1, z));
                }
            }
        }
        for (int g = 0; g < crystals.size(); g++) {
            int finalG = g;
            SchedulerUtils.runLater(() -> {
                world.spawnEntity(crystals.get(finalG), EntityType.ENDER_CRYSTAL);
                world.spawnParticle(Particle.FLASH, crystals.get(finalG), 10);
                world.playSound(crystals.get(finalG), Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, 10, 10);
                }, g*20);
        }



        enderDragon.getDragonBattle().getBossBar().setColor(BarColor.RED);
        enderDragon.getDragonBattle().getBossBar().setTitle(ChatColor.DARK_PURPLE + "Ender Dragon");


    }
}
