package de.minestar.clashofkingdoms.classes;

import java.util.ArrayList;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerClass {

    private final String className;
    private ArrayList<ItemStack> itemList;

    public PlayerClass(String className) {
        this.className = className;
    }

    public final void registerItem(ItemStack itemStack) {
        this.itemList.add(itemStack);
    }

    public final void unregisterItem(ItemStack itemStack) {
        String name = "";
        int index = 0;
        for (ItemStack stack : itemList) {
            name = stack.toString();
            if (name.equalsIgnoreCase(itemStack.toString())) {
                break;
            }
            index++;
        }
        if (this.itemList.size() > 0 && index <= this.itemList.size() - 1) {
            this.itemList.remove(index);
        }
    }

    public final void giveItems(Player player) {
        String name = "";
        for (ItemStack stack : itemList) {
            name = stack.getType().name().toLowerCase();
            if (name.contains("helmet")) {
                player.getInventory().setHelmet(stack.clone());
            } else if (name.contains("chestplate")) {
                player.getInventory().setChestplate(stack.clone());
            } else if (name.contains("leggings")) {
                player.getInventory().setLeggings(stack.clone());
            } else if (name.contains("boots")) {
                player.getInventory().setBoots(stack.clone());
            } else {
                player.getInventory().addItem(stack.clone());
            }
        }
    }

    public String getClassName() {
        return className;
    }
}
