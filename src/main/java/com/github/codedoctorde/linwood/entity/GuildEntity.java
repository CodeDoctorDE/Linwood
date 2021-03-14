package com.github.codedoctorde.linwood.entity;

import com.github.codedoctorde.linwood.Linwood;
import com.sun.istack.NotNull;
import net.dv8tion.jda.api.entities.Role;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.*;

/**
 * @author CodeDoctorDE
 */
@Entity
@Table(name = "guild")
public class GuildEntity {
    @Id
    @Column(name="id", unique = true, nullable = false)
    private long guildId;
    @ElementCollection
    @CollectionTable(name="Prefixes", joinColumns=@JoinColumn(name="guild_id"))
    @Column(name="prefix")
    private final Set<String> prefixes = new HashSet<>(Linwood.getInstance().getConfig().getPrefixes());
    private String locale = Locale.ENGLISH.toLanguageTag();
    @OneToOne(cascade={CascadeType.ALL}, optional = false)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private final GameEntity gameEntity = new GameEntity();
    @OneToMany
    @NotNull
    private final Set<WikiEntity> wikis = new HashSet<>();
    @OneToOne(cascade={CascadeType.ALL}, optional = false)
    @JoinColumn(name = "karma_id", referencedColumnName = "id")
    private final KarmaEntity karmaEntity = new KarmaEntity();
    @OneToOne(cascade={CascadeType.ALL}, optional = false)
    @JoinColumn(name = "notification_id", referencedColumnName = "id")
    private final NotificationEntity notificationEntity = new NotificationEntity();
    private Long maintainerId = null;
    private GuildPlan plan = GuildPlan.COMMUNITY;

    public GuildEntity(){
    }
    public GuildEntity(long id) {
        this.guildId = id;
    }

    public Long getGuildId() {
        return guildId;
    }

    public String getLocale() {
        return locale;
    }

    public Locale getLocalization(){
        return Locale.forLanguageTag(locale);
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public void save(Session session){
        var t = session.beginTransaction();
        session.saveOrUpdate(this);
        t.commit();
    }

    public static GuildEntity get(Session session, long guildId){
        return Linwood.getInstance().getDatabase().getGuildById(session, guildId);
    }

    public GameEntity getGameEntity() {
        return gameEntity;
    }

    public KarmaEntity getKarmaEntity() {
        return karmaEntity;
    }

    public NotificationEntity getNotificationEntity() {
        return notificationEntity;
    }

    public Long getMaintainerId() {
        return maintainerId;
    }
    public Role getMaintainer(){
        if(maintainerId == null)
            return null;
        return Linwood.getInstance().getJda().getRoleById(maintainerId);
    }

    public void setMaintainerId(Long maintainer) {
        this.maintainerId = maintainer;
    }

    public void setMaintainer(Role role){
        if(role == null)
            maintainerId = null;
        else
            maintainerId = role.getIdLong();
    }

    public Set<String> getPrefixes() {
        return prefixes;
    }

    public GuildPlan getPlan() {
        return plan;
    }

    public void setPlan(GuildPlan plan) {
        this.plan = plan;
    }
    public boolean addPrefix(String prefix){
        if(plan.getPrefixLimit() == -1 || plan.getPrefixLimit() <= getPrefixes().size() + 1)
            return getPrefixes().add(prefix);
        return false;
    }
}
