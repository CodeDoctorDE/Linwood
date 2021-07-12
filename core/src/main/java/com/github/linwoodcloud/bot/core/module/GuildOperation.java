package com.github.linwoodcloud.bot.core.module;

import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import org.jetbrains.annotations.NotNull;

public interface GuildOperation {
    @NotNull
    default String translate(final CommandEvent event, final String key) {
        return translate(event.getEntity(), key);
    }

    @NotNull
    default String translate(final GeneralGuildEntity entity, final String key) {
        return entity.translate(getClass().getCanonicalName(), key);
    }
}
