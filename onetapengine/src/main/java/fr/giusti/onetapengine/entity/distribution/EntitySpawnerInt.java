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
public class EntitySpawnerInt extends EntitySpawner<Integer>{

    protected EntitySpawnerInt(EntitySpawnerBuilder builder) {
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
    public ArrayList<Entity> onConditionProgress(Integer cdtProgress, eConditions conditionsType) {
        if (!useSharedMobList && entityList.isEmpty()) {
            listener.onSpawnerEmpty(this);
            return null;
        }

        if (conditionsType == eConditions.MOB_COUNT)
            return (cdtProgress == conditionGoalValue) ? onConditionMet() : null;

        if (conditionsType == eConditions.TIMER)
            return ((cdtProgress % conditionGoalValue) == 0) ? onConditionMet() : null;

        conditionProgress += cdtProgress;
        if (conditionProgress == conditionGoalValue) {
            conditionProgress = initialProgressValue;
            return onConditionMet();
        }
        return null;
    }

        @Deprecated
        public static class EntitySpawnerBuilder {

        private final eEntityDistributionMode distibutionMode;
        private final eConditions[] conditionType;
        private final int conditionGoalValue;
        private final int initialProgressValue;

        private ArrayList<Entity> entityList;
        private boolean useSharedList = true;
        private boolean infinitePop = false;
        private int groupeSize = 5;

        public EntitySpawnerBuilder(eEntityDistributionMode distibutionMode, Integer conditionGoalValue, Integer initialProgressValue, eConditions... conditionType) {
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

        public EntitySpawnerInt build() {
            return new EntitySpawnerInt(this);
        }

    }
}
