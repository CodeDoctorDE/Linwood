package com.github.codedoctorde.linwood.commands.settings.team;

import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.commands.CommandManager;
import com.github.codedoctorde.linwood.entity.GuildEntity;
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
public class TeamSettingsCommand extends CommandManager {

    @Override
    public @NotNull Command[] commands() {
        return new Command[0];
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Arrays.asList("team", "teams"));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.settings.team.Base", entity.getLocalization());
    }
}
