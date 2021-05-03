package me.F64.PlayTime.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;

import me.F64.PlayTime.Main;

public class UpdateChecker {
    static Main plugin;
    public static int resourceId;

    public UpdateChecker(Main instance, int resourceId) {
        plugin = instance;
        UpdateChecker.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> c) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId)
                    .openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    c.accept(scanner.next());
                }
            } catch (IOException exception) {
                plugin.getLogger().info("Cannot look for updates: " + exception.getMessage());
            }
        });
    }
}