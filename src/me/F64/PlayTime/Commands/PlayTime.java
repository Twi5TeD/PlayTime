package me.F64.PlayTime.Commands;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import me.F64.PlayTime.Main;
import me.F64.PlayTime.Utils.Chat;
import me.F64.PlayTime.Utils.ConfigWrapper;
import me.F64.PlayTime.Utils.TimeFormat;

public class PlayTime implements CommandExecutor {
    Main plugin;
    public static ConfigWrapper PlayTimeConfig;

    public PlayTime(Main instance) {
        plugin = instance;
        PlayTime.PlayTimeConfig = new ConfigWrapper(instance, null, "config.yml");
        PlayTime.PlayTimeConfig.createFile(null,
                "# Playtime By F64_Rx - Need Help? PM me on Spigot or post in the discussion.\r\n"
                        + "# =================\r\n" + "# | CONFIGURATION |\r\n" + "# =================\r\n" + "\r\n"
                        + "# available placeholders\r\n" + "# %playtime_player% - returns the player name\r\n"
                        + "# %offlineplayer% - returns the offline player name\r\n"
                        + "# %offlinetime% - shows offline time of a player\r\n"
                        + "# %offlinetimesjoined% - shows the amount of joins a player has had\r\n"
                        + "# %playtime_time% - shows time played\r\n"
                        + "# %playtime_timesjoined% - shows the amount of times the player has joined the server\r\n"
                        + "# %playtime_serveruptime% - shows the uptime of the server\r\n"
                        + "# %playtime_top_#_place% - shows the place of the top 10\r\n"
                        + "# %playtime_top_#_name% - shows the name of the top 10\r\n"
                        + "# %playtime_top_#_time% - shows the time of the top 10\r\n"
                        + "# You can also use any other placeholder that PlaceholderAPI supports :) \r\n" + "");
        FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
        c.addDefault("time.second", "s");
        c.addDefault("time.minute", "m");
        c.addDefault("time.hour", "h");
        c.addDefault("time.day", "d");
        c.addDefault("messages.no_permission", Arrays.asList("&8[&bPlayTime&8] &cYou don't have permission."));
        c.addDefault("messages.doesnt_exist",
                Arrays.asList("&8[&bPlayTime&8] &cPlayer %offlineplayer% has not joined before!"));
        c.addDefault("messages.player", Arrays.asList("&b%playtime_player%'s Stats are:",
                "&bPlayTime: &7%playtime_time%", "&bTimes Joined: &7%playtime_timesjoined%"));
        c.addDefault("messages.offline_players", Arrays.asList("&b%offlineplayer%'s Stats are:",
                "&bPlayTime: &7%offlinetime%", "&bTimes Joined: &7%offlinetimesjoined%"));
        c.addDefault("messages.other_players", Arrays.asList("&b%playtime_player%'s Stats are:",
                "&bPlayTime: &7%playtime_time%", "&bTimes Joined: &7%playtime_timesjoined%"));
        c.addDefault("messages.playtimetop.header", Arrays.asList("&bTop &e10 &bplayers playtime:", ""));
        c.addDefault("messages.playtimetop.message", Arrays.asList("&a%position%. &b%player%: &e%playtime%"));
        c.addDefault("messages.playtimetop.footer", Arrays.asList(""));
        c.addDefault("messages.server_uptime",
                Arrays.asList("&8[&bPlayTime&8] &bServer's total uptime is %playtime_serveruptime%"));
        c.options().copyDefaults(true);
        PlayTime.PlayTimeConfig.saveConfig();
        PlayTime.PlayTimeConfig.reloadConfig();
    }

    public String getPlayerTime(String name) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(plugin.storagePath);
            JSONArray players = (JSONArray) jsonParser.parse(reader);
            for (Object o : players) {
                JSONObject player = (JSONObject) o;
                if (player.get("lastName").equals(name)) {
                    return player.get("time").toString();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPlayerJoins(String name) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(plugin.storagePath);
            JSONArray players = (JSONArray) jsonParser.parse(reader);
            for (Object o : players) {
                JSONObject player = (JSONObject) o;
                if (player.get("lastName").equals(name)) {
                    return player.get("joins").toString();
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
            if (cmd.getName().equalsIgnoreCase("playtime")) {
                if (!(sender.hasPermission("playtime.check"))) {
                    for (String NoPermission : c.getStringList("messages.no_permission"))
                        Chat.message(sender, p, NoPermission);
                    return true;
                }
                if (args.length == 0) {
                    for (String Player : c.getStringList("messages.player"))
                        Chat.message(sender, p, Player);
                } else {
                    Player t = plugin.getServer().getPlayer(args[0]);
                    if (t == null) {
                        String storedTime = getPlayerTime(args[0]);
                        String storedJoins = getPlayerJoins(args[0]);
                        if (storedTime == null || storedJoins == null) {
                            for (String NotOnline : c.getStringList("messages.doesnt_exist"))
                                Chat.message(sender, t, NotOnline.replace("%offlineplayer%", args[0]));
                        } else {
                            for (String OfflinePlayers : c.getStringList("messages.offline_players"))
                                Chat.message(sender, t,
                                        OfflinePlayers.replace("%offlineplayer%", args[0])
                                                .replace("%offlinetime%",
                                                        TimeFormat.getTime(Integer.valueOf(storedTime)))
                                                .replace("%offlinetimesjoined%", String.valueOf(storedJoins)));
                        }
                    } else {
                        for (String OtherPlayers : c.getStringList("messages.other_players"))
                            Chat.message(p, t, OtherPlayers);
                    }
                }
            }
            return true;
        }
        return false;
    }
}