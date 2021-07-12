package com.github.linwoodcloud.bot.karma;

import com.github.linwoodcloud.bot.core.module.LinwoodModule;
import com.github.linwoodcloud.bot.karma.entity.KarmaMemberEntity;

/**
 * @author CodeDoctorDE
 */
public class KarmaAddon extends LinwoodModule {
    private static KarmaAddon instance;

    public KarmaAddon() {
        super("karma");
        instance = this;
    }

    public static KarmaAddon getInstance() {
        return instance;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUnregister() {
        super.onUnregister();
    }

    public KarmaMemberEntity[] getKarmaLeaderboard() {
        return getKarmaLeaderboard(null);
    }

    public KarmaMemberEntity[] getKarmaLeaderboard(String guildId) {
        return getKarmaLeaderboard(guildId, 20);
    }

    public KarmaMemberEntity[] getKarmaLeaderboard(int maxResults) {
        return getKarmaLeaderboard(null, maxResults);
    }

    public KarmaMemberEntity[] getKarmaLeaderboard(String guildId, int maxResults) {
/*// Create CriteriaBuilder
        var builder = session.getCriteriaBuilder();

// Create CriteriaQuery
        var cq = builder.createQuery(KarmaMemberEntity.class);
        var member = cq.from(KarmaMemberEntity.class);
        var all = cq.select(member);
        if(guildId != null)
            all.where(builder.equal(member.get("guildId"), guildId));
        all.orderBy(builder.desc(member.get("likes")), builder.asc(member.get("dislikes")));
        var allQuery = session.createQuery(all);
        allQuery.setMaxResults(maxResults);
        return allQuery.getResultList().toArray(new KarmaMemberEntity[0]);*/
        // TODO: Implement new karma leaderboard
        return new KarmaMemberEntity[0];
    }
}
