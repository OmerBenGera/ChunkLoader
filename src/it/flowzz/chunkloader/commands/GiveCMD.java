package it.flowzz.chunkloader.commands;

import it.flowzz.chunkloader.Main;
import it.flowzz.chunkloader.utils.Items;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class GiveCMD implements CommandExecutor {

    private static FileConfiguration lang = Main.getInstance().getLang();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("chunkloader.give"))
            return false;
        if (args.length < 4) {
            sendSyntax(sender);
            return false;
        }
        if (args[0].equalsIgnoreCase("give")) {
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("chunkloader-player-offline")));
                return false;
            }
            try {
                int number = Integer.parseInt(args[2]);
                int time = Integer.parseInt(args[3]);
                target.getInventory().addItem(Items.getChunkLoader(number,time));
            }
            catch (Exception e) {
                sendSyntax(sender);
                return false;
            }
        }
        return false;
    }

    private void sendSyntax(CommandSender sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', lang.getString("chunkloader-syntax")));
    }

}
