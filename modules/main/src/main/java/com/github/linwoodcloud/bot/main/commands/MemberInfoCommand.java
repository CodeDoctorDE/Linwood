package com.github.linwoodcloud.bot.main.commands;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.core.utils.TagUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;
import java.util.stream.Collectors;

public class MemberInfoCommand extends Command {
    public MemberInfoCommand() {
        super("memberinfo", "member-info", "member", "memberinformation", "member-information", "user", "userinfo", "user-info", "user-information", "userinformation");
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var args = event.getArguments();
        var entity = event.getEntity();
        if (args.length > 1)
            throw new CommandSyntaxException(this);
        if (args.length == 0)
            sendInfo(event, Objects.requireNonNull(event.getMember()));
        else {
            var action =
                    TagUtil.convertIdToMember(event.getMessage().getGuild(), args[0]);
            if (action == null) {
                event.reply(translate(entity, "SetMultiple")).queue();
                return;
            }
            try {
                action.queue(member -> {
                    if (member == null) {
                        member = TagUtil.convertNameToMember(event.getMessage().getGuild(), args[0]);
                        if (member == null) {
                            event.reply(translate(entity, "SetMultiple")).queue();
                            return;
                        }
                    }
                    sendInfo(event, member);
                });
            } catch (Exception e) {
                event.reply(translate(entity, "Invalid"));
            }
        }
    }

    public void sendInfo(CommandEvent event, Member member) {
        var entity = event.getEntity();
        var dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(event.getEntity().getLocalization());
        var user = member.getUser();
        event.reply(new EmbedBuilder()
                .setTitle(translate(entity, "Title"))
                .setDescription(translate(entity, "Body"))
                .setColor(new Color(0x3B863B))
                .setTimestamp(LocalDateTime.now())
                .setAuthor(member.getUser().getAsTag(), "https://discord.com", user.getAvatarUrl())
                .addField(translate(entity, "Joined"), member.getTimeJoined().format(dateFormatter), true)
                .addField(translate(entity, "Created"), user.getTimeCreated().format(dateFormatter), true)
                .addField(translate(entity, "Owner"), translate(entity, member.isOwner() ? "IsOwner" : "NoOwner"), true)
                .addField(translate(entity, "Roles"), member.getRoles().stream().map(IMentionable::getAsMention).collect(Collectors.joining(", ")), false)
                .addField(translate(entity, "Boost"), member.getTimeBoosted() != null ? member.getTimeBoosted().format(dateFormatter) : translate(entity, "NoBoost"), true)
                .addField(translate(entity, "Bot"), translate(entity, user.isBot() ? "IsBot" : "NoBot"), true)
                .build()).queue();
    }
}
