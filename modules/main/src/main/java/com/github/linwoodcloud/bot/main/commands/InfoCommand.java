package com.github.linwoodcloud.bot.main.commands;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.lang.management.ManagementFactory;

/**
 * @author CodeDoctorDE
 */
public class InfoCommand extends Command {
    public InfoCommand() {
        super(
                "", "info", "i", "information"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        event.getTextChannel().sendMessageEmbeds(new EmbedBuilder().setTitle(infoFormat(event.getMessage(), event.getEntity(), translate(entity, "title")))
                .setDescription(infoFormat(event.getMessage(), event.getEntity(), translate(entity, "Body.regexp"))).build()).queue();
    }

    public String infoFormat(Message message, GeneralGuildEntity entity, String text) {
        var uptime = ManagementFactory.getRuntimeMXBean().getUptime();
        long millis = uptime % 1000;
        long second = (uptime / 1000) % 60;
        long minute = (uptime / (1000 * 60)) % 60;
        long hour = (uptime / (1000 * 60 * 60)) % 24;
        long days = uptime / (1000 * 60 * 60 * 24);
        var prefixes = String.join(", ", entity.getPrefixes());
        return String.format(text, Linwood.getInstance().getVersion(), message.getAuthor().getAsMention(), prefixes.isBlank() ? " " : prefixes,
                days, hour, minute, second, millis, Linwood.getInstance().getConfig().getSupportURL());
    }
}