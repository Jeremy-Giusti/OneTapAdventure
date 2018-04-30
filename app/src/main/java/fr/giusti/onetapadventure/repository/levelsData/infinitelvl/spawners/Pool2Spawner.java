package fr.giusti.onetapadventure.repository.levelsData.infinitelvl.spawners;

import java.util.ArrayList;

import fr.giusti.onetapadventure.repository.levelsData.infinitelvl.InfiniteLvlConstant;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.distribution.EntitySpawner;
import fr.giusti.onetapengine.entity.distribution.eEntityDistributionMode;
import fr.giusti.onetapengine.rules.eConditions;

import static fr.giusti.onetapadventure.repository.levelsData.infinitelvl.InfiniteLvlConstant.MAX_NUMBER_OF_MOB;

/**
 * Created by jérémy on 14/04/2018.
 */

public class Pool2Spawner extends EntitySpawner<Long> {


    private long lastScoreOnSpawn = 0;


    public Pool2Spawner(ArrayList<Entity> entityList) {
        super(new eConditions[]{eConditions.TIMER, eConditions.MOB_COUNT},
                eEntityDistributionMode.GROUPED_RANDOM,
                0L,
                0L);
        setEntityList(entityList);
        setInfinitePop(true);
    }

    /**
     * spawn every at a changing interval depending on score (min is {@link InfiniteLvlConstant#POOL2_MIN_INTEVALE_OF_SPAWN} at {@link InfiniteLvlConstant#POOL2_TIME_INTERVAL_CAP} score) max is {@link InfiniteLvlConstant#POOL2_MAX_INTERVALE_OF_SPAWN}
     *
     * @param cdtProgress   condition evolution
     * @param conditionType mobcount or score
     * @return group size depend on mobCount at spawn
     */
    @Override
    public ArrayList<Entity> onConditionProgress(Long cdtProgress, eConditions conditionType) {
        if (conditionType == eConditions.TIMER) {
            long selectedInterval = (cdtProgress < InfiniteLvlConstant.POOL2_TIME_INTERVAL_CAP) ? getInterval(cdtProgress) : InfiniteLvlConstant.POOL2_MIN_INTEVALE_OF_SPAWN;
            if ((cdtProgress - lastScoreOnSpawn) > selectedInterval) {
                lastScoreOnSpawn = cdtProgress;
                return getEntityListOnConditionMet();
            }
        } else {
            //mob count
            mConditionProgress = cdtProgress;
            groupeSize = getGroupSizeCalculatedValue(cdtProgress);
        }
        return null;
    }

    /**
     * @param mobCount mobCount on board
     * @return 0-{@link InfiniteLvlConstant#POOL2_SPAWN_GROUP_SIZE} depending on how close mobCount is to {@link fr.giusti.onetapadventure.repository.levelsData.infinitelvl.InfiniteLvlConstant#MAX_NUMBER_OF_MOB}
     */
    private int getGroupSizeCalculatedValue(Long mobCount) {
        if (mobCount < MAX_NUMBER_OF_MOB / 3) {
            return InfiniteLvlConstant.POOL2_SPAWN_GROUP_SIZE + 2;

        } else if (mobCount > (MAX_NUMBER_OF_MOB / 3) * 2) {
            return InfiniteLvlConstant.POOL2_SPAWN_GROUP_SIZE - 2;

        } else {
            return InfiniteLvlConstant.POOL2_SPAWN_GROUP_SIZE;
        }
    }


    private long getInterval(Long currentScore) {
        return (long) (InfiniteLvlConstant.POOL2_MAX_INTERVALE_OF_SPAWN - ((currentScore / (double) InfiniteLvlConstant.POOL2_TIME_INTERVAL_CAP) * InfiniteLvlConstant.POOL2_MIN_INTEVALE_OF_SPAWN));
    }

}
