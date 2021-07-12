package com.github.linwoodcloud.bot.game.commands;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandPermissionException;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.game.entity.GameEntity;
import com.github.linwoodcloud.bot.game.mode.tictactoe.TicTacToeWorld;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author CodeDoctorDE
 */
public class TicTacToeCommand extends Command {
    public TicTacToeCommand() {
        super(
                "tictactoe",
                "tic-tac-toe",
                "ttt"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var args = event.getArguments();
        if (args.length > 2)
            throw new CommandSyntaxException(this);
        int rounds = 5;
        var entity = event.getEntity();
        var member = event.getMember();
        assert member != null;
        if (!GameEntity.get(event.getGuildId()).isGameMaster(member)) {
            throw new CommandPermissionException(this);
        }
        if (event.getArguments().length != 0)
            try {
                rounds = Integer.parseInt(args[0]);
            } catch (Exception e) {
                event.reply(translate(entity, "NoNumber")).queue();
                return;
            }
        if (rounds > 50 || rounds < 1) {
            event.reply(translate(entity, "Invalid")).queue();
            return;
        }
        // Main.getInstance().getGameManager().startGame(entity.getGuildId(), new WhatIsIt(rounds, event.getMessage().getChannel().getId()));
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(new TicTacToeWorld().render(), "png", stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        event.reply(translate(entity, "Success")).addFile(stream.toByteArray(), "TicTacToe.png").queue();
    }
}
