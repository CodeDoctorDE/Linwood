package com.github.linwoodcloud.bot.core.apps.single;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import net.dv8tion.jda.api.entities.Guild;

public class SingleApplication {
    private final String guildId;
    private final int id;
    private SingleApplicationMode mode;

    public SingleApplication(int id, String guildId, SingleApplicationMode mode) {
        this.id = id;
        this.guildId = guildId;
        this.mode = mode;
    }

    public SingleApplicationMode getMode() {
        return mode;
    }

    public void setMode(SingleApplicationMode mode) {
        this.mode = mode;
    }

    public String getGuildId() {
        return guildId;
    }

    public Guild getGuild() {
        return Linwood.getInstance().getJda().getGuildById(guildId);
    }

    public GeneralGuildEntity getGuildEntity() {
        return GeneralGuildEntity.get(guildId);
    }

    public void stop() {
        Linwood.getInstance().getJda().removeEventListener(this);
        mode.stop();
    }

    public void start() {
        Linwood.getInstance().getJda().removeEventListener(this);
        mode.start(this);
    }

    public int getId() {
        return id;
    }
}
