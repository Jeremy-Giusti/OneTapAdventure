package fr.giusti.onetapadventure.repository.levelsData.infinitelvl.spawners;

import java.util.ArrayList;

import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.distribution.EntitySpawner;
import fr.giusti.onetapengine.entity.distribution.eEntityDistributionMode;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jérémy on 14/04/2018.
 */

public class Pool1Spawner extends EntitySpawner<Long> {
    private static final long LONG_INTEVALE_OF_SPAWN = 1500;
    private static final long SHORT_INTEVALE_OF_SPAWN = 750;
    private static final int MOB_COUNT_FOR_INTERVAL_SELECTION = 10;

    private long lastTimeOfSpawn = 0;


    public Pool1Spawner(ArrayList<Entity> entityList) {
        super(new eConditions[]{eConditions.TIMER, eConditions.MOB_COUNT},
                eEntityDistributionMode.ONE_BY_ONE_RANDOM,
                10l,
                0l);
        setEntityList(entityList);
        setInfinitePop(true);
    }

    /**
     * spawn every 1.5sec or 0.75 if count<10)
     *
     * @param cdtProgress   condition evolution
     * @param conditionType mobcount or timer
     * @return 1 simple mob
     */
    @Override
    public ArrayList<Entity> onConditionProgress(Long cdtProgress, eConditions conditionType) {
        if (conditionType == eConditions.TIMER) {
            long selectedInterval = (conditionProgress < MOB_COUNT_FOR_INTERVAL_SELECTION) ? SHORT_INTEVALE_OF_SPAWN : LONG_INTEVALE_OF_SPAWN;
            if ((cdtProgress - lastTimeOfSpawn) > selectedInterval) {
                lastTimeOfSpawn = cdtProgress;
                return getEntityListOnConditionMet();
            }
        } else {
            conditionProgress = cdtProgress;
        }

        return null;
    }
}
