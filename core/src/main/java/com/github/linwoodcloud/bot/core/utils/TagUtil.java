package com.github.linwoodcloud.bot.core.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.regex.Pattern;

public final class TagUtil {
    public static TextChannel convertToTextChannel(Guild guild, String input) throws UnsupportedOperationException {
        TextChannel channel = null;
        var pattern = Pattern.compile("(<#)?(\\d+)(>)?");
        var matcher = pattern.matcher(input);
        if (matcher.find())
            try {
                channel = guild.getTextChannelById(matcher.group(2));
            } catch (Exception ignored) {

            }
        if (channel == null) {
            var channels = guild.getTextChannelsByName(input, true);
            if (channels.size() == 1)
                channel = channels.get(0);
            else
                throw new UnsupportedOperationException();
        }
        return channel;
    }

    public static Role convertToRole(Guild guild, String input) throws UnsupportedOperationException {
        Role role = null;
        var pattern = Pattern.compile("(<&)?(\\d+)(>)?");
        var matcher = pattern.matcher(input);
        if (matcher.find())
            try {
                role = guild.getRoleById(matcher.group(2));
            } catch (Exception ignored) {

            }
        if (role == null) {
            var roles = guild.getRolesByName(input, true);
            if (roles.size() == 1)
                role = roles.get(0);
            else
                throw new UnsupportedOperationException();
        }
        return role;
    }

    public static Member convertNameToMember(Guild guild, String input) {
        var members = guild.getMembersByName(input, true);
        if (members.size() == 1)
            return members.get(0);
        else
            return null;
    }

    public static RestAction<Member> convertIdToMember(Guild guild, String input) {
        var pattern = Pattern.compile("(<@(!)?)?(?<id>\\d+)(>)?");
        var matcher = pattern.matcher(input);
        if (matcher.find())
            try {
                return guild.retrieveMemberById(matcher.group("id"));
            } catch (Exception ignored) {

            }
        return null;
    }
}
