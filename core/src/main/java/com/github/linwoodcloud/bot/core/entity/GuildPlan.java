package com.github.linwoodcloud.bot.core.entity;

public enum GuildPlan {
    COMMUNITY, PRO, PRIVATE;

    public int getPrefixLimit() {
        switch (this) {
            case COMMUNITY:
                return 3;
            case PRO:
                return 10;
        }
        return -1;
    }

    public int getTeamLimit() {
        switch (this) {
            case COMMUNITY:
                return 10;
            case PRO:
                return 50;
        }
        return -1;
    }
}
