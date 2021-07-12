package com.github.linwoodcloud.bot.game.commands;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandPermissionException;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.game.entity.GameEntity;

/**
 * @author CodeDoctorDE
 */
public class StopGameCommand extends Command {
    public StopGameCommand() {
        super(
                "stop",
                "stopgame",
                "cancel",
                "stop-game",
                "s",
                "c"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        if (event.getMember() == null)
            throw new CommandSyntaxException(this);
        if (!GameEntity.get(event.getGuildId()).isGameMaster(event.getMember())) {
            throw new CommandPermissionException(this);
        }
        if (Linwood.getInstance().getGameManager().getGame(entity.getGuildId()) == null)
            event.reply(translate(entity, "NoGameRunning")).queue();
        else {
            Linwood.getInstance().getGameManager().stopGame(entity.getGuildId());
            event.reply(translate(entity, "Success")).queue();
        }
    }
}
