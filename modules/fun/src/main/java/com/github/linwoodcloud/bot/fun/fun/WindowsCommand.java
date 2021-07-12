package com.github.linwoodcloud.bot.fun.fun;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;

import java.io.InputStream;
import java.util.Random;

/**
 * @author CodeDoctorDE
 */
public class WindowsCommand extends Command {
    private final Random random = new Random();

    public WindowsCommand() {
        super(
                "windows",
                "win",
                "window"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        String response;
        InputStream file = null;
        var entity = event.getEntity();
        switch (random.nextInt(3)) {
            case 0:
                response = translate(entity, "Crash");

                file = getClass().getClassLoader().getResourceAsStream("assets/crash.png");
                break;
            case 1:
                response = translate(entity, "Update");

                file = getClass().getClassLoader().getResourceAsStream(("assets/update.png"));
                break;
            default:
                response = translate(entity, "Loading");
                break;
        }
        var action = event.reply(response);
        if (file != null)
            action = action.addFile(file, "windows.png");
        action.queue();
    }
}
