package de.minestar.clashofkingdoms.classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class LeaderClass extends PlayerClass {

    public LeaderClass() {
        super(EnumPlayerClass.LEADER.getClassName(), 1.0);
    }

    @Override
    public void defaultConfig(YamlConfiguration config) {
        ArrayList<String> stringList = new ArrayList<String>();
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.GOLD_HELMET.getId(), 1)));
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.DIAMOND_CHESTPLATE.getId(), 1)));
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.DIAMOND_LEGGINGS.getId(), 1)));
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.GOLD_BOOTS.getId(), 1)));
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.DIAMOND_SWORD.getId(), 1)));
        config.set("class.items", stringList);
    }
}
