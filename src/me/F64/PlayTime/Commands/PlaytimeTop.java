package me.F64.PlayTime.Commands;

import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

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
import me.F64.PlayTime.Utils.TimeFormat;
import me.F64.PlayTime.Utils.TopPlayers;

public class PlaytimeTop implements CommandExecutor {
    static Main plugin;

    public PlaytimeTop(Main instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            FileConfiguration c = Playtime.config.getConfig();
            if (cmd.getName().equalsIgnoreCase("playtimetop")) {
                if (!(sender.hasPermission("playtime.check"))) {
                    for (String noPermission : c.getStringList("messages.no_permission"))
                        Chat.message(sender, player, noPermission);
                    return true;
                }
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
                        Chat.message(sender, player,
                                message.replace("%position%", Integer.toString(i + 1))
                                .replace("%player%", top10[i].name).replace("%playtime%",
                                        TimeFormat.getTime(Duration.of(top10[i].time, ChronoUnit.SECONDS))));
                }
                for (String footer : c.getStringList("messages.playtimetop.footer"))
                    Chat.message(sender, player, footer);
            }
        }
        return true;
    }

    public static TopPlayers[] getTopTen() {
        TopPlayers[] topTen = {};
        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(plugin.storagePath);
            JSONArray players = (JSONArray) jsonParser.parse(reader);
            int len = Math.min(players.size(), 10);
            topTen = new TopPlayers[len];
            for (int i = 0; (i < len); i++) {
                JSONObject player = (JSONObject) players.get(i);
                TopPlayers top = new TopPlayers(player.get("lastName").toString(), player.get("uuid").toString(),
                        Integer.parseInt(player.get("time").toString()));
                topTen[i] = top;
            }
            return topTen;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return topTen;
    }

    public static TopPlayers[] checkOnlinePlayers(TopPlayers[] top10) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (Chat.ticksPlayed(player) > top10[top10.length - 1].time) {
                TopPlayers top = new TopPlayers(player.getName(), player.getUniqueId().toString(),
                        Chat.ticksPlayed(player));
                for (int i = 0; i < top10.length; i++) {
                    if (top10[i].time < top.time) {
                        if (top10[i].uuid.equals(top.uuid)) {
                            top10[i] = top;
                            break;
                        } else {
                            TopPlayers temp = top10[i];
                            top10[i] = top;
                            top = temp;
                        }
                    }
                }
            }
        }
        return top10;
    }
}