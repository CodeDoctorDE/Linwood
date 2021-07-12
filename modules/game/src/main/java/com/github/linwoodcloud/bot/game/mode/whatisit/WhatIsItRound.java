package com.github.linwoodcloud.bot.game.mode.whatisit;

import net.dv8tion.jda.api.entities.Member;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author CodeDoctorDE
 */
public class WhatIsItRound {
    private final String writerId;
    private final WhatIsIt whatIsIt;
    private final HashSet<String> guesser = new HashSet<>();
    private String word;
    private Timer timer = new Timer();
    private int time = 120;

    public WhatIsItRound(String writerId, WhatIsIt whatIsIt) {
        this.writerId = writerId;
        this.whatIsIt = whatIsIt;
    }

    public void inputWriter() {
        whatIsIt.getGame().getGuild().retrieveMemberById(writerId).queue(member ->
                member.getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(whatIsIt.getBundle().getString("Input")).queue())
        );
        stopTimer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                whatIsIt.cancelRound();
            }
        }, 60 * 1000);
    }

    public String getWord() {
        return word;
    }

    public String getWriterId() {
        return writerId;
    }

    public Member getWriter() {
        return whatIsIt.getGame().getGuild().getMemberById(writerId);
    }

    public void startRound(String word) {
        this.word = word;
        whatIsIt.clearWantWriterMessage();
        stopTimer();
        time = 120;
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                var bundle = whatIsIt.getBundle();
                if (time <= 0 || whatIsIt.getTextChannel() == null) {
                    timer.cancel();
                    whatIsIt.finishRound();
                } else {
                    String message = null;
                    switch (time) {
                        case 120:
                            message = String.format(bundle.getString("CountdownMinutes"), time / 60);
                            break;
                        case 60:
                            message = bundle.getString("CountdownMinute");
                            break;
                        case 30:
                        case 20:
                        case 10:
                        case 5:
                        case 4:
                        case 3:
                        case 2:
                            message = String.format(bundle.getString("CountdownSeconds"), time);
                            break;
                        case 1:
                            message = bundle.getString("CountdownSecond");
                    }
                    if (message != null)
                        whatIsIt.getTextChannel().sendMessage(message).queue();
                    time--;
                }
            }
        }, 1000, 1000);
    }

    public void stopRound() {
        var bundle = whatIsIt.getBundle();
        if (word != null)
            whatIsIt.getTextChannel().sendMessageFormat(bundle.getString("Word"), word).queue();
        stopTimer();
    }

    /**
     * Points:
     * 1st: 5 points
     * 2nd: 4 points
     * 3rd: 3 points
     * 4th: 2 points
     * 5th and under: 1 point
     *
     * @param member Current guesser
     */
    public int guessCorrectly(Member member) {
        guesser.add(member.getId());
        int points = (guesser.size() < 5) ? 6 - guesser.size() : 1;
        whatIsIt.givePoints(member, points);
        return points;
    }

    public boolean isGuesser(Member member) {
        return guesser.stream().anyMatch(memberId -> memberId.equals(member.getId()));
    }

    public HashSet<String> getGuesser() {
        return guesser;
    }

    public void stopTimer() {
        try {
            timer.cancel();
            timer = new Timer();
        } catch (Exception ignored) {

        }
    }

    public void checkEverybody() {
        var last = new HashSet<>(whatIsIt.getWantWriter());
        guesser.forEach(last::remove);
        last.remove(writerId);
        if (whatIsIt.getWantWriter().size() <= 1)
            return;
        if (last.size() <= 0) {
            whatIsIt.getTextChannel().sendMessage(whatIsIt.getBundle().getString("Everybody")).queue();
            earlyStop();
        }
    }

    private void earlyStop() {
        time = 15;
    }
}
