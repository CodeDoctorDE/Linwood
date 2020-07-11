package com.github.codedoctorde.linwood.apps.single.game.engine;

/**
 * @author CodeDoctorDE
 */
public class Transform {
    private Vector2 location = new Vector2();
    private double rotation = 0;
    private Vector2 scale = new Vector2(100, 100);

    public Vector2 getLocation() {
        return location;
    }

    public double getRotation() {
        return rotation;
    }

    public Vector2 getScale() {
        return scale;
    }

    public void setLocation(Vector2 location) {
        this.location = location;
    }

    public void setLocation(int x, int y){
        this.location = new Vector2(x, y);
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector2 scale) {
        this.scale = scale;
    }
    public void setScale(int x, int y){
        this.scale = new Vector2(x, y);
    }
}
