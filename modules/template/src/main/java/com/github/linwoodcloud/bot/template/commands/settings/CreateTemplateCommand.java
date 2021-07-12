package com.github.linwoodcloud.bot.template.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;

/**
 * @author CodeDoctorDE
 */
public class CreateTemplateCommand extends Command {
    @Override
    public void onCommand(final CommandEvent event) {
        throw new CommandSyntaxException(this);
    }

}
