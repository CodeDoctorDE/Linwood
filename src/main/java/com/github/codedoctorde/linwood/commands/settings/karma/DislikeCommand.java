package com.github.codedoctorde.linwood.commands.settings.karma;

import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.*;

/**
 * @author CodeDoctorDE
 */
public class DislikeCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        ResourceBundle bundle = getBundle(entity);
        if(args.length > 1)
            return false;
        if(args.length == 0)
            if(entity.getKarmaEntity().getDislikeEmote() != null)
                message.getChannel().sendMessageFormat(bundle.getString("Get"), entity.getKarmaEntity().getDislikeEmote()).queue();
            else
                message.getChannel().sendMessage(bundle.getString("GetNull")).queue();
        else {
            var emote = args[0];
            if(Objects.equals(emote, entity.getKarmaEntity().getDislikeEmote())){
                message.getChannel().sendMessage(bundle.getString("Same")).queue();
                return true;
            }
                entity.getKarmaEntity().setDislikeEmote(emote);
                entity.save(session);
                message.getChannel().sendMessageFormat(bundle.getString("Set"), entity.getKarmaEntity().getDislikeEmote()).queue();
        }
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList(
                "dislike",
                "dis-like"
        ));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.settings.karma.Dislike", entity.getLocalization());
    }
}
