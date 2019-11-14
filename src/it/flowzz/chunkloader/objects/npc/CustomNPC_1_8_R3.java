package it.flowzz.chunkloader.objects.npc;

import com.mojang.authlib.GameProfile;
import it.flowzz.chunkloader.objects.network.CustomNetworkManager_1_8_R3;
import it.flowzz.chunkloader.objects.connection.CustomPlayerConnection_1_8_R3;
import net.minecraft.server.v1_8_R3.*;

public class CustomNPC_1_8_R3 extends EntityPlayer implements CustomNPC {

    public CustomNPC_1_8_R3(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super(minecraftserver, worldserver, gameprofile, new PlayerInteractManager(worldserver));
        NetworkManager nm = new CustomNetworkManager_1_8_R3();
        this.playerConnection = new CustomPlayerConnection_1_8_R3(this.server, nm, this);
        this.playerInteractManager.b(WorldSettings.EnumGamemode.CREATIVE);
        this.fauxSleeping = true;
    }

}
