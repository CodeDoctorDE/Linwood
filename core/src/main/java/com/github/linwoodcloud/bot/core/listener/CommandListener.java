package com.github.linwoodcloud.bot.core.listener;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import com.github.linwoodcloud.bot.core.exceptions.CommandPermissionException;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import io.sentry.Sentry;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.SubscribeEvent;
import org.apache.tools.ant.types.Commandline;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

/**
 * @author CodeDoctorDE
 */
public class CommandListener {
    public static final Pattern PATTERN = Pattern.compile("(?:^(?<module>[A-z]+):)?(?<command>[A-z]+)?(?<args> [A-z]+$)?");


    @SubscribeEvent
    public void onCommand(@Nonnull MessageReceivedEvent event) {
        var guild = GeneralGuildEntity.get(event.getGuild().getId());
        if (event.getChannelType() == ChannelType.TEXT && !event.getAuthor().isBot()) {
            var content = event.getMessage().getContentRaw();
            assert guild != null;
            var prefixes = guild.getPrefixes();
            var id = event.getJDA().getSelfUser().getId();
            var nicknameMention = "<@!" + id + ">";
            var normalMention = "<@" + id + ">";
            String prefix = "";
            for (var current : prefixes)
                if (content.startsWith(current))
                    prefix = current;
            String split = null;
            if (!prefix.isBlank() && content.startsWith(prefix))
                split = prefix;
            else if (content.startsWith(nicknameMention))
                split = nicknameMention;
            else if (content.startsWith(normalMention))
                split = normalMention;
            if (split != null) {
                var command = Commandline.translateCommandline(content.substring(split.length()));
                try {
                    execute(new CommandEvent(event.getMessage(), guild, prefix, command));
                } catch (CommandSyntaxException e) {
                    event.getChannel().sendMessageFormat(translate(guild, "Syntax"), e.getCommand().translate(guild, "Syntax")).queue();
                } catch (InsufficientPermissionException e) {
                    event.getChannel().sendMessageFormat(translate(guild, "InsufficientPermission"), e.getMessage()).queue();
                } catch (CommandPermissionException e) {
                    event.getChannel().sendMessage(translate(guild, "NoPermission")).queue();
                } catch (Exception e) {
                    event.getChannel().sendMessageFormat(translate(guild, "Error"), e.getMessage()).queue();
                    e.printStackTrace();
                    Sentry.captureException(e);
                }
            }
        }
    }

    public String translate(GeneralGuildEntity entity, String key) {
        return entity.translate("Command", key);
    }

    public Command findCommand(String command) {
        var matcher = PATTERN.matcher(command);
        if (!matcher.find())
            return null;
        var commandString = matcher.group("command");
        var moduleString = matcher.group("module");
        return findCommand(commandString, moduleString);
    }

    public Command findCommand(String commandString, String moduleString) {
        commandString = commandString == null ? "" : commandString;
        if (moduleString != null) {
            var module = Linwood.getInstance().getModule(moduleString);
            if (module != null)
                return module.getCommand(commandString);
        }
        for (var current :
                Linwood.getInstance().getModules()) {
            var currentCommand = current.getCommand(commandString);
            if (currentCommand != null)
                return currentCommand;
        }
        return null;
    }

    public void execute(CommandEvent commandEvent) {
        var command =
                findCommand(commandEvent.getArgumentsString());

        if (command != null)
            command.onCommand(commandEvent.upper());
        else
            commandEvent.reply(translate(commandEvent.getEntity(), "CommandNotFound")).queue();
    }
}
