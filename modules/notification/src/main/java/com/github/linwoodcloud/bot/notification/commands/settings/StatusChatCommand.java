package com.github.linwoodcloud.bot.notification.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.core.utils.TagUtil;
import com.github.linwoodcloud.bot.notification.entity.NotificationEntity;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * @author CodeDoctorDE
 */
public class StatusChatCommand extends Command {
    public StatusChatCommand() {
        super(
                "statuschat",
                "status-chat",
                "status",
                "sc",
                "s"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        var args = event.getArguments();
        var notificationEntity = NotificationEntity.get(event.getGuildId());
        assert notificationEntity != null;
        if (args.length > 1)
            throw new CommandSyntaxException(this);
        if (args.length == 0) if (notificationEntity.getStatusChatId() != null)
            event.replyFormat(translate(entity, "Get"), notificationEntity.getStatusChat().getName(), notificationEntity.getStatusChatId()).queue();
        else
            event.reply(translate(entity, "GetNull")).queue();
        else {
            try {
                TextChannel channel;
                try {
                    channel = TagUtil.convertToTextChannel(event.getMessage().getGuild(), args[0]);
                } catch (UnsupportedOperationException ignored) {
                    event.reply(translate(entity, "SetMultiple")).queue();
                    return;
                }
                if (channel == null) {
                    event.reply(translate(entity, "SetNothing")).queue();
                    return;
                }
                notificationEntity.setStatusChat(channel);
                entity.save();
                event.replyFormat(translate(entity, "Set"), notificationEntity.getStatusChat().getAsMention(), notificationEntity.getStatusChatId()).queue();
            } catch (NullPointerException e) {
                event.reply(translate(entity, "NotValid")).queue();
            }
        }
    }
}
