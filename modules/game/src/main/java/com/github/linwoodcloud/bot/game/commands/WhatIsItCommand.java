package com.github.linwoodcloud.bot.game.commands;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandPermissionException;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.game.entity.GameEntity;
import com.github.linwoodcloud.bot.game.mode.whatisit.WhatIsIt;

/**
 * @author CodeDoctorDE
 */
public class WhatIsItCommand extends Command {
    public WhatIsItCommand() {
        super(
                "whatisit",
                "what-is-it",
                "what"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var args = event.getArguments();
        var entity = event.getEntity();
        if (args.length > 2)
            throw new CommandSyntaxException(this);
        int rounds = 5;
        assert event.getMember() != null;
        if (!GameEntity.get(event.getGuildId()).isGameMaster(event.getMember())) {
            throw new CommandPermissionException(this);
        }
        if (event.getArguments().length != 0)
            try {
                rounds = Integer.parseInt(args[0]);
            } catch (Exception e) {
                event.reply(translate(entity, "NoNumber")).queue();
            }
        if (rounds > 50 || rounds < 1) {
            event.reply(translate(entity, "Invalid")).queue();
        }
        Linwood.getInstance().getGameManager().startGame(event.getEntity().getGuildId(), new WhatIsIt(rounds, event.getMessage().getChannel().getId()));
        event.reply(translate(entity, "Success")).queue();
    }
}
