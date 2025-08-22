package com.commandmanager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }

        // 如果配置文件不存在，复制默认配置
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        // 加载默认配置作为回退
        InputStream defaultStream = plugin.getResource("config.yml");
        if (defaultStream != null) {
            try {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new java.io.InputStreamReader(defaultStream));
                config.setDefaults(defaultConfig);
            } catch (Exception e) {
                plugin.getLogger().severe("无法加载默认配置: " + e.getMessage());
            }
        }
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            loadConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法保存配置文件: " + e.getMessage());
        }
    }

    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
        InputStream defaultStream = plugin.getResource("config.yml");
        if (defaultStream != null) {
            try {
                YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new java.io.InputStreamReader(defaultStream));
                config.setDefaults(defaultConfig);
            } catch (Exception e) {
                plugin.getLogger().severe("无法加载默认配置: " + e.getMessage());
            }
        }
    }

    // 获取指令的权限节点
    public String getCommandPermission(String command) {
        return getConfig().getString("commands." + command + ".permission");
    }

    // 获取拒绝消息
    public String getDeniedMessage(String command) {
        String message = getConfig().getString("commands." + command + ".denied-message");
        return message != null ? message : getConfig().getString("default-messages.denied");
    }

    // 获取缺少权限的消息
    public String getNoPermissionMessage(String command) {
        String message = getConfig().getString("commands." + command + ".no-permission-message");
        return message != null ? message : getConfig().getString("default-messages.no-permission");
    }

    // 添加新的命令权限配置
    public void addCommandPermission(String command, String permission) {
        getConfig().set("commands." + command + ".permission", permission);
        saveConfig();
    }
    
    // 移除命令权限配置
    public boolean removeCommandPermission(String command) {
        if (getConfig().contains("commands." + command)) {
            getConfig().set("commands." + command, null);
            saveConfig();
            return true;
        }
        return false;
    }
    
    // 编辑命令权限配置
    public boolean editCommandPermission(String command, String newPermission) {
        if (getConfig().contains("commands." + command)) {
            getConfig().set("commands." + command + ".permission", newPermission);
            saveConfig();
            return true;
        }
        return false;
    }
}