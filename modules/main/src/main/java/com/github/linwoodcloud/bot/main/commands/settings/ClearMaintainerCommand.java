package com.github.linwoodcloud.bot.main.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;

/**
 * @author CodeDoctorDE
 */
public class ClearMaintainerCommand extends Command {
    public ClearMaintainerCommand() {
        super("clear-control", "clear-controller", "clear-maint", "clear-maintainer", "clearcontrol", "clearcontroller", "clearmaint", "clearmaintainer");
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        entity.setMaintainer(null);
        entity.save();
        event.reply(translate(entity, "Clear")).queue();
    }
}
