package com.github.linwoodcloud.bot.karma.commands;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.karma.KarmaAddon;
import com.github.linwoodcloud.bot.karma.entity.KarmaMemberEntity;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author CodeDoctorDE
 */
public class KarmaLeaderboardCommand extends Command {
    public KarmaLeaderboardCommand() {
        super(
                "leaderboard", "lb", "rank", "ranks", "top", "toplist", "top-list", "list"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        var entity = event.getEntity();
        var leaderboard = KarmaAddon.getInstance().getKarmaLeaderboard(event.getMessage().getGuild().getId());
        event.getMessage().getGuild().retrieveMembersByIds(Arrays.stream(leaderboard).map(KarmaMemberEntity::getMemberId).toArray(String[]::new)).onSuccess(members -> {
            var description = new StringBuilder();
            description.append(translate(entity, "LeaderboardBodyStart")).append("\n");
            for (int i = 0; i < leaderboard.length; i++) {
                int finalI = i;
                var member = members.stream().filter(current -> current.getId() == leaderboard[finalI].getMemberId()).findFirst().orElse(null);
                var me = leaderboard[i];
                if (member != null)
                    description.append(String.format(translate(entity, "LeaderboardBody"), i + 1, member.getUser().getAsMention(), me.getKarma(), me.getLikes(), me.getDislikes()));
            }
            description.append(translate(entity, "LeaderboardBodyEnd"));
            event.reply(new EmbedBuilder()
                    .setTitle(translate(entity, "LeaderboardHeader"))
                    .setFooter(translate(entity, "LeaderboardFooter"))
                    .setDescription(description)
                    .setColor(new Color(0x3B863B))
                    .setTimestamp(LocalDateTime.now())
                    .build()).queue();
        });
    }
}