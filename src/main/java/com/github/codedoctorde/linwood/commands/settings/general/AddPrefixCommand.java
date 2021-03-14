package com.github.codedoctorde.linwood.commands.settings.general;

import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public class AddPrefixCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        ResourceBundle bundle = getBundle(entity);
        if(args.length == 0)
            return false;
        else try {
            var prefix = String.join(" ", args);
            if(!entity.addPrefix(prefix)){
                message.getChannel().sendMessage(bundle.getString("Invalid")).queue();
                return true;
            }
            entity.save(session);
            message.getChannel().sendMessageFormat(bundle.getString("Success"), prefix).queue();
        } catch (NullPointerException e) {
            message.getChannel().sendMessage(bundle.getString("NotValid")).queue();
        }
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList(
                "addprefix",
                "addpre-fix",
                "add-prefix",
                "add-pre-fix"
        ));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.settings.general.AddPrefix", entity.getLocalization());
    }
}
