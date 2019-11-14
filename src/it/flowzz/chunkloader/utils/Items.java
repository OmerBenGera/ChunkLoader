package it.flowzz.chunkloader.utils;

import it.flowzz.chunkloader.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class Items {

    private static FileConfiguration config = Main.getInstance().getConfig();

    public static ItemStack getChunkLoader(int amount, int time){
        String name;
        if(time > 0) {
            String[] duration = Util.getTimeFromSecond(time);
            name = ChatColor.translateAlternateColorCodes('&', config.getString("ChunkLoader-Layout.name")
                    .replace("%days%", duration[0])
                    .replace("%hours%", duration[1])
                    .replace("%minutes%", duration[2])
                    .replace("%seconds%", duration[3])
            );
        }else {
            name = ChatColor.translateAlternateColorCodes('&', config.getString("ChunkLoader-Permanent-Layout.name")
                    .replace("%time%", "∞")
            );
        }
        ItemStack item = new ItemStack(Material.getMaterial(config.getInt("ChunkLoader-Layout.id")), amount, (short)config.getInt("ChunkLoader-Layout.subid"));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        ArrayList<String> lore = new ArrayList<>();
        for(String s : config.getStringList("ChunkLoader-Layout.lore")){
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return NbtUtil.set(item, time,"time");
    }
}

