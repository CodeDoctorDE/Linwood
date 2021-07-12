package com.github.linwoodcloud.bot.core.engine;

/**
 * @author CodeDoctorDE
 */
public class Vector2 {
    private final int x;
    private final int y;

    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector2() {
        x = 0;
        y = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
