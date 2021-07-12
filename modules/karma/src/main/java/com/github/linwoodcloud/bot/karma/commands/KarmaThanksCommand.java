package com.github.linwoodcloud.bot.karma.commands;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;

public class KarmaThanksCommand extends Command {
    @Override
    public void onCommand(final CommandEvent event) {
        throw new CommandSyntaxException(this);
    }

}
