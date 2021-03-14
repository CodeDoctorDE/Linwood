package com.github.codedoctorde.linwood.commands.settings.team;

import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import net.dv8tion.jda.api.entities.Message;
import org.hibernate.Session;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author CodeDoctorDE
 */
public class TeamInviteCommand implements Command {
    @Override
    public boolean onCommand(Session session, Message message, GuildEntity entity, String label, String[] args) {
        return true;
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return null;
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return null;
    }
}
