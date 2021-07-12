package com.github.linwoodcloud.bot.core.module;

import com.github.linwoodcloud.bot.core.commands.Command;
import com.github.linwoodcloud.bot.core.commands.CommandEvent;
import com.github.linwoodcloud.bot.core.entity.DatabaseEntity;
import com.github.linwoodcloud.bot.core.entity.GeneralGuildEntity;
import com.github.linwoodcloud.bot.core.utils.GuildLogLevel;
import net.dv8tion.jda.api.entities.Guild;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

/**
 * @author CodeDoctorDE
 */
public abstract class LinwoodModule {
    private final Set<Object> listeners = new HashSet<>();
    private final Set<Command> commands = new HashSet<>();
    private final Set<Class<? extends DatabaseEntity>> entities = new HashSet<>();
    private final Set<Command> settingsCommands = new HashSet<>();
    private final Set<String> activities = new HashSet<>();
    private final String name;
    private final String supportURL;
    private final Logger logger;

    protected LinwoodModule(String name) {
        this(name, null);
    }

    protected LinwoodModule(String name, String supportURL) {
        this.name = name;
        this.supportURL = supportURL;
        logger = LogManager.getLogger(getClass());
    }

    public String getSupportURL() {
        return supportURL;
    }

    public Set<Object> getListeners() {
        return Set.copyOf(listeners);
    }

    public Set<Command> getCommands() {
        return Set.copyOf(commands);
    }

    public Set<Command> getSettingsCommands() {
        return Set.copyOf(settingsCommands);
    }

    public Set<Class<? extends DatabaseEntity>> getEntities() {
        return Set.copyOf(entities);
    }

    protected void registerEntity(Class<? extends DatabaseEntity> entity) {
        entities.add(entity);
    }

    protected void unregisterEntity(Class<? extends DatabaseEntity> entity) {
        entities.remove(entity);
    }

    protected void registerEntities(Class<? extends DatabaseEntity>... current) {
        Arrays.stream(current).forEach(this::registerEntity);
    }

    protected void unregisterEntities(Class<? extends DatabaseEntity>... current) {
        Arrays.stream(current).forEach(this::unregisterEntity);
    }

    protected void registerEvents(Object... eventListeners) {
        Arrays.stream(eventListeners).forEach(this::registerEvent);
    }

    protected void registerEvent(Object eventListener) {
        listeners.add(eventListener);
    }

    protected void unregisterEvents(Object... eventListeners) {
        Arrays.stream(eventListeners).forEach(this::unregisterEvent);
    }

    protected void unregisterEvent(Object eventListener) {
        listeners.remove(eventListener);
    }

    protected void registerCommands(Command... registeredCommands) {
        Arrays.stream(registeredCommands).forEach(this::registerCommand);
    }

    protected void registerCommand(Command registeredCommand) {
        commands.add(registeredCommand);
    }

    protected void unregisterCommands(Command... registeredCommands) {
        commands.removeAll(Arrays.asList(registeredCommands));
    }

    protected void unregisterCommand(Command registeredCommand) {
        commands.remove(registeredCommand);
    }

    protected void registerSettingsCommands(Command... registeredCommands) {
        Arrays.stream(registeredCommands).forEach(this::registerSettingsCommand);
    }

    protected void registerSettingsCommand(Command registeredCommand) {
        settingsCommands.add(registeredCommand);
    }

    protected void unregisterSettingsCommands(Command... registeredCommands) {
        Arrays.stream(registeredCommands).forEach(this::unregisterSettingsCommand);
    }

    protected void unregisterSettingsCommand(Command registeredCommand) {
        settingsCommands.remove(registeredCommand);
    }

    public Command getCommand(String alias) {
        return getCommand(alias, true);
    }

    @Nullable
    public Command getCommand(String alias, boolean includeSettings) {
        return (includeSettings ? Stream.concat(commands.stream(), settingsCommands.stream()) : commands.stream()).filter(command -> command.hasAlias(alias)).findFirst().orElse(null);
    }

    @Nullable
    public Command getSettingsCommand(String alias) {
        return settingsCommands.stream().filter(command -> command.hasAlias(alias)).findFirst().orElse(null);
    }

    public Set<String> getActivities() {
        return Set.copyOf(activities);
    }

    public void registerActivities(String... activities) {
        Arrays.stream(activities).forEach(this::registerActivity);
    }

    public void registerActivity(String activity) {
        activities.add(activity);
    }

    public void unregisterActivities(String... activities) {
        Arrays.stream(activities).forEach(this::unregisterActivity);
    }

    public void unregisterActivity(String activity) {
        activities.remove(activity);
    }

    protected void clearCommands() {
        commands.clear();
    }

    public void onRegister() {
        logger.info(name + " module was registered!");
    }

    public void onUnregister() {
        clearCommands();
        logger.info(name + " module was unregistered!");
    }

    public void onStart() {
        logger.info(name + " module was started!");
    }

    public void onStop() {
        logger.info(name + " module was stopped!");
    }

    public void log(GeneralGuildEntity entity, GuildLogLevel level, String message) {
        entity.log(level, this, message);
    }

    public void onEnable(GeneralGuildEntity entity, Guild guild) {
        log(entity, GuildLogLevel.INFO, entity.translate("Module", "Enabled"));
    }

    public void onDisable(GeneralGuildEntity entity, Guild guild) {
        log(entity, GuildLogLevel.INFO, entity.translate("Module", "Disabled"));
    }


    public String getName() {
        return name;
    }

    public String translate(CommandEvent event, String key) {
        return translate(event.getEntity(), key);
    }

    public String translate(GeneralGuildEntity entity, String key) {
        return entity.translate(getClass().getCanonicalName(), key);
    }

    public boolean isEnabled(GeneralGuildEntity entity) {
        return entity.getEnabledModules().contains(name);
    }
}
