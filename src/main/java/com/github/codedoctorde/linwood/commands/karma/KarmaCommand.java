package com.github.codedoctorde.linwood.commands.karma;

import com.github.codedoctorde.linwood.commands.Command;
import com.github.codedoctorde.linwood.commands.CommandManager;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class KarmaCommand extends CommandManager {
    @Override
    public @NotNull Command[] commands() {
        return new Command[]{
                new KarmaInfoCommand(),
                new KarmaLeaderboardCommand(),
                new KarmaThanksCommand(),
                new KarmaGuideCommand()
        };
    }

    @Override
    public @NotNull Set<String> aliases(GuildEntity entity) {
        return new HashSet<>(Collections.singletonList(
                "karma"
        ));
    }

    @Override
    public @NotNull ResourceBundle getBundle(GuildEntity entity) {
        return ResourceBundle.getBundle("locale.commands.karma.Base", entity.getLocalization());
    }
}
