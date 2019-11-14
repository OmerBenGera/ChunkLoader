package it.flowzz.chunkloader.objects.connection;

import net.minecraft.server.v1_7_R4.*;

public class CustomPlayerConnection_1_7_R4 extends PlayerConnection {
    public CustomPlayerConnection_1_7_R4(MinecraftServer minecraftserver, NetworkManager networkmanager, EntityPlayer entityplayer) {
        super(minecraftserver, networkmanager, entityplayer);
    }


    public void a(PacketPlayInWindowClick packetplayinwindowclick) {}

    public void a(PacketPlayInTransaction packetplayintransaction) {}

    public void a(PacketPlayInFlying packetplayinflying) {}

    public void a(PacketPlayInUpdateSign packetplayinupdatesign) {}

    public void a(PacketPlayInBlockDig packetplayinblockdig) {}

    public void a(PacketPlayInBlockPlace packetplayinblockplace) {}

    public void disconnect(String s) {}

    public void a(PacketPlayInHeldItemSlot packetplayinhelditemslot) {}

    public void a(PacketPlayInChat packetplayinchat) {}

    public void sendPacket(Packet packet) {}
}
