package com.github.linwoodcloud.bot.main.commands;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;

/**
 * @author CodeDoctorDE
 */
public class ModuleCommand extends Command {
    public ModuleCommand() {
        super(
                "module",
                "modules"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        if (event.getArguments().length == 0)
            sendModuleList(event);
        else if (event.getArguments().length == 1)
            sendModuleHelp(event);
        else
            throw new CommandSyntaxException(this);
    }

    public void sendModuleList(CommandEvent event) {
        var builder = new EmbedBuilder()
                .setTitle(translate(event, "ModuleListTitle"), "https://linwood.tk/bot/modules/overview")
                .setDescription(translate(event, "ModuleListDescription"));
        builder.addField(translate(event, "EnabledModules"), "", false);
        Arrays.stream(Linwood.getInstance().getModules()).filter(module -> module.isEnabled(event.getEntity())).forEach(module -> builder.addField(module.getName(), module.translate(event, "Description"), true));
        builder.addBlankField(false).addField(translate(event, "DisabledModules"), "", false);
        Arrays.stream(Linwood.getInstance().getModules()).filter(module -> !module.isEnabled(event.getEntity())).forEach(module -> builder.addField(module.getName(), module.translate(event, "Description"), true));
        event.reply(builder.build()).queue();
    }

    public void sendModuleHelp(CommandEvent event) {
        var entity = event.getEntity();
        var module = Linwood.getInstance().getModule(event.getArgumentsString());
        if (module != null) {
            var builder = new EmbedBuilder().setTitle(module.getName(), module.getSupportURL()).setDescription(module.translate(event, "Description")).addBlankField(false);
            if (!module.getCommands().isEmpty())
                builder.addField(translate(entity, "Commands"), translate(entity, "CommandsNote"), false).setDescription(module.translate(entity, "Description"));
            module.getCommands().forEach(command -> builder.addField(command.translate(entity, "Syntax"), command.translate(entity, "Description"), true));
            if (!module.getSettingsCommands().isEmpty())
                builder.addBlankField(false).addField(translate(entity, "SettingsCommands"), translate(entity, "SettingsCommandsNote"), false);
            module.getSettingsCommands().forEach(command -> builder.addField(command.translate(entity, "Syntax"), command.translate(entity, "Description"), true));
            event.reply(builder.build()).queue();
        } else
            event.reply(translate(entity, "HelpNotFound")).queue();
    }
}
