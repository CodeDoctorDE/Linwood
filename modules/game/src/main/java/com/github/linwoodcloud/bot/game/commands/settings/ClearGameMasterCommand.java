package com.github.linwoodcloud.bot.game.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.game.entity.GameEntity;

/**
 * @author CodeDoctorDE
 */
public class ClearGameMasterCommand extends Command {
    public ClearGameMasterCommand() {
        super(
                "cleargamemaster",
                "clear-game-master",
                "clearmaster",
                "clear-master"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        GameEntity.get(event.getGuildId()).setGameMasterRole(null);
        entity.save();
        event.reply(translate(entity, "Clear")).queue();
    }
}
