package com.github.linwoodcloud.bot.fun.fun;

import com.github.linwoodcloud.bot.core.commands.CommandManager;

/**
 * @author CodeDoctorDE
 */
public class FunCommand extends CommandManager {
    public FunCommand() {
        super(
                "fun", "f", "funny"
        );
        registerCommands(
                new WindowsCommand(),
                new DiceCommand()
        );
    }
}
