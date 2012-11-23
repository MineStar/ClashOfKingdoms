package de.minestar.clashofkingdoms.data;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import de.minestar.clashofkingdoms.COKCore;
import de.minestar.clashofkingdoms.classes.ArcherClass;
import de.minestar.clashofkingdoms.classes.EnumPlayerClass;
import de.minestar.clashofkingdoms.classes.KnightClass;
import de.minestar.clashofkingdoms.classes.LeaderClass;
import de.minestar.clashofkingdoms.classes.PlayerClass;
import de.minestar.clashofkingdoms.classes.RefereeClass;
import de.minestar.clashofkingdoms.enums.EnumTeam;
import de.minestar.clashofkingdoms.utils.BlockVector;
import de.minestar.clashofkingdoms.utils.StringUtils;

public class GameSettings {

    private int baseHeight;
    private int baseTypeID;
    private byte baseSubID;

    private ArrayList<PlayerClass> playerClassList;

    public GameSettings() {
        this.baseHeight = 5;
        this.baseTypeID = Material.STONE.getId();
        this.baseSubID = (byte) 0;
        this.playerClassList = new ArrayList<PlayerClass>();
        this.playerClassList.add(new LeaderClass());
        this.playerClassList.add(new KnightClass());
        this.playerClassList.add(new ArcherClass());
        this.playerClassList.add(new RefereeClass());
    }

    public int getBaseHeight() {
        return baseHeight;
    }

    public int getBaseTypeID() {
        return baseTypeID;
    }

    public byte getBaseSubID() {
        return baseSubID;
    }

    public void loadConfig(String settingsName, HashMap<EnumTeam, TeamData> teamData) {
        try {
            File gameDir = new File(COKCore.INSTANCE.getDataFolder(), "settings");
            File thisGameDir = new File(gameDir, settingsName);
            thisGameDir.mkdir();
            File file = new File(thisGameDir, settingsName + ".dat");

            if (!file.exists()) {
                for (Map.Entry<EnumTeam, TeamData> entry : teamData.entrySet()) {
                    entry.getValue().setSpawn(null);
                }
                return;
            }

            YamlConfiguration config = new YamlConfiguration();
            config.load(file);
            this.baseHeight = config.getInt("base.height", this.baseHeight);
            this.baseTypeID = config.getInt("base.TypeID", this.baseTypeID);
            this.baseSubID = (byte) config.getInt("base.SubID", this.baseSubID);
            for (Map.Entry<EnumTeam, TeamData> entry : teamData.entrySet()) {
                entry.getValue().setSpawn(StringUtils.LocationFromString(config.getString("spawn." + entry.getKey().getTeamName(), "NULL")));

                // load baseblocks
                List<String> baseBlockList = config.getStringList("baseblocks." + entry.getKey().getTeamName());
                if (baseBlockList != null) {
                    for (String singleBlock : baseBlockList) {
                        BlockVector vector = StringUtils.BlockVectorFromString(singleBlock);
                        if (vector != null) {
                            entry.getValue().registerBaseBlock(vector, this.baseHeight);
                        }
                    }
                }
            }

            this.loadPlayerClasses(thisGameDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void saveConfig(String settingsName, HashMap<EnumTeam, TeamData> teamData) {
        try {
            File gameDir = new File(COKCore.INSTANCE.getDataFolder(), "settings");
            File thisGameDir = new File(gameDir, settingsName);
            thisGameDir.mkdir();
            File file = new File(thisGameDir, settingsName + ".dat");

            if (file.exists()) {
                file.delete();
            }

            YamlConfiguration config = new YamlConfiguration();
            config.set("base.height", this.baseHeight);
            config.set("base.TypeID", this.baseTypeID);
            config.set("base.SubID", this.baseSubID);
            for (Map.Entry<EnumTeam, TeamData> entry : teamData.entrySet()) {
                config.set("spawn." + entry.getKey().getTeamName(), StringUtils.LocationToString(entry.getValue().getSpawn()));

                // save baseblocks
                ArrayList<String> baseBlockList = new ArrayList<String>();
                for (BaseBlock block : entry.getValue().getBlockBase().getBaseBlocks()) {
                    baseBlockList.add(StringUtils.BlockVectorToString(block.getVector()));
                }
                config.set("baseblocks." + entry.getKey().getTeamName(), baseBlockList);
            }
            config.save(file);
            this.savePlayerClasses(thisGameDir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void savePlayerClasses(File thisGameDir) {
        for (PlayerClass clazz : this.playerClassList) {
            clazz.saveConfig(new File(thisGameDir, clazz.getClassName() + ".cls"));
        }
    }

    private void loadPlayerClasses(File thisGameDir) {
        this.playerClassList.clear();

        for (EnumPlayerClass clazz : EnumPlayerClass.values()) {
            PlayerClass instance = PlayerClass.loadFromSettings(new File(thisGameDir, clazz.getTypeName() + ".cls"));
            if (instance == null) {
                continue;
            }
            this.playerClassList.add(instance);
        }
    }
}
