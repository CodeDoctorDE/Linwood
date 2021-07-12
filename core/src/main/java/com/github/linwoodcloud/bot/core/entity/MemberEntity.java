package com.github.linwoodcloud.bot.core.entity;

public abstract class MemberEntity extends GuildEntity {
    public GeneralMemberEntity getMember() {
        return GeneralMemberEntity.get(getGuildId(), getMemberId());
    }

    public abstract String getMemberId();
}
