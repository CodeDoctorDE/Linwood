package com.github.linwoodcloud.bot.core.config;

import com.zaxxer.hikari.HikariConfig;

/**
 * @author CodeDoctorDE
 */
public class DatabaseConfig {
    private DatabaseType type;
    private String username;
    private String password;
    private String host;
    private String port;
    private String database;
    private String prefix;

    public DatabaseConfig() {

    }

    public DatabaseType getType() {
        return type;
    }

    public void setType(DatabaseType type) {
        this.type = type;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getUrl() {
        return "jdbc:" + type.getIdentifier() + "://" + host + ":" + port + "/" + database;
    }

    public HikariConfig build() {
        var config = new HikariConfig();
        config.setJdbcUrl(getUrl());
        config.setUsername(username);
        config.setPassword(password);
        return config;
    }
}
