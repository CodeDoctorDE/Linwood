package com.github.linwoodcloud.bot.karma.commands;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.core.utils.TagUtil;
import com.github.linwoodcloud.bot.karma.entity.KarmaMemberEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

public class KarmaInfoCommand extends Command {
    public KarmaInfoCommand() {
        super(
                "karma",
                "likes",
                "level",
                "levels",
                "rank"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var args = event.getArguments();
        var entity = event.getEntity();
        if (args.length > 1)
            throw new CommandSyntaxException(this);
        if (args.length == 0)
            karmaCommand(event, Objects.requireNonNull(event.getMember()));
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
                    karmaCommand(event, member);
                });
            } catch (Exception e) {
                event.reply(translate(entity, "Invalid"));
            }
        }
    }

    public void karmaCommand(CommandEvent event, Member member) {
        var entity = event.getEntity();
        if (member.getUser().isBot()) {
            event.reply(translate(entity, "Invalid")).queue();
            return;
        }
        var karmaMemberEntity = KarmaMemberEntity.get(member);
        event.reply(" ").setEmbeds(new EmbedBuilder()
                .setTitle(translate(entity, "Title"))
                .setDescription(translate(entity, "Body"))
                .setColor(new Color(0x3B863B))
                .setTimestamp(LocalDateTime.now())
                .setAuthor(member.getUser().getAsTag(), "https://discord.com", member.getUser().getAvatarUrl())
                .addField(translate(entity, "KarmaPoints"), String.valueOf(karmaMemberEntity.getKarma()), true)
                .addField(translate(entity, "Likes"), String.valueOf(karmaMemberEntity.getLikes()), true)
                .addField(translate(entity, "Dislikes"), String.valueOf(karmaMemberEntity.getDislikes()), true)
                .addField(translate(entity, "Level"), String.valueOf(karmaMemberEntity.getLevel()), false)
                .addField(translate(entity, "Experience"), Math.round(karmaMemberEntity.getRemainingKarma() * 100 * 100) / 100 + "%", true)
                .addField(translate(entity, "Rank"), "invalid", true)
                .build()).queue();
    }
}
