package com.github.linwoodcloud.bot.game.engine;

/**
 * @author CodeDoctorDE
 */
public class Vector3 {
    private final int x;
    private final int y;
    private final int z;

    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3() {
        x = 0;
        y = 0;
        z = 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
