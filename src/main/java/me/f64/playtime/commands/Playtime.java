package me.f64.playtime.commands;

import java.io.File;
import java.io.FileReader;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;

import me.f64.playtime.Main;
import me.f64.playtime.utils.ConfigWrapper;
import me.f64.playtime.utils.TimeFormat;
import me.f64.playtime.utils.TopPlayers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import me.f64.playtime.utils.Chat;

public class Playtime implements TabExecutor {
    static Main plugin;
    public static ConfigWrapper config;

    public Playtime(Main instance) {
        plugin = instance;
        Playtime.config = new ConfigWrapper(instance, null, "config.yml");
        Playtime.config.createFile(null,
                "Playtime By F64_Rx - Need Help? PM me on Spigot or post in the discussion.\r\n" + "\r\n"
                        + " =================\r\n" + " | CONFIGURATION |\r\n" + " =================\r\n" + "\r\n"
                        + " available placeholders\r\n" + " %playtime_player% - returns the player name\r\n"
                        + " %offlineplayer% - returns the offline player name\r\n"
                        + " %offlinetime% - shows offline time of a player\r\n"
                        + " %offlinetimesjoined% - shows the amount of joins a player has had\r\n"
                        + " %playtime_time% - shows time played\r\n"
                        + " %playtime_timesjoined% - shows the amount of times the player has joined the server\r\n"
                        + " %playtime_serveruptime% - shows the uptime of the server\r\n"
                        + " %playtime_position% - shows the players current position\r\n"
                        + " %playtime_top_#_name% - shows the name of the top 10\r\n"
                        + " %playtime_top_#_time% - shows the time of the top 10\r\n"
                        + " You can also use any other placeholder that PlaceholderAPI supports :) \r\n" + "");
        FileConfiguration c = Playtime.config.getConfig();
        c.addDefault("time.second.enabled", true);
        c.addDefault("time.second.prefix", "s");
        c.addDefault("time.minute.enabled", true);
        c.addDefault("time.minute.prefix", "m");
        c.addDefault("time.hour.enabled", true);
        c.addDefault("time.hour.prefix", "h");
        c.addDefault("time.day.enabled", true);
        c.addDefault("time.day.prefix", "d");
        c.addDefault("time.week.enabled", true);
        c.addDefault("time.week.prefix", "w");
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
        c.addDefault("messages.reload_config",
                Arrays.asList("&8[&bPlayTime&8] &bYou have successfully reloaded the config."));
        c.addDefault("placeholder.top.name", "none");
        c.addDefault("placeholder.top.time", "-");
        c.options().copyDefaults(true);
        Playtime.config.saveConfig();
    }

