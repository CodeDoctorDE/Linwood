package com.github.linwoodcloud.bot.game.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.game.entity.GameEntity;

/**
 * @author CodeDoctorDE
 */
public class ClearGameCategoryCommand extends Command {
    public ClearGameCategoryCommand() {
        super(
                "cleargamecategory",
                "clear-game-category",
                "clearcategory",
                "clear-category"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = GameEntity.get(event.getGuildId());
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        entity.setGameCategory(null);
        entity.save();
        event.reply(translate(event, "Clear")).queue();
    }
}
