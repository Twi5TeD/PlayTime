package me.F64.PlayTime.Commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import me.F64.PlayTime.Main;
import me.F64.PlayTime.Utils.Compatibility;
import me.F64.PlayTime.Utils.ConfigWrapper;
import me.F64.PlayTime.Utils.TimeFormat;
import me.clip.placeholderapi.PlaceholderAPI;

public class PlayTime implements CommandExecutor, Listener {
    Main plugin;
    public static ConfigWrapper PlayTimeConfig;
    public PlayTime(Main instance) {
        plugin = instance;
        PlayTime.PlayTimeConfig = new ConfigWrapper(instance, null, "config.yml");
        PlayTime.PlayTimeConfig.createFile(null ,"# Playtime By F64_Rx - Need Help? PM me on Spigot or post in the discussion.\r\n" + 
                "# =================\r\n" + 
                "# | CONFIGURATION |\r\n" + 
                "# =================\r\n" + 
                "\r\n" + 
                "# available placeholders\r\n" + 
                "# %playtime_player% - returns the player name\r\n" + 
                "# %offlineplayer% - returns the offline player name\r\n" + 
                "# %offlinetime% - shows offline time of a player\r\n" + 
                "# %offlinetimesjoined% - shows the amount of joins a player has had\r\n" + 
                "# %playtime_time% - shows time played\r\n" + 
                "# %playtime_timesjoined% - shows the amount of times the player has joined the server\r\n" + 
                "# %playtime_serveruptime% - shows the uptime of the server\r\n" + 
                "# You can also use any other placeholder that PlaceholderAPI supports :) \r\n" + 
                "");
        FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
        c.addDefault("time.second", "s");
        c.addDefault("time.minute", "m");
        c.addDefault("time.hour", "h");
        c.addDefault("time.day", "d");
        c.addDefault("messages.no_permission", Arrays.asList(
                "",
                "&8[&bPlayTime&8] &cYou don't have permission.",
                ""));
        c.addDefault("messages.doesnt_exist", Arrays.asList(
                "",
                "&8[&bPlayTime&8] &cPlayer %offlineplayer% has not joined before!",
                ""));
        c.addDefault("messages.player", Arrays.asList(
                "&8[&bPlayTime&8]",
                "&b%playtime_player%'s Stats are:",
                "&bPlayTime: &7%playtime_time%",
                "&bTimes Joined: &7%playtime_timesjoined%"));
        c.addDefault("messages.offline_players", Arrays.asList(
                "&8[&bPlayTime&8]",
                "&b%offlineplayer%'s Stats are:",
                "&bPlayTime: &7%offlinetime%",
                "&bTimes Joined: &7%offlinetimesjoined%"));
        c.addDefault("messages.other_players", Arrays.asList(
                "&8[&bPlayTime&8]",
                "&b%playtime_player%'s Stats are:",
                "&bPlayTime: &7%playtime_time%",
                "&bTimes Joined: &7%playtime_timesjoined%"));
        c.addDefault("messages.server_uptime", Arrays.asList(
                "",
                "&8[&bPlayTime&8] &bServer's total uptime is %playtime_serveruptime%",
                ""));
        c.options().copyDefaults(true);
        PlayTime.PlayTimeConfig.saveConfig();
        PlayTime.PlayTimeConfig.reloadConfig();
    }

    public static String format(String format) {
        return ChatColor.translateAlternateColorCodes('&', format);
    }

    public static int TicksPlayed(Player p) {
        if(!Compatibility.isLegacy()) {
            return  p.getStatistic(Statistic.valueOf("PLAY_ONE_MINUTE"))/20;
        }
        return p.getStatistic(Statistic.valueOf("PLAY_ONE_TICK"))/20;       
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
            if(cmd.getName().equalsIgnoreCase("playtime")) {
                if(!(sender.hasPermission("playtime.check"))) {
                    for(String NoPermission : c.getStringList("messages.no_permission"))
                        sender.sendMessage(PlaceholderAPI.setPlaceholders(p, format(NoPermission)));
                    return true;
                }
                if(args.length == 0) {
                    for (String Player : c.getStringList("messages.player"))
                        sender.sendMessage(PlaceholderAPI.setPlaceholders(p, format(Player))); 
                } else {
                    Player t = plugin.getServer().getPlayer(args[0]);
                    if (t == null) {
                        String storedTime = plugin.getPlayerTime(args[0]);
                        String storedJoins = plugin.getPlayerJoins(args[0]);
                        if (storedTime == null || storedJoins == null) {
                            for (String NotOnline : c.getStringList("messages.doesnt_exist"))
                                sender.sendMessage(PlaceholderAPI.setPlaceholders(t, format(NotOnline.replace("%offlineplayer%", args[0]))));
                        } else {
                            for (String OfflinePlayers :c.getStringList("messages.offline_players"))
                                sender.sendMessage(PlaceholderAPI.setPlaceholders(t, format(OfflinePlayers.replace("%offlineplayer%", args[0]).replace("%offlinetime%", TimeFormat.getTime(Integer.valueOf(storedTime))).replace("%offlinetimesjoined%", String.valueOf(storedJoins)))));
                        }
                    } else {
                        for (String OtherPlayers :c.getStringList("messages.other_players"))
                            sender.sendMessage(PlaceholderAPI.setPlaceholders(t, format(OtherPlayers)));
                    }
                }
            }
            return true;
        }
        return false;
    }
}