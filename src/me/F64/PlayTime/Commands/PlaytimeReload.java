package me.F64.PlayTime.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.F64.PlayTime.Main;
import me.F64.PlayTime.Utils.Chat;

public class PlaytimeReload implements CommandExecutor {
    Main plugin;

    public PlaytimeReload(Main instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            FileConfiguration c = Playtime.config.getConfig();
            if (cmd.getName().equalsIgnoreCase("playtimereload")) {
                if (!(sender.hasPermission("playtime.reload"))) {
                    for (String noPermission : c.getStringList("messages.no_permission"))
                        Chat.message(sender, player, noPermission);
                    return true;
                }
                for (String reloadConfig : c.getStringList("messages.reload_config"))
                    Chat.message(sender, player, reloadConfig);
                Playtime.config.reloadConfig();
            }
        }
        return true;
    }
}