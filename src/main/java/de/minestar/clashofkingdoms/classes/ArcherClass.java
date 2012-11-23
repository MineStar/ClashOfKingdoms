package de.minestar.clashofkingdoms.classes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class ArcherClass extends PlayerClass {

    public ArcherClass() {
        super(EnumPlayerClass.ARCHER.getTypeName(), 0.2);
    }

    @Override
    public void defaultConfig(YamlConfiguration config) {
        ArrayList<String> stringList = new ArrayList<String>();
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.BOW.getId(), 1)));
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.ARROW.getId(), 32)));
        stringList.add(PlayerClass.ItemStackToString(new ItemStack(Material.LEATHER_HELMET.getId(), 1)));
        config.set("class.items", stringList);
    }
}
