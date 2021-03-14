package com.github.codedoctorde.linwood.commands.karma;

import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class KarmaThanksCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        if(args.length > 1)
            return false;
        var bundle = getBundle(entity);
        var likeEmote = entity.getKarmaEntity().getLikeEmote();
        if(likeEmote == null){
            message.getChannel().sendMessage(bundle.getString("NoSetup")).queue();
            return true;
        }
        if(args.length == 0){
            message.getChannel().sendMessageFormat(bundle.getString("ThanksSimple"), likeEmote).queue();
        }else{
            message.getChannel().retrieveMessageById(args[0]).submit().whenComplete((mentionedMessage, throwable) -> {
                if(throwable != null)
                   message.getChannel().sendMessage(bundle.getString("NotFound")).queue();
               else {
                   mentionedMessage.addReaction(entity.getKarmaEntity().getLikeEmote()).queue();
                   message.getChannel().sendMessage(new EmbedBuilder().setTitle(bundle.getString("Title"))
                           .setAuthor(mentionedMessage.getAuthor().getAsTag(), "https://discord.com", mentionedMessage.getAuthor().getAvatarUrl()).setDescription(mentionedMessage.getContentRaw())
                           .addField(String.format(bundle.getString("JumpHeader"), entity.getKarmaEntity().getLikeEmote()), String.format(bundle.getString("JumpBody"), mentionedMessage.getJumpUrl()), false).build()).queue();
               }});
        }
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList("t", "thanks"));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.karma.Thanks");
    }
}
