package com.github.linwoodcloud.bot.core.config;


import java.util.*;

/**
 * @author CodeDoctorDE
 */
public class MainConfig {
    private final Set<String> prefixes = new HashSet<>(Arrays.asList("+lw", "+linwood"));
    private final Set<Long> owners = new HashSet<>(new ArrayList<>());
    private final List<String> activities = Arrays.asList("by CodeDoctor", "%2$s servers", "%2$s players");
    private String secret;
    private DatabaseConfig database = new DatabaseConfig();
    private boolean userStats = false;
    private String supportURL = "https://discord.gg/97zFtYN";

    public MainConfig() {

    }

    public Set<String> getPrefixes() {
        return prefixes;
    }

    public List<String> getActivities() {
        return activities;
    }

    public String getSupportURL() {
        return supportURL;
    }

    public void setSupportURL(String supportURL) {
        this.supportURL = supportURL;
    }

    public boolean isUserStats() {
        return userStats;
    }

    public void setUserStats(boolean userStats) {
        this.userStats = userStats;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Set<Long> getOwners() {
        return owners;
    }

    public DatabaseConfig getDatabase() {
        return database;
    }
}
