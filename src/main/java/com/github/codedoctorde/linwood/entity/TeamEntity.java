package com.github.codedoctorde.linwood.entity;

import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class TeamEntity {
    @Id
    @Column(unique = true)
    private String name;
    @OneToMany
    private final List<TeamMemberEntity> guilds = new ArrayList<>();

    public TeamEntity(){}
    public TeamEntity(String name, GuildEntity ownerGuild, GuildEntity... memberGuilds){
        this.name = name;
        guilds.add(new TeamMemberEntity(ownerGuild, this, PermissionLevel.OWNER));
        Arrays.stream(memberGuilds).forEach(guild -> guilds.add(new TeamMemberEntity(guild, this, PermissionLevel.MEMBER)));
    }

    public List<TeamMemberEntity> getGuilds() {
        return guilds;
    }

    public String getName() {
        return name;
    }

    public void delete(Session session) {

    }
}