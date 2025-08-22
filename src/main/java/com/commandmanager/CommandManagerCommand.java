package com.commandmanager;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

public class CommandManagerCommand implements CommandExecutor {
    private final ConfigManager configManager;

    public CommandManagerCommand(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sendHelpMessage(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                return handleReload(sender);
            case "help":
                sendHelpMessage(sender);
                return true;
            case "add":
                return handleAddCommand(sender, args);
            case "list":
                return handleList(sender);
            case "remove":
                return handleRemoveCommand(sender, args);
            case "edit":
                return handleEditCommand(sender, args);
            default:
                sender.sendMessage(ChatColor.RED + "未知命令! 使用 /commandmanager help 查看帮助");
                return true;
        }
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("commandmanager.reload")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此操作!");
            return true;
        }

        configManager.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "配置文件已重新加载!");
        return true;
    }

    private boolean handleAddCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("commandmanager.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此操作!");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "用法: /commandmanager add <命令> <权限>");
            return true;
        }

        String command = args[1].toLowerCase();
        String permission = args[2];

        configManager.addCommandPermission(command, permission);
        sender.sendMessage(ChatColor.GREEN + "已添加命令限制: /" + command + " 需要权限: " + permission);
        return true;
    }

    private boolean handleList(CommandSender sender) {
        if (!sender.hasPermission("commandmanager.reload")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此操作!");
            return true;
        }

        ConfigurationSection commandsSection = configManager.getConfig().getConfigurationSection("commands");
        if (commandsSection == null) {
            sender.sendMessage(ChatColor.YELLOW + "当前没有配置受限制的命令。");
            return true;
        }

        Set<String> commandNames = commandsSection.getKeys(false);
        if (commandNames.isEmpty()) {
            sender.sendMessage(ChatColor.YELLOW + "当前没有配置受限制的命令。");
            return true;
        }

        sender.sendMessage(ChatColor.GOLD + "===== 受限制命令列表 ======");
        for (String commandName : commandNames) {
            String permission = configManager.getCommandPermission(commandName);
            if (permission != null) {
                sender.sendMessage(ChatColor.YELLOW + "/" + commandName + " - 权限: " + permission);
            }
        }
        sender.sendMessage(ChatColor.GOLD + "==========================");
        return true;
    }

    private boolean handleRemoveCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("commandmanager.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此操作!");
            return true;
        }

        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: /commandmanager remove <命令>");
            return true;
        }

        String command = args[1].toLowerCase();
        boolean removed = configManager.removeCommandPermission(command);
        
        if (removed) {
            sender.sendMessage(ChatColor.GREEN + "已移除命令限制: /" + command);
        } else {
            sender.sendMessage(ChatColor.RED + "命令 /" + command + " 不存在于限制列表中!");
        }
        return true;
    }
    
    private boolean handleEditCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("commandmanager.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此操作!");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "用法: /commandmanager edit <命令> <新权限>");
            return true;
        }

        String command = args[1].toLowerCase();
        String newPermission = args[2];
        boolean edited = configManager.editCommandPermission(command, newPermission);
        
        if (edited) {
            sender.sendMessage(ChatColor.GREEN + "已更新命令限制: /" + command + " 现在需要权限: " + newPermission);
        } else {
            sender.sendMessage(ChatColor.RED + "命令 /" + command + " 不存在于限制列表中!");
        }
        return true;
    }
    
    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "===== CommandManager 帮助 ======");
        sender.sendMessage(ChatColor.YELLOW + "/commandmanager help - 显示此帮助信息");
        sender.sendMessage(ChatColor.YELLOW + "/commandmanager reload - 重新加载配置文件");
        sender.sendMessage(ChatColor.YELLOW + "/commandmanager add <命令> <权限> - 添加新的命令权限限制");
        sender.sendMessage(ChatColor.YELLOW + "/commandmanager remove <命令> - 移除命令权限限制");
        sender.sendMessage(ChatColor.YELLOW + "/commandmanager edit <命令> <新权限> - 编辑命令权限限制");
        sender.sendMessage(ChatColor.YELLOW + "/commandmanager list - 列出所有受限制的命令和权限");
        sender.sendMessage(ChatColor.GOLD + "=================================");
    }
}