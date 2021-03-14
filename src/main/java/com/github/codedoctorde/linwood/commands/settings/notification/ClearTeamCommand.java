package com.github.codedoctorde.linwood.commands.settings.notification;

import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public class ClearTeamCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        ResourceBundle bundle = getBundle(entity);
        if(args.length != 0)
            return false;
        entity.getNotificationEntity().setTeamRole(null);
        entity.save(session);
        message.getChannel().sendMessage(bundle.getString("Clear")).queue();
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList(
                "clearteam",
                "clear-team",
                "cleart",
                "clear-t"
        ));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.settings.notification.ClearTeam", entity.getLocalization());
    }
}
