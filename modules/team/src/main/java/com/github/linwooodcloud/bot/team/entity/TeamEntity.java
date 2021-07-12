package com.github.linwooodcloud.bot.team.entity;

import com.github.linwoodcloud.bot.core.entity.DatabaseEntity;
import com.github.linwoodcloud.bot.core.entity.PermissionLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamEntity extends DatabaseEntity {
    private final List<TeamMemberEntity> guilds = new ArrayList<>();
    private Long id;

    public TeamEntity() {
    }

    public TeamEntity(String ownerGuildId, String... memberGuildIds) {
        guilds.add(new TeamMemberEntity(ownerGuildId, PermissionLevel.OWNER));
        Arrays.stream(memberGuildIds).forEach(guild -> guilds.add(new TeamMemberEntity(guild, PermissionLevel.MEMBER)));
    }

    public List<TeamMemberEntity> getGuilds() {
        return guilds;
    }

    public Long getId() {
        return id;
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