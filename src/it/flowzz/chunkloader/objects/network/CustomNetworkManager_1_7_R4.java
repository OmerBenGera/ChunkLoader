package it.flowzz.chunkloader.objects.network;

import it.flowzz.chunkloader.objects.CustomChannel;
import net.minecraft.server.v1_7_R4.NetworkManager;

import java.lang.reflect.Field;

public class CustomNetworkManager_1_7_R4 extends NetworkManager {

    public CustomNetworkManager_1_7_R4() {
        super(false);
        swapFields();
    }

    private void swapFields() {
        try {
            Field channelField = NetworkManager.class.getDeclaredField("m");
            channelField.setAccessible(true);
            channelField.set(this, new CustomChannel(null));
            channelField.setAccessible(false);

            Field socketAddressField = NetworkManager.class.getDeclaredField("n");
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
