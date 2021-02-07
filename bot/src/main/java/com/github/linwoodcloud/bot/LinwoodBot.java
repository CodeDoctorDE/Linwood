package com.github.linwoodcloud.bot;


import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.game.GameAddon;
import com.github.linwoodcloud.bot.karma.KarmaAddon;
import com.github.linwoodcloud.bot.main.MainAddon;

/**
 * @author CodeDoctorDE
 */
public class LinwoodBot {
    public static void main(String[] args) {
        var bot = new Linwood(System.getenv("DISCORD_TOKEN"));
        bot.registerModules(new MainAddon(), new GameAddon(), new KarmaAddon());
        bot.run();
    }
}
