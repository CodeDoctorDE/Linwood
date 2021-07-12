package com.github.linwoodcloud.bot.karma.entity;

import com.github.linwoodcloud.bot.core.entity.GuildEntity;

public class KarmaEntity extends GuildEntity {
    private Long id;
    private int maxGiving = 3;
    private String likeEmote = null;
    private String dislikeEmote = null;
    private int constant = 1;
    private String guildId;

    public KarmaEntity(String guildId) {
        this.guildId = guildId;
    }

    public KarmaEntity() {

    }

    public static KarmaEntity get(String guildId) {
        return new KarmaEntity(guildId);
    }

    public String getGuildId() {
        return guildId;
    }

    public String getLikeEmote() {
        return likeEmote;
    }

    public void setLikeEmote(String likeEmote) {
        this.likeEmote = likeEmote;
    }

    public String getDislikeEmote() {
        return dislikeEmote;
    }

    public void setDislikeEmote(String dislikeEmote) {
        this.dislikeEmote = dislikeEmote;
    }

    public int getConstant() {
        return constant;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }

    public int getMaxGiving() {
        return maxGiving;
    }

    public void setMaxGiving(int maxGiving) {
        this.maxGiving = maxGiving;
    }

    @Override
    public void insert() {

    }

    @Override
    public void save() {

    }

    @Override
    public void delete() {

    }
}
