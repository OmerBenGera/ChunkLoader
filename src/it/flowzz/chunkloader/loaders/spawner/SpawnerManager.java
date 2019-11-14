package it.flowzz.chunkloader.loaders.spawner;

import org.bukkit.block.CreatureSpawner;

public interface SpawnerManager {

    void editSpawner(CreatureSpawner spawner);

    void resetSpawner(CreatureSpawner spawner);

}
