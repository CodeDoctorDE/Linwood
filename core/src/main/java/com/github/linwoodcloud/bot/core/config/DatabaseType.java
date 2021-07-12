package com.github.linwoodcloud.bot.core.config;

public enum DatabaseType {
    MYSQL("mysql"), MARIADB("mariadb"), POSTGRESQL("postgresql"), SQLITE(null);

    private final String identifier;

    DatabaseType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
