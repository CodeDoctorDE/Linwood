package com.github.linwoodcloud.bot.core.utils;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.config.DatabaseConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author CodeDoctorDE
 */
public class DatabaseUtil {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseUtil.class);
    private static DatabaseUtil instance;
    private HikariDataSource dataSource;

    public DatabaseUtil() {
        if (instance == null)
            instance = this;
    }

    public static DatabaseUtil getInstance() {
        return instance;
    }

    public static DatabaseConfig getConfig() {
        return Linwood.getInstance().getConfig().getDatabase();
    }

    public void connect() {
        var config = Linwood.getInstance().getConfig().getDatabase().build();
        dataSource = new HikariDataSource(config);
    }

    public boolean isConnected() {
        return dataSource != null && !dataSource.isClosed();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void close() {
        if (!isConnected())
            return;
        dataSource.close();
        dataSource = null;
    }

    public void update(String query, Object... params) {
        try {
            var connection = getConnection();
            var statement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++)
                statement.setObject(i + 1, params[i]);
            statement.executeUpdate();
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }
    }

    public ResultSet query(String query, Object... params) {
        try {
            var connection = getConnection();
            var statement = connection.prepareStatement(query);
            for (int i = 0; i < params.length; i++)
                statement.setObject(i + 1, params[i]);
            return statement.executeQuery();
        } catch (SQLException e) {
            connect();
            e.printStackTrace();
        }
        return null;
    }
}
