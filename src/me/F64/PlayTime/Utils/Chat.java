package me.F64.PlayTime.Utils;

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
    public static String format(String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    public static String message(CommandSender sender,  Player p, String message) {
        sender.sendMessage(PlaceholderAPI.setPlaceholders(p, Chat.format(message)));
        return message;
    }

    public static int TicksPlayed(Player p) {
        if(!Compatibility.isLegacy()) {
            return p.getStatistic(Statistic.valueOf("PLAY_ONE_MINUTE"))/20;
        }
        return p.getStatistic(Statistic.valueOf("PLAY_ONE_TICK"))/20;       
    }
}