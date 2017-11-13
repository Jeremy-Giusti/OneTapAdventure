package fr.giusti.onetapengine;

import java.util.ArrayList;

import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jérémy on 13/11/2017.
 */

public class EventsHolder {
    public long elapsedTime = 0;
    public int score = 0;
    public int mobCount = 0;

    public ArrayList<MobEvent> mobEvents = new ArrayList<>();
    public ArrayList<MobEvent> oldMobEvents = new ArrayList<>();

    public static class MobEvent {
        public GameMob mob;
        public eConditions reason;

        public MobEvent(GameMob mob, eConditions reason) {
            this.mob = mob;
            this.reason = reason;
        }
    }
}
