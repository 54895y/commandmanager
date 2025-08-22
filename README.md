# CommandManager

## 项目简介
CommandManager 是一个 Minecraft 插件，用于管理命令权限和自定义消息。它允许服务器管理员灵活控制玩家可以使用的命令，并自定义拒绝访问时显示的消息。

## 功能特点
- 自定义命令权限限制
- 配置文件自动重载监听
- 自定义拒绝访问消息
- 完整的命令管理系统，包括添加、删除、编辑和列出命令权限配置

## 支持版本
- Minecraft 1.12.2

## 安装说明
1. 下载最新版本的 `CommandManager.jar` 文件
2. 将文件放入服务器的 `plugins` 文件夹中
3. 重启服务器或使用 `/reload` 命令
4. 插件会自动生成配置文件

## 命令说明
CommandManager 提供了以下命令：

### 主命令
- `/commandmanager` 或 `/cmdmgr` 或 `/cm`：显示帮助信息

### 子命令
- `/commandmanager reload` 或 `/cmdmgr reload` 或 `/cm reload`：重新加载配置文件
- `/commandmanager add <command> <permission>` 或 `/cmdmgr add <command> <permission>` 或 `/cm add <command> <permission>`：添加命令权限配置
- `/commandmanager remove <command>` 或 `/cmdmgr remove <command>` 或 `/cm remove <command>`：删除命令权限配置
- `/commandmanager edit <command> <newPermission>` 或 `/cmdmgr edit <command> <newPermission>` 或 `/cm edit <command> <newPermission>`：编辑命令权限配置
- `/commandmanager list` 或 `/cmdmgr list` 或 `/cm list`：列出所有命令权限配置
- `/commandmanager help` 或 `/cmdmgr help` 或 `/cm help`：显示帮助信息

## 权限说明
- `commandmanager.admin`：允许使用所有 CommandManager 管理命令（默认仅操作员）
- `commandmanager.reload`：允许重新加载配置文件（默认仅操作员）

## 配置指南
插件加载后会在 `plugins/CommandManager` 文件夹中生成 `config.yml` 配置文件。配置文件包含以下内容：

### 命令权限配置
您可以在配置文件中为特定命令设置所需的权限：
```yaml
commands:
  ban:
    permission: "admin.ban"
    denied-message: "&c您没有使用此命令的权限！"
```

### 默认拒绝消息
如果未为特定命令设置拒绝消息，插件将使用默认拒绝消息：
```yaml
default-denied-message: "&c您没有权限使用此命令！"
```

### 自动重载
插件会自动监视配置文件的变化并在修改时自动重载配置，无需手动执行 reload 命令。

## 开发者信息
作者：YourName
版本：1.0
描述：A plugin to manage command permissions and custom messages

## 反馈与支持
如有任何问题或建议，请联系插件作者或在项目仓库中提交 issue。