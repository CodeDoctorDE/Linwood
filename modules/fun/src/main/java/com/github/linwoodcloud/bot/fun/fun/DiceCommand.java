package com.github.linwoodcloud.bot.fun.fun;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;

import java.util.Random;

/**
 * @author CodeDoctorDE
 */
public class DiceCommand extends Command {
    private final Random random = new Random();

    public DiceCommand() {
        super(
                "dice",
                "d"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        var entity = event.getEntity();
        event.getMessage().getChannel().sendMessageFormat(translate(entity, "Output"), random.nextInt(5) + 1).queue();
    }
}
