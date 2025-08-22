# CommandManager 插件wiki

## 目录
- [1. 概述](#1-概述)
- [2. 安装指南](#2-安装指南)
- [3. 命令详解](#3-命令详解)
- [4. 配置文件详解](#4-配置文件详解)
- [5. 权限系统](#5-权限系统)
- [6. 自动重载功能](#6-自动重载功能)
- [7. 常见问题](#7-常见问题)
- [8. 开发者文档](#8-开发者文档)

## 1. 概述
CommandManager 是一个功能强大的 Minecraft 插件，专为服务器管理员设计，用于精细控制玩家可以使用的命令，并自定义拒绝访问时显示的消息。

### 主要功能
- 为服务器中的任何命令设置特定的权限要求
- 为每个受限命令配置自定义的拒绝访问消息
- 通过简单的命令管理界面动态添加、删除、编辑和列出命令权限配置
- 配置文件自动重载功能，修改配置后无需重启服务器

## 2. 安装指南

### 前提条件
- 运行 Minecraft 1.12.2 版本的 Spigot 或 Paper 服务器
- 已安装 Java 8 或更高版本

### 安装步骤
1. 从插件发布页面下载最新版本的 `CommandManager.jar` 文件
2. 将下载的 JAR 文件复制到服务器的 `plugins` 文件夹中
3. 重启服务器
4. 插件将在首次启动时自动生成必要的配置文件

### 验证安装
安装完成后，可以通过以下命令验证插件是否正常工作：
```
/cm help
```
如果插件成功加载，将显示命令帮助信息。

## 3. 命令详解

### 主命令
CommandManager 提供了以下三个等价的主命令：
- `/commandmanager`
- `/cmdmgr`
- `/cm`

### 子命令详解

#### 3.1 help 命令
**功能**：显示插件的帮助信息
**用法**：`/cm help`
**权限要求**：无需特殊权限

#### 3.2 reload 命令
**功能**：手动重新加载配置文件
**用法**：`/cm reload`
**权限要求**：`commandmanager.reload` 或 `commandmanager.admin`
**示例**：
```
/cm reload
```
执行后，插件将重新读取配置文件并应用最新设置。

#### 3.3 add 命令
**功能**：添加新的命令权限配置
**用法**：`/cm add <command> <permission>`
**权限要求**：`commandmanager.admin`
**参数说明**：
- `<command>`：要限制的命令名称（不包含斜杠）
- `<permission>`：执行该命令所需的权限节点
**示例**：
```
/cm add kick admin.kick
```
此命令将设置玩家需要 `admin.kick` 权限才能使用 `/kick` 命令。

#### 3.4 remove 命令
**功能**：删除已有的命令权限配置
**用法**：`/cm remove <command>`
**权限要求**：`commandmanager.admin`
**参数说明**：
- `<command>`：要删除限制的命令名称（不包含斜杠）
**示例**：
```
/cm remove kick
```
此命令将删除对 `/kick` 命令的权限限制。

#### 3.5 edit 命令
**功能**：修改已有的命令权限配置
**用法**：`/cm edit <command> <newPermission>`
**权限要求**：`commandmanager.admin`
**参数说明**：
- `<command>`：要修改的命令名称（不包含斜杠）
- `<newPermission>`：新的权限节点要求
**示例**：
```
/cm edit kick mod.kick
```
此命令将把 `/kick` 命令的权限要求从原来的值修改为 `mod.kick`。

#### 3.6 list 命令
**功能**：列出所有已配置的命令权限限制
**用法**：`/cm list`
**权限要求**：`commandmanager.admin`
**示例**：
```
/cm list
```
执行后，插件将列出所有当前配置的命令权限限制。

## 4. 配置文件详解

CommandManager 的配置文件位于 `plugins/CommandManager/config.yml`。配置文件使用 YAML 格式，包含以下主要部分：

### 4.1 命令权限配置
这是配置文件的核心部分，用于设置命令的权限要求和自定义拒绝消息：
```yaml
commands:
  ban:
    permission: "admin.ban"
    denied-message: "&c您没有权限使用封禁命令！"
  kick:
    permission: "mod.kick"
  spawn:
    permission: "user.spawn"
    denied-message: "&c您需要先解锁此功能！"
```

### 4.2 默认拒绝消息
如果某个命令没有设置特定的拒绝消息，插件将使用默认拒绝消息：
```yaml
default-denied-message: "&c您没有权限使用此命令！"
```

### 4.3 颜色代码支持
配置文件中的消息支持 Minecraft 颜色代码，可以使用 `&` 符号后跟 0-9 或 a-f 来添加颜色：
- `&c`：红色
- `&a`：绿色
- `&e`：黄色
- `&b`：青色
- `&3`：深蓝色
- `&d`：紫色
- `&1`：蓝色
- `&2`：深绿色
- `&4`：深红色
- `&6`：金色
- `&8`：深灰色
- `&9`：蓝色
- `&0`：黑色
- `&7`：灰色
- `&f`：白色

## 5. 权限系统

CommandManager 使用 Minecraft 的内置权限系统来控制对插件功能的访问：

### 5.1 权限节点
- `commandmanager.admin`：允许使用所有 CommandManager 管理命令（默认仅操作员）
  - 包含子权限：`commandmanager.reload`
- `commandmanager.reload`：允许重新加载配置文件（默认仅操作员）

### 5.2 权限继承
`commandmanager.admin` 权限包含了 `commandmanager.reload` 权限，因此拥有 `commandmanager.admin` 权限的用户也可以使用 reload 命令。

## 6. 自动重载功能

CommandManager 包含一个内置的配置文件监视系统，当 `config.yml` 文件被修改时，插件会自动检测并应用新的配置，无需手动执行 reload 命令。

### 工作原理
1. 插件启动时会启动一个后台线程监视配置文件的变化
2. 当检测到配置文件被修改、创建或删除时，插件会自动重新加载配置
3. 配置重载完成后，新的设置会立即生效

## 7. 常见问题

### 7.1 命令不生效
**问题**：添加了命令权限配置，但命令限制未生效
**解决方案**：
- 确保命令名称正确，不包含斜杠
- 检查权限节点是否正确设置
- 尝试使用 `/cm reload` 手动重新加载配置
- 确认插件已正确安装和启用

### 7.2 颜色代码不显示
**问题**：配置文件中的颜色代码显示为普通文本
**解决方案**：
- 确保使用 `&` 符号而不是 `§` 符号
- 检查颜色代码格式是否正确（`&` 后跟 0-9 或 a-f）

### 7.3 自动重载不工作
**问题**：修改配置文件后，设置没有自动更新
**解决方案**：
- 确保正确保存了配置文件
- 尝试手动执行 `/cm reload` 命令
- 检查服务器日志是否有相关错误信息

## 8. 开发者文档

### 8.1 插件结构
CommandManager 插件主要包含以下核心类：
- `CommandManager`：插件主类，负责初始化和管理插件组件
- `ConfigManager`：负责加载、保存和访问配置文件
- `CommandListener`：监听玩家命令执行并应用权限检查
- `CommandManagerCommand`：处理插件命令的执行
- `ConfigFileWatcher`：监视配置文件变化并触发自动重载

### 8.2 API 参考
目前，CommandManager 不提供公共 API。如有特殊需求，请直接修改插件源代码。

### 8.3 版本兼容性
- 插件开发基于 Spigot 1.12.2 API
- 可能与更高版本的 Minecraft 服务器不完全兼容
- 建议在 1.12.2 版本的服务器上使用以获得最佳效果