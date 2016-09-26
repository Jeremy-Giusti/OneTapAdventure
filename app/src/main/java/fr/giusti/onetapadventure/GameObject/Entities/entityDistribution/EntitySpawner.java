package fr.giusti.onetapadventure.gameObject.entities.entityDistribution;

import java.util.ArrayList;

import fr.giusti.onetapadventure.callback.OnPopperEmptyListener;
import fr.giusti.onetapadventure.gameObject.entities.Entity;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;

/**
 * Created by jgiusti on 26/09/2016.
 * hold a list of entity
 * will distribute those entities depending on it condition progress
 */

public class EntitySpawner {
    public final eConditions conditionType;
    private int conditionValue = 0;
    private int conditionProgress = -1;
    private int initialProgressValue = -1;

    private boolean infinitePop = false;
    private eEntityDistributionMode distibutionMode = eEntityDistributionMode.ONE_BY_ONE_ORDERED;

    private ArrayList<Entity> entityList;
    private int mobIndex = 0;

    private OnPopperEmptyListener listener;

    /**
     * @param conditionType      more like an id work as the condition type in wich this spawner should be notified on a change
     * @param conditionGoalValue the condition goal if the progress match this value, a entity spawn will be triggered
     * @param conditionProgress  the initial progress
     * @param infinitePop        if true then this spawner will never run out of mob
     * @param distibutionMode    the way entity will be choosed (by goup/by order/randomly)
     * @param entityList         the entiies to spawn
     * @param listener           listerner for when the spawner is empty (is never called for infinite spawner)
     */
    public EntitySpawner(eConditions conditionType, int conditionGoalValue, int conditionProgress, boolean infinitePop, eEntityDistributionMode distibutionMode, ArrayList<Entity> entityList, OnPopperEmptyListener listener) {
        this.conditionType = conditionType;
        this.conditionValue = conditionGoalValue;
        this.initialProgressValue = conditionProgress;
        this.conditionProgress = conditionProgress;
        this.infinitePop = infinitePop;
        this.distibutionMode = distibutionMode;
        this.entityList = entityList;
        this.listener = listener;
    }

    public ArrayList<Entity> onConditionProgress(int cdtProgress) {
        if (entityList.isEmpty()) {
            listener.onPopperEmpty(this);
            return null;
        }

        if (conditionType == eConditions.MOB_COUNT)
            return (cdtProgress == conditionValue) ? onConditionMet() : null;

        conditionProgress += cdtProgress;
        if (conditionProgress == conditionValue) {
            conditionProgress = initialProgressValue;
            return onConditionMet();
        }
        return null;
    }

    private ArrayList<Entity> onConditionMet() {
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
