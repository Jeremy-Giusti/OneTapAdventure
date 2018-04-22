package fr.giusti.onetapengine.entity.distribution;

import java.util.ArrayList;

import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by giusti on 25/02/2018.
 */

public class EntitySpawnerFactory {

    public EntitySpawner<Integer> getIntSharedEntitySpawner(eEntityDistributionMode distributionMode, int goal, int initValue, eConditions... conditionType) {
        EntitySpawner<Integer> result = new EntitySpawner<Integer>(conditionType, distributionMode, goal, initValue) {

            @Override
            public ArrayList<Entity> onConditionProgress(Integer cdtProgress, eConditions conditionType) {
                if (!useSharedMobList && entityList.isEmpty()) {
                    listener.onSpawnerEmpty(this);
                    return null;
                }

                if (conditionType == eConditions.MOB_COUNT)
                    return (cdtProgress == conditionGoalValue) ? getEntityListOnConditionMet() : null;

                if (conditionType == eConditions.TIMER)
                    return ((cdtProgress % conditionGoalValue) == 0) ? getEntityListOnConditionMet() : null;

                conditionProgress += cdtProgress;
                if (conditionProgress == conditionGoalValue) {
                    conditionProgress = initialProgressValue;
                    return getEntityListOnConditionMet();
                }
                return null;            }
        };
        result.setUseSharedMobList(true);
        return result;
    }


}
