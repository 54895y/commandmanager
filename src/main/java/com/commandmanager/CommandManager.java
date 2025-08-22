package com.commandmanager;

import org.bukkit.plugin.java.JavaPlugin;

public class CommandManager extends JavaPlugin {
    private static CommandManager instance;
    private ConfigManager configManager;
    private ConfigFileWatcher configFileWatcher;

    @Override
    public void onEnable() {
        instance = this;
        configManager = new ConfigManager(this);
        configManager.loadConfig();

        // 初始化并启动配置文件监视器
        configFileWatcher = new ConfigFileWatcher(this, configManager);
        configFileWatcher.start();

        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new CommandListener(configManager), this);

        // 注册命令执行器
        getCommand("commandmanager").setExecutor(new CommandManagerCommand(configManager));

        getLogger().info("CommandManager 插件已成功启用!");
    }

    @Override
    public void onDisable() {
        // 停止配置文件监视器
        if (configFileWatcher != null) {
            configFileWatcher.stop();
        }
        
        getLogger().info("CommandManager 插件已禁用!");
    }

    public static CommandManager getInstance() {
        return instance;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}