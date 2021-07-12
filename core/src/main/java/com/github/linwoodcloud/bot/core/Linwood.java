package com.github.linwoodcloud.bot.core;

import com.github.linwoodcloud.bot.core.apps.single.SingleApplicationManager;
import com.github.linwoodcloud.bot.core.config.MainConfig;
import com.github.linwoodcloud.bot.core.listener.CommandListener;
import com.github.linwoodcloud.bot.core.listener.ConnectionListener;
import com.github.linwoodcloud.bot.core.module.LinwoodModule;
import com.github.linwoodcloud.bot.core.utils.DatabaseUtil;
import com.github.linwoodcloud.bot.core.utils.LinwoodActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author CodeDoctorDE
 */
public class Linwood {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger LOGGER = LogManager.getLogger(Linwood.class);
    private static Linwood instance;
    private final CommandListener commandListener;
    private final LinwoodActivity activity;
    private final File configFile = new File("./config.json");
    private final DatabaseUtil database;
    private final String token;
    private final SingleApplicationManager gameManager;
    private final SingleApplicationManager audioManager;
    private final List<LinwoodModule> modules = new ArrayList<>();
    private ShardManager jda;
    private MainConfig config;


    public Linwood(String token) {
        instance = this;
        this.token = token;
        //Sentry.init();
        commandListener = new CommandListener();
        activity = new LinwoodActivity();
        gameManager = new SingleApplicationManager();
        audioManager = new SingleApplicationManager();
        database = new DatabaseUtil();

        // Read config file
        if (!configFile.exists()) {
            try {
                if (!configFile.createNewFile())
                    LOGGER.warn("Can't create a config file!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                config = GSON.fromJson(new FileReader(configFile), MainConfig.class);
            } catch (IOException e) {
                config = new MainConfig();
                e.printStackTrace();
            }
        }
        if (config == null)
            config = new MainConfig();
        saveConfig();
    }

    public static Linwood getInstance() {
        return instance;
    }

    public void run() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            getGameManager().clearGames();
            modules.forEach(LinwoodModule::onStop);
            LOGGER.info("Shutting down...");
        }));
        var builder = DefaultShardManagerBuilder.createDefault(token)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .setEventManagerProvider((id) -> new AnnotatedEventManager())
                .addEventListeners(commandListener)
                .addEventListeners(new ConnectionListener());
        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
        System.out.println(" _     _  _      _      ____  ____  ____ \n" +
                "/ \\   / \\/ \\  /|/ \\  /|/  _ \\/  _ \\/  _ \\\n" +
                "| |   | || |\\ ||| |  ||| / \\|| / \\|| | \\|\n" +
                "| |_/\\| || | \\||| |/\\||| \\_/|| \\_/|| |_/|\n" +
                "\\____/\\_/\\_/  \\|\\_/  \\|\\____/\\____/\\____/\n" +
                "                                         \n" +
                "Version " + getVersion() + "\n");
        database.connect();
        modules.forEach(LinwoodModule::onStart);
        LOGGER.info("Successfully started the bot!");
    }

    public void saveConfig() {
        try {
            var writer = new FileWriter(configFile);
            GSON.toJson(config, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainConfig getConfig() {
        return config;
    }

    public ShardManager getJda() {
        return jda;
    }

    public LinwoodActivity getActivity() {
        return activity;
    }

    public void registerModules(LinwoodModule... registeredModules) {
        var array = Arrays.asList(registeredModules);
        array.forEach(LinwoodModule::onRegister);
        modules.addAll(array);
    }

    public void unregisterModules(LinwoodModule... registeredModules) {
        var array = Arrays.asList(registeredModules);
        array.forEach(LinwoodModule::onUnregister);
        modules.removeAll(array);
    }

    public LinwoodModule[] getModules() {
        return modules.toArray(LinwoodModule[]::new);
    }

    public String[] getModulesStrings() {
        return modules.stream().map(LinwoodModule::getName).toArray(String[]::new);
    }

    public String getModulesString() {
        return String.join(" ", getModulesStrings());
    }

    @Nullable
    public <T extends LinwoodModule> T getModule(Class<T> moduleClass) {
        return modules.stream().filter(moduleClass::isInstance).findFirst().map(moduleClass::cast).orElse(null);
    }

    public LinwoodModule getModule(String module) {
        return getModule(module, LinwoodModule.class);
    }

    @Nullable
    public <T extends LinwoodModule> T getModule(String module, Class<T> moduleClass) {
        return modules.stream().filter(current -> current.getName().equalsIgnoreCase(module)).findFirst().map(moduleClass::cast).orElse(null);
    }

    public String getVersion() {
        var implementation = getClass().getPackage().getImplementationVersion();
        return implementation == null ? "Snapshot" : implementation;
    }

    public DatabaseUtil getDatabase() {
        return database;
    }

    public SingleApplicationManager getGameManager() {
        return gameManager;
    }

    public SingleApplicationManager getAudioManager() {
        return audioManager;
    }

    public CommandListener getCommandListener() {
        return commandListener;
    }
}
