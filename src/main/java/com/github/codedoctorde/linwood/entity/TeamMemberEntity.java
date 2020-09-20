package com.github.codedoctorde.linwood.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class TeamMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @JoinColumn
    @Column(name = "teamName")
    @OneToOne(cascade={CascadeType.ALL}, optional = false)
    private TeamEntity team;
    @OneToOne(cascade={CascadeType.ALL}, optional = false)
    @Column(name = "guildId")
    private GuildEntity guild;
    private PermissionLevel level;
    @ElementCollection
    private final Map<String, Long> channels = new HashMap<>();

    public TeamMemberEntity(GuildEntity guild, TeamEntity team, PermissionLevel level){
        this.level = level;
        this.team = team;
        this.guild = guild;
    }

    public TeamMemberEntity() {

    }

    public PermissionLevel getLevel() {
        return level;
    }

    public void setLevel(PermissionLevel permissionLevel) {
        this.level = permissionLevel;
    }

    public Long getId() {
        return id;
    }

    public GuildEntity getGuild() {
        return guild;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public Map<String, Long> getChannels() {
        return channels;
    }
}
