package it.flowzz.chunkloader.loaders.nms;

import it.flowzz.chunkloader.loaders.ChunkLoader;
import org.bukkit.Location;

public interface Loader {

    void spawnNpc(ChunkLoader chunkLoader, Location location);

    void removeNpc(ChunkLoader chunkLoader);
}
