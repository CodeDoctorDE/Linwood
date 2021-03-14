package com.github.codedoctorde.linwood;

import com.github.codedoctorde.linwood.commands.BaseCommand;
import com.github.codedoctorde.linwood.config.MainConfig;
import com.github.codedoctorde.linwood.apps.single.SingleApplicationManager;
import com.github.codedoctorde.linwood.listener.ConnectionListener;
import com.github.codedoctorde.linwood.listener.CommandListener;
import com.github.codedoctorde.linwood.listener.KarmaListener;
import com.github.codedoctorde.linwood.listener.NotificationListener;
import com.github.codedoctorde.linwood.server.WebInterface;
import com.github.codedoctorde.linwood.utils.ActivityChanger;
import com.github.codedoctorde.linwood.utils.DatabaseUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.sentry.Sentry;
import io.sentry.SentryClientFactory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.AnnotatedEventManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.security.auth.login.LoginException;
import java.io.*;

/**
 * @author CodeDoctorDE
 */
public class Linwood {
    private final Object sentry;
    private final WebInterface webInterface;
    private JDA jda;
    private final ActivityChanger activityChanger;
    private final BaseCommand baseCommand;
    private static Linwood instance;
    private final DatabaseUtil database;
    private final SingleApplicationManager gameManager;
    private final SingleApplicationManager audioManager;
    private MainConfig config;
    private final KarmaListener userListener = new KarmaListener();
    private final File configFile = new File("./config.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Logger logger = LogManager.getLogger(Linwood.class);


    public static void main(String[] args) {
        new Linwood();
    }
    public Linwood(){
        instance = this;
        Sentry.init();
        sentry = SentryClientFactory.sentryClient();

        // Read config file
        if(!configFile.exists()){
            try {
                if(!configFile.createNewFile())
                    logger.warn("Can't create a config file!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            try{
                config = gson.fromJson(new FileReader(configFile), MainConfig.class);
            } catch (IOException e) {
                config = new MainConfig();
                e.printStackTrace();
            }
        }
        if(config == null)
            config = new MainConfig();
        saveConfig();
        var builder = JDABuilder.createDefault(config.getToken())
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .setEventManager(new AnnotatedEventManager())
                .addEventListeners(new CommandListener())
                .addEventListeners(userListener)
                .addEventListeners(new NotificationListener())
                .addEventListeners(new ConnectionListener());
        activityChanger = new ActivityChanger();
        baseCommand = new BaseCommand();
        gameManager = new SingleApplicationManager();
        audioManager = new SingleApplicationManager();
        database = new DatabaseUtil();
        try {
            jda = builder.build();
        } catch (LoginException e) {
            e.printStackTrace();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            getGameManager().clearGames();
            logger.info("Shutting down...");
        }));
        configure();
        activityChanger.start();
            webInterface = new WebInterface();
            new Thread(webInterface::start).start();
        System.out.println(" _     _  _      _      ____  ____  ____ \n" +
                "/ \\   / \\/ \\  /|/ \\  /|/  _ \\/  _ \\/  _ \\\n" +
                "| |   | || |\\ ||| |  ||| / \\|| / \\|| | \\|\n" +
                "| |_/\\| || | \\||| |/\\||| \\_/|| \\_/|| |_/|\n" +
                "\\____/\\_/\\_/  \\|\\_/  \\|\\____/\\____/\\____/\n" +
                "                                         \n");
        logger.info("Successfully started the bot!");
    }

    public JDA getJda() {
        return jda;
    }

    public ActivityChanger getActivityChanger() {
        return activityChanger;
    }

    public BaseCommand getBaseCommand() {
        return baseCommand;
    }

    public static Linwood getInstance() {
        return instance;
    }

    public String getVersion(){
        return getClass().getPackage().getImplementationVersion();
    }

    public DatabaseUtil getDatabase() {
        return database;
    }

    public MainConfig getConfig() {
        return config;
    }

    public SingleApplicationManager getGameManager() {
        return gameManager;
    }

    public SingleApplicationManager getAudioManager() {
        return audioManager;
    }

    public WebInterface getWebInterface() {
        return webInterface;
    }

    public void saveConfig(){
        try {
            var writer = new FileWriter(configFile);
            gson.toJson(config, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void configure(){
        activityChanger.getActivities().clear();
        config.getActivities().forEach(activityChanger.getActivities()::add);
    }

    public KarmaListener getUserListener() {
        return userListener;
    }
}
