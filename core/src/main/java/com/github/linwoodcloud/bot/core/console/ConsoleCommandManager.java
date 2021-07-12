package com.github.linwoodcloud.bot.core.console;

import com.github.linwoodcloud.bot.core.Linwood;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

/**
 * @author CodeDoctorDE
 */
public abstract class ConsoleCommandManager implements ConsoleCommand {
    private static final Logger LOGGER = LogManager.getLogger(Linwood.class);

    public abstract ConsoleCommand[] commands();

    @Override
    public void onCommand(String label, String[] args) {
        for (ConsoleCommand command : commands())
            if (Arrays.asList(command.aliases()).contains(
                    (args.length != 0) ? args[0].toLowerCase() : "")) {
                command.onCommand(
                        (args.length != 0) ? args[0] : "",
                        (args.length != 0) ? Arrays.copyOfRange(args, 1, args.length) : new String[0]);
                return;
            }
    }

    public ConsoleCommand getCommand(String... args) {
        ConsoleCommand command = this;
        for (String arg :
                args) {
            for (ConsoleCommand current :
                    commands())
                if (Arrays.asList(current.aliases()).contains(arg)) {
                    if (current instanceof ConsoleCommandManager)
                        command = ((ConsoleCommandManager) current).getCommand(Arrays.copyOfRange(args, 1, args.length));
                    else command = current;
                    break;
                } else
                    return null;
        }
        return command;
    }
}
