package com.github.linwoodcloud.bot.main.commands;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;

/**
 * @author CodeDoctorDE
 */
public class CommandHelpCommand extends Command {
    public CommandHelpCommand() {
        super(
                "command",
                "help",
                "h"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        if (event.getArguments().length == 0)
            throw new CommandSyntaxException(this);
        var command = Linwood.getInstance().getCommandListener().findCommand(event.getArgumentsString());
        if (command == null)
            throw new CommandSyntaxException(this);
        event.reply(new EmbedBuilder().setTitle(command.translate(event, "Syntax")).setDescription(command.translate(event, "Description")).addField(translate(event, "Aliases"), String.join(", ", command.getAliases()), true).build()).queue();
    }
}
