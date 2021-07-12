package com.github.linwoodcloud.bot.game.mode.whatisit;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.apps.single.SingleApplication;
import com.github.linwoodcloud.bot.core.apps.single.SingleApplicationMode;
import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import com.github.linwoodcloud.bot.game.entity.GameEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.*;

/**
 * @author CodeDoctorDE
 */
public class WhatIsIt implements SingleApplicationMode {
    private final HashSet<String> wantWriter = new HashSet<>();
    private final int maxRounds;
    private final Random random = new Random();
    private final String rootChannel;
    private final HashMap<String, Integer> points = new HashMap<>();
    private SingleApplication game;
    private WhatIsItRound round;
    private String textChannelId;
    private String wantWriterMessageId;
    private int currentRound = 0;
    private Timer timer = new Timer();
    private WhatIsItEvents events;
    //private boolean hasChannelDisabled = false;

    public WhatIsIt(int maxRounds, String rootChannel) {
        this.maxRounds = maxRounds;
        this.rootChannel = rootChannel;
    }

    @Override
    public void start(SingleApplication app) {
        this.game = app;

        events = new WhatIsItEvents(this);
        Linwood.getInstance().getJda().addEventListener(events);
        var guild = GeneralGuildEntity.get(game.getGuildId());
        var entity = GameEntity.get(game.getGuildId());
        Category category = null;
        if (entity.getGameCategoryId() != null)
            category = entity.getGameCategory();
        var bundle = getBundle();
        Category finalCategory = category;
        ((finalCategory == null) ? game.getGuild().createTextChannel(String.format(bundle.getString("TextChannel"), game.getId())) :
                finalCategory.createTextChannel(String.format(bundle.getString("TextChannel"), game.getId()))).queue((textChannel -> {
            this.textChannelId = textChannel.getId();
            if (finalCategory != null)
                textChannel.getManager().setParent(finalCategory).queue();
            chooseNextPlayer();
        }));
        //hasChannelDisabled = Linwood.getInstance().getUserListener().getDisabledChannels().add(textChannelId);
    }

    @Override
    public void stop() {
        stopTimer();
        Linwood.getInstance().getJda().removeEventListener(events);
        if (round != null)
            round.stopTimer();
        sendLeaderboard(Linwood.getInstance().getJda().getTextChannelById(rootChannel));
        var textChannel = getTextChannel();
        if (textChannel != null)
            textChannel.delete().queue();
        /*if(hasChannelDisabled)
            Linwood.getInstance().getUserListener().getDisabledChannels().remove(textChannelId);*/
    }

    public void chooseNextPlayer() {
        var bundle = getBundle();
        sendLeaderboard();
        getTextChannel().sendMessageFormat(bundle.getString("Next"), currentRound + 1).queue(message -> {
            wantWriterMessageId = message.getId();
            message.addReaction("\uD83D\uDD90️").queue(aVoid ->
                    message.addReaction("⛔").queue());
            stopTimer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    stopTimer();
                    var wantWriterList = new ArrayList<>(wantWriter);
                    if (wantWriter.size() < 1) finishGame();
                    else nextRound(wantWriterList.get(random.nextInt(wantWriterList.size())));
                }
            }, 45 * 1000);
        });
    }

    public void stopTimer() {
        try {
            timer.cancel();
            timer = new Timer();
        } catch (Exception ignored) {

        }
    }

    public void nextRound(String writerId) {
        currentRound++;
        round = new WhatIsItRound(writerId, this);
        var bundle = getBundle();
        game.getGuild().retrieveMemberById(writerId).queue(member -> {
            getTextChannel().sendMessageFormat(bundle.getString("Round"), member.getAsMention()).queue();
            round.inputWriter();
        });
    }

    public void cancelRound() {
        var bundle = getBundle();
        getTextChannel().sendMessage(bundle.getString("Cancel")).queue();
        finishRound();
    }

    public void finishGame() {
        stopTimer();
        clearWantWriterMessage();
        var bundle = getBundle();
        var textChannel = getTextChannel();
        textChannel.sendMessage(bundle.getString("Finish")).queue();
        sendLeaderboard();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                stopTimer();
                Linwood.getInstance().getGameManager().stopGame(game);
            }
        }, 30 * 1000);
    }

    public void clearWantWriterMessage() {
        if (wantWriterMessageId == null)
            return;
        getTextChannel().retrieveMessageById(wantWriterMessageId).queue(message -> message.delete().queue());
        wantWriterMessageId = null;
    }

    public void finishRound() {
        game.getGuild().retrieveMemberById(round.getWriterId()).queue(member -> {
            givePoints(member, round.getGuesser().size() * 2);
            round.stopRound();
            round = null;
            wantWriterMessageId = null;
            wantWriter.clear();
            if (currentRound >= maxRounds) finishGame();
            else
                chooseNextPlayer();
        });
    }

    public void sendLeaderboard() {
        sendLeaderboard(getTextChannel());
    }

    public void sendLeaderboard(TextChannel textChannel) {
        var bundle = getBundle();
        sendLeaderboard(bundle, textChannel);
    }

    private void sendLeaderboard(ResourceBundle bundle, TextChannel textChannel) {
        var leaderboard = getLeaderboard();
        if (textChannel == null)
            return;
        textChannel.getGuild().retrieveMembersByIds(leaderboard.stream().map(Map.Entry::getKey).toArray(String[]::new)).onSuccess(members -> {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < members.size(); i++) {
                var member = members.get(i);
                if (member != null)
                    stringBuilder.append(String.format(bundle.getString("Leaderboard"), i + 1,
                            member.getAsMention(), leaderboard.get(i).getValue()));
            }
            textChannel.sendMessageEmbeds(new EmbedBuilder().setTitle(bundle.getString("LeaderboardHeader")).setDescription(stringBuilder.toString()).setFooter(bundle.getString("LeaderboardFooter")).build()).queue();
        });
    }

    private ArrayList<Map.Entry<String, Integer>> getLeaderboard() {
        var set = points.entrySet();
        var list = new ArrayList<>(set);
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        return list;
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("locale.game.WhatIsIt", Objects.requireNonNull(GeneralGuildEntity.get(game.getGuildId())).getLocalization());
    }

    public String getTextChannelId() {
        return textChannelId;
    }

    public TextChannel getTextChannel() {
        return Linwood.getInstance().getJda().getTextChannelById(textChannelId);
    }

    public SingleApplication getGame() {
        return game;
    }

    public WhatIsItRound getRound() {
        return round;
    }

    public HashMap<String, Integer> getPoints() {
        return points;
    }

    public void givePoints(Member member, int number) {
        points.put(member.getId(), points.getOrDefault(member.getId(), 0) + number);
    }

    public int getPoints(Member member) {
        return points.getOrDefault(member.getId(), 0);
    }

    public int getCurrentRound() {
        return currentRound;
    }

    public int getMaxRounds() {
        return maxRounds;
    }

    public HashSet<String> getWantWriter() {
        return wantWriter;
    }

    public String getWantWriterMessageId() {
        return wantWriterMessageId;
    }

    public boolean wantWriter(Member member) {
        if (getRound() != null)
            return false;
        wantWriter.add(member.getId());
        getTextChannel().sendMessageFormat(getBundle().getString("Join"), member.getUser().getAsMention()).queue();
        return true;
    }

    public void removeWriter(Member member) {
        wantWriter.remove(member.getId());
        getTextChannel().sendMessageFormat(getBundle().getString("Leave"), member.getUser().getAsMention()).queue();
    }
}
