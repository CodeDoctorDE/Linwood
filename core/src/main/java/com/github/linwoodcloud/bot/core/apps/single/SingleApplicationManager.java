package com.github.linwoodcloud.bot.core.apps.single;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CodeDoctorDE
 */
public class SingleApplicationManager {
    private final List<SingleApplication> apps = new ArrayList<>();

    public SingleApplicationManager() {
    }

    public SingleApplication startGame(String guildId, SingleApplicationMode appMode) {
        stopGame(guildId);
        var app = new SingleApplication(apps.size(), guildId, appMode);
        apps.add(app);
        app.start();
        return app;
    }

    public void stopGame(String guildId) {
        apps.stream().filter(app -> app.getGuildId().equals(guildId)).forEach(app -> stopGame(app, false));
        apps.clear();
    }

    public void stopGame(SingleApplication app) {
        stopGame(app, true);
    }

    private void stopGame(SingleApplication app, boolean remove) {
        if (!apps.contains(app))
            return;
        app.stop();
        if (remove)
            apps.remove(app);
    }

    @Nullable
    public SingleApplication getGame(String guildId) {
        SingleApplication current = null;
        for (var app :
                apps)
            if (app.getGuildId().equals(guildId))
                current = app;
        return current;
    }

    public void clearGames() {
        apps.forEach(app -> stopGame(app, false));
        apps.clear();
    }

    public SingleApplication[] getApps() {
        return apps.toArray(new SingleApplication[0]);
    }
}
