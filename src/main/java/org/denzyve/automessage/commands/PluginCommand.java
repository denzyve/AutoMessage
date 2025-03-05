package org.denzyve.automessage.commands;

import org.bukkit.configuration.file.FileConfiguration;
import org.denzyve.automessage.AutoMessage;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Command executor and tab completer for the plugin
 */
public class PluginCommand implements CommandExecutor, TabCompleter {
    
    private final AutoMessage plugin;
    private final List<String> subcommands = Arrays.asList("status", "toggle", "reload", "debug", "info", "list", "send");
    
    /**
     * Constructor for the command executor
     * 
     * @param plugin Reference to the main plugin instance
     */
    public PluginCommand(AutoMessage plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("automessage")) {
            return false;
        }

        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        String subCommand = args[0].toLowerCase();
        
        // Check permissions for each subcommand
        if (!hasPermission(sender, subCommand)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command.");
            return true;
        }

        switch (subCommand) {
            case "status":
                return handleStatusCommand(sender);
                
            case "toggle":
                return handleToggleCommand(sender);
                
            case "debug":
                return handleDebugCommand(sender);
                
            case "reload":
                return handleReloadCommand(sender);

            case "send":
                return handleSendCommand(sender, args);
                
            default:
                sender.sendMessage(ChatColor.RED + "Unknown subcommand. Use /automessage for help.");
                return true;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return subcommands.stream()
                .filter(s -> s.startsWith(args[0].toLowerCase()))
                .filter(s -> hasPermission(sender, s))
                .collect(Collectors.toList());
        }
        
        return new ArrayList<>();
    }
    
    /**
     * Checks if the sender has permission for a specific subcommand
     * 
     * @param sender The command sender
     * @param subCommand The subcommand to check
     * @return True if the sender has permission, false otherwise
     */
    private boolean hasPermission(CommandSender sender, String subCommand) {
        if (sender.hasPermission("automessage.admin")) {
            return true;
        }
        
        return sender.hasPermission("automessage." + subCommand);
    }
    
    /**
     * Shows the help message to the sender
     * 
     * @param sender The command sender
     */
    private void showHelp(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "AutoMessage Commands:");
        
        if (hasPermission(sender, "status")) {
            sender.sendMessage(ChatColor.YELLOW + "/automessage status " + ChatColor.WHITE + "- Show current settings");
        }
        
        if (hasPermission(sender, "toggle")) {
            sender.sendMessage(ChatColor.YELLOW + "/automessage toggle " + ChatColor.WHITE + "- Enable/disable plugin");
        }
        
        if (hasPermission(sender, "reload")) {
            sender.sendMessage(ChatColor.YELLOW + "/automessage reload " + ChatColor.WHITE + "- Reload configuration");
        }
        
        if (hasPermission(sender, "debug")) {
            sender.sendMessage(ChatColor.YELLOW + "/automessage debug " + ChatColor.WHITE + "- Toggle debug mode");
        }
    }
    
    /**
     * Handles the status subcommand
     * 
     * @param sender The command sender
     * @return True if the command was handled successfully
     */
    private boolean handleStatusCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "AutoMessage Status:");
        sender.sendMessage(ChatColor.YELLOW + "Plugin enabled: " + 
            (plugin.isPluginFunctionalityEnabled() ? ChatColor.GREEN + "Yes" : ChatColor.RED + "No"));
        sender.sendMessage(ChatColor.YELLOW + "Debug mode: " + 
            (plugin.isDebugEnabled() ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled"));
        
        return true;
    }
    
    /**
     * Handles the toggle subcommand
     * 
     * @param sender The command sender
     * @return True if the command was handled successfully
     */
    private boolean handleToggleCommand(CommandSender sender) {
        boolean newState = !plugin.isPluginFunctionalityEnabled();
        plugin.setPluginFunctionalityEnabled(newState);
        
        sender.sendMessage(ChatColor.YELLOW + "AutoMessage is now " +
            (newState ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
        
        return true;
    }
    
    /**
     * Handles the debug subcommand
     * 
     * @param sender The command sender
     * @return True if the command was handled successfully
     */
    private boolean handleDebugCommand(CommandSender sender) {
        boolean newState = !plugin.isDebugEnabled();
        plugin.setDebugEnabled(newState);
        
        sender.sendMessage(ChatColor.YELLOW + "Debug mode is now " + 
            (newState ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
        
        return true;
    }
    
    /**
     * Handles the reload subcommand
     * 
     * @param sender The command sender
     * @return True if the command was handled successfully
     */
    private boolean handleReloadCommand(CommandSender sender) {
        plugin.loadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded!");
        
        return true;
    }

    private boolean handleSendCommand(CommandSender sender, String[] args) {
        FileConfiguration config = plugin.getConfig();
        List<String> messages = config.getStringList("send-messages." + args[1]);

        if (messages.isEmpty()) {
            String singleMessage = config.getString("send-messages." + args[1]);
            if (singleMessage != null) {
                messages = Collections.singletonList(singleMessage);
            } else {
                sender.sendMessage(ChatColor.RED + "❌ No message found. Configure in config.yml");
                return true;
            }
        }

        for (String line : messages) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
        plugin.logDebug("Sended message[\"" + args[1] + "\"] to " + sender.getName());
        return true;
    }

}