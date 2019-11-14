package it.flowzz.chunkloader;

import it.flowzz.chunkloader.commands.GiveCMD;
import it.flowzz.chunkloader.listeners.ChunkListener;
import it.flowzz.chunkloader.listeners.FactionUUIDListener;
import it.flowzz.chunkloader.listeners.MassiveFactionListener;
import it.flowzz.chunkloader.listeners.PlayerListener;
import it.flowzz.chunkloader.loaders.ChunkLoader;
import it.flowzz.chunkloader.loaders.nms.*;
import it.flowzz.chunkloader.loaders.spawner.*;
import it.flowzz.chunkloader.storage.MySQL;
import it.flowzz.chunkloader.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin {

    private FileConfiguration data;
    private File dataFile;
    private FileConfiguration lang;
    private static Main instance;
    private Loader loader;
    private SpawnerManager spawnerManager;
    private MySQL database;
    private boolean holoSupport = false;
    private boolean isSupSkyblock = false;
    private boolean isFactionUUID = false;
    private boolean isMassiveFaction = false;
    private boolean isSkyblock = false;
    private boolean isMySQL = false;

    public boolean isMySQL() { return isMySQL; }

    public boolean isHoloSupport() { return holoSupport; }

    public boolean isSupSkyblock() { return isSupSkyblock; }

    public boolean isFactionUUID() { return isFactionUUID; }

    public boolean isMassiveFaction() { return isMassiveFaction; }

    public boolean isSkyblock() { return isSkyblock; }

    public FileConfiguration getLang() { return lang; }

    public static Main getInstance() { return instance; }

    public Loader getLoader() {
        return loader;
    }

    public SpawnerManager getSpawnerManager() { return spawnerManager; }

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        if(getConfig().getBoolean("MySQL.enabled")){
            isMySQL = true;
            database = new MySQL(this);
        }
        loadConfigs();
        setupServerVersion();
        checkHoloSupport();
        loadCompatibility();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ChunkListener(), this);
        pm.registerEvents(new PlayerListener(), this);
        if(isFactionUUID) pm.registerEvents(new FactionUUIDListener(), this);
        if(isMassiveFaction) pm.registerEvents(new MassiveFactionListener(), this);
        if(!isFactionUUID && !isMassiveFaction){
            getLogger().log(Level.INFO, "No Faction plugin found! falling back to factionUUID");
            pm.registerEvents(new FactionUUIDListener(), this);
        }
        getCommand("ChunkLoader").setExecutor(new GiveCMD());
        loadChunkLoaders();
        startUpdateTask();
    }

    private void checkHoloSupport() {
        holoSupport = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays") & Main.getInstance().getConfig().getBoolean("Settings.Hologram.enable");
    }

    private void startUpdateTask() {
        new BukkitRunnable(){
            @Override
            public void run() {
                ChunkLoader.updateChunkLoaders();
            }
        }.runTaskTimer(this, 20 * getConfig().getInt("Settings.update-delay"), 20 *getConfig().getInt("Settings.update-delay"));
    }



    public void onDisable() {
        if(isMySQL()){
            getLogger().info("Saving Chunkloaders data....");
            long start = System.currentTimeMillis();
            if(!ChunkLoader.getChunkLoaders().isEmpty()) {
                try {
                    PreparedStatement clearStatement = database.getConnection().prepareStatement("TRUNCATE `loaders`");
                    clearStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (ChunkLoader chunkLoader : ChunkLoader.getChunkLoaders()) {
                    try {
                        PreparedStatement statement = database.getConnection().prepareStatement("INSERT INTO loaders(LOCATION,TIME,ISPERMANENT) VALUE (?,?,?)");
                        String location = chunkLoader.getLocation().getWorld().getName() + "," + chunkLoader.getLocation().getX() + "," + chunkLoader.getLocation().getY() + "," + chunkLoader.getLocation().getZ();
                        statement.setString(1, location);
                        statement.setInt(2, chunkLoader.getTime());
                        statement.setBoolean(3, chunkLoader.isPermanent());
                        statement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
            getLogger().info("Chunkloaders data save in " + (System.currentTimeMillis() - start) + " ms");
        }else saveChunkloaders();
        instance = null;
    }

    private void saveData() {
        try { data.save(dataFile); } catch (IOException e) { e.printStackTrace(); }
    }

    public void saveChunkloaders() {
        if(isMySQL()){
            if(!ChunkLoader.getChunkLoaders().isEmpty()) {
                try {
                    PreparedStatement clearStatement = database.getConnection().prepareStatement("TRUNCATE `loaders`");
                    clearStatement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                for (ChunkLoader chunkLoader : ChunkLoader.getChunkLoaders()) {
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            try {
                                PreparedStatement statement = database.getConnection().prepareStatement("INSERT INTO loaders(LOCATION,TIME,ISPERMANENT) VALUE (?,?,?)");
                                String location = chunkLoader.getLocation().getWorld().getName() + "," + chunkLoader.getLocation().getX() + "," + chunkLoader.getLocation().getY() + "," + chunkLoader.getLocation().getZ();
                                statement.setString(1, location);
                                statement.setInt(2, chunkLoader.getTime());
                                statement.setBoolean(3, chunkLoader.isPermanent());
                                statement.executeUpdate();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }.runTaskAsynchronously(this);
                }
            }
            return;
        }
        for(String key : data.getKeys(false)){ data.set(key,null); }
        if(!ChunkLoader.getChunkLoaders().isEmpty()) {
            for (ChunkLoader chunkLoader : ChunkLoader.getChunkLoaders()) {
                data.set(UUID.randomUUID().toString(), chunkLoader.serialize());
            }
        }
        saveData();
    }

    private void loadConfigs() {
        File langFile = new File(getDataFolder(), "lang.yml");
        dataFile = new File(getDataFolder(), "data.yml");

        if(!langFile.exists())
            saveResource("lang.yml", true);
        if(!dataFile.exists())
            saveResource("data.yml", true);

        try {
            lang = YamlConfiguration.loadConfiguration(langFile);
            data = YamlConfiguration.loadConfiguration(dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCompatibility(){
        if(Bukkit.getPluginManager().isPluginEnabled("Factions")){
            Plugin plugin = Bukkit.getPluginManager().getPlugin("Factions");
            if(plugin.getDescription().getDepend().contains("MassiveCore"))
            isMassiveFaction = true;
            else isFactionUUID = true;
        }
        if(Bukkit.getPluginManager().isPluginEnabled("ASkyBlock"))
            isSkyblock = true;
        else if(Bukkit.getPluginManager().isPluginEnabled("SuperiorSkyblock2")){
            isSupSkyblock = true;
        }
    }

    private void setupServerVersion() {

        String serverVersion = "1.0.0";

        try {
            serverVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        } catch (ArrayIndexOutOfBoundsException ignored) { }

        getLogger().info("Your server is running version " + serverVersion);

        switch (serverVersion) {
            case "v1_7_R4":
                loader = new Loader_1_7_R4();
                spawnerManager = new SpawnerManager_1_7_R4();
                break;
            case "v1_8_R3":
                loader = new Loader_1_8_R3();
                spawnerManager = new SpawnerManager_1_8_R3();
                break;
            case "v1_9_R2":
                loader = new Loader_1_9_R2();
                spawnerManager = new SpawnerManager_1_9_R2();
                break;
            case "v1_10_R1":
                loader = new Loader_1_10_R1();
                spawnerManager = new SpawnerManager_1_10_R1();
                break;
            case "v1_11_R1":
                loader = new Loader_1_11_R1();
                spawnerManager = new SpawnerManager_1_11_R1();
                break;
            case "v1_12_R1":
                loader = new Loader_1_12_R1();
                spawnerManager = new SpawnerManager_1_12_R1();
                break;
            default:
                getLogger().info("Chunkloader does not support version: " + serverVersion);
                Bukkit.getPluginManager().disablePlugin(this);
                break;
        }

    }

    private void loadChunkLoaders() {
        if(isMySQL()){ try (PreparedStatement statement = database.getConnection().prepareStatement("SELECT * FROM loaders",
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
                ResultSet resultSet = statement.executeQuery()){
            while(resultSet.next()){
                new ChunkLoader(Util.deserializeLocation(resultSet.getString("LOCATION")), resultSet.getInt("TIME"), resultSet.getBoolean("ISPERMANENT"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return;
        }
        for(String key : data.getKeys(false)){
            new ChunkLoader(((MemorySection) data.get(key)).getValues(false));
        }
    }


}
