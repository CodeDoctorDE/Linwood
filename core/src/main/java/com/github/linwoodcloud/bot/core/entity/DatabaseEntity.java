package com.github.linwoodcloud.bot.core.entity;


import com.github.linwoodcloud.bot.core.utils.DatabaseUtil;

import java.sql.ResultSet;

public abstract class DatabaseEntity {
    public static String getPrefix() {
        return DatabaseUtil.getConfig().getPrefix();
    }

    public static void update(String query, Object... params) {
        DatabaseUtil.getInstance().update(query, params);
    }

    public static ResultSet query(String query, Object... params) {
        return DatabaseUtil.getInstance().query(query, params);
    }

    public abstract void insert();

    public abstract void save();

    public abstract void delete();
}
