package com.github.linwoodcloud.bot.karma.entity;

import com.github.linwoodcloud.bot.core.entity.MemberEntity;
import net.dv8tion.jda.api.entities.Member;

public class KarmaMemberEntity extends MemberEntity {
    private final String guildId;
    private final String memberId;
    private long id;
    private int points;
    private long likes = 0;
    private long dislikes = 0;


    public KarmaMemberEntity(String guildId, String memberId) {
        this.guildId = guildId;
        this.memberId = memberId;
    }

    public static KarmaMemberEntity get(Member member) {
        return get(member.getGuild().getId(), member.getId());
    }

    public static KarmaMemberEntity get(String guildId, String memberId) {
        return new KarmaMemberEntity(guildId, memberId);
    }

    public long getLikes() {
        return likes;
    }

    public void setLikes(long likes) {
        this.likes = likes;
    }

    public long getDislikes() {
        return dislikes;
    }

    public void setDislikes(long dislikes) {
        this.dislikes = dislikes;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public KarmaEntity getKarmaEntity() {
        return KarmaEntity.get(guildId);
    }

    public int getLevel() {
        return (int) (getKarmaEntity().getConstant() * Math.sqrt(getKarma()));
    }

    public double getRemainingKarma() {
        return getKarmaEntity().getConstant() * Math.sqrt(getKarma()) - getLevel();
    }

    public long getKarma() {
        return likes - dislikes;
    }

    @Override
    public String getGuildId() {
        return guildId;
    }

    @Override
    public String getMemberId() {
        return memberId;
    }

    public long getId() {
        return id;
    }

    @Override
    public void insert() {

    }

    @Override
    public void save() {

    }
}
