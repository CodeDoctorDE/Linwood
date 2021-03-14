package com.github.codedoctorde.linwood.commands.settings.notification;

import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class TeamCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        ResourceBundle bundle = getBundle(entity);
        if(args.length > 1)
            return false;
        if(args.length == 0)
            if(entity.getNotificationEntity().getTeamRoleId() != null)
                message.getChannel().sendMessageFormat(bundle.getString("Get"), entity.getNotificationEntity().getTeamRole().getName(), entity.getNotificationEntity().getTeamRoleId()).queue();
            else
                message.getChannel().sendMessage(bundle.getString("GetNull")).queue();
        else {
            try {
                Role role = null;
                try{
                    role = message.getGuild().getRoleById(args[0]);
                }catch(Exception ignored){ }
                if(role == null){
                    var roles = message.getGuild().getRolesByName(args[0], true);
                    if(roles.size() < 1)
                        message.getChannel().sendMessage(bundle.getString("SetNothing")).queue();
                    else if(roles.size() > 1)
                        message.getChannel().sendMessage(bundle.getString("SetMultiple")).queue();
                    else
                        role = roles.get(0);
                }
                if(role == null)
                    return true;
                entity.getNotificationEntity().setTeamRole(role);
                entity.save(session);
                message.getChannel().sendMessageFormat(bundle.getString("Set"), entity.getNotificationEntity().getTeamRole().getName(), entity.getNotificationEntity().getTeamRoleId()).queue();
            }catch(NullPointerException e){
                message.getChannel().sendMessage(bundle.getString("NotValid")).queue();
            }
        }
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList(
                "team",
                "t"
        ));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.settings.notification.Team", entity.getLocalization());
    }
}
