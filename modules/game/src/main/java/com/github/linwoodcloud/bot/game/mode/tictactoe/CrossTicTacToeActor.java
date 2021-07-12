package com.github.linwoodcloud.bot.game.mode.tictactoe;

import com.github.linwoodcloud.bot.game.engine.Actor;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

/**
 * @author CodeDoctorDE
 */
public class CrossTicTacToeActor extends Actor {
    public CrossTicTacToeActor() {
        try {
            setImage(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("assets/tictactoe/cross.png"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void act() {

    }
}
