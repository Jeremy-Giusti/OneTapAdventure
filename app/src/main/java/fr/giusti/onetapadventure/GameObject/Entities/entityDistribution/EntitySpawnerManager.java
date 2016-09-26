package fr.giusti.onetapadventure.gameObject.entities.entityDistribution;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapadventure.callback.OnBoardEventListener;
import fr.giusti.onetapadventure.callback.OnPopperEmptyListener;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;

/**
 * Created by jgiusti on 26/09/2016.
 */

public class EntitySpawnerManager implements OnBoardEventListener, OnPopperEmptyListener {
    private HashMap<eConditions, ArrayList<EntitySpawner>> entitySpawnerList = new HashMap<>();
    private GameBoard board;

    public EntitySpawnerManager(ArrayList<EntitySpawner> spawnerList, GameBoard board) {
        this.board = board;
        for (eConditions conditions : eConditions.values()) {
            entitySpawnerList.put(conditions, new ArrayList<EntitySpawner>());
        }

        for (EntitySpawner spawner : spawnerList) {
            entitySpawnerList.get(spawner.conditionType).add(spawner);
        }
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
            case NEW_MOB:
                onNewMob(mob);
                break;
        }

        //find if a rule is linked to the condition and test it
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.MOB_COUNT);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(count));
        }
    }

    private void onNewMob(GameMob mob) {
        ArrayList<EntitySpawner> spawnerList = entitySpawnerList.get(eConditions.NEW_MOB);
        EntitySpawner spawner;
        for (int i = 0; i < spawnerList.size(); i++) {
            spawner = spawnerList.get(i);
            board.onNewEntities(spawner.onConditionProgress(1));
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
    public void onPopperEmpty(EntitySpawner popper) {
        entitySpawnerList.get(popper.conditionType).remove(popper);
    }
}
