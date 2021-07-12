package com.github.linwoodcloud.bot.karma.commands;

import com.github.linwoodcloud.bot.core.commands.CommandManager;

public class KarmaCommand extends CommandManager {
    public KarmaCommand() {
        super(
                "karma"
        );
        registerCommands(
                new KarmaInfoCommand(),
                new KarmaLeaderboardCommand()
        );
    }
}
