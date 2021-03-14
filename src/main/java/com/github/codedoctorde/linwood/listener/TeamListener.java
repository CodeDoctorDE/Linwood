package com.github.codedoctorde.linwood.listener;

import com.github.codedoctorde.linwood.Linwood;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.SubscribeEvent;

/**
 * @author CodeDoctorDE
 */
public class TeamListener {
    @SubscribeEvent
    public void onChat(MessageReceivedEvent event){
        if(!event.isFromGuild())
            return;
        var textChannel = event.getTextChannel();
        var session = Linwood.getInstance().getDatabase().getSessionFactory().openSession();
        var entity  = Linwood.getInstance().getDatabase().getGuildById(session, event.getGuild().getIdLong());
        var teams = entity.getTeams(session);
        
    }
}
