package com.github.linwoodcloud.bot.notification.entity;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.entity.GuildEntity;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.SQLException;

public class NotificationEntity extends GuildEntity {
    private final String guildId;
    private String teamRoleId = null;
    private String supportChatId = null;
    private String statusChatId = null;
    private String logChatId = null;

    public NotificationEntity(String guildId) {
        this.guildId = guildId;
    }

    public static NotificationEntity get(String guildId) {
        var rs = query("SELECT * FROM " + getPrefix() + "guild WHERE guild=?", guildId);
        try {
            rs.next();
            NotificationEntity entity = new NotificationEntity(guildId);
            entity.teamRoleId = rs.getString("team_role");
            entity.supportChatId = rs.getString("support_chat");
            entity.statusChatId = rs.getString("status_chat");
            entity.logChatId = rs.getString("log_chat");

            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getGuildId() {
        return guildId;
    }

    public Role getTeamRole() {
        if (teamRoleId == null)
            return null;
        return Linwood.getInstance().getJda().getRoleById(teamRoleId);
    }

    public void setTeamRole(Role teamRole) {
        if (teamRole == null)
            teamRoleId = null;
        else
            teamRoleId = teamRole.getId();
    }

    public String getTeamRoleId() {
        return teamRoleId;
    }

    public void setTeamRoleId(String teamRoleId) {
        this.teamRoleId = teamRoleId;
    }

    public String getSupportChatId() {
        return supportChatId;
    }

    public void setSupportChatId(String supportChatId) {
        this.supportChatId = supportChatId;
    }

    public TextChannel getSupportChat() {
        if (supportChatId == null)
            return null;
        else
            return Linwood.getInstance().getJda().getTextChannelById(supportChatId);
    }

    public void setSupportChat(TextChannel chat) {
        if (chat == null)
            supportChatId = null;
        else
            supportChatId = chat.getId();
    }

    public String getStatusChatId() {
        return statusChatId;
    }

    public void setStatusChatId(String statusChatId) {
        this.statusChatId = statusChatId;
    }

    public TextChannel getStatusChat() {
        if (statusChatId == null)
            return null;
        else
            return Linwood.getInstance().getJda().getTextChannelById(statusChatId);
    }

    public void setStatusChat(TextChannel chat) {
        if (chat == null)
            statusChatId = null;
        else
            statusChatId = chat.getId();
    }

    public String getLogChatId() {
        return logChatId;
    }

    public void setLogChatId(String logChatId) {
        this.logChatId = logChatId;
    }

    public TextChannel getLogChat() {
        if (logChatId == null)
            return null;
        else
            return Linwood.getInstance().getJda().getTextChannelById(logChatId);
    }

    public void setLogChat(TextChannel chat) {
        if (chat == null)
            logChatId = null;
        else
            logChatId = chat.getId();
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
