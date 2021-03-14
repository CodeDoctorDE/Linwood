package com.github.codedoctorde.linwood.commands.settings.team;

import com.github.codedoctorde.linwood.Linwood;
import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import com.github.codedoctorde.linwood.entity.PermissionLevel;
import net.dv8tion.jda.api.entities.Message;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author CodeDoctorDE
 */
public class LeaveTeamCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        if(args.length != 1)
        return false;
        var bundle = getBundle(entity);
        var teamMember = Linwood.getInstance().getDatabase().getTeamMember(session, message.getGuild().getIdLong(), args[0]);
        if(teamMember == null || teamMember.getLevel() == PermissionLevel.INVITED){
            message.getChannel().sendMessage(bundle.getString("NotJoined")).queue();
            return true;
        }
        if(teamMember.getLevel() == PermissionLevel.OWNER) {
            message.getChannel().sendMessage(bundle.getString("OwnerLeave")).queue();
            return true;
        }
        // todo: implement leave
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Collections.singletonList("leave"));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return null;
    }
}
