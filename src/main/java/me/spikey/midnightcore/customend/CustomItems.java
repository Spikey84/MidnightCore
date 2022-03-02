package me.spikey.midnightcore.customend;

import com.google.common.collect.Lists;
import me.spikey.midnightcore.utils.C;
import me.spikey.midnightcore.utils.I;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public class CustomItems {

    public static String heartID = "424001";
    public static String essID = "424002";
    public static String endChestID = "424003";
    public static String miteChitinID = "424004";

    public static ItemStack getEndermanHeart() {
        ItemStack heart = new ItemStack(Material.CHORUS_FLOWER);
        ItemMeta itemMeta = heart.getItemMeta();
        itemMeta.setDisplayName(C.purple + "Enderman Heart");
        List<String> list = Lists.newArrayList();
        list.add(C.lightPurple + "Useful in many advanced crafting recipes.");
        list.add(C.gray + heartID);
        itemMeta.setLore(list);

        heart.setItemMeta(itemMeta);
        return heart;
    }

    public static boolean isEnderHeart(ItemStack itemStack) {
        return compareItems(itemStack, heartID);
    }

    public static ItemStack getCreeperEssence() {
        ItemStack item = new ItemStack(Material.GUNPOWDER);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(C.green + "Creeper Essence");
        List<String> list = Lists.newArrayList();
        list.add(C.lightGreen + "Useful in many advanced crafting recipes.");
        list.add(C.gray + essID);
        itemMeta.setLore(list);

        item.setItemMeta(itemMeta);
        return item;
    }

    public static boolean isCreeperEssence(ItemStack itemStack) {
        return compareItems(itemStack, essID);
    }

    public static ItemStack getEndChestplate() {
        ItemStack item = new ItemStack(Material.GOLDEN_CHESTPLATE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(C.purple + "Chestplate of the End");

        itemMeta.addAttributeModifier(Attribute.GENERIC_MAX_HEALTH, new AttributeModifier(UUID.randomUUID(),"generic.max_health", 10, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        List<String> list = Lists.newArrayList();
        list.add(C.lightPurple + "Provides extra health.");
        list.add(C.gray + endChestID);
        itemMeta.setLore(list);

        item.setItemMeta(itemMeta);
        return item;
    }

    public static boolean isEndChestplate(ItemStack itemStack) {
        return compareItems(itemStack, endChestID);
    }

    public static ItemStack getMiteChitin() {
        ItemStack item = new ItemStack(Material.CHORUS_FRUIT);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(C.blue + "Ender Mite Chitin");

        List<String> list = Lists.newArrayList();
        list.add(C.lightGreen + "Useful in many advanced crafting recipes.");
        list.add(C.lightRed + "***WARNING: Do not eat this!***");
        list.add(C.gray + miteChitinID);
        itemMeta.setLore(list);

        item.setItemMeta(itemMeta);
        return item;
    }

    public static boolean isMiteChitin(ItemStack itemStack) {
        return compareItems(itemStack, miteChitinID);
    }

    public static boolean compareItems(ItemStack i, String id) {
        if (i.lore() == null) return false;
        for (Component component : i.lore()) {
            if (component.toString().contains(id)) return true;
        }
        return false;
    }
}
