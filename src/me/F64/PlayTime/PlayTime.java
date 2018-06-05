package me.F64.PlayTime;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class PlayTime implements CommandExecutor, Listener {
	 Main plugin;

	 static ConfigWrapper PlayTimeConfig;

	static long Time;
	
	
	
	 public PlayTime(Main instance) {
		  plugin = instance;
		  
		  PlayTime.PlayTimeConfig = new ConfigWrapper(instance, null, "config.yml");
		  PlayTime.PlayTimeConfig
		   .createFile(
				   null,
				   "# Playtime By F64_Rx - Need Help? PM me on Spigot or post in the discussion.\r\n" + 
				   "# =================\r\n" + 
				   "# | CONFIGURATION |\r\n" + 
				   "# =================\r\n" + 
				   "\r\n" + 
				   "# available placeholders\r\n" + 
				   "# %player% - returns the player name\r\n" + 
				   "# %time% - shows time played\r\n" + 
				   "# %timesjoined% - shows the amount of times the player has joined the server\r\n" + 
				   "# %serveruptime% - shows the uptime of the server\r\n" + 
				   "# %prefix% - shows the prefix\r\n" + 
				   "");

		  FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
		  c.addDefault("prefix", "&3[&aPlaytime&3]");
		  c.addDefault("time.second", "s");
		  c.addDefault("time.minute", "m");
		  c.addDefault("time.hour", "h");
		  c.addDefault("time.day", "d");
		  
		  c.addDefault("messages.no_permission", Arrays.asList(
				  "",
				  "&8[&bPlayTime&8] &cYou don't have permission.",
				  ""));
		  
		  c.addDefault("messages.not_online", Arrays.asList(
				  "",
				  "&8[&bPlayTime&8] &cPlayer %player% is not online!",
				  ""));
		  
		  c.addDefault("messages.player", Arrays.asList(
				  "",
				  "&8[&bPlayTime&8]",
				  "&b%player%'s Stats are:",
				  "&bPlayTime: &7%time%",
				  "&bTimes Joined: &7%timesjoined%",
				  ""
		  ));

		  c.addDefault("messages.other_players", Arrays.asList(
				   "",
				   "&8[&bPlayTime&8]",
				   "&b%player%'s Stats are:",
				   "&bPlayTime: &7%time%",
				   "&bTimes Joined: &7%timesjoined%",
				   ""
		  ));
		  
		  c.addDefault("messages.server_uptime", Arrays.asList(
				   "",
				   "&8[&bPlayTime&8] &bServer's total uptime is %serveruptime%",
				   ""
		 ));
		  
		  c.options().copyDefaults(true);
		  PlayTime.PlayTimeConfig.saveConfig();
		  PlayTime.PlayTimeConfig.reloadConfig();
		 }
	
	public static String format(String format) {
		return ChatColor.translateAlternateColorCodes('&', format);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if ((sender instanceof Player)) {
			  FileConfiguration c = PlayTime.PlayTimeConfig.getConfig();
			Player p = (Player) sender;
			if ((cmd.getName().equalsIgnoreCase("playtime"))) {
				if (!(sender.hasPermission("playtime.check"))) {
					for (String NoPermission : c.getStringList("messages.no_permission"))
					sender.sendMessage(format((NoPermission)).replace("%prefix%",format(c.getString("prefix"))));
					return true;
					}
				if (args.length == 0) { 
					for (String Player : c.getStringList("messages.player"))
								sender.sendMessage(format((Player)).replace("%player%",sender.getName()).replace("%time%",TimeFormat.getTime(p.getStatistic(Statistic.PLAY_ONE_TICK)/20)).replace("%timesjoined%",String.valueOf(p.getStatistic(Statistic.LEAVE_GAME)+1)).replace("%prefix%",format(c.getString("prefix"))));
					} else {
						Player target = Bukkit.getServer().getPlayer(args[0]);
						if (target == null) {
							for (String NotOnline : c.getStringList("messages.not_online"))
								sender.sendMessage(format((NotOnline).replace("%player%",args[0])).replace("%prefix%",format(c.getString("prefix"))));
							return true;
							}
						Player t = Bukkit.getPlayer(args[0]);
						for (String OtherPlayers :c.getStringList("messages.other_players"))
						sender.sendMessage(format((OtherPlayers)).replace("%player%",t.getName()).replace("%time%",TimeFormat.getTime(t.getStatistic(Statistic.PLAY_ONE_TICK)/20)).replace("%timesjoined%",String.valueOf(t.getStatistic(Statistic.LEAVE_GAME)+1)).replace("%prefix%",format(c.getString("prefix"))));
						}
				return true;
				}
			if ((cmd.getName().equalsIgnoreCase("serveruptime"))) {
				if (!(sender.hasPermission("playtime.uptime"))) {
					for (String NoPermission : c.getStringList("messages.no_permission"))
					sender.sendMessage(format((NoPermission)).replace("%prefix%",format(c.getString("prefix"))));
					return true;
					}
				for (String ServerUptime : c.getStringList("messages.server_uptime"))
					sender.sendMessage(format((ServerUptime)).replace("%serveruptime%",TimeFormat.Uptime()).replace("%prefix%",format(c.getString("prefix"))));
				return true;
				}
			}
		return true;
		}
	}