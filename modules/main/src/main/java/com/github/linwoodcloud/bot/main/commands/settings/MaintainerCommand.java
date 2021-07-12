package com.github.linwoodcloud.bot.main.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.entities.Role;

public class MaintainerCommand extends Command {
    public MaintainerCommand() {
        super(
                "maintainer",
                "maint",
                "controller",
                "control"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        var args = event.getArguments();
        if (args.length > 1)
            throw new CommandSyntaxException(this);
        if (args.length == 0)
            if (entity.getMaintainerId() != null)
                event.replyFormat(translate(entity, "Get"), entity.getMaintainer().getName(), entity.getMaintainerId()).queue();
            else
                event.reply(translate(entity, "GetNull")).queue();
        else {
            try {
                Role role = null;
                try {
                    role = event.getGuild().getRoleById(args[0]);
                } catch (Exception ignored) {
                }
                if (role == null) {
                    var roles = event.getGuild().getRolesByName(args[0], true);
                    if (roles.size() < 1)
                        event.reply(translate(entity, "SetNothing")).queue();
                    else if (roles.size() > 1)
                        event.reply(translate(entity, "SetMultiple")).queue();
                    else
                        role = roles.get(0);
                }
                if (role == null)
                    return;
                entity.setMaintainer(role);
                entity.save();
                event.replyFormat(translate(entity, "Set"), entity.getMaintainer().getName(), entity.getMaintainerId()).queue();
            } catch (NullPointerException e) {
                event.reply(translate(entity, "NotValid")).queue();
            }
        }
    }
}