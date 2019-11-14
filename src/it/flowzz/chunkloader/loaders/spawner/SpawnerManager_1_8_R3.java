package it.flowzz.chunkloader.loaders.spawner;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.TileEntityMobSpawner;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

public class SpawnerManager_1_8_R3 implements SpawnerManager {

    @Override
    public void editSpawner(CreatureSpawner spawner) {
        TileEntityMobSpawner nbtSpawner = getTileSpawner(spawner);
        NBTTagCompound nbt = new NBTTagCompound();
        nbtSpawner.b(nbt);
        nbt.setShort("RequiredPlayerRange", (short)-1);
        nbtSpawner.a(nbt);
        spawner.update(true);
    }

    @Override
    public void resetSpawner(CreatureSpawner spawner) {
        TileEntityMobSpawner nbtSpawner = getTileSpawner(spawner);
        NBTTagCompound nbt = new NBTTagCompound();
        nbtSpawner.b(nbt);
        nbt.setShort("RequiredPlayerRange", (short)16);
        nbtSpawner.a(nbt);
        spawner.update(true);
    }

    private TileEntityMobSpawner getTileSpawner(CreatureSpawner spawner) {
        return (TileEntityMobSpawner)((CraftWorld)spawner.getWorld()).getHandle().getTileEntity(new BlockPosition(spawner.getX(), spawner.getY(), spawner.getZ()));
    }
}
