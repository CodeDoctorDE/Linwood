package com.github.codedoctorde.linwood.core.apps.single;

import com.github.codedoctorde.linwood.core.Linwood;
import com.github.codedoctorde.linwood.core.entity.GeneralGuildEntity;
import net.dv8tion.jda.api.entities.Guild;
import org.hibernate.Session;

public class SingleApplication {
    private final long guildId;
    private SingleApplicationMode mode;
    private final int id;

    public SingleApplication(int id, long guildId, SingleApplicationMode mode){
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

    public long getGuildId() {
        return guildId;
    }

    public Guild getGuild(){
        return Linwood.getInstance().getJda().getGuildById(guildId);
    }

    public GeneralGuildEntity getGuildEntity(Session session){
        return Linwood.getInstance().getDatabase().getGuildById(session, guildId);
    }
    public void stop(){
        Linwood.getInstance().getJda().removeEventListener(this);
        mode.stop();
    }
    public void start(){
        Linwood.getInstance().getJda().removeEventListener(this);
        mode.start(this);
    }

    public int getId() {
        return id;
    }
}
