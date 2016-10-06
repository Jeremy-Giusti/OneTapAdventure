package fr.giusti.onetapadventure.gameObject.entities.entityDistribution;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapadventure.callback.OnBoardEventListener;
import fr.giusti.onetapadventure.callback.SpawnerListener;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.Entity;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;

/**
 * Created by jgiusti on 26/09/2016.
 */

public class EntitySpawnerManager implements OnBoardEventListener, SpawnerListener {
    private HashMap<eConditions, ArrayList<EntitySpawner>> entitySpawnerList = new HashMap<>();
    private GameBoard board;

    private ArrayList<Entity> initialList;
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
    public void onMobCountChange(int count, eConditions reason, GameMob mob) {
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
//            case NEW_MOB:
//                onNewMob(mob);
//                break;
        }

        //find if a rule is linked to the condition and test it
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.MOB_COUNT);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(count));
        }
    }

    //should be avoided to prevent stack overflow/infiniteloop
//    private void onNewMob(GameMob mob) {
//        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.NEW_MOB);
//        EntitySpawner spawner;
//        for (int i = 0; i < spawnerList.size(); i++) {
//            spawner = spawnerList.get(i);
//            board.onNewEntities(spawner.onConditionProgress(1));
//        }
//    }

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
    public void onScorePlus(int add) {
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.SCORE);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(add));
        }
    }

    @Override
    public void onScoreMinus(int remove) {
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.SCORE);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(-remove));
        }
    }

    @Override
    public void onTimeProgress(int progress) {
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
                result.addAll(sharedList);
                if (!infinitePop) sharedList.clear();
                break;
            case ONE_BY_ONE_ORDERED:
                //add a mob from the list by order
                result.add(sharedList.get(sharedMobIndex));
                if (!infinitePop) sharedList.remove(sharedMobIndex);
                else if (sharedMobIndex < sharedList.size() + 1)
                    sharedMobIndex++;
                else
                    sharedMobIndex = 0;
                break;
            case ONE_BY_ONE_RANDOM:
                //add a random mob from list
                sharedMobIndex = (int) (Math.random() * sharedList.size()) - 1;
                result.add(sharedList.get(sharedMobIndex));
                if (!infinitePop) sharedList.remove(sharedMobIndex);
                break;
            case GROUPED_ORDERED:
                for (int i = 0; i < groupeSize; i++) {
                    result.add(sharedList.get(sharedMobIndex));
                    if (!infinitePop) sharedList.remove(sharedMobIndex);
                    else if (sharedMobIndex < sharedList.size() + 1)
                        sharedMobIndex++;
                    else
                        sharedMobIndex = 0;
                }
                break;
            case GROUPED_RANDOM:
                for (int i = 0; i < groupeSize; i++) {
                    sharedMobIndex = (int) (Math.random() * sharedList.size()) - 1;
                    result.add(sharedList.get(sharedMobIndex));
                    if (!infinitePop) sharedList.remove(sharedMobIndex);
                }
                break;
            case GROUPED_SEMIRANDOM:
                sharedMobIndex = (int) (Math.random() * sharedList.size()) - 1;
                for (int i = 0; i < groupeSize; i++) {
                    result.add(sharedList.get(sharedMobIndex));
                    if (!infinitePop) sharedList.remove(sharedMobIndex);
                    else if (sharedMobIndex < sharedList.size() + 1)
                        sharedMobIndex++;
                    else
                        sharedMobIndex = 0;
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
