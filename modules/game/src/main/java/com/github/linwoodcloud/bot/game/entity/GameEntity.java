package com.github.linwoodcloud.bot.game.entity;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.entity.GuildEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import javax.annotation.Nullable;

public class GameEntity extends GuildEntity {
    private Long id;
    private String gameMasterRoleId;
    private String gameCategoryId;
    private String guildId;

    public GameEntity(String guildId) {
        this.guildId = guildId;
    }

    public static GameEntity get(String guildId) {
        return new GameEntity(guildId);
    }

    public Long getId() {
        return id;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getGameCategoryId() {
        return gameCategoryId;
    }

    public void setGameCategoryId(String gameCategoryId) {
        this.gameCategoryId = gameCategoryId;
    }

    public Category getGameCategory() {
        if (gameCategoryId == null)
            return null;
        else
            return Linwood.getInstance().getJda().getCategoryById(gameCategoryId);
    }

    public void setGameCategory(@Nullable Category category) {
        if (category == null)
            gameCategoryId = null;
        else
            gameCategoryId = category.getId();
    }

    public boolean isGameMaster(Member member) {
        var category = getGameCategory();
        return member.getRoles().stream().map(Role::getId).anyMatch(id -> id.equals(gameMasterRoleId)) ||
                (category == null ? member.hasPermission(Permission.MANAGE_CHANNEL) : member.hasPermission(category, Permission.MANAGE_CHANNEL));
    }

    public String getGameMasterRoleId() {
        return gameMasterRoleId;
    }

    public void setGameMasterRoleId(String gameMasterRoleId) {
        this.gameMasterRoleId = gameMasterRoleId;
    }

    public Role getGameMasterRole() {
        if (gameMasterRoleId == null)
            return null;
        return Linwood.getInstance().getJda().getRoleById(gameMasterRoleId);
    }

    public void setGameMasterRole(@Nullable Role role) {
        if (role == null)
            gameMasterRoleId = null;
        else
            gameMasterRoleId = role.getId();
    }

    @Override
    public void insert() {

    }

    @Override
    public void save() {

    }
}
