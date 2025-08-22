package com.commandmanager;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandListener implements Listener {
    private final ConfigManager configManager;

    public CommandListener(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage().trim();

        // 跳过以/commandmanager开头的命令（插件自身命令）
        if (message.startsWith("/commandmanager")) {
            return;
        }

        // 提取命令名称（去掉开头的/）
        String command = message.split(" ")[0].substring(1).toLowerCase();

        // 检查配置中是否存在该命令的限制
        String requiredPermission = configManager.getCommandPermission(command);
        if (requiredPermission == null) {
            return; // 配置中未限制此命令，允许执行
        }

        // 检查玩家是否有权限
        if (!player.hasPermission(requiredPermission)) {
            event.setCancelled(true);
            String noPermissionMessage = configManager.getNoPermissionMessage(command);
            if (noPermissionMessage != null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', noPermissionMessage)
                    .replace("{player}", player.getName())
                    .replace("{command}", command)
                    .replace("{permission}", requiredPermission));
            }
        }
    }
}