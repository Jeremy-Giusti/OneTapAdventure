package fr.giusti.onetapengine.entity.distribution;

import java.util.ArrayList;

import fr.giusti.onetapengine.callback.SpawnerListener;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jgiusti on 26/09/2016.
 * either hold a list of entity
 * will distribute those entities depending on it condition progress <br>
 * or use the entity list of its listener
 */

public class EntitySpawner {

    /**
     * more like an id work as the condition type in wich this spawner should be notified on a change
     */
    public final eConditions conditionType;

    /**
     * the way entity will be choosen (by goup/by order/randomly)
     */
    public final eEntityDistributionMode distibutionMode;

    /**
     * the condition goal if the progress match this value, a entity spawn will be triggered
     * and reset progress to initialValue
     */
    protected final int conditionGoalValue;
    /**
     * the initial progress
     */
    protected final int initialProgressValue;
    protected int conditionProgress = -1;


    protected int groupeSize = 5;
    protected boolean useSharedMobList = false;
    /**
     * if true then this spawner will never run out of mob
     */
    protected boolean infinitePop = false;
    /**
     * the entiies to spawn if null do spawnRequest from listener
     */
    protected ArrayList<Entity> entityList;
    protected int mobIndex = 0;

    protected SpawnerListener listener;


    protected EntitySpawner(EntitySpawnerBuilder builder) {
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

    /**
     * the spawner condition evoluted, a check is done to choose whether or not a spawn is in order
     *
     * @param cdtProgress condition evolution
     * @return nothing || a list of entity to spawn || nothing but call listener.onSpawnRequested for a custom spawning event (shared entity list notably)
     */
    public ArrayList<Entity> onConditionProgress(long cdtProgress) {
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

    /**
     * spawn condition met, depending on spawner attribut choose the best way to distribute the newly spawned entity(s)
     *
     * @return nothing || a list of entity to spawn || nothing but call listener.onSpawnRequested for a custom spawning event (shared entity list notably)
     */
    private ArrayList<Entity> onConditionMet() {
        if (useSharedMobList) {
            listener.onSpawnRequested(infinitePop, distibutionMode, groupeSize);
            return null;
        } else {
            ArrayList<Entity> result = new ArrayList<>();
            switch (distibutionMode) {
                case ALL_AT_ONCE:
                    //put all mob
                    if (!infinitePop) {
                        result.addAll(entityList);
                        entityList.clear();
                    } else {
                        for (Entity entity : entityList)
                            result.add(entity.clone());

                    }
                    break;
                case ONE_BY_ONE_ORDERED:
                    //add a mob from the list by order
                    if (!infinitePop) {
                        result.add(entityList.get(mobIndex));
                        entityList.remove(mobIndex);
                    } else {
                        result.add(entityList.get(mobIndex).clone());
                        if (mobIndex + 1 < entityList.size())
                            mobIndex++;
                        else
                            mobIndex = 0;
                    }
                    break;
                case ONE_BY_ONE_RANDOM:
                    //add a random mob from list
                    mobIndex = (int) (Math.random() * entityList.size());
                    if (!infinitePop) {
                        result.add(entityList.get(mobIndex));
                        entityList.remove(mobIndex);
                    } else {
                        result.add(entityList.get(mobIndex).clone());
                    }
                    break;
                case GROUPED_ORDERED:
                    for (int i = 0; i < groupeSize; i++) {
                        if (!infinitePop) {
                            result.add(entityList.get(mobIndex));
                            entityList.remove(mobIndex);
                        } else {
                            result.add(entityList.get(mobIndex).clone());
                            if (mobIndex + 1 < entityList.size())
                                mobIndex++;
                            else
                                mobIndex = 0;
                        }
                    }
                    break;
                case GROUPED_RANDOM:
                    for (int i = 0; i < groupeSize; i++) {
                        mobIndex = (int) (Math.random() * entityList.size());
                        if (!infinitePop) {
                            result.add(entityList.get(mobIndex));
                            entityList.remove(mobIndex);
                        } else {
                            result.add(entityList.get(mobIndex).clone());
                        }
                    }
                    break;
                case GROUPED_SEMIRANDOM:
                    mobIndex = (int) ((Math.random() * entityList.size() / groupeSize)) * groupeSize;//if group size==5 index can be 5-10-15-...
                    for (int i = 0; i < groupeSize; i++) {
                        result.add(entityList.get(mobIndex));
                        if (!infinitePop) entityList.remove(mobIndex);
                        else if (mobIndex + 1 < entityList.size())
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
