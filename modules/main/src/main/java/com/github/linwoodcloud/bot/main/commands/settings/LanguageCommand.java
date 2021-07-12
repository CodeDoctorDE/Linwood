package com.github.linwoodcloud.bot.main.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;

/**
 * @author CodeDoctorDE
 */
public class LanguageCommand extends Command {
    public LanguageCommand() {
        super(
                "language",
                "locale",
                "lang"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var args = event.getArguments();
        var entity = event.getEntity();
        if (args.length > 1)
            throw new CommandSyntaxException(this);
        if (args.length == 0)
            event.replyFormat(translate(entity, "Get"), entity.getLocalization().getDisplayName(entity.getLocalization())).queue();
        else {
            try {
                entity.setLocale(args[0]);
                entity.save();
                event.replyFormat(translate(entity, "Set"), entity.getLocalization().getDisplayName(entity.getLocalization())).queue();
            } catch (NullPointerException e) {
                event.reply(translate(entity, "NotValid")).queue();
            }
        }
    }
}
