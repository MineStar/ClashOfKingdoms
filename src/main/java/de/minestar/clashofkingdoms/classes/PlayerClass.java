package de.minestar.clashofkingdoms.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class PlayerClass {

    private final String className;
    protected ArrayList<ItemStack> itemList = new ArrayList<ItemStack>();
    private double punishMultiplicator = 0.0d;
    private boolean enabled = true;

    public abstract void defaultConfig(YamlConfiguration config);

    private void saveDefaultConfig(File file) {
        if (file.exists()) {
            file.delete();
        }
        try {
            YamlConfiguration config = new YamlConfiguration();
            this.defaultConfig(config);
            config.set("class.type", this.getClassName());
            config.set("class.punishMultiplicator", this.getPunishMultiplicator());
            config.set("class.enabled", this.enabled);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveConfig(File file) {
        if (file.exists()) {
            file.delete();
        } else {
            this.saveDefaultConfig(file);
            return;
        }

        try {
            YamlConfiguration config = new YamlConfiguration();

            config.set("class.type", this.getClassName());
            config.set("class.punishMultiplicator", this.getPunishMultiplicator());

            ArrayList<String> stringList = new ArrayList<String>();
            for (ItemStack stack : this.itemList) {
                stringList.add(PlayerClass.ItemStackToString(stack));
            }
            config.set("class.items", stringList);
            config.set("class.enabled", this.enabled);
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerClass(String className, double punishMultiplicator) {
        this.className = className;
        this.punishMultiplicator = punishMultiplicator;
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

    public final String getClassName() {
        return className;
    }

    public double getPunishMultiplicator() {
        return punishMultiplicator;
    }

    public void setPunishMultiplicator(double punishMultiplicator) {
        this.punishMultiplicator = punishMultiplicator;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static PlayerClass loadFromSettings(File file) {
        if (!file.exists()) {
            return null;
        }

        try {
            YamlConfiguration config = new YamlConfiguration();
            config.load(file);

            String type = config.getString("class.type", "UNKNOWN");
            EnumPlayerClass typeEnum = EnumPlayerClass.byType(type);
            if (typeEnum == null) {
                return null;
            }

            PlayerClass instance = typeEnum.getClazz().newInstance();
            List<String> itemList = config.getStringList("class.items");
            if (itemList != null) {
                for (String text : itemList) {
                    try {
                        ItemStack stack = ItemStackFromString(text);
                        if (stack != null) {
                            instance.registerItem(stack);
                        }
                    } catch (Exception e) {
                        continue;
                    }

                }
            }

            double punishMultiplicator = config.getDouble("class.punishMultiplicator", 0.0d);
            boolean enabled = config.getBoolean("class.enabled", true);
            punishMultiplicator = Math.max(0d, punishMultiplicator);
            instance.setPunishMultiplicator(punishMultiplicator);
            instance.setEnabled(enabled);
            return instance;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected static ItemStack ItemStackFromString(String text) {
        try {
            String[] split = text.split("-");
            int amount = Integer.valueOf(split[0]);
            int typeID = Integer.valueOf(split[1]);
            short subID = Short.valueOf(split[2]);
            ItemStack stack = new ItemStack(typeID);
            stack.setAmount(amount);
            stack.setDurability(subID);
            return stack;
        } catch (Exception e) {
            return null;
        }
    }

    protected static String ItemStackToString(ItemStack stack) {
        return stack.getAmount() + "-" + stack.getTypeId() + "-" + stack.getDurability();
    }

}
