package com.github.linwoodcloud.bot.notification.commands.settings;

import com.github.linwoodcloud.bot.core.commands.CommandManager;

public class NotificationSettingsCommand extends CommandManager {
    public NotificationSettingsCommand() {
        super(
                "notification",
                "notifications",
                "notif",
                "n"
        );
        registerCommands(
                new ClearTeamCommand(),
                new TeamCommand(),
                new ClearSupportChatCommand(),
                new SupportChatCommand(),
                new ClearStatusChatCommand(),
                new StatusChatCommand()
        );
    }
}
