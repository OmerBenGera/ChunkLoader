package it.flowzz.chunkloader.objects.npc;

import com.mojang.authlib.GameProfile;
import it.flowzz.chunkloader.objects.connection.CustomPlayerConnection_1_9_R2;
import it.flowzz.chunkloader.objects.network.CustomNetworkManager_1_9_R2;
import net.minecraft.server.v1_9_R2.*;

public class CustomNPC_1_9_R2 extends EntityPlayer implements CustomNPC {
    public CustomNPC_1_9_R2(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super(minecraftserver, worldserver, gameprofile, new PlayerInteractManager(worldserver));
        NetworkManager nm = new CustomNetworkManager_1_9_R2();
        this.playerConnection = new CustomPlayerConnection_1_9_R2(this.server, nm, this);
        this.playerInteractManager.b(WorldSettings.EnumGamemode.CREATIVE);
        this.fauxSleeping = true;
    }
}
