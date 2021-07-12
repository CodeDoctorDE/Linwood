package com.github.linwoodcloud.bot.game.mode.tictactoe;

import com.github.linwoodcloud.bot.game.engine.Vector2;
import com.github.linwoodcloud.bot.game.engine.World;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * @author CodeDoctorDE
 */
public class TicTacToeWorld extends World {
    public TicTacToeWorld() {
        super(new Vector2(100, 100));
        try {
            setBackgroundImage(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("assets/tictactoe/background.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void act() {
        getActors().add(new CrossTicTacToeActor() {{
            getTransform().setLocation(0, 0);
        }});
        getActors().add(new TriangleTicTacToeActor() {{
            getTransform().setLocation(2, 0);
        }});
        getActors().add(new CrossTicTacToeActor() {{
            getTransform().setLocation(0, 1);
        }});
        getActors().add(new CrossTicTacToeActor() {{
            getTransform().setLocation(1, 2);
        }});
        getActors().add(new CrossTicTacToeActor() {{
            getTransform().setLocation(2, 2);
        }});
        getActors().add(new CircleTicTacToeActor() {{
            getTransform().setLocation(1, 0);
        }});
        getActors().add(new TriangleTicTacToeActor() {{
            getTransform().setLocation(2, 1);
        }});
        getActors().add(new CircleTicTacToeActor() {{
            getTransform().setLocation(0, 2);
        }});
        getActors().add(new CircleTicTacToeActor() {{
            getTransform().setLocation(1, 1);
        }});
    }

    public boolean addCircle(int x, int y) {
        return addCircle(new Vector2(x, y));
    }

    private boolean addCircle(Vector2 vector2) {
        if (getActors(vector2).length > 0)
            return false;
        addActor(new CircleTicTacToeActor(), vector2);
        return true;
    }

    public boolean addCross(int x, int y) {
        return addCross(new Vector2(x, y));
    }

    private boolean addCross(Vector2 vector2) {
        if (getActors(vector2).length > 0)
            return false;
        addActor(new CrossTicTacToeActor(), vector2);
        return true;
    }

    public boolean addTriangle(int x, int y) {
        return addTriangle(new Vector2(x, y));
    }

    private boolean addTriangle(Vector2 vector2) {
        if (getActors(vector2).length > 0)
            return false;
        addActor(new TriangleTicTacToeActor(), vector2);
        return true;
    }

    public void checkWin() {

    }
}
