package it.flowzz.chunkloader.objects.network;

import it.flowzz.chunkloader.objects.CustomChannel;
import net.minecraft.server.v1_12_R1.EnumProtocolDirection;
import net.minecraft.server.v1_12_R1.NetworkManager;

import java.lang.reflect.Field;

public class CustomNetworkManager_1_12_R1 extends NetworkManager {


    public CustomNetworkManager_1_12_R1() {
        super(EnumProtocolDirection.SERVERBOUND);
        swapFields();
    }

    private void swapFields() {
        try {
            Field channelField = NetworkManager.class.getDeclaredField("channel");
            channelField.setAccessible(true);
            channelField.set(this, new CustomChannel(null));
            channelField.setAccessible(false);

            Field socketAddressField = NetworkManager.class.getDeclaredField("l");
            socketAddressField.setAccessible(true);
            socketAddressField.set(this, null);
            socketAddressField.setAccessible(true);
            socketAddressField.set(this, null);
        }
        catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
