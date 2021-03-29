package me.F64.PlayTime.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.F64.PlayTime.Main;
import me.clip.placeholderapi.PlaceholderAPI;

public class Uptime implements CommandExecutor {
    Main plugin;
    public Uptime(Main instance) {
        plugin = instance;
    }

    public static String format(String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if(cmd.getName().equalsIgnoreCase("serveruptime")) {
                if (!(sender.hasPermission("playtime.uptime"))) {
                    for (String NoPermission : c.getStringList("messages.no_permission"))
                        sender.sendMessage(PlaceholderAPI.setPlaceholders(p, format(NoPermission)));
                    return true;
                }
                for (String ServerUptime : c.getStringList("messages.server_uptime"))
                    sender.sendMessage(PlaceholderAPI.setPlaceholders(p, format(ServerUptime)));
            }
        }
        return true;
    }
}