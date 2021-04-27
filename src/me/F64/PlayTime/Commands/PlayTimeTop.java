package me.F64.PlayTime.Commands;

import java.io.FileReader;
import java.io.IOException;

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

public class PlayTimeTop implements CommandExecutor {
    static Main plugin;

    public PlayTimeTop(Main instance) {
        plugin = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
            if (cmd.getName().equalsIgnoreCase("playtimetop")) {
                if (!(sender.hasPermission("playtime.check"))) {
                    for (String NoPermission : c.getStringList("messages.no_permission"))
                        Chat.message(sender, p, NoPermission);
                    return true;
                }
                TopPlayers[] top10 = getTopTen();
                top10 = checkOnlinePlayers(top10);
                for (String header : c.getStringList("messages.playtimetop.header"))
                    Chat.message(sender, p, header);
                for (int i = 0; i < top10.length; i++) {
                    if (top10[i].time == 0) {
                        break;
                    }
                    for (String message : c.getStringList("messages.playtimetop.message"))
                        Chat.message(sender, p,
                                message.replace("%position%", Integer.toString(i + 1))
                                        .replace("%player%", top10[i].name)
                                        .replace("%playtime%", TimeFormat.getTime(top10[i].time)));
                }
                for (String footer : c.getStringList("messages.playtimetop.footer"))
                    Chat.message(sender, p, footer);
            }
        }
        return true;
    }

    public static TopPlayers[] getTopTen() {
        TopPlayers[] topTen = new TopPlayers[10];
        for (int i = 0; i < 10; i++)
            topTen[i] = new TopPlayers();

        try {
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader(plugin.storagePath);
            JSONArray players = (JSONArray) jsonParser.parse(reader);
            if (players.size() > 0) {
                for (int i = 0; (i < 10) && (i < players.size()); i++) {
                    JSONObject player = (JSONObject) players.get(i);
                    TopPlayers target = new TopPlayers(player.get("lastName").toString(), player.get("uuid").toString(),
                            Integer.parseInt(player.get("time").toString()));
                    topTen[i] = target;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return topTen;
    }

    public static TopPlayers[] checkOnlinePlayers(TopPlayers[] top10) {
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            if (Chat.TicksPlayed(p) > top10[9].time) {
                TopPlayers newPlayer = new TopPlayers(p.getName(), p.getUniqueId().toString(), Chat.TicksPlayed(p));
                for (int i = 0; i < 10; i++) {
                    if (top10[i].time < newPlayer.time) {
                        if (top10[i].uuid.equals(newPlayer.uuid)) {
                            top10[i] = newPlayer;
                            break;
                        } else {
                            TopPlayers temp = top10[i];
                            top10[i] = newPlayer;
                            newPlayer = temp;
                        }
                    }
                }
            }
        }
        return top10;
    }
}