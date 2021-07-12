package com.github.linwoodcloud.bot.main;

import com.github.linwoodcloud.bot.core.Linwood;
import com.github.linwoodcloud.bot.core.module.LinwoodModule;
import com.github.linwoodcloud.bot.main.commands.*;
import com.github.linwoodcloud.bot.main.commands.settings.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author CodeDoctorDE
 */
public class MainAddon extends LinwoodModule {
    private static final Logger LOGGER = LogManager.getLogger(MainAddon.class);
    private static MainAddon instance;

    public MainAddon() {
        super("main", "https://linwood.tk/docs/bot/modules/main/overview");
        instance = this;
    }

    public static MainAddon getInstance() {
        return instance;
    }

    @Override
    public void onRegister() {
        registerCommands(new ClearCommand(), new CommandHelpCommand(), new InfoCommand(), new ModuleCommand(), new ServerInfoCommand(), new MemberInfoCommand());
        registerSettingsCommands(new AddPrefixCommand(), new ClearMaintainerCommand(), new LanguageCommand(), new MaintainerCommand(), new PlanCommand(), new PrefixesCommand(), new RemovePrefixCommand());
        registerActivities(Linwood.getInstance().getConfig().getActivities().toArray(String[]::new));
        super.onRegister();
    }

    @Override
    public void onUnregister() {
        super.onUnregister();
    }
}
