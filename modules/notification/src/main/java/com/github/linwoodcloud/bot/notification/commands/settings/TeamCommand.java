package com.github.linwoodcloud.bot.notification.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.notification.entity.NotificationEntity;
import net.dv8tion.jda.api.entities.Role;

public class TeamCommand extends Command {
    public TeamCommand() {
        super(
                "team",
                "t"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var args = event.getArguments();
        var entity = event.getEntity();
        var notificationEntity = NotificationEntity.get(event.getGuildId());
        assert notificationEntity != null;
        if (args.length > 1)
            throw new CommandSyntaxException(this);
        if (args.length == 0)
            if (notificationEntity.getTeamRoleId() != null)
                event.replyFormat(translate(entity, "Get"), notificationEntity.getTeamRole().getName(), notificationEntity.getTeamRoleId()).queue();
            else
                event.reply(translate(entity, "GetNull")).queue();
        else {
            try {
                Role role = null;
                try {
                    role = event.getMessage().getGuild().getRoleById(args[0]);
                } catch (Exception ignored) {
                }
                if (role == null) {
                    var roles = event.getMessage().getGuild().getRolesByName(args[0], true);
                    if (roles.size() < 1)
                        event.reply(translate(entity, "SetNothing")).queue();
                    else if (roles.size() > 1)
                        event.reply(translate(entity, "SetMultiple")).queue();
                    else
                        role = roles.get(0);
                }
                if (role == null)
                    return;
                notificationEntity.setTeamRole(role);
                entity.save();
                event.replyFormat(translate(entity, "Set"), notificationEntity.getTeamRole().getName(), notificationEntity.getTeamRoleId()).queue();
            } catch (NullPointerException e) {
                event.reply(translate(entity, "NotValid")).queue();
            }
        }
    }
}
