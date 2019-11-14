package it.flowzz.chunkloader.loaders.spawner;

import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.TileEntityMobSpawner;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;

public class SpawnerManager_1_10_R1 implements SpawnerManager{

    @Override
    public void editSpawner(CreatureSpawner spawner) {
        TileEntityMobSpawner nbtSpawner = getTileSpawner(spawner);
        NBTTagCompound nbt = new NBTTagCompound();
        nbtSpawner.getSpawner().b(nbt);
        nbt.setShort("RequiredPlayerRange", (short)-1);
        nbtSpawner.getSpawner().a(nbt);
        spawner.update(true);
    }

    @Override
    public void resetSpawner(CreatureSpawner spawner) {
        TileEntityMobSpawner nbtSpawner = getTileSpawner(spawner);
        NBTTagCompound nbt = new NBTTagCompound();
        nbtSpawner.getSpawner().b(nbt);
        nbt.setShort("RequiredPlayerRange", (short)16);
        nbtSpawner.getSpawner().a(nbt);
        spawner.update(true);
    }

    private TileEntityMobSpawner getTileSpawner(CreatureSpawner spawner) {
        return (TileEntityMobSpawner)((CraftWorld)spawner.getWorld()).getHandle().getTileEntity(new BlockPosition(spawner.getX(), spawner.getY(), spawner.getZ()));
    }
}
