package it.flowzz.chunkloader.listeners;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.struct.Role;
import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import it.flowzz.chunkloader.Main;
import it.flowzz.chunkloader.loaders.ChunkLoader;
import it.flowzz.chunkloader.utils.Items;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.UUID;

public class FactionUUIDListener implements Listener {
    private static FileConfiguration lang = Main.getInstance().getLang();


    @EventHandler
    public void onLoaderBreak(BlockBreakEvent event) {
        if(event.isCancelled())
            return;
        if (event.getBlock().getType() != Items.getChunkLoader(1,1).getType())
            return;
        ChunkLoader chunkLoader = ChunkLoader.getChunkLoader(event.getBlock().getChunk());
        if(chunkLoader != null){
            if (event.getPlayer().hasPermission("Chunkloader.breakbypass") || hasRight(event.getPlayer(), chunkLoader)) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("chunkloader-broken")));
                event.setCancelled(true);
                event.getPlayer().getInventory().addItem(Items.getChunkLoader(1, chunkLoader.getTime()));
                chunkLoader.delete(false);
                Main.getInstance().saveChunkloaders();
            }else { event.setCancelled(true); }
        }
    }

    private boolean playerCanBreak(Player player) {
            if (Main.getInstance().getConfig().getBoolean("Settings.onlyModeratorCanBreak"))
                return FPlayers.getInstance().getByPlayer(player).getRole().isAtLeast(Role.MODERATOR);
            return true;
    }

    private boolean hasRight(Player player, ChunkLoader loader){
        if(Main.getInstance().isFactionUUID()) {
            String id = Board.getInstance().getIdAt(new FLocation(loader.getLocation()));
            if (FPlayers.getInstance().getByPlayer(player).getFactionId().equals(id) || id.equals(Factions.getInstance().getWilderness().getId())) {
                if (!playerCanBreak(player) && !id.equals(Factions.getInstance().getWilderness().getId())) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("chunkloader-not-moderator")));
                    return false;
                }
                return true;
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',lang.getString("chunkloader-wrong-faction")));
                return false;
            }
        }
        if(Main.getInstance().isSkyblock()){
            Island island = ASkyBlockAPI.getInstance().getIslandAt(loader.getLocation());
            if(island == null || !island.getMembers().contains(player.getUniqueId())){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("chunkloader-not-your-island")));
                return false;
            }
            return true;
        }
        else if(Main.getInstance().isSupSkyblock()){
            com.bgsoftware.superiorskyblock.api.island.Island island = SuperiorSkyblockAPI.getIslandAt(loader.getLocation());
            if(island == null || !island.getAllMembers().contains(player.getUniqueId())){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("chunkloader-not-your-island")));
                return false;
            }
            return true;

        }
        return false;
    }
}
