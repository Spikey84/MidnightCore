package me.spikey.midnightcore.customend;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Random;

public class CustomEndManager implements Listener {
    private Random random;

    public CustomEndManager(Plugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.random = new Random();



        craftElytra(plugin);
        craftEndChestplate(plugin);
        craftTNT(plugin);
    }

    public void craftElytra(Plugin plugin) {
        ItemStack item = new ItemStack(Material.ELYTRA);

        NamespacedKey key = new NamespacedKey(plugin, "elytra");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("C C", "CEC", "CDC");

        recipe.setIngredient('C', Material.CHORUS_FRUIT);
        recipe.setIngredient('D', Material.DIAMOND);
        recipe.setIngredient('E', Material.ENDER_EYE);

        Bukkit.addRecipe(recipe);
    }

    public void craftTNT(Plugin plugin) {
        ItemStack item = new ItemStack(Material.TNT);
        item.setAmount(10);

        NamespacedKey key = new NamespacedKey(plugin, "tnt_custom");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("G");

        recipe.setIngredient('G', Material.GUNPOWDER);


        Bukkit.addRecipe(recipe);
    }

    public void craftEndChestplate(Plugin plugin) {
        ItemStack item = new ItemStack(CustomItems.getEndChestplate());

        NamespacedKey key = new NamespacedKey(plugin, "end_chestplate");
        ShapedRecipe recipe = new ShapedRecipe(key, item);

        recipe.shape("E E", "GHG", "GGG");

        recipe.setIngredient('E', Material.ENDER_EYE);
        recipe.setIngredient('G', Material.GOLD_INGOT);
        recipe.setIngredient('H', Material.CHORUS_FLOWER);

        Bukkit.addRecipe(recipe);
    }


    @EventHandler
    public void mobSpawnEvent(CreatureSpawnEvent event) {
        if (!event.getEntity().getWorld().getName().equals("Custom_end")) return;

        Entity entity = event.getEntity();

        if (entity.getType().equals(EntityType.ENDERMAN)) {
            Enderman enderman = (Enderman) entity;

            enderman.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(20);
            enderman.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(300);
            enderman.setHealth(enderman.getMaxHealth());

        } else if (entity.getType().equals(EntityType.ENDERMITE)) {
            Endermite endermite = (Endermite) entity;
            endermite.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.NETHERITE_SWORD));
            endermite.getEquipment().setDropChance(EquipmentSlot.HAND, 0);
            endermite.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(90);
            endermite.setHealth(endermite.getMaxHealth());

        } else if (entity.getType().equals(EntityType.CREEPER)) {
            Creeper creeper = (Creeper) entity;

            creeper.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(250);
            creeper.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(300);
            creeper.setHealth(creeper.getMaxHealth());
        }

    }

    @EventHandler
    public void totemDropEvent(EntityDeathEvent event) {
        if (!event.getEntity().getType().equals(EntityType.EVOKER)) return;

        List<ItemStack> itemStacks = Lists.newArrayList();
        for (ItemStack item : event.getDrops()) {
            if (!item.getType().equals(Material.TOTEM_OF_UNDYING)) continue;
            itemStacks.add(item);
        }
        event.getDrops().removeAll(itemStacks);
    }


    @EventHandler
    public void customMobDrops(EntityDeathEvent event) {
        if (!event.getEntity().getWorld().getName().equals("Custom_end")) return;
        int number = random.nextInt(0, 420);

        if (event.getEntity().getType().equals(EntityType.ENDERMAN)) {
            if (number < 4) event.getDrops().add(CustomItems.getEndermanHeart());
            if (number > 150) event.getDrops().add(new ItemStack(Material.TOTEM_OF_UNDYING));
            if (number > 250 && number < 300) event.getDrops().add(new ItemStack(Material.SHULKER_SHELL));
            if (number > 200 && number < 260) event.getDrops().add(new ItemStack(Material.END_CRYSTAL));
            if (number > 100 && number < 150) event.getDrops().add(new ItemStack(Material.ENDER_EYE));
        } else if(event.getEntity().getType().equals(EntityType.ENDERMITE)) {
            if (number > 100) event.getDrops().add(CustomItems.getMiteChitin());
        } else if(event.getEntity().getType().equals(EntityType.CREEPER)) {
            if (number > 100) event.getDrops().add(CustomItems.getCreeperEssence());
        }

    }

    @EventHandler
    public void craft(CraftItemEvent event) {
        if (CustomItems.isEndChestplate(event.getRecipe().getResult())) {
            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack == null) return;
                if (!itemStack.getType().equals(Material.CHORUS_FLOWER)) continue;
                if (!CustomItems.isEnderHeart(itemStack)) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else if (event.getRecipe().getResult().getType().equals(Material.ELYTRA)) {

            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack == null) return;
                if (!itemStack.getType().equals(Material.CHORUS_FRUIT)) continue;
                if (!CustomItems.isMiteChitin(itemStack)) {
                    event.setCancelled(true);
                    return;
                }
            }


        } else if (event.getRecipe().getResult().getType().equals(Material.TNT) && event.getRecipe().getResult().getAmount() != 1) {
            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack == null) return;
                if (!itemStack.getType().equals(Material.GUNPOWDER)) continue;
                if (!CustomItems.isCreeperEssence(itemStack)) {
                    event.setCancelled(true);
                    return;
                }
            }
        } else if (event.getRecipe().getResult().getType().equals(Material.PLAYER_HEAD)) {

            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack == null) return;
                if (!itemStack.getType().equals(Material.CHORUS_FRUIT)) continue;
                if (!CustomItems.isMiteChitin(itemStack)) {
                    event.setCancelled(true);
                    return;
                }
            }

            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack == null) return;
                if (!itemStack.getType().equals(Material.CHORUS_FLOWER)) continue;
                if (!CustomItems.isEnderHeart(itemStack)) {
                    event.setCancelled(true);
                    return;
                }
            }

            for (ItemStack itemStack : event.getInventory().getContents()) {
                if (itemStack == null) return;
                if (!itemStack.getType().equals(Material.PLAYER_HEAD)) continue;
                if (!itemStack.displayName().toString().contains("EnderPack")) {
                    event.setCancelled(true);
                    return;
                }
            }


        }
    }

    @EventHandler
    public void teleport(PlayerTeleportEvent event) {
        if (!event.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL)) return;
        if (event.getFrom().distance(new Location(event.getFrom().getWorld(), 262, 67, 108)) > 100) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void damage(ProjectileHitEvent event) {
        if (!event.getEntity().getWorld().getName().equals("Custom_end")) return;
        if (!event.getEntity().getType().equals(EntityType.CREEPER)) return;

        event.setCancelled(true);
    }
}
