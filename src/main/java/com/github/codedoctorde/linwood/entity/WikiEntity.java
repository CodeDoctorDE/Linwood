package com.github.codedoctorde.linwood.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * @author CodeDoctorDE
 */
@Entity
@Table(name = "templates", uniqueConstraints = {
        @UniqueConstraint(columnNames = "ID")
})
public class WikiEntity {
    @Column(name="ID", unique = true, nullable = false)
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name="server_id", nullable=false)
    private GuildEntity server;
    @Column(nullable = false)
    private String name;
    @Column(name="content", nullable = false, columnDefinition = "TEXT")
    String content;
    @Column(nullable = false)
    private Visibility visibility = Visibility.INTERNAL;
    public WikiEntity(){

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
