package com.github.linwoodcloud.bot.karma.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.karma.entity.KarmaEntity;

/**
 * @author CodeDoctorDE
 */
public class ClearLikeCommand extends Command {
    public ClearLikeCommand() {
        super(
                "clearlike",
                "clear-like"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        KarmaEntity.get(event.getGuildId()).setLikeEmote(null);
        entity.save();
        event.reply(translate(entity, "Clear")).queue();
    }
}
