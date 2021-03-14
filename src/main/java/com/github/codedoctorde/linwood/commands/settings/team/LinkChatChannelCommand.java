package com.github.codedoctorde.linwood.commands.settings.team;

import com.github.codedoctorde.linwood.Linwood;
import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import com.github.codedoctorde.linwood.entity.VisibilityLevel;
import com.github.codedoctorde.linwood.utils.TagUtil;
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
public class LinkChatChannelCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        if(args.length != 3)
            return false;
        var bundle = getBundle(entity);
        var channel= TagUtil.convertToTextChannel(message.getGuild(), args[0]);
        if(channel == null) {
            message.getChannel().sendMessage(bundle.getString("InvalidChannel")).queue();
            return true;
        }
        var team = Linwood.getInstance().getDatabase().getTeamMember(session, message.getGuild().getIdLong(), args[1]);
        if(team == null){
            message.getChannel().sendMessage(bundle.getString("InvalidTeam")).queue();
            return true;
        }
        var teamChannel = team.getTeam().getChannelByName(args[2]);
        if(teamChannel == null || !teamChannel.isAllowed(team.getLevel())){
            message.getChannel().sendMessage(bundle.getString("NotFound")).queue();
            return true;
        }
        teamChannel.getChannels().add(channel.getIdLong());
        teamChannel.save(session);
        message.getChannel().sendMessage(bundle.getString("Success")).queue();
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList("link", "l"));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.settings.team.Unlink", entity.getLocalization());
    }
}