    public String getPlayerTime(String name) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(plugin.getPlayerPath(name));
            JSONObject player = (JSONObject) jsonParser.parse(reader);
            reader.close();
            return player.get("time").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPlayerJoins(String name) {
        JSONParser jsonParser = new JSONParser();
        try {
            FileReader reader = new FileReader(plugin.getPlayerPath(name));
            JSONObject player = (JSONObject) jsonParser.parse(reader);
            reader.close();
            return player.get("joins").toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static TopPlayers[] getTopTen() {
        TopPlayers[] topTen = {};
        try {
            JSONParser jsonParser = new JSONParser();

            File dir = new File(plugin.storagePath);

            File[] fileList = dir.listFiles();

            if (fileList != null) {
                ArrayList<TopPlayers> allPlayers = new ArrayList<>();

                for (File jsonFile : fileList) {
                    FileReader reader = new FileReader(jsonFile);
                    JSONObject player = (JSONObject) jsonParser.parse(reader);
                    reader.close();

                    allPlayers.add(new TopPlayers(player.get("lastName").toString(), player.get("uuid").toString(),
                            Integer.parseInt(player.get("time").toString() )));
                }

                allPlayers.sort(Comparator.comparing(e -> e.time));
                Collections.reverse(allPlayers);

                int len = Math.min(allPlayers.size(), 10);
                topTen = new TopPlayers[len];
                topTen[0] = allPlayers.get(0);

                for (int i = 0; i < len; ++i) {
                    topTen[i] = allPlayers.get(i);
                }
            }
            return topTen;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topTen;
    }

    public static TopPlayers[] checkOnlinePlayers(TopPlayers[] top10) {
        Chat chat = new Chat(plugin);
        for (Player player : plugin.getServer().getOnlinePlayers())
            if (chat.ticksPlayed(player) > (top10.length == 0 ? 0 : top10[top10.length - 1].time)) {
                TopPlayers top = new TopPlayers(player.getName(), player.getUniqueId().toString(),
                        chat.ticksPlayed(player));
                for (int i = 0; i < top10.length; ++i)
                    if (top10[i].time <= top.time)
                        if (top10[i].uuid.equals(top.uuid)) {
                            top10[i] = top;
                            break;
                        } else {
                            TopPlayers temp = top10[i];
                            top10[i] = (top = temp);
                        }
            }
        return top10;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            FileConfiguration c = Playtime.config.getConfig();
            if (cmd.getName().equalsIgnoreCase("playtime")) {
                if (!(sender.hasPermission("playtime.check"))) {
                    for (String noPermission : c.getStringList("messages.no_permission"))
                        Chat.message(sender, player, noPermission);
                    return true;
                }
                if (args.length == 0) {
                    for (String thisPlayer : c.getStringList("messages.player"))
                        Chat.message(sender, player, thisPlayer);
                } else {
                    if (args[0].equals("reload")) {
                        if (!(sender.hasPermission("playtime.reload"))) {
                            for (String noPermission : c.getStringList("messages.no_permission"))
                                Chat.message(sender, player, noPermission);
                            return true;
                        }
                        for (String reloadConfig : c.getStringList("messages.reload_config"))
                            Chat.message(sender, player, reloadConfig);
                        Playtime.config.reloadConfig();
                    } else if (args[0].equals("uptime")) {
                        if (!(sender.hasPermission("playtime.uptime"))) {
                            for (String noPermission : c.getStringList("messages.no_permission"))
                                Chat.message(sender, player, noPermission);
                            return true;
                        }
                        for (String serverUptime : c.getStringList("messages.server_uptime"))
                            Chat.message(sender, player, serverUptime);
                    } else if (args[0].equals("top")) {
                        TopPlayers[] top10;
                        top10 = getTopTen();
                        top10 = checkOnlinePlayers(top10);
                        for (String header : c.getStringList("messages.playtimetop.header"))
                            Chat.message(sender, player, header);
                        for (int i = 0; i < top10.length; i++) {
                            if (top10[i].time == 0) {
                                break;
                            }
                            for (String message : c.getStringList("messages.playtimetop.message"))
                                Chat.message(sender, player, message.replace("%position%", Integer.toString(i + 1))
                                        .replace("%player%", top10[i].name).replace("%playtime%",
                                                TimeFormat.getTime(Duration.of(top10[i].time, ChronoUnit.SECONDS))));
                        }
                        for (String footer : c.getStringList("messages.playtimetop.footer"))
                            Chat.message(sender, player, footer);

                    } else {
                        Player target = plugin.getServer().getPlayer(args[0]);
                        if (target == null) {
                            String storedTime = getPlayerTime(args[0]);
                            String storedJoins = getPlayerJoins(args[0]);
                            if (storedTime == null || storedJoins == null) {
                                for (String notOnline : c.getStringList("messages.doesnt_exist"))
                                    Chat.message(sender, target, notOnline.replace("%offlineplayer%", args[0]));
                            } else {
                                for (String offlinePlayers : c.getStringList("messages.offline_players"))
                                    Chat.message(sender, target,
                                            offlinePlayers.replace("%offlineplayer%", args[0])
                                                    .replace("%offlinetime%",
                                                            TimeFormat.getTime(Duration.of(Integer.valueOf(storedTime),
                                                                    ChronoUnit.SECONDS)))
                                                    .replace("%offlinetimesjoined%", storedJoins));
                            }
                        } else {
                            for (String otherPlayer : c.getStringList("messages.other_players"))
                                Chat.message(player, target, otherPlayer);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] args) {
        List<String> tabComplete = new ArrayList<>();
        tabComplete.add("reload");
        tabComplete.add("uptime");
        tabComplete.add("top");
        for (Player p : plugin.getServer().getOnlinePlayers())
            tabComplete.add(p.getName());
        if (args.length == 1) {
            return StringUtil.copyPartialMatches(args[0], tabComplete, new ArrayList<>());
        }
        return null;
    }
}