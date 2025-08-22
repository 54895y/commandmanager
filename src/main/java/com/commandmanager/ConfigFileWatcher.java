package com.commandmanager;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigFileWatcher implements Runnable {
    private final JavaPlugin plugin;
    private final ConfigManager configManager;
    private final WatchService watchService;
    private final Path configPath;
    private ExecutorService executorService;
    private volatile boolean running = false;

    public ConfigFileWatcher(JavaPlugin plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        
        try {
            // 获取插件数据文件夹的路径
            this.configPath = Paths.get(plugin.getDataFolder().getAbsolutePath());
            // 创建WatchService
            this.watchService = FileSystems.getDefault().newWatchService();
            // 注册监视事件（修改事件）
            this.configPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            throw new RuntimeException("无法创建配置文件监视器", e);
        }
    }

    public void start() {
        if (running) {
            return;
        }
        
        running = true;
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this);
        plugin.getLogger().info("配置文件监视已启动，将在文件变化时自动重载配置");
    }

    public void stop() {
        if (!running) {
            return;
        }
        
        running = false;
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
        try {
            watchService.close();
        } catch (IOException e) {
            plugin.getLogger().warning("关闭配置文件监视器时出错: " + e.getMessage());
        }
        plugin.getLogger().info("配置文件监视已停止");
    }

    @Override
    public void run() {
        while (running) {
            try {
                // 获取下一个监视键
                WatchKey key = watchService.poll(1, java.util.concurrent.TimeUnit.SECONDS);
                if (key == null) {
                    continue;
                }

                // 处理所有事件
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

                    // 如果是 OVERFLOW 事件，忽略
                    if (kind == StandardWatchEventKinds.OVERFLOW) {
                        continue;
                    }

                    // 获取变化的文件名称
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileName = ev.context();

                    // 检查是否是配置文件发生了变化
                    if (fileName.toString().equals("config.yml")) {
                        plugin.getLogger().info("检测到配置文件变化，正在重新加载...");
                        // 延迟一点时间再重新加载，确保文件完全写入
                        Thread.sleep(100);
                        configManager.reloadConfig();
                        plugin.getLogger().info("配置文件已成功重新加载！");
                    }
                }

                // 重置监视键，以便继续接收事件
                boolean valid = key.reset();
                if (!valid) {
                    break; // 如果监视键无效，退出循环
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                plugin.getLogger().severe("配置文件监视过程中出错: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}