package com.github.linwoodcloud.bot.game.engine;

import com.github.linwoodcloud.bot.core.utils.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CodeDoctorDE
 */
public abstract class World {
    private final List<Actor> actors = new ArrayList<>();
    private Vector2 cellSize;
    private BufferedImage backgroundImage = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);

    public World(Vector2 cellSize) {
        this.cellSize = cellSize;
        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/assets/tictactoe/background.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Vector2 getCellSize() {
        return cellSize;
    }

    public void setCellSize(Vector2 cellSize) {
        this.cellSize = cellSize;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void addActor(Actor actor) {
        addActor(actor, 0, 0);
    }

    public void addActor(Actor actor, int x, int y) {
        addActor(actor, new Vector2(x, y));
    }

    public void addActor(Actor actor, Vector2 location) {
        actor.getTransform().setLocation(location);
        actors.add(actor);
    }

    public void removeActor() {

    }

    public BufferedImage getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Actor[] getActors(int x, int y) {
        return getActors(new Vector2(x, y));
    }

    public Actor[] getActors(Vector2 location) {
        return actors.stream().filter(actor -> actor.getTransform().getLocation().getX() == location.getX() && actor.getTransform().getLocation().getY() == location.getY()).toArray(Actor[]::new);
    }

    public BufferedImage render() {
        act();
        actors.forEach(Actor::act);
        var renderedImage = ImageUtil.clone(backgroundImage);
        var graphics = renderedImage.getGraphics();
        actors.forEach(actor -> graphics.drawImage(actor.render(), actor.getTransform().getLocation().getX() * cellSize.getX(), actor.getTransform().getLocation().getY() * cellSize.getY(),
                actor.getTransform().getScale().getX(), actor.getTransform().getScale().getY(), null));
        graphics.dispose();
        return renderedImage;
    }

    public abstract void act();
}
