package org.denzyve.automessage.util;

import org.bukkit.Bukkit;
import org.denzyve.automessage.AutoMessage;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class MessageTimer extends BukkitRunnable {

    private final AutoMessage plugin;
    private final List<String> messages;
    private final int interval;

    public MessageTimer(AutoMessage plugin, List<String> messages, int interval) {
        this.plugin = plugin;
        this.messages = messages;
        this.interval = interval;
    }

    public void start() {
        this.runTaskTimer(plugin, interval * 20L, interval * 20L);
    }

    @Override
    public void run() {
        for (String message : messages) {
            Bukkit.broadcastMessage(message.replace("&", "§")); // Підтримка кольорів
        }

    }
}
