package fr.giusti.onetapengine;

import java.util.ArrayList;

import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jérémy on 13/11/2017.<br>
 * store all boards events ticks by ticks<br>
 * Events include:<br>
 * - elapsed time<br>
 * - score<br>
 * - mobCount<br>
 * - mob Event<br>
 * -- Mom Death<br>
 * -- Mob AWAY<br>
 * -- New Mob<br>
 */

public class EventsHolder {
    public long elapsedTime = 0;
    public int score = 0;
    public int mobCount = 0;

    public ArrayList<MobEvent> mobEvents = new ArrayList<>();
    public ArrayList<MobEvent> oldMobEvents = new ArrayList<>();

    /**
     * used to hold mob event data<br>
     *     include:<br>
     * -- Mom Death<br>
     * -- Mob AWAY<br>
     * -- New Mob<br>     */
    public static class MobEvent {
        public GameMob mob;
        public eConditions reason;

        public MobEvent(GameMob mob, eConditions reason) {
            this.mob = mob;
            this.reason = reason;
        }
    }
}
