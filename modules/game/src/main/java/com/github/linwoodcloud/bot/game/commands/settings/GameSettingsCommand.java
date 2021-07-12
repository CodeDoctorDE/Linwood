package com.github.linwoodcloud.bot.game.commands.settings;

import com.github.linwoodcloud.bot.core.commands.CommandManager;

public class GameSettingsCommand extends CommandManager {
    public GameSettingsCommand() {
        super(
                "game",
                "games",
                "gm"
        );
        registerCommands(
                new ClearGameCategoryCommand(),
                new GameCategoryCommand(),
                new ClearGameMasterCommand(),
                new GameMasterCommand()
        );
    }
}
