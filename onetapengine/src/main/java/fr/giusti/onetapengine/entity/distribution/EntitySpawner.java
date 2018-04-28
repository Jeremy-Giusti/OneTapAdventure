package fr.giusti.onetapengine.entity.distribution;

import java.util.ArrayList;

import fr.giusti.onetapengine.callback.SpawnerListener;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by giusti on 24/02/2018.
 */

public abstract class EntitySpawner<T> {
    /**
     * more like an id work as the condition ruleResult in which this spawner should be notified on a change
     */
    public final eConditions[] conditionTypes;

    /**
     * the way entity will be choosen (by goup/by order/randomly)
     */
    public final eEntityDistributionMode distibutionMode;

    /**
     * the condition mGoal if the mProgress match this value, a entity spawn will be triggered
     * and reset mProgress to initialValue
     */
    protected final T conditionGoalValue;
    /**
     * the initial mProgress
     */
    protected final T initialProgressValue;
    protected T conditionProgress;


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
    private boolean mIsResized = false;


    public EntitySpawner(eConditions[] conditionTypes, eEntityDistributionMode distibutionMode, T conditionGoalValue, T initialProgressValue) {
        this.conditionTypes = conditionTypes;
        this.distibutionMode = distibutionMode;
        this.conditionGoalValue = conditionGoalValue;
        this.initialProgressValue = initialProgressValue;
        this.conditionProgress = initialProgressValue;
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
    public abstract ArrayList<Entity> onConditionProgress(T cdtProgress, eConditions conditionType);

    /**
     * spawn condition met, depending on spawner attribut choose the best way to distribute the newly spawned entity(s)
     *
     * @return nothing || a list of entity to spawn || nothing but call listener.onSpawnRequested for a custom spawning event (shared entity list notably)
     */
    protected ArrayList<Entity> getEntityListOnConditionMet() {
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
        if (!useSharedMobList && !mIsResized) {
            for (Entity entity : entityList) {
                entity.resize(ratio);
            }
        }
        this.mIsResized = true;
    }
}
