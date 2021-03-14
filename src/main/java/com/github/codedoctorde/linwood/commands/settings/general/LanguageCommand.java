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
public class LanguageCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        ResourceBundle bundle = getBundle(entity);
        if(args.length > 1)
            return false;
        if(args.length == 0)
            message.getChannel().sendMessageFormat(bundle.getString("Get"), entity.getLocalization().getDisplayName(entity.getLocalization())).queue();
        else {
            try {
                entity.setLocale(args[0]);
                entity.save(session);
                message.getChannel().sendMessageFormat(bundle.getString("Set"), entity.getLocalization().getDisplayName(entity.getLocalization())).queue();
            }catch(NullPointerException e){
                message.getChannel().sendMessage(bundle.getString("NotValid")).queue();
            }
        }
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList(
                "language",
                "locale",
                "lang"
        ));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.settings.general.Language", entity.getLocalization());
    }
}
