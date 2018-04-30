package fr.giusti.onetapadventure.repository.levelsData.infinitelvl.spawners;

import java.util.ArrayList;

import fr.giusti.onetapadventure.repository.levelsData.infinitelvl.InfiniteLvlConstant;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.distribution.EntitySpawner;
import fr.giusti.onetapengine.entity.distribution.eEntityDistributionMode;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jérémy on 14/04/2018.
 */

public class Pool1Spawner extends EntitySpawner<Long> {

    private long lastTimeOfSpawn = 0;
    private long selectedInterval = 500;
    private final long INTERVAL_DIFFERENCE_SHORT_LONG = InfiniteLvlConstant.POOL1_LONG_INTEVALE_OF_SPAWN - InfiniteLvlConstant.POOL1_SHORT_INTEVALE_OF_SPAWN;


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
            if ((cdtProgress - lastTimeOfSpawn) > selectedInterval) {
                lastTimeOfSpawn = cdtProgress;
                return getEntityListOnConditionMet();
            }
        } else {
            //MOB COUNT
            if (mConditionProgress != cdtProgress) {
                //intervale  tend vers LongInterval quand count tend vers maxMob
                mConditionProgress = cdtProgress;
                selectedInterval =(long) (InfiniteLvlConstant.POOL1_SHORT_INTEVALE_OF_SPAWN +((cdtProgress /(double) InfiniteLvlConstant.MAX_NUMBER_OF_MOB)*(INTERVAL_DIFFERENCE_SHORT_LONG)));
            }
        }

        return null;
    }
}
