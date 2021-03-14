package com.github.codedoctorde.linwood.utils;

import com.github.codedoctorde.linwood.Linwood;
import com.github.codedoctorde.linwood.entity.GuildEntity;
import com.github.codedoctorde.linwood.entity.MemberEntity;
import com.github.codedoctorde.linwood.entity.TeamEntity;
import com.github.codedoctorde.linwood.entity.TeamMemberEntity;
import com.sun.istack.Nullable;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.Order;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author CodeDoctorDE
 */
public class DatabaseUtil {
    private static final Logger logger = LogManager.getLogger(DatabaseUtil.class);
    private final SessionFactory sessionFactory;

    public DatabaseUtil() {
        try {
            createFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        var configuration = new Configuration();
        configuration.configure();

        ResourceBundle prop;
        try (FileInputStream fis = new FileInputStream("db.properties")) {
            prop =  new PropertyResourceBundle(fis);
        } catch (IOException e) {
            e.printStackTrace();
            prop = ResourceBundle.getBundle("db");
        }

// Basic connection information
        configuration.setProperty("hibernate.connection.username", prop.getString("db.username"));
        configuration.setProperty("hibernate.connection.password", prop.getString("db.password"));
        var url = prop.getString("db.url");
        if(url.isBlank())
            configuration.setProperty("hibernate.connection.url", System.getenv("JDBC_DATABASE_URL"));
        else
            configuration.setProperty("hibernate.connection.url", url);
        configuration.setProperty("hibernate.connection.driver_class", prop.getString("db.driver"));
        configuration.setProperty("hibernate.dialect", prop.getString("db.dialect"));
        configuration.setProperty("hibernate.default_schema", prop.getString("db.default_schema"));

        configuration.setProperty("hibernate.current_session_context_class", prop.getString("hibernate.session_context"));

// Handling SQL statements in the logs
        configuration.setProperty("show_sql", prop.getString("hibernate.show_sql"));
        configuration.setProperty("format_sql", prop.getString("hibernate.format_sql"));
        configuration.setProperty("use_sql_comments", prop.getString("hibernate.use_sql_comments"));

// C3P0 Settings
        configuration.setProperty("hibernate.c3p0.min_size", prop.getString("hibernate.c3p0.min_size"));
        configuration.setProperty("hibernate.c3p0.max_size", prop.getString("hibernate.c3p0.max_size"));
        configuration.setProperty("hibernate.c3p0.timeout", prop.getString("hibernate.c3p0.timeout"));
        configuration.setProperty("hibernate.c3p0.max_statements", prop.getString("hibernate.c3p0.max_statements"));
        configuration.setProperty("hibernate.c3p0.idle_test_period", prop.getString("hibernate.c3p0.idle_test_period"));
        configuration.setProperty("hibernate.c3p0.preferredTestQuery", prop.getString("hibernate.c3p0.preferredTestQuery"));
        sessionFactory = configuration.buildSessionFactory();
    }

    public void createFile() throws IOException {
        var file = new File("./db.properties");
        if(!file.exists()){
            Files.copy(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("db.properties")), file.toPath());
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    public GuildEntity getGuildById(Session session, long guildId){
        GuildEntity entity = session.get(GuildEntity.class, guildId);
        if ( entity != null ) return entity;
        else try {
            return createGuild(session, guildId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public GuildEntity createGuild(Session session, long guildId){
        var guild = new GuildEntity(guildId);
        guild.save(session);
        return guild;
    }
    public void updateEntity(Session session, GuildEntity entity){
        session.update(entity);
    }

    public void cleanup(Session session){
        cleanupGuilds(session);
    }

    private void cleanupGuilds(Session session) {
        logger.info("Guild clean up...");
// Create CriteriaBuilder
        var builder = session.getCriteriaBuilder();

// Create CriteriaQuery
        var cq = builder.createQuery(GuildEntity.class);
        var from = cq.from(GuildEntity.class);
        var all = cq.select(from);
        var allQuery = session.createQuery(all);
        int count = 0;
        for (var entity :
                allQuery.getResultList())
            if (Linwood.getInstance().getJda().getGuildById(entity.getGuildId()) == null) {
                session.delete(entity);
                count++;
            }
        logger.info("Successfully clean up " + count + " guilds!");
    }
    public MemberEntity[] getKarmaLeaderboard(Session session, @Nullable Long guild){
        return getKarmaLeaderboard(session, guild, 20);
    }

    public MemberEntity[] getKarmaLeaderboard(Session session, @Nullable Long guild, int maxResults){
// Create CriteriaBuilder
        var builder = session.getCriteriaBuilder();

// Create CriteriaQuery
        var cq = builder.createQuery(MemberEntity.class);
        var member = cq.from(MemberEntity.class);
        var all = cq.select(member);
        if(guild != null)
            all.where(builder.equal(member.get("guildId"), guild));
        all.orderBy(builder.desc(member.get("likes")), builder.asc(member.get("dislikes")));
        var allQuery = session.createQuery(all);
        allQuery.setMaxResults(maxResults);
        return allQuery.getResultList().toArray(new MemberEntity[0]);
    }

    public MemberEntity getMemberEntity(Session session, Member member){
        return getMemberById(session, member.getGuild().getIdLong(), member.getIdLong());
    }

    public MemberEntity getMemberById(Session session, long guildId, long memberId){
        var cb = session.getCriteriaBuilder();
        var cq = cb.createQuery(MemberEntity.class);
        var from = cq.from(MemberEntity.class);

        cq = cq.select(from).where(
                cb.equal(from.get("guildId"), guildId),
                cb.equal(from.get("memberId"), memberId)
        );
        var result = session.createQuery(cq).list();
        if(result.size() < 1)
            return createMember(session, guildId, memberId);
        return result.get(0);
    }

    public TeamMemberEntity getTeamMember(Session session, GuildEntity entity, TeamEntity team){
        return getTeamMember(session, entity.getGuildId(), team.getName());
    }

    public TeamMemberEntity getTeamMember(Session session, Guild guild, TeamEntity team){
        return getTeamMember(session, guild.getIdLong(), team.getName());
    }

    public TeamMemberEntity getTeamMember(Session session, long guildId, String teamName){
        var cb = session.getCriteriaBuilder();
        var cq = cb.createQuery(TeamMemberEntity.class);
        var from = cq.from(TeamMemberEntity.class);

        cq = cq.select(from).where(
                cb.equal(from.get("guildId"), guildId),
                cb.equal(from.get("teamName"), teamName)
        );
        var result = session.createQuery(cq).list();
        if(result.size() < 1)
            return null;
        return result.get(0);
    }

    private MemberEntity createMember(Session session, long guildId, long memberId) {
        var member = new MemberEntity(guildId, memberId);
        member.save(session);
        return member;
    }

    public TeamEntity getTeam(Session session, String name){
        return session.get(TeamEntity.class, name);
    }
}
