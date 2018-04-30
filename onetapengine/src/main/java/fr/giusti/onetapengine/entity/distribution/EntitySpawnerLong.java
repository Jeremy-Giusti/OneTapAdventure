package fr.giusti.onetapengine.entity.distribution;

import java.util.ArrayList;

import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jgiusti on 26/09/2016.
 * if it hold a list of entity
 * will distribute those entities depending on it condition mProgress <br>
 * else use the entity list of its listener
 */
@Deprecated
public class EntitySpawnerLong extends EntitySpawner<Long>{

    protected EntitySpawnerLong(EntitySpawnerBuilder builder) {
        super(builder.conditionType,builder.distibutionMode,builder.conditionGoalValue,builder.initialProgressValue);

        this.infinitePop = builder.infinitePop;
        this.useSharedMobList = builder.useSharedList;
        this.entityList = builder.entityList;
        this.groupeSize = builder.groupeSize;
    }

    /**
     * the spawner condition evoluted, a check is done to choose whether or not a spawn is in order
     *
     * @param cdtProgress condition evolution
     * @return nothing || a list of entity to spawn || nothing but call listener.onSpawnRequested for a custom spawning event (shared entity list notably)
     */
    public ArrayList<Entity> onConditionProgress(Long cdtProgress, eConditions conditionsType) {
        if (!useSharedMobList && entityList.isEmpty()) {
            listener.onSpawnerEmpty(this);
            return null;
        }

        if (conditionsType == eConditions.MOB_COUNT)
            return (cdtProgress == conditionGoalValue) ? getEntityListOnConditionMet() : null;

        if (conditionsType == eConditions.TIMER)
            return ((cdtProgress % conditionGoalValue) == 0) ? getEntityListOnConditionMet() : null;

        mConditionProgress += cdtProgress;
        if (mConditionProgress == conditionGoalValue) {
            mConditionProgress = initialProgressValue;
            return getEntityListOnConditionMet();
        }
        return null;
    }

    /**
     * make the transition easier
     * @param cdtProgress
     * @return
     */
    public ArrayList<Entity> onConditionProgress(int cdtProgress, eConditions conditionType) {
        return onConditionProgress(Long.valueOf(cdtProgress),conditionType);
    }

        @Deprecated
        public static class EntitySpawnerBuilder {

        private final eEntityDistributionMode distibutionMode;
        private final eConditions[] conditionType;
        private final Long conditionGoalValue;
        private final Long initialProgressValue;

        private ArrayList<Entity> entityList;
        private boolean useSharedList = true;
        private boolean infinitePop = false;
        private int groupeSize = 5;

        public EntitySpawnerBuilder(eEntityDistributionMode distibutionMode, Long conditionGoalValue, Long initialProgressValue, eConditions... conditionType) {
            this.distibutionMode = distibutionMode;
            this.conditionType = conditionType;
            this.conditionGoalValue = conditionGoalValue;
            this.initialProgressValue = initialProgressValue;
            this.useSharedList = true;
        }

        public EntitySpawnerBuilder setEntityList(ArrayList<Entity> entityList) {
            if (entityList == null || entityList.isEmpty()) {
                setUseSharedList();
            }

            this.entityList = entityList;
            useSharedList = false;
            return this;
        }

        public EntitySpawnerBuilder setUseSharedList() {
            useSharedList = true;
            return this;
        }

        public EntitySpawnerBuilder setSpawnerInfinite() {
            this.infinitePop = true;
            return this;
        }

        public EntitySpawnerBuilder setSpawnerLimited() {
            this.infinitePop = false;
            return this;
        }

        public EntitySpawnerBuilder setSpawnGroupSize(int size) {
            this.groupeSize = size;
            return this;
        }

        public EntitySpawnerLong build() {
            return new EntitySpawnerLong(this);
        }

    }
}
