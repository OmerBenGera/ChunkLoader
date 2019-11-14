package it.flowzz.chunkloader.listeners;

import it.flowzz.chunkloader.Main;
import it.flowzz.chunkloader.loaders.ChunkLoader;
import it.flowzz.chunkloader.utils.Items;
import it.flowzz.chunkloader.utils.NbtUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;


public class PlayerListener implements Listener {

    private static FileConfiguration lang = Main.getInstance().getLang();

    private boolean isChunkloader(ItemStack stack, ItemStack other) {
        if (stack == null) {
            return false;
        } else {
            return other.getType() == stack.getType() &&
                    other.getDurability() == stack.getDurability() &&
                    other.hasItemMeta() == stack.hasItemMeta() &&
                    other.getItemMeta().hasLore() == stack.getItemMeta().hasLore() &&
                    other.getItemMeta().getLore().equals(stack.getItemMeta().getLore());
        }
    }

    @EventHandler
    public void onLoaderPlace(BlockPlaceEvent event) {
        if(event.isCancelled())
            return;
        if(!event.canBuild())
            return;
        if(isChunkloader(event.getItemInHand(), Items.getChunkLoader(1, -1))){
            if(ChunkLoader.getChunkLoader(event.getBlockPlaced().getChunk()) != null){
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("chunkloader-already-placed")));
                event.setCancelled(true);
                return;
            }
            event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("chunkloader-placed")));
            int time = NbtUtil.getInt(event.getItemInHand(), "time");
            new ChunkLoader(event.getBlockPlaced().getLocation(), time, time < 0);
            Main.getInstance().saveChunkloaders();
        }
    }

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent event) {
        if(event.isCancelled())
            return;
        if(!event.canBuild())
            return;
        if(event.getBlock().getTypeId() != 52) {
            return;
        }
        if(ChunkLoader.getChunkLoader(event.getBlockPlaced().getChunk()) != null){
            CreatureSpawner spawner = (CreatureSpawner)event.getBlockPlaced().getState();
            Main.getInstance().getSpawnerManager().editSpawner(spawner);
        }

    }

    @EventHandler
    public void onLoaderExplode(EntityExplodeEvent e){
        if(e.isCancelled())
            return;
        Iterator iterator = e.blockList().iterator();
        while(iterator.hasNext()){
            Block block = (Block)iterator.next();
            if(block.getType() != Items.getChunkLoader(1,-1).getType()) continue;
            ChunkLoader loader = ChunkLoader.getChunkLoader(e.getLocation().getChunk());
            if(loader != null) {
                block.setType(Material.AIR);
                iterator.remove();
                loader.getLocation().getWorld().dropItemNaturally(loader.getLocation(), Items.getChunkLoader(1, loader.getTime()));
                loader.delete(false);
                Main.getInstance().saveChunkloaders();
            }
        }
    }


    @EventHandler
    public void onCollectorClick(PlayerInteractEvent event){
        if(event.isCancelled()) return;
        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(event.getClickedBlock().getType() != Items.getChunkLoader(1, -1).getType()) return;
        ChunkLoader loader = ChunkLoader.getChunkLoader(event.getClickedBlock().getChunk());
        if(loader != null) {
            event.setCancelled(true);
        }

    }


}
