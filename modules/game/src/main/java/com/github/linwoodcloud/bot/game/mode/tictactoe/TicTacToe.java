package com.github.linwoodcloud.bot.game.mode.tictactoe;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.apps.single.SingleApplication;
import com.github.linwoodcloud.bot.core.apps.single.SingleApplicationMode;
import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import com.github.linwoodcloud.bot.game.engine.Board;
import com.github.linwoodcloud.bot.game.entity.GameEntity;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.ChannelAction;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author CodeDoctorDE
 */
public class TicTacToe extends Board implements SingleApplicationMode {
    private final int maxRounds;
    private final String rootChannel;
    private final List<Long> players = new ArrayList<>();
    private SingleApplication game;
    private TicTacToeEvents events;
    private String textChannelId;
    private long ownerId;
    /*
    0\uFE0F\u20E3
    1\uFE0F\u20E3
    2\uFE0F\u20E3
    3\uFE0F\u20E3
    4\uFE0F\u20E3
    5\uFE0F\u20E3
    6\uFE0F\u20E3
    7\uFE0F\u20E3
    8\uFE0F\u20E3
    9\uFE0F\u20E3
     */

    public TicTacToe(int maxRounds, String rootChannel) {
        this.maxRounds = maxRounds;
        this.rootChannel = rootChannel;
    }

    @Override
    public void start(SingleApplication app) {
        game = app;
        events = new TicTacToeEvents(this);
        Linwood.getInstance().getJda().addEventListener(events);
        var guild = GeneralGuildEntity.get(game.getGuildId());
        var entity = GameEntity.get(game.getGuildId());
        Category category = null;
        if (entity.getGameCategoryId() != null)
            category = entity.getGameCategory();
        var bundle = getBundle();
        Category finalCategory = category;
        ChannelAction<TextChannel> action;
        action = finalCategory == null ? game.getGuild().createTextChannel(String.format(bundle.getString("TextChannel"), game.getId())) :
                finalCategory.createTextChannel(String.format(bundle.getString("TextChannel"), game.getId()));
        action.queue((textChannel -> {
            this.textChannelId = textChannel.getId();
            if (finalCategory != null)
                textChannel.getManager().setParent(finalCategory).queue();
            chooseNextPlayer();
        }));
    }

    private void chooseNextPlayer() {

    }

    private ResourceBundle getBundle() {
        return ResourceBundle.getBundle("locale.game.TicTacToe", GeneralGuildEntity.get(game.getGuildId()).getLocalization());
    }


    @Override
    public void stop() {

    }

    @Override
    public void act() {

    }
}
