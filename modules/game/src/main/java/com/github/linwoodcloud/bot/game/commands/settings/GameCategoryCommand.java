package com.github.linwoodcloud.bot.game.commands.settings;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.exceptions.CommandSyntaxException;
import com.github.linwoodcloud.bot.game.entity.GameEntity;
import net.dv8tion.jda.api.entities.Category;

/**
 * @author CodeDoctorDE
 */
public class GameCategoryCommand extends Command {
    public GameCategoryCommand() {
        super(
                "gamecategory",
                "game-category",
                "category"
        );
    }

    @Override
    public void onCommand(final CommandEvent event) {
        var entity = event.getEntity();
        var args = event.getArguments();
        var gameEntity = GameEntity.get(event.getGuildId());
        if (args.length > 1)
            throw new CommandSyntaxException(this);
        if (args.length == 0)
            if (gameEntity.getGameCategoryId() != null)
                event.replyFormat(translate(entity, "Get"), gameEntity.getGameCategory().getName(), gameEntity.getGameCategoryId()).queue();
            else
                event.reply(translate(entity, "GetNull")).queue();
        else {
            try {
                Category category = null;
                try {
                    category = event.getMessage().getGuild().getCategoryById(args[0]);
                } catch (Exception ignored) {

                }
                if (category == null) {
                    var categories = event.getMessage().getGuild().getCategoriesByName(args[0], true);
                    if (categories.size() < 1)
                        event.reply(translate(entity, "SetNothing")).queue();
                    else if (categories.size() > 1)
                        event.reply(translate(entity, "SetMultiple")).queue();
                    else
                        category = categories.get(0);
                    if (category == null)
                        return;
                }
                gameEntity.setGameCategory(category);
                entity.save();
                event.replyFormat(translate(entity, "Set"), gameEntity.getGameCategory().getName(), gameEntity.getGameCategoryId()).queue();
            } catch (NullPointerException e) {
                event.reply(translate(entity, "NotValid")).queue();
            }
        }
    }
}
