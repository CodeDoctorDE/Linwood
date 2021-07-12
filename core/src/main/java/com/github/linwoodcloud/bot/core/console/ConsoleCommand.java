package com.github.linwoodcloud.bot.core.console;

/**
 * @author CodeDoctorDE
 */
public interface ConsoleCommand {
    void onCommand(final String label, final String[] args);

    String[] aliases();

    String description();

    String syntax();
}
