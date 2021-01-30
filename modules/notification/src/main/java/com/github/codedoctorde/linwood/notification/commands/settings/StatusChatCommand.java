package com.github.codedoctorde.linwood.notification.commands.settings;

import com.github.codedoctorde.linwood.core.commands.Command;
import com.github.codedoctorde.linwood.core.commands.CommandEvent;
import com.github.codedoctorde.linwood.core.exceptions.CommandSyntaxException;
import com.github.codedoctorde.linwood.core.utils.TagUtil;
import com.github.codedoctorde.linwood.notification.entity.NotificationEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

/**
 * @author CodeDoctorDE
 */
public class StatusChatCommand extends Command {
    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        var args = event.getArguments();
        var notificationEntity = event.getGuildEntity(NotificationEntity.class);
        if(args.length > 1)
            throw new CommandSyntaxException(this);
        if(args.length == 0)
            if(notificationEntity.getStatusChatId() != null)
                event.replyFormat(translate(entity, "Get"), notificationEntity.getStatusChat().getName(), notificationEntity.getStatusChatId()).queue();
            else
                event.reply(translate(entity, "GetNull")).queue();
        else {
            try {
                TextChannel channel;
                try {
                    channel = TagUtil.convertToTextChannel(event.getMessage().getGuild(), args[0]);
                }catch(UnsupportedOperationException ignored) {
                    event.reply(translate(entity, "SetMultiple")).queue();
                    return;
                }
                if(channel == null) {
                    event.reply(translate(entity, "SetNothing")).queue();
                    return;
                }
                notificationEntity.setStatusChat(channel);
                entity.save(event.getSession());
                event.replyFormat(translate(entity, "Set"), notificationEntity.getStatusChat().getAsMention(), notificationEntity.getStatusChatId()).queue();
            }catch(NullPointerException e){
                event.reply(translate(entity, "NotValid")).queue();
            }
        }
    }


    public StatusChatCommand() {
        super(
                "statuschat",
                "status-chat",
                "status",
                "sc",
                "s"
        );
    }
}
