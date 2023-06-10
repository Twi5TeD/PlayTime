package me.f64.playtime.utils;

import me.f64.playtime.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.clip.placeholderapi.PlaceholderAPI;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

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

    public int ticksPlayed(Player player) {
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(plugin.getPlayerPath(player.getName()));
            JSONObject playerJson = (JSONObject) jsonParser.parse(reader);
            reader.close();

            int sessionOnTime = (int) (System.currentTimeMillis()
                    - plugin.Sessions.get(player.getUniqueId().toString())) / 1000;

            return Integer.parseInt(playerJson.get("time").toString()) + sessionOnTime;
        } catch (Exception e) {
            e.printStackTrace();
            if (!Compatibility.IS_LEGACY) {
                return player.getStatistic(Statistic.valueOf("PLAY_ONE_MINUTE")) / 20;
            }
            return player.getStatistic(Statistic.valueOf("PLAY_ONE_TICK")) / 20;
        }
    }

    public int sessionsPlayed(Player player) {
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(plugin.getPlayerPath(player.getName()));
            JSONObject playerJson = (JSONObject) jsonParser.parse(reader);
            reader.close();

            return Integer.parseInt(playerJson.get("joins").toString()) + 1;
        } catch (Exception e) {
            e.printStackTrace();

            return player.getStatistic(Statistic.LEAVE_GAME) + 1;
        }
    }
}
