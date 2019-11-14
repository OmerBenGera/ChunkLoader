package it.flowzz.chunkloader.loaders.spawner;

import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.TileEntityMobSpawner;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

public class SpawnerManager_1_7_R4 implements SpawnerManager{

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
        return (TileEntityMobSpawner)((CraftWorld)spawner.getWorld()).getHandle().getTileEntity(spawner.getX(), spawner.getY(), spawner.getZ());
    }
}
