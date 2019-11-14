package it.flowzz.chunkloader.loaders;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import it.flowzz.chunkloader.Main;
import it.flowzz.chunkloader.objects.npc.CustomNPC;
import it.flowzz.chunkloader.utils.Util;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.*;

public class ChunkLoader implements ConfigurationSerializable {

   private static List<ChunkLoader> chunkLoaders = new ArrayList<>();
   private final static int UPDATE_DELAY = Main.getInstance().getConfig().getInt("Settings.update-delay");
   private List<CustomNPC> npcs = new ArrayList<>();
   private Location location;
   private int time;
   private boolean isPermanent;

    public static List<ChunkLoader> getChunkLoaders() { return chunkLoaders; }
    public List<CustomNPC> getNpcs() { return npcs; }
    public void addNpc(CustomNPC npc){
        this.npcs.add(npc);
    }
    public Location getLocation() {
        return location;
    }
    public int getTime() {
        return time;
    }
    public boolean isPermanent() { return isPermanent; }

    public static void updateChunkLoaders(){
       Iterator<ChunkLoader> iterator = chunkLoaders.iterator();
       while(iterator.hasNext()){
           ChunkLoader loader = iterator.next();
           int newTime = loader.time - UPDATE_DELAY;
           if(!loader.isPermanent && newTime > 0){
               loader.time = newTime;
               if(Main.getInstance().isHoloSupport()){
                   loader.updateHologram(loader.time, loader);
               }
           }
           else if(!loader.isPermanent){
               loader.delete(true);
               iterator.remove();
           }
       }
   }

    private void updateHologram(int timeLeft, ChunkLoader loader) {
        Location holoLocation = loader.getLocation().clone().add(0.5,1.5,0.5);
        for(Hologram holo : HologramsAPI.getHolograms(Main.getInstance())){
            if(holo.getLocation().equals(holoLocation)){
                holo.clearLines();
                String[] times = Util.getTimeFromSecond(timeLeft);
                holo.insertTextLine(0, ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Settings.Hologram.text")
                        .replace("%days%", times[0])
                        .replace("%hours%", times[1])
                        .replace("%minutes%", times[2])
                        .replace("%seconds%", times[3])
                ));
            }
        }

    }

    public ChunkLoader(Map<String, Object> map){
        this(Util.deserializeLocation((String)map.get("location")), (int)map.get("time"), (boolean)map.get("isPermanent"));
    }

    public ChunkLoader(Location location, int time, boolean isPermanent){
       this.location = location;
       this.time =  time;
       this.isPermanent = isPermanent;

       //Edit Spawner NBT
        for(BlockState entity : location.getChunk().getTileEntities()){
            if(entity instanceof CreatureSpawner){
                Main.getInstance().getSpawnerManager().editSpawner((CreatureSpawner)entity);
            }
        }

       //Load Chunk
       location.getChunk().load(true);


        if(Main.getInstance().getConfig().getBoolean("Settings.Hologram.enable") && !isPermanent){
            Location holoLocation = location.clone().add(0.5,1.5,0.5);
            Hologram holo = HologramsAPI.createHologram(Main.getInstance(), holoLocation);
            String[] times = Util.getTimeFromSecond(time);
            holo.insertTextLine(0, ChatColor.translateAlternateColorCodes('&', Main.getInstance().getConfig().getString("Settings.Hologram.text")
                    .replace("%days%", times[0])
                    .replace("%hours%", times[1])
                    .replace("%minutes%", times[2])
                    .replace("%seconds%", times[3])
            ));
        }
       //Spawn npc
       Location centro = new Location(location.getWorld(), location.getChunk().getX() << 4, 0.0D, location.getChunk().getZ() << 4);
       centro = centro.add(8.0D, 0.0D, 8.0D);
       for(int i = 1; i <= 16 ; i++){
           centro.add(0,16 * i,0);
           Main.getInstance().getLoader().spawnNpc(this, centro);
       }

       chunkLoaders.add(this);
   }

   public void delete(boolean expired){
       this.location.getBlock().setType(Material.AIR);
       Location holoLocation = location.clone().add(0.5,1.5,0.5);
       for(Hologram holo : HologramsAPI.getHolograms(Main.getInstance())){
           if(holo.getLocation().equals(holoLocation))
           holo.delete();
       }
       Main.getInstance().getLoader().removeNpc(this);
       for(BlockState entity : location.getChunk().getTileEntities()){
           if(entity instanceof CreatureSpawner){
               Main.getInstance().getSpawnerManager().resetSpawner((CreatureSpawner)entity);
           }
       }
       if(!expired) chunkLoaders.remove(this);
   }

    public static ChunkLoader getChunkLoader(Chunk chunk) {
       for(ChunkLoader chunkLoader : chunkLoaders){
           if(chunkLoader.getLocation().getChunk().equals(chunk))
               return chunkLoader;
       }
       return null;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap();
        result.put("location", location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ());
        result.put("time", time);
        result.put("isPermanent", isPermanent);
        return result;
    }

}
