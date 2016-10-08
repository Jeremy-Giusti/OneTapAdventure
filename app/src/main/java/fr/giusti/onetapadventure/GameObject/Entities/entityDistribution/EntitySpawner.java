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
    public final eEntityDistributionMode distibutionMode;

    /**
     * once reached by progress trigger entity spawn <br>
     * and reset progress to initialValue
     */
    private final int conditionGoalValue;
    private final int initialProgressValue;
    private int conditionProgress = -1;


    private int groupeSize = 5;
    private boolean useSharedMobList = false;
    private boolean infinitePop = false;

    private ArrayList<Entity> entityList;
    private int mobIndex = 0;

    private SpawnerListener listener;


    /**
     * more like an id work as the condition type in wich this spawner should be notified on a change
     * <br> the condition goal if the progress match this value, a entity spawn will be triggered
     * <br> the initial progress
     * <br>if true then this spawner will never run out of mob
     * <br>the way entity will be choosed (by goup/by order/randomly)
     * <br>the entiies to spawn if null request listener to spawn entity
     */
    private EntitySpawner(EntitySpawnerBuilder builder) {
        this.conditionType = builder.conditionType;
        this.conditionGoalValue = builder.conditionGoalValue;
        this.initialProgressValue = builder.initialProgressValue;
        this.conditionProgress = builder.initialProgressValue;
        this.infinitePop = builder.infinitePop;
        this.distibutionMode = builder.distibutionMode;
        this.useSharedMobList = builder.useSharedList;
        this.entityList = builder.entityList;
        this.groupeSize = builder.groupeSize;
    }


    public int getGroupeSize() {
        return groupeSize;
    }

    public void setGroupeSize(int groupeSize) {
        this.groupeSize = groupeSize;
    }

    public boolean isUseSharedMobList() {
        return useSharedMobList;
    }

    public void setUseSharedMobList(boolean useSharedMobList) {
        this.useSharedMobList = useSharedMobList;
    }

    public boolean isInfinitePop() {
        return infinitePop;
    }

    public void setInfinitePop(boolean infinitePop) {
        this.infinitePop = infinitePop;
    }

    public ArrayList<Entity> getEntityList() {
        return entityList;
    }

    public void setEntityList(ArrayList<Entity> entityList) {
        this.entityList = entityList;
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
            return (cdtProgress == conditionGoalValue) ? onConditionMet() : null;

        if (conditionType == eConditions.TIMER)
            return ((cdtProgress % conditionGoalValue) == 0) ? onConditionMet() : null;

        conditionProgress += cdtProgress;
        if (conditionProgress == conditionGoalValue) {
            conditionProgress = initialProgressValue;
            return onConditionMet();
        }
        return null;
    }

    private ArrayList<Entity> onConditionMet() {
        if (useSharedMobList) {
            listener.onSpawnRequested(infinitePop, distibutionMode, groupeSize);
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
                case GROUPED_ORDERED:
                    for (int i = 0; i < groupeSize; i++) {
                        result.add(entityList.get(mobIndex));
                        if (!infinitePop) entityList.remove(mobIndex);
                        else if (mobIndex < entityList.size() + 1)
                            mobIndex++;
                        else
                            mobIndex = 0;
                    }
                    break;
                case GROUPED_RANDOM:
                    for (int i = 0; i < groupeSize; i++) {
                        mobIndex = (int) (Math.random() * entityList.size()) - 1;
                        result.add(entityList.get(mobIndex));
                        if (!infinitePop) entityList.remove(mobIndex);
                    }
                    break;
                case GROUPED_SEMIRANDOM:
                    mobIndex = (int)( (Math.random() * entityList.size()/groupeSize) - 1)*groupeSize;//if group size==5 index can be 5-10-15-...
                    for (int i = 0; i < groupeSize; i++) {
                        result.add(entityList.get(mobIndex));
                        if (!infinitePop) entityList.remove(mobIndex);
                        else if (mobIndex < entityList.size() + 1)
                            mobIndex++;
                        else
                            mobIndex = 0;
                    }
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


    public static class EntitySpawnerBuilder {

        private final eEntityDistributionMode distibutionMode;
        private final eConditions conditionType;
        private final int conditionGoalValue;
        private final int initialProgressValue;

        private ArrayList<Entity> entityList;
        private boolean useSharedList = true;
        private boolean infinitePop = false;
        private int groupeSize = 5;

        public EntitySpawnerBuilder(eEntityDistributionMode distibutionMode, eConditions conditionType, int conditionGoalValue, int initialProgressValue) {
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

        public EntitySpawner build() {
            return new EntitySpawner(this);
        }

    }
}
