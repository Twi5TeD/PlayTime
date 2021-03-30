package me.F64.PlayTime.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.F64.PlayTime.Main;
import me.F64.PlayTime.Utils.Chat;

public class Uptime implements CommandExecutor {
    Main plugin;
    public Uptime(Main instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
            if(cmd.getName().equalsIgnoreCase("serveruptime")) {
                if (!(sender.hasPermission("playtime.uptime"))) {
                    for (String NoPermission : c.getStringList("messages.no_permission"))
                        Chat.message(sender, p, NoPermission);
                    return true;
                }
                for (String ServerUptime : c.getStringList("messages.server_uptime"))
                    Chat.message(sender, p, ServerUptime);
            }
        }
        return true;
    }
}