package it.flowzz.chunkloader.listeners;

import it.flowzz.chunkloader.loaders.ChunkLoader;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkListener implements Listener {

    @EventHandler
    public void onUnload(ChunkUnloadEvent event) {
        if(ChunkLoader.getChunkLoader(event.getChunk()) != null)
            event.setCancelled(true);
    }
}
