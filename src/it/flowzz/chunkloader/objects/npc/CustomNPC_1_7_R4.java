package it.flowzz.chunkloader.objects.npc;

import it.flowzz.chunkloader.objects.connection.CustomPlayerConnection_1_7_R4;
import it.flowzz.chunkloader.objects.network.CustomNetworkManager_1_7_R4;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.com.mojang.authlib.GameProfile;

public class CustomNPC_1_7_R4 extends EntityPlayer implements CustomNPC {

    public CustomNPC_1_7_R4(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super(minecraftserver, worldserver, gameprofile, new PlayerInteractManager(worldserver));
        NetworkManager nm = new CustomNetworkManager_1_7_R4();
        this.playerConnection = new CustomPlayerConnection_1_7_R4(this.server, nm, this);
        this.playerInteractManager.b(EnumGamemode.CREATIVE);
        this.fauxSleeping = true;
    }
}
