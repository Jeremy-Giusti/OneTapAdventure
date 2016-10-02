package fr.giusti.onetapadventure.gameObject.entities.entityDistribution;

import java.util.ArrayList;

import fr.giusti.onetapadventure.callback.SpawnerListener;
import fr.giusti.onetapadventure.gameObject.entities.Entity;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;

/**
 * Created by jgiusti on 26/09/2016.
 * either hold a list of entity
 * will distribute those entities depending on it condition progress <br>
 * or use the entity list of its listener
 */

public class EntitySpawner {
    public final eConditions conditionType;
    private int conditionValue = 0;
    private int conditionProgress = -1;
    private int initialProgressValue = -1;

    private boolean useSharedMobList = false;
    private boolean infinitePop = false;
    private eEntityDistributionMode distibutionMode = eEntityDistributionMode.ONE_BY_ONE_ORDERED;

    private ArrayList<Entity> entityList;
    private int mobIndex = 0;

    private SpawnerListener listener;

    /**
     * @param conditionType      more like an id work as the condition type in wich this spawner should be notified on a change
     * @param conditionGoalValue the condition goal if the progress match this value, a entity spawn will be triggered
     * @param conditionProgress  the initial progress
     * @param infinitePop        if true then this spawner will never run out of mob
     * @param distibutionMode    the way entity will be choosed (by goup/by order/randomly)
     * @param entityList         the entiies to spawn if null request listener to spawn entity
     */
    public EntitySpawner(eConditions conditionType, int conditionGoalValue, int conditionProgress, boolean infinitePop, eEntityDistributionMode distibutionMode, ArrayList<Entity> entityList) {
        this.conditionType = conditionType;
        this.conditionValue = conditionGoalValue;
        this.initialProgressValue = conditionProgress;
        this.conditionProgress = conditionProgress;
        this.infinitePop = infinitePop;
        this.distibutionMode = distibutionMode;
        if (entityList == null || entityList.isEmpty()) {
            useSharedMobList = true;
        } else {
            this.entityList = entityList;
        }
    }

    public void setListener(SpawnerListener listener) {
        this.listener = listener;
    }

    public ArrayList<Entity> onConditionProgress(int cdtProgress) {
        if (!useSharedMobList && entityList.isEmpty()) {
            listener.onSpawnerEmpty(this);
            return null;
        }

        if (conditionType == eConditions.MOB_COUNT)
            return (cdtProgress == conditionValue) ? onConditionMet() : null;

        if (conditionType == eConditions.TIMER)
            return ((cdtProgress % conditionValue) == 0) ? onConditionMet() : null;

        conditionProgress += cdtProgress;
        if (conditionProgress == conditionValue) {
            conditionProgress = initialProgressValue;
            return onConditionMet();
        }
        return null;
    }

    private ArrayList<Entity> onConditionMet() {
        if (useSharedMobList) {
            listener.onSpawnRequested(infinitePop, distibutionMode);
            return null;
        } else {
            ArrayList<Entity> result = new ArrayList<>();
            switch (distibutionMode) {
                case ALL_AT_ONCE:
                    //put all mob
                    result.addAll(entityList);
                    if (!infinitePop) entityList.clear();
                    break;
                case ONE_BY_ONE_ORDERED:
                    //add a mob from the list by order
                    result.add(entityList.get(mobIndex));
                    if (!infinitePop) entityList.remove(mobIndex);
                    else if (mobIndex < entityList.size() + 1)
                        mobIndex++;
                    else
                        mobIndex = 0;
                    break;
                case ONE_BY_ONE_RANDOM:
                    //add a random mob from list
                    mobIndex = (int) (Math.random() * entityList.size()) - 1;
                    result.add(entityList.get(mobIndex));
                    if (!infinitePop) entityList.remove(mobIndex);
                    break;
            }
            return result;
        }
    }

    public void resize(float ratio) {
        if (!useSharedMobList) {
            for (Entity entity : entityList) {
                entity.resize(ratio);
            }
        }
    }
}
