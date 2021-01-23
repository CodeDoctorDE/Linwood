package com.github.codedoctorde.linwood.main.commands;

import com.github.codedoctorde.linwood.core.commands.Command;
import com.github.codedoctorde.linwood.core.commands.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;

public class ServerInfoCommand extends Command {
    @Override
    public boolean onCommand(final CommandEvent event) {
        var args = event.getArguments();
        var message = event.getMessage();
        if(args.length == 0)
            return false;
        var bundle = getBundle(event.getEntity());
        var guild = event.getMessage().getGuild();
        guild.findMembers(member -> !member.getUser().isBot() && member.getOnlineStatus() == OnlineStatus.ONLINE).onSuccess(onlineMembers -> guild.findMembers(member -> member.getUser().isBot()).onSuccess(bots ->
                guild.findMembers(member -> !member.getUser().isBot() && member.getOnlineStatus() != OnlineStatus.ONLINE).onSuccess(offlineMembers -> guild.retrieveBanList().queue(bans ->
                        event.reply(" ").embed(new EmbedBuilder()
                                .addField(bundle.getString("TextChannels"), String.valueOf(guild.getTextChannels().size()), true)
                                .addField(bundle.getString("VoiceChannels"), String.valueOf(guild.getVoiceChannels().size()), true)

                                .addField(bundle.getString("Roles"), String.valueOf(guild.getRoles().size()), false)
                                .addField(bundle.getString("Emotes"), String.valueOf(guild.getEmotes().size()), true)

                                .addField(bundle.getString("OnlineMembers"), String.valueOf(onlineMembers.size()), false)
                                .addField(bundle.getString("OfflineMembers"), String.valueOf(offlineMembers.size()), true)
                                .addField(bundle.getString("Bots"), String.valueOf(bots.size()), true)

                                .addField(bundle.getString("Bans"), String.valueOf(bots.size()), false)
                                .addField(bundle.getString("Boosts"), String.valueOf(guild.getBoostCount()), true)
                                .build()).queue()))));
        return true;
    }
    public ServerInfoCommand(){
        super("s-i", "s-info", "server-i", "server-info", "serveri", "serverinfo", "si", "sinfo");
    }
}