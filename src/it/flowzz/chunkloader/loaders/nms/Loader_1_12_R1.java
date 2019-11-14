package it.flowzz.chunkloader.loaders.nms;

import com.mojang.authlib.GameProfile;
import it.flowzz.chunkloader.loaders.ChunkLoader;
import it.flowzz.chunkloader.objects.npc.CustomNPC;
import it.flowzz.chunkloader.objects.npc.CustomNPC_1_12_R1;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.MinecraftServer;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;

import java.util.UUID;

public class Loader_1_12_R1 implements Loader{

    @Override
    public void spawnNpc(ChunkLoader chunkLoader, Location location) {
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld)chunkLoader.getLocation().getWorld()).getHandle();
        CustomNPC npc = new CustomNPC_1_12_R1(server, world, new GameProfile(UUID.randomUUID(), "Loader"), new PlayerInteractManager(world));

        EntityPlayer player = (EntityPlayer)npc;
        player.setViewDistance(1);
        player.affectsSpawning = true;

        player.spawnIn(world);
        player.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        world.players.add(player);
        world.getPlayerChunkMap().addPlayer(player);
        chunkLoader.addNpc(npc);
    }

    @Override
    public void removeNpc(ChunkLoader chunkLoader) {
        WorldServer world = ((CraftWorld)chunkLoader.getLocation().getWorld()).getHandle();
        for(CustomNPC npc : chunkLoader.getNpcs()){
            EntityPlayer entityPlayer = (EntityPlayer)npc;
            entityPlayer.world.players.remove(entityPlayer);
            world.getPlayerChunkMap().removePlayer(entityPlayer);
            entityPlayer.affectsSpawning = false;
            entityPlayer.die();
        }
    }
}
