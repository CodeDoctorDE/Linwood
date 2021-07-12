package com.github.linwoodcloud.bot.main.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;

public class PrefixesCommand extends Command {
    public PrefixesCommand() {
        super(
                "prefixes",
                "pre-fixes",
                "list-prefixes",
                "list-pre-fixes",
                "listprefixes",
                "listpre-fixes"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        var entity = event.getEntity();
        event.replyFormat(translate(entity, "Get"), String.join(",", entity.getPrefixes())).queue();
    }

}
