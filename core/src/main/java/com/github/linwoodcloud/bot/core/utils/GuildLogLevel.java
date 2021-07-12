package com.github.linwoodcloud.bot.core.utils;

public enum GuildLogLevel {
    INFO, VERBOSE, WARNING, ERROR;

    public int getColor() {
        switch (this) {
            case INFO:
                return 0x63c0df;
            case VERBOSE:
                return 0xffffff;
            case WARNING:
                return 0xf0541e;
            case ERROR:
                return 0xcf000f;
        }
        return 0;
    }
}
