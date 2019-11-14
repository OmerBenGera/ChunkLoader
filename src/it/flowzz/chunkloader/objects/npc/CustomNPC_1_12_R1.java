package it.flowzz.chunkloader.objects.npc;

import com.mojang.authlib.GameProfile;
import it.flowzz.chunkloader.objects.connection.CustomPlayerConnection_1_12_R1;
import it.flowzz.chunkloader.objects.network.CustomNetworkManager_1_12_R1;
import net.minecraft.server.v1_12_R1.*;

public class CustomNPC_1_12_R1 extends EntityPlayer implements CustomNPC {

    public CustomNPC_1_12_R1(MinecraftServer minecraftserver, WorldServer worldserver, GameProfile gameprofile, PlayerInteractManager playerinteractmanager) {
        super(minecraftserver, worldserver, gameprofile, playerinteractmanager);
        NetworkManager nm = new CustomNetworkManager_1_12_R1();
        this.playerConnection = new CustomPlayerConnection_1_12_R1(this.server, nm, this);
        this.playerInteractManager.b(EnumGamemode.CREATIVE);
        this.fauxSleeping = true;
    }


}
