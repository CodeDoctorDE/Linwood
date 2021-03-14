package com.github.codedoctorde.linwood.config;


import net.dv8tion.jda.api.entities.Activity;

import java.util.*;

/**
 * @author CodeDoctorDE
 */
public class MainConfig {
    private final Set<String> prefixes = new HashSet<>(Arrays.asList("+lw", "+linwood"));
    private String secret;
    private Integer port = 9000;
    private boolean userStats = false;
    private final Set<Long> owners = new HashSet<>(new ArrayList<>());


    private final List<ActivityConfig> activities = new ArrayList<>(){{
        add(new ActivityConfig(Activity.ActivityType.LISTENING, "CodeDoctor"));
        add(new ActivityConfig(Activity.ActivityType.WATCHING, "github/CodeDoctorDE"));
        add(new ActivityConfig(Activity.ActivityType.DEFAULT, "games with players :D"));
        add(new ActivityConfig(Activity.ActivityType.DEFAULT, "%s"));
        add(new ActivityConfig(Activity.ActivityType.WATCHING, "on %2$s servers"));
        add(new ActivityConfig(Activity.ActivityType.DEFAULT, "with %2$s players"));
    }};
    private String supportURL = "https://discord.gg/97zFtYN";
    private String redirectURI = "https://app.linwood.tk/#/callback";
    private String token = "";
    private String clientId = "";
    private String clientSecret = "";

    public MainConfig(){

    }

    public Set<String> getPrefixes() {
        return prefixes;
    }

    public List<ActivityConfig> getActivities() {
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

    public Integer getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Set<Long> getOwners() {
        return owners;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }

    public String getToken() {
        return token;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
