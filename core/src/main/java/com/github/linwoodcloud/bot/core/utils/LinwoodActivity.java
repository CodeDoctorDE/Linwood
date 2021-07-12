package com.github.linwoodcloud.bot.core.utils;

import com.github.linwoodcloud.bot.core.Linwood;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CodeDoctorDE
 */
public class LinwoodActivity {
    private OnlineStatus status = OnlineStatus.ONLINE;
    private Activity.ActivityType type = Activity.ActivityType.WATCHING;

    public LinwoodActivity() {
    }

    public void updateStatus() {
        Linwood.getInstance().getJda().setPresence(status, Activity.of(type, String.format(String.join(" | ", getActivities()), Linwood.getInstance().getVersion(), Linwood.getInstance().getJda().getGuilds().size(),
                getMemberCount())));
    }

    public int getMemberCount() {
        return Linwood.getInstance().getJda().getGuilds().stream().mapToInt(Guild::getMemberCount).sum();
    }

    public List<String> getActivities() {
        return Arrays.stream(Linwood.getInstance().getModules()).flatMap(module -> module.getActivities().stream()).collect(Collectors.toList());
    }

    public Activity.ActivityType getType() {
        return type;
    }

    public void setType(Activity.ActivityType type) {
        this.type = type;
    }

    public OnlineStatus getStatus() {
        return status;
    }

    public void setStatus(OnlineStatus status) {
        this.status = status;
    }
}
