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

public class PrefixesCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        if(args.length != 0)
            return false;
        var bundle = getBundle(entity);
        message.getChannel().sendMessageFormat(bundle.getString("Get"), String.join("," , entity.getPrefixes())).queue();
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList(
                "prefixes",
                "pre-fixes",
                "list-prefixes",
                "list-pre-fixes",
                "listprefixes",
                "listpre-fixes"
        ));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.settings.general.Prefixes", entity.getLocalization());
    }
}
