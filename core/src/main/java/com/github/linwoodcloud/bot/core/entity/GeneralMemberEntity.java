package com.github.linwoodcloud.bot.core.entity;

import com.github.linwoodcloud.bot.core.utils.DatabaseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

/**
 * @author CodeDoctorDE
 */
public class GeneralMemberEntity extends MemberEntity {
    private static final Logger LOGGER = LogManager.getLogger(GeneralMemberEntity.class);
    private final long id;
    private final String memberId;
    private final String guildId;
    private String locale = null;

    public GeneralMemberEntity(long id, String guildId, String memberId) {
        this.id = id;
        this.guildId = guildId;
        this.memberId = memberId;
    }

    public static void create() {
        var config = DatabaseUtil.getConfig();
        update("CREATE TABLE IF NOT EXISTS `" + config.getPrefix() + "member` ( " +
                "`id` BIGINT NOT NULL PRIMARY KEY , " +
                "`member` VARCHAR(255) NOT NULL , " +
                "`guild` VARCHAR(255) NOT NULL , " +
                "`locale` VARCHAR(20) NULL DEFAULT NULL" +
                ")");
        LOGGER.info("Tables initialized!");
    }

    public static GeneralMemberEntity get(String guildId, String memberId) {
        var rs = query("SELECT * FROM `" + getPrefix() + "guild WHERE guild=? AND member=?");
        try {
            rs.next();
            GeneralMemberEntity entity = new GeneralMemberEntity(rs.getLong("id"), guildId, memberId);
            entity.locale = rs.getString("locale");

            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getMemberId() {
        return memberId;
    }

    public long getId() {
        return id;
    }

    @Override
    public void insert() {
        update("INSERT INTO " + getPrefix() + "member (id, member, guild, locale) VALUES (?,?,?,?)", id, memberId, guildId, locale);
    }

    @Override
    public void save() {
        update("UPDATE " + getPrefix() + "member SET locale=? WHERE guild=?", locale);
    }

    @Override
    public void delete() {
        update("DELETE FROM " + getPrefix() + "member WHERE id=?", id);
    }

    @Override
    public String getGuildId() {
        return guildId;
    }
}
