package fr.giusti.onetapengine.entity.distribution;

import java.util.ArrayList;
import java.util.HashMap;


import fr.giusti.onetapengine.GameBoard;
import fr.giusti.onetapengine.callback.OnBoardEventListener;
import fr.giusti.onetapengine.callback.SpawnerListener;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jgiusti on 26/09/2016.
 */

public class EntitySpawnerManager implements OnBoardEventListener, SpawnerListener {
    //an indexed list of spawners, each spawner is notified when it's associated condition has an evolution
    protected HashMap<eConditions, ArrayList<EntitySpawner>> entitySpawnerList = new HashMap<>();
    private GameBoard board;

    //list of entities that should appear on the board at start
    protected ArrayList<Entity> initialList;
    //a list that can be used by different spawner to spawn mob from the same base
    private ArrayList<Entity> sharedList;
    private int sharedMobIndex = 0;

    public EntitySpawnerManager(ArrayList<Entity> initList, ArrayList<Entity> sharedList) {
        this.initialList = initList;
        this.sharedList = sharedList;
        for (eConditions conditions : eConditions.values()) {
            entitySpawnerList.put(conditions, new ArrayList<EntitySpawner>());
        }
    }

    public EntitySpawnerManager(ArrayList<EntitySpawner> spawnerList, ArrayList<Entity> initList, ArrayList<Entity> sharedList) {
        this.initialList = initList;
        this.sharedList = sharedList;
        for (eConditions conditions : eConditions.values()) {
            entitySpawnerList.put(conditions, new ArrayList<EntitySpawner>());
        }

        for (EntitySpawner spawner : spawnerList) {
            entitySpawnerList.get(spawner.conditionType).add(spawner);
            spawner.setListener(this);
        }

    }

    public void setSharedList(ArrayList<Entity> sharedList) {
        this.sharedList = sharedList;
    }

    public void addSpawner(EntitySpawner spawner) {
        entitySpawnerList.get(spawner.conditionType).add(spawner);
        spawner.setListener(this);
    }

    public void setBoard(GameBoard board) {
        this.board = board;
    }

    public ArrayList<Entity> getInitialList() {
        return initialList;
    }

    @Override
    public void firstUpdate() {
        //nothing ?
    }

    @Override
    public void onMobEvent(eConditions reason, GameMob mob){
        //dispatch event.
        switch (reason) {
            case MOB_DEATH:
                onMobDeath(mob);
                onMobCountDown(mob);
                break;
            case MOB_AWAY:
                onMobGetAway(mob);
                onMobCountDown(mob);
                break;
        }
    }

    @Override
    public void onMobCountChange(int count) {
        //find if a rule is linked to the condition and test it
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.MOB_COUNT);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(count));
        }
    }


    private void onMobGetAway(GameMob mob) {
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.MOB_AWAY);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(1));
        }
    }

    private void onMobCountDown(GameMob mob) {
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.MOB_AWAY);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(1));
        }
    }

    private void onMobDeath(GameMob mob) {
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.MOB_DEATH);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(1));
        }
    }

    @Override
    public void onScoreChange(int add) {
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.SCORE);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(add));
        }
    }

    @Override
    public void onTimeProgress(long progress) {
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.TIMER);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(progress));
        }
    }

    @Override
    public void onSpawnerEmpty(EntitySpawner popper) {
        entitySpawnerList.get(popper.conditionType).remove(popper);
    }

    @Override
    public void onSpawnRequested(boolean infinitePop, eEntityDistributionMode distribMode, int groupeSize) {
        if (sharedList == null || sharedList.isEmpty()) return;
        if (sharedMobIndex >= sharedList.size()) sharedMobIndex = 0;

        ArrayList<Entity> result = new ArrayList<>();
        switch (distribMode) {
            case ALL_AT_ONCE:
                //put all mob
                if (!infinitePop) {
                    result.addAll(sharedList);
                    sharedList.clear();
                } else {
                    for (Entity entity : sharedList) {
                        result.add(entity.clone());
                    }

                }
                break;
            case ONE_BY_ONE_ORDERED:
                //add a mob from the list by order
                if (!infinitePop) {
                    result.add(sharedList.get(sharedMobIndex));
                    sharedList.remove(sharedMobIndex);
                } else {
                    result.add(sharedList.get(sharedMobIndex).clone());

                    if (sharedMobIndex < sharedList.size() + 1)
                        sharedMobIndex++;
                    else
                        sharedMobIndex = 0;
                }
                break;
            case ONE_BY_ONE_RANDOM:
                //add a random mob from list
                sharedMobIndex = (int) (Math.random() * sharedList.size()) - 1;
                if (!infinitePop) {
                    result.add(sharedList.get(sharedMobIndex));
                    sharedList.remove(sharedMobIndex);
                } else {
                    result.add(sharedList.get(sharedMobIndex).clone());
                }
                break;
            case GROUPED_ORDERED:
                for (int i = 0; i < groupeSize; i++) {
                    if (!infinitePop) {
                        result.add(sharedList.get(sharedMobIndex));
                        sharedList.remove(sharedMobIndex);
                    } else {
                        result.add(sharedList.get(sharedMobIndex).clone());
                        if (sharedMobIndex < sharedList.size() + 1)
                            sharedMobIndex++;
                        else
                            sharedMobIndex = 0;
                    }
                }
                break;
            case GROUPED_RANDOM:
                for (int i = 0; i < groupeSize; i++) {
                    sharedMobIndex = (int) (Math.random() * sharedList.size()) - 1;
                    if (!infinitePop) {
                        result.add(sharedList.get(sharedMobIndex));
                        sharedList.remove(sharedMobIndex);
                    } else {
                        result.add(sharedList.get(sharedMobIndex).clone());
                    }
                }
                break;
            case GROUPED_SEMIRANDOM:
                sharedMobIndex = (int) ((Math.random() * sharedList.size() / groupeSize) - 1) * groupeSize;//if group size==5 index can be 5-10-15-...
                for (int i = 0; i < groupeSize; i++) {
                    if (!infinitePop) {
                        result.add(sharedList.get(sharedMobIndex));
                        sharedList.remove(sharedMobIndex);
                    } else {
                        result.add(sharedList.get(sharedMobIndex).clone());
                        if (sharedMobIndex < sharedList.size() + 1)
                            sharedMobIndex++;
                        else
                            sharedMobIndex = 0;
                    }

                }
                break;
        }
        board.onNewEntities(result);
    }

    public void resize(float ratio) {
//        for (Entity entity : initialList) {
//            entity.resize(ratio);
//        }

        for (Entity entity : sharedList) {
            entity.resize(ratio);
        }

        for (ArrayList<EntitySpawner> spawnerList : entitySpawnerList.values()) {
            for (EntitySpawner spawner : spawnerList) {
                spawner.resize(ratio);
            }
        }
    }
}
