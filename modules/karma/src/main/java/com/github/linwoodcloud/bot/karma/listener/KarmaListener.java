package com.github.linwoodcloud.bot.karma.listener;

import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import com.github.linwoodcloud.bot.karma.entity.KarmaEntity;
import com.github.linwoodcloud.bot.karma.entity.KarmaMemberEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.StatusChangeEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KarmaListener {
    private final HashMap<String, Integer> memberGivingHashMap = new HashMap<>();
    private final Set<String> disabledChannels = new HashSet<>();
    private LocalDate lastReset = LocalDate.now();

    @SubscribeEvent
    public void onGive(MessageReactionAddEvent event) {
        if (disabledChannels.contains(event.getChannel().getId()))
            return;
        maybeReset();
        if (event.getChannelType() != ChannelType.TEXT)
            return;
        event.retrieveMember().queue(donor -> {
            var emote = event.getReactionEmote().isEmoji() ? event.getReactionEmote().getAsReactionCode() : event.getReactionEmote().getEmote().getAsMention();
            var entity = GeneralGuildEntity.get(event.getGuild().getId());
            var karma = KarmaEntity.get(event.getGuild().getId());
            event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> event.getGuild().retrieveMember(message.getAuthor()).queue(taker -> {
                if (karma.getLikeEmote() != null) {
                    boolean works = true;
                    if (taker == null || taker.getUser().isBot() || donor.getUser().isBot()) return;
                    if (!donor.equals(taker)) if (emote.equals(karma.getLikeEmote()))
                        works = giveLike(karma, donor, taker);
                    else if (emote.equals(karma.getDislikeEmote()))
                        works = giveDislike(karma, donor, taker);
                    if (!works)
                        event.getReaction().removeReaction(donor.getUser()).queue();
                }
            }));
        });
    }

    @SubscribeEvent
    public void onRemove(MessageReactionRemoveEvent event) {
        if (disabledChannels.contains(event.getChannel().getId()))
            return;
        maybeReset();
        event.retrieveMember().queue(donor -> {
            var emote = event.getReactionEmote().isEmoji() ? event.getReactionEmote().getAsReactionCode() : event.getReactionEmote().getEmote().getAsMention();
            var entity = GeneralGuildEntity.get(event.getGuild().getId());
            var karma = KarmaEntity.get(event.getGuild().getId());
            event.getChannel().retrieveMessageById(event.getMessageIdLong()).queue(message -> event.getGuild().retrieveMember(message.getAuthor()).queue(taker -> {
                if (taker != null && !taker.getUser().isBot() && !donor.getUser().isBot() && !donor.equals(taker) && karma.getLikeEmote() != null)
                    if (emote.equals(karma.getLikeEmote()))
                        removeLike(donor, taker);
                    else if (emote.equals(karma.getDislikeEmote()))
                        removeDislike(donor, taker);
            }));
        });
    }

    public boolean givingAction(KarmaEntity entity, Member member) {
        if (entity.getMaxGiving() <= memberGivingHashMap.getOrDefault(member.getId(), 0) && !member.hasPermission(Permission.MANAGE_SERVER))
            return false;
        memberGivingHashMap.put(member.getId(), memberGivingHashMap.getOrDefault(member.getId(), 0) + 1);
        return true;
    }

    public boolean giveLike(KarmaEntity entity, Member donor, Member taker) {
        if (!givingAction(entity, donor))
            return false;
        var donorEntity = KarmaMemberEntity.get(donor);
        var takerEntity = KarmaMemberEntity.get(taker);
        takerEntity.setLikes(takerEntity.getLikes() + donorEntity.getLevel() + 1);
        takerEntity.save();
        return true;
    }

    public void removeLike(Member donor, Member taker) {
        var donorEntity = KarmaMemberEntity.get(donor);
        var takerEntity = KarmaMemberEntity.get(taker);
        takerEntity.setLikes(takerEntity.getLikes() - donorEntity.getLevel() - 1);
        memberGivingHashMap.put(donor.getId(), memberGivingHashMap.getOrDefault(donor.getId(), 0) - 1);
        takerEntity.save();
    }

    public boolean giveDislike(KarmaEntity entity, Member donor, Member taker) {
        if (!givingAction(entity, donor))
            return false;
        var donorEntity = KarmaMemberEntity.get(donor);
        var takerEntity = KarmaMemberEntity.get(taker);
        takerEntity.setDislikes(takerEntity.getDislikes() + donorEntity.getLevel() + 1);
        takerEntity.save();
        return true;
    }

    public void removeDislike(Member donor, Member taker) {
        var donorEntity = KarmaMemberEntity.get(donor);
        var takerEntity = KarmaMemberEntity.get(taker);
        takerEntity.setDislikes(takerEntity.getDislikes() - donorEntity.getLevel() - 1);
        memberGivingHashMap.put(donor.getId(), memberGivingHashMap.getOrDefault(donor.getId(), 0) - 1);
        takerEntity.save();
    }


    public void maybeReset() {
        var now = LocalDate.now();
        if (!now.isAfter(lastReset))
            return;
        memberGivingHashMap.clear();
        lastReset = now;
    }

    public HashMap<String, Integer> getMemberGivingHashMap() {
        return memberGivingHashMap;
    }

    public LocalDate getLastReset() {
        return lastReset;
    }

    public Set<String> getDisabledChannels() {
        return disabledChannels;
    }


    @SubscribeEvent
    public void onStatusChange(StatusChangeEvent event) {

    }
}
