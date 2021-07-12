package com.github.linwoodcloud.bot.core.exceptions;

import com.github.linwoodcloud.bot.core.commands.Command;

public class CommandSyntaxException extends RuntimeException {
    private final Command command;

    public CommandSyntaxException(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
