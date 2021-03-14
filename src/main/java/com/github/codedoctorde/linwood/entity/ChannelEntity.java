package com.github.codedoctorde.linwood.entity;

import com.github.codedoctorde.linwood.Linwood;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author CodeDoctorDE
 */
@Entity
@Table
public class ChannelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @OneToOne
    private TeamEntity team;
    private String name;
    private VisibilityLevel visibility = VisibilityLevel.PUBLIC;

    public Long getId() {
        return id;
    }

    public TeamEntity getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public VisibilityLevel getVisibility() {
        return visibility;
    }

    public void setVisibility(VisibilityLevel visibility) {
        this.visibility = visibility;
    }
    public boolean isAllowed(PermissionLevel level){
        return visibility == VisibilityLevel.PUBLIC && level != PermissionLevel.INVITED ||
                visibility == VisibilityLevel.INTERNAL && level != PermissionLevel.INVITED && level != PermissionLevel.MEMBER ||
                visibility == VisibilityLevel.PRIVATE && (level == PermissionLevel.OWNER || level == PermissionLevel.ADMIN);
    }

    public void save(Session session) {
        session.saveOrUpdate(this);
    }
}
