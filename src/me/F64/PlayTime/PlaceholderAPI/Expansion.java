package me.F64.PlayTime.PlaceholderAPI;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import me.F64.PlayTime.Main;
import me.F64.PlayTime.Commands.PlayTimeTop;
import me.F64.PlayTime.Utils.Chat;
import me.F64.PlayTime.Utils.TimeFormat;
import me.F64.PlayTime.Utils.TopPlayers;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public class Expansion extends PlaceholderExpansion {
    private Main plugin;
    public Expansion(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean persist(){
        return true;
    }

    @Override
    public boolean canRegister(){
        return true;
    }

    @Override
    public String getAuthor(){
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getIdentifier(){
        return "playtime";
    }

    @Override
    public String getVersion(){
        return plugin.getDescription().getVersion();
    }

    @Override
    public String onPlaceholderRequest(Player p, String identifier) {
        if (p == null) return "";
        if (identifier.equals("player")) return String.valueOf(p.getName());
        if (identifier.equals("time")) return String.valueOf(TimeFormat.getTimeFormatted(Chat.TicksPlayed(p)));
        if (identifier.equals("timesjoined")) return String.valueOf(p.getStatistic(Statistic.LEAVE_GAME) + 1);
        if (identifier.equals("serveruptime")) return String.valueOf(TimeFormat.Uptime());
        TopPlayers[] top10 = PlayTimeTop.getTopTen();
        top10 = PlayTimeTop.checkOnlinePlayers(top10);
        for (int i = 1; i < top10.length; i++) {
            if (top10[i].time == 0) {
                break;
            }
            if(identifier.equals("top_" + i + "_place")) return Integer.toString(i);
            if(identifier.equals("top_" + i + "_name")) return top10[i].name;
            if(identifier.equals("top_" + i + "_time")) return TimeFormat.getTimeFormatted(top10[i].time);
        }        
        return null;
    }
}