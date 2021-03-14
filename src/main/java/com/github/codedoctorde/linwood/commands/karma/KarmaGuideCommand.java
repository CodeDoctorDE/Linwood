package com.github.codedoctorde.linwood.commands.karma;

import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public class KarmaGuideCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        if(args.length == 0)
            return false;
        var self = message.getJDA().getSelfUser();
        var bundle = getBundle(entity);
        if(entity.getKarmaEntity().getLikeEmote() == null){
            message.getChannel().sendMessage(bundle.getString("NoSetup")).queue();
            return true;
        }
        var embedBuilder = new EmbedBuilder().setAuthor(self.getName(), "https://linwood.tk", self.getAvatarUrl()).setTitle(bundle.getString("Title"))
                .setColor(new Color(0x3B863B)).setTimestamp(LocalDateTime.now())
                .addField(bundle.getString("LikeTitle"), bundle.getString("LikeBody"), false);
        if(entity.getKarmaEntity().getDislikeEmote() != null)
            embedBuilder.addField(bundle.getString("DislikeTitle"), bundle.getString("DislikeBody"), false);
        embedBuilder.addField(bundle.getString("WikiTitle"), bundle.getString("WikiBody"), false);
        message.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList("guide", "how", "g"));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.karma.Guide", entity.getLocalization());
    }
}
