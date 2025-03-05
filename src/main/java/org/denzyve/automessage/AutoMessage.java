package org.denzyve.automessage;

import org.denzyve.automessage.commands.PluginCommand;
import org.denzyve.automessage.util.MessageTimer;
import org.denzyve.automessage.util.UpdateChecker;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the plugin
 */
public class AutoMessage extends JavaPlugin {
    private FileConfiguration config;
    private boolean enabled;
    private boolean debug;

    @Override
    public void onEnable() {
        // Save default config if it doesn't exist
        saveDefaultConfig();
        loadConfig();
        loadMessages();

        
        // Register commands
        PluginCommand commandExecutor = new PluginCommand(this);
        getCommand("automessage").setExecutor(commandExecutor);
        getCommand("automessage").setTabCompleter(commandExecutor);

        
        // Initialize update checker if enabled
        if (config.getBoolean("update-checker.enabled", true)) {
            int resourceId = 123049;
            boolean notifyAdmins = config.getBoolean("update-checker.notify-admins", true);

            UpdateChecker updateChecker = new UpdateChecker(this, resourceId, notifyAdmins);
            updateChecker.checkForUpdates();
            logDebug("Update checker initialized with resource ID: " + resourceId);
        }
        
        getLogger().info("AutoMessage has been enabled!");
        logDebug("Debug mode is enabled");
    }

    /**
     * Loads configuration from config.yml
     */
    public void loadConfig() {
        reloadConfig();
        config = getConfig();
        enabled = config.getBoolean("enabled", true);
        debug = config.getBoolean("debug", false);

        
        logDebug("Configuration loaded");
    }

    /**
     * Logs a debug message if debug mode is enabled
     * 
     * @param message The message to log
     */
    public void logDebug(String message) {
        if (debug) {
            getLogger().info("[DEBUG] " + message);
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("AutoMessage has been disabled!");
    }

    
    /**
     * Checks if the plugin functionality is enabled
     * 
     * @return True if enabled, false otherwise
     */
    public boolean isPluginFunctionalityEnabled() {
        return enabled;
    }
    
    /**
     * Sets whether the plugin functionality is enabled
     * 
     * @param enabled True to enable, false to disable
     */
    public void setPluginFunctionalityEnabled(boolean enabled) {
        this.enabled = enabled;
        config.set("enabled", enabled);
        saveConfig();
    }
    
    /**
     * Checks if debug mode is enabled
     * 
     * @return True if debug mode is enabled, false otherwise
     */
    public boolean isDebugEnabled() {
        return debug;
    }
    
    /**
     * Sets whether debug mode is enabled
     * 
     * @param debug True to enable debug mode, false to disable
     */
    public void setDebugEnabled(boolean debug) {
        this.debug = debug;
        config.set("debug", debug);
        saveConfig();
    }


    private void loadMessages() {
        FileConfiguration config = getConfig();
        if (!config.contains("send-messages")) return;

        if (config.getConfigurationSection("send-messages") == null) return;

        for (String key : config.getConfigurationSection("send-messages").getKeys(false)) {
            if (!config.getBoolean("send-messages." + key + ".enabled", true)) continue;

            int interval = config.getInt("send-messages." + key + ".interval", 60);
            java.util.List<String> messages = config.getStringList("send-messages." + key + ".messages");

            if (messages.isEmpty()) continue;

            new MessageTimer(this, messages, interval).start();
        }
    }
}