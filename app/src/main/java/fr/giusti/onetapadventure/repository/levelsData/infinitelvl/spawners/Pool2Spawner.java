package fr.giusti.onetapadventure.repository.levelsData.infinitelvl.spawners;

import java.util.ArrayList;

import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.distribution.EntitySpawner;
import fr.giusti.onetapengine.entity.distribution.eEntityDistributionMode;
import fr.giusti.onetapengine.rules.eConditions;

import static fr.giusti.onetapadventure.repository.levelsData.infinitelvl.InfiniteLvlConstant.MAX_NUMBER_OF_MOB;

/**
 * Created by jérémy on 14/04/2018.
 */

public class Pool2Spawner extends EntitySpawner<Long> {
    /**
     * value at which the SHORTEST_INTEVALE_OF_SPAWN is used.
     */
    private static final long SCORE_INTERVAL_CAP = 15000;
    private static final long SHORTEST_INTEVALE_OF_SPAWN = 750;
    private static final long MAX_INTERVALE_OF_SPAWN = 1500;
    private static final int MAX_SPAWN_GROUP_SIZE = 7;


    private long lastScoreOnSpawn = 0;


    public Pool2Spawner(ArrayList<Entity> entityList) {
        super(new eConditions[]{eConditions.SCORE, eConditions.MOB_COUNT},
                eEntityDistributionMode.GROUPED_RANDOM,
                0L,
                0L);
        setEntityList(entityList);
        setInfinitePop(true);
    }

    /**
     * spawn every at a changing interval depending on score (min is {@link #SHORTEST_INTEVALE_OF_SPAWN} at {@link #SCORE_INTERVAL_CAP} score) max is {@link #MAX_INTERVALE_OF_SPAWN}
     *
     * @param cdtProgress   condition evolution
     * @param conditionType mobcount or score
     * @return group size depend on mobCount at spawn
     */
    @Override
    public ArrayList<Entity> onConditionProgress(Long cdtProgress, eConditions conditionType) {
        if (conditionType == eConditions.SCORE) {
            long selectedInterval = (cdtProgress < SCORE_INTERVAL_CAP) ? getInterval(cdtProgress) : SHORTEST_INTEVALE_OF_SPAWN;
            if ((cdtProgress - lastScoreOnSpawn) > selectedInterval) {
                lastScoreOnSpawn = cdtProgress;
                return getEntityListOnConditionMet();
            }
        } else {
            //mob count
            conditionProgress = cdtProgress;
            groupeSize = getGroupSizeCalculatedValue(cdtProgress);
        }
        return null;
    }

    /**
     * @param mobCount mobCount on board
     * @return 0-{@link #MAX_SPAWN_GROUP_SIZE} depending on how close mobCount is to {@link fr.giusti.onetapadventure.repository.levelsData.infinitelvl.InfiniteLvlConstant#MAX_NUMBER_OF_MOB}
     */
    private int getGroupSizeCalculatedValue(Long mobCount) {
        return (int) ((MAX_NUMBER_OF_MOB - mobCount) / MAX_NUMBER_OF_MOB) * MAX_SPAWN_GROUP_SIZE;
    }


    private long getInterval(Long currentScore) {
        return (MAX_INTERVALE_OF_SPAWN - SHORTEST_INTEVALE_OF_SPAWN) / (SCORE_INTERVAL_CAP - currentScore);
    }

}
