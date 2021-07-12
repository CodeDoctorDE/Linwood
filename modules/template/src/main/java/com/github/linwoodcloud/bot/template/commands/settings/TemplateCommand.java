package com.github.linwoodcloud.bot.template.commands.settings;

import com.github.linwoodcloud.bot.core.commands.CommandManager;

/**
 * @author CodeDoctorDE
 */
public class TemplateCommand extends CommandManager {
    public TemplateCommand() {
        registerCommands(
                new CreateTemplateCommand(),
                new ListTemplateCommand(),
                new RemoveTemplateCommand()
        );
    }

}
