package com.github.linwoodcloud.bot.game.mode.whatisit;

import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import com.github.linwoodcloud.bot.game.entity.GameEntity;
import io.sentry.Sentry;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.exceptions.PermissionException;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

/**
 * @author CodeDoctorDE
 */
public class WhatIsItEvents {
    private final WhatIsIt whatIsIt;

    public WhatIsItEvents(WhatIsIt whatIsIt) {
        this.whatIsIt = whatIsIt;
    }

    @SubscribeEvent
    public void onGuess(MessageReceivedEvent event) {
        try {
            if (!event.getChannel().getId().equals(whatIsIt.getTextChannelId()) || event.getMember() == null)
                return;
            var message = event.getMessage();
            if (whatIsIt.getTextChannelId().equals(message.getTextChannel().getId())) {
                var round = whatIsIt.getRound();
                if (round != null && round.getWord() != null && message.getContentStripped().toLowerCase().contains(round.getWord().toLowerCase())) {
                    message.delete().queue();
                    if (!round.isGuesser(event.getMember()) && !message.getAuthor().getId().equals(round.getWriterId()))
                        event.getChannel().sendMessageFormat(whatIsIt.getBundle().getString("Guess"), event.getAuthor().getName(), round.guessCorrectly(event.getMember())).queue(message1 -> {
                            round.checkEverybody();
                        });
                }
            }
        } catch (PermissionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    @SubscribeEvent
    public void onWord(MessageReceivedEvent event) {
        try {
            if (event.getChannelType() == ChannelType.PRIVATE && whatIsIt.getRound() != null && whatIsIt.getRound().getWriterId() == event.getAuthor().getId() && whatIsIt.getRound().getWord() == null)
                whatIsIt.getRound().startRound(event.getMessage().getContentStripped());
        } catch (PermissionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    @SubscribeEvent
    public void onJoin(MessageReactionAddEvent event) {
        try {
            event.retrieveMember().queue(member -> {
                var entity = GeneralGuildEntity.get(event.getGuild().getId());
                var gameEntity = GameEntity.get(event.getGuild().getId());
                var bundle = whatIsIt.getBundle();
                if (event.getChannel().getId().equals(whatIsIt.getTextChannelId()) && event.getMember() != null &&
                        event.getMessageId().equals(whatIsIt.getWantWriterMessageId()) && !event.getMember().getUser().isBot() && event.getReactionEmote().isEmoji())
                    switch (event.getReactionEmote().getAsCodepoints()) {
                        case "U+1f590U+fe0f":
                            if (!whatIsIt.wantWriter(event.getMember()))
                                event.getReaction().removeReaction(event.getMember().getUser()).queue();
                            break;
                        case "U+26d4":
                            if (gameEntity.isGameMaster(event.getMember())) {
                                whatIsIt.finishGame();
                                break;
                            } else
                                event.getMember().getUser().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(bundle.getString("NoPermission")).queue());
                        default:
                            event.getReaction().removeReaction(event.getMember().getUser()).queue();
                    }
            });
        } catch (PermissionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    @SubscribeEvent
    public void onLeave(MessageReactionRemoveEvent event) {
        try {
            event.retrieveMember().queue(member -> {
                if (event.getChannel().getId().equals(whatIsIt.getTextChannelId()) && member != null &&
                        event.getMessageId().equals(whatIsIt.getWantWriterMessageId()) && !member.getUser().isBot() && event.getReactionEmote().getAsCodepoints().equals("U+1f590U+fe0f"))
                    whatIsIt.removeWriter(member);
            });
        } catch (PermissionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    public WhatIsIt getWhatIsIt() {
        return whatIsIt;
    }
}
