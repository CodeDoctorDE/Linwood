package com.github.linwoodcloud.bot.core.entity;

public abstract class GuildEntity extends DatabaseEntity {
    public GeneralGuildEntity getGuild() {
        return GeneralGuildEntity.get(getGuildId());
    }

    public abstract String getGuildId();

    @Override
    public void delete() {
        update("DELETE FROM " + getPrefix() + "guild WHERE guild=?", getGuildId());
    }
}
