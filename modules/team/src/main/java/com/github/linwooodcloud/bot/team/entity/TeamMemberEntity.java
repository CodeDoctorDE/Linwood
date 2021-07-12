package com.github.linwooodcloud.bot.team.entity;

import com.github.linwoodcloud.bot.core.entity.DatabaseEntity;
import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import com.github.linwoodcloud.bot.core.entity.PermissionLevel;

public class TeamMemberEntity extends DatabaseEntity {
    private Long id;
    private String guildId;
    private PermissionLevel permissionLevel;

    public TeamMemberEntity(String guildId, PermissionLevel level) {
        permissionLevel = level;
        this.guildId = guildId;
    }

    public TeamMemberEntity() {

    }

    public PermissionLevel getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(PermissionLevel permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public Long getId() {
        return id;
    }

    public String getGuildId() {
        return guildId;
    }

    public GeneralGuildEntity getGuild() {
        return GeneralGuildEntity.get(guildId);
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
