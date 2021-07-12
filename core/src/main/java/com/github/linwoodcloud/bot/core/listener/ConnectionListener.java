package com.github.linwoodcloud.bot.core.listener;

import com.github.linwoodcloud.bot.core.Linwood;
import io.sentry.Sentry;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

/**
 * @author CodeDoctorDE
 */
public class ConnectionListener {
    public ConnectionListener() {

    }

    @SubscribeEvent
    public void onGuildJoin(GuildJoinEvent event) {
        try {
            /*var session = Linwood.getInstance().getDatabase().getSessionFactory().openSession();
            var guild = GeneralGuildEntity.get(event.getGuildId());
            guild.save(session);
            session.close();*/
        } catch (Exception e) {
            e.printStackTrace();
            Sentry.captureException(e);
        }
    }

    @SubscribeEvent
    public void onBotStart(ReadyEvent event) {
        Linwood.getInstance().getActivity().updateStatus();
    }

    @SubscribeEvent
    public void onBotShutdown(ShutdownEvent event) {
        Linwood.getInstance().getGameManager().clearGames();
    }
}
