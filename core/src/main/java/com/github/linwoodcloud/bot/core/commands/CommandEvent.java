package com.github.linwoodcloud.bot.core.commands;

import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * @author CodeDoctorDE
 */
public class CommandEvent {
    private final Message message;
    private final GeneralGuildEntity entity;
    private final String label;
    private final String[] arguments;

    public CommandEvent(Message message, GeneralGuildEntity entity, String label, String[] arguments) {
        this.message = message;
        this.entity = entity;
        this.label = label;
        this.arguments = arguments;
    }

    public GeneralGuildEntity getEntity() {
        return entity;
    }

    public Message getMessage() {
        return message;
    }

    public String getLabel() {
        return label;
    }

    public String[] getArguments() {
        return arguments;
    }

    public String getArgument(int index, String defaultString) {
        var argument = getArgument(index);
        return (argument == null) ? defaultString : argument;
    }

    public String getArgument(int index) {
        if (arguments.length > index)
            return arguments[index];
        else
            return null;
    }

    public boolean isMaintainer() {
        var member = getMember();
        return member.hasPermission(Permission.MANAGE_SERVER) || entity.getMaintainerId() != null && member.getRoles().contains(member.getGuild().getRoleById(entity.getMaintainerId()));
    }

    public String getArgumentsString() {
        return String.join(" ", arguments);
    }

    public TextChannel getTextChannel() {
        return message.getTextChannel();
    }

    public MessageAction reply(Message message) {
        return getTextChannel().sendMessage(message);
    }

    public MessageAction reply(MessageEmbed embed) {
        return getTextChannel().sendMessageEmbeds(embed);
    }

    public MessageAction reply(String text) {
        return getTextChannel().sendMessage(text);
    }

    public MessageAction replyFormat(String format, Object... args) {
        return getTextChannel().sendMessageFormat(format, args);
    }

    public Member getMember() {
        return getMessage().getMember();
    }

    public Guild getGuild() {
        return getMessage().getGuild();
    }

    public String getGuildId() {
        return getGuild().getId();
    }

    public CommandEvent upper() {
        return new CommandEvent(message, entity, (arguments.length > 0) ? arguments[0] : "",
                (arguments.length > 0) ? Arrays.copyOfRange(arguments, 1, arguments.length) : new String[0]);
    }

    public void sendSyntaxError() {
        var bundle = getBundle();
        reply(bundle.getString("sSyntax")).queue();
    }

    public ResourceBundle getBundle() {
        return ResourceBundle.getBundle("locale.Command", entity.getLocalization());
    }
}
