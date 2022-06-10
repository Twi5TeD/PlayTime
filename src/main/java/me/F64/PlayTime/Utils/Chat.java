package me.F64.PlayTime.Utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.F64.PlayTime.Main;
import me.clip.placeholderapi.PlaceholderAPI;

public class Chat {
    static Main plugin;

    public Chat(Main instance) {
        plugin = instance;
    }

    public static String format(String commandLabel) {
        return ChatColor.translateAlternateColorCodes('&', commandLabel);
    }

    public static String message(CommandSender sender, Player player, String commandLabel) {
        sender.sendMessage(PlaceholderAPI.setPlaceholders(player, format(commandLabel)));
        return commandLabel;
    }

    public static String console(String commandLabel) {
        Bukkit.getConsoleSender().sendMessage(format(commandLabel));
        return commandLabel;
    }

    public static int ticksPlayed(Player player) {
        if (!Compatibility.IS_LEGACY) {
            return player.getStatistic(Statistic.valueOf("PLAY_ONE_MINUTE")) / 20;
        }
        return player.getStatistic(Statistic.valueOf("PLAY_ONE_TICK")) / 20;
    }
}