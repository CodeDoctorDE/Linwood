package com.github.linwoodcloud.bot.game.commands;

import com.github.linwoodcloud.bot.core.commands.CommandManager;

/**
 * @author CodeDoctorDE
 */
public class GameCommand extends CommandManager {
    public GameCommand() {
        super(
                "game", "games", "play"
        );
        registerCommands(
                new StopGameCommand(),
                new WhatIsItCommand(),
                new TicTacToeCommand()
        );
    }
}
