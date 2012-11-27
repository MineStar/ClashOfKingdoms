package de.minestar.clashofkingdoms.classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class KnightClass extends PlayerClass {

    public KnightClass() {
        super(EnumPlayerClass.KNIGHT.getClassName(), 0.5);
    }

    @Override
    public void init() {
        this.registerItem(new ItemStack(Material.IRON_CHESTPLATE.getId(), 1));
        this.registerItem(new ItemStack(Material.IRON_LEGGINGS.getId(), 1));
        this.registerItem(new ItemStack(Material.IRON_SWORD.getId(), 1));
        this.setAllowChanges(1, false);
        this.setAllowChanges(2, false);
    }

    @Override
    public void defaultConfig(YamlConfiguration config) {
        ArrayList<String> stringList = new ArrayList<String>();
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.IRON_CHESTPLATE.getId(), 1)));
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.IRON_LEGGINGS.getId(), 1)));
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.IRON_SWORD.getId(), 1)));
        config.set("class.items", stringList);
    }
}
