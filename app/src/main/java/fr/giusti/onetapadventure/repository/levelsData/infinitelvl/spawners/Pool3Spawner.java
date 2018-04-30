package fr.giusti.onetapadventure.repository.levelsData.infinitelvl.spawners;

import java.util.ArrayList;
import java.util.Random;

import fr.giusti.onetapadventure.repository.levelsData.infinitelvl.InfiniteLvlConstant;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.distribution.EntitySpawner;
import fr.giusti.onetapengine.entity.distribution.eEntityDistributionMode;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jérémy on 14/04/2018.
 */

public class Pool3Spawner extends EntitySpawner<Long> {


    private long lastScoreOnSpawn = 0;


    public Pool3Spawner(ArrayList<Entity> entityList) {
        super(new eConditions[]{eConditions.MOB_COUNT, eConditions.MOB_DEATH},
                eEntityDistributionMode.GROUPED_SEMIRANDOM,
                0L,
                0L);
        setEntityList(entityList);
        setInfinitePop(true);
        setGroupeSize(InfiniteLvlConstant.POOL3_GROUP_SIZE);
    }

    /**
     *
     */
    @Override
    public ArrayList<Entity> onConditionProgress(Long cdtProgress, eConditions conditionType) {
        if (conditionType == eConditions.MOB_COUNT) {
            mConditionProgress = cdtProgress;
        } else {
            return shouldPop() ? getEntityListOnConditionMet() : null;
        }
        return null;
    }

    /**
     * @return chance to be true increase the more MobCount is near MAX_NUMBER_OF_MOB
     */
    private boolean shouldPop() {
        long ratio = ((mConditionProgress + 2) *100) /InfiniteLvlConstant.MAX_NUMBER_OF_MOB;
        if (ratio >= 100) {
            return false;
        }
        long chanceToPop = ratio / 10;
        int randomInt = new Random(System.currentTimeMillis()).nextInt(100);

        return chanceToPop > randomInt;
    }


    private long getInterval(Long currentScore) {
        return (long) (InfiniteLvlConstant.POOL2_MAX_INTERVALE_OF_SPAWN - ((currentScore / (double) InfiniteLvlConstant.POOL2_TIME_INTERVAL_CAP) * InfiniteLvlConstant.POOL2_MIN_INTEVALE_OF_SPAWN));
    }

}
