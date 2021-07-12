package com.github.linwoodcloud.bot.main.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;

/**
 * @author CodeDoctorDE
 */
public class AddPrefixCommand extends Command {
    public AddPrefixCommand() {
        super(
                "addprefix",
                "addpre-fix",
                "add-prefix",
                "add-pre-fix"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        var args = event.getArguments();
        if (event.getArguments().length != 1)
            throw new CommandSyntaxException(this);
        else try {
            if (!entity.addPrefix(args[0])) {
                event.reply(translate(entity, "Invalid")).queue();
                return;
            }
            entity.save();
            event.replyFormat(translate(entity, "Success"), args[0]).queue();
        } catch (NullPointerException e) {
            event.reply(translate(entity, "NotValid")).queue();
        }
    }
}
