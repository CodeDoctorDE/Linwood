package com.github.linwoodcloud.bot.main.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import net.dv8tion.jda.api.EmbedBuilder;

/**
 * @author CodeDoctorDE
 */
public class PlanCommand extends Command {
    public PlanCommand() {
        super(
                "plan",
                "plans",
                "limit",
                "limits"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        if (event.getArguments().length != 0)
            throw new CommandSyntaxException(this);
        var entity = event.getEntity();
        event.getMessage().getChannel().sendMessageEmbeds(new EmbedBuilder()
                .addField(translate(entity, "Plan"), translate(entity, "Plan" + entity.getPlan().name()), false)
                .addField(translate(entity, "PrefixLimitTitle"), String.format(translate(entity, "PrefixLimitBody"), entity.getPlan().getPrefixLimit()), false)
                .build()).queue();
    }
}
