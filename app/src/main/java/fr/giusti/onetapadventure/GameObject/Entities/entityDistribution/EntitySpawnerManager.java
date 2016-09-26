package fr.giusti.onetapadventure.gameObject.entities.entityDistribution;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapadventure.callback.OnBoardEventListener;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;

/**
 * Created by jgiusti on 26/09/2016.
 */

public class EntitySpawnerManager implements OnBoardEventListener {
    HashMap<eConditions, ArrayList<EntitySpawner>> entitySpawnerList = new HashMap<>();


    public EntitySpawnerManager(ArrayList<EntitySpawner> spawnerList) {
        for (eConditions conditions : eConditions.values()) {
            entitySpawnerList.put(conditions, new ArrayList<EntitySpawner>())
        }
        
        for (EntitySpawner spawner : spawnerList) {
            entitySpawnerList.get(spawner.conditionType).add(spawner);
        }
    }

    @Override
    public void firstUpdate() {

    }

    @Override
    public void onMobCountChange(int count, eConditions reason, GameMob mob) {

    }

    @Override
    public void onScorePlus(int add) {

    }

    @Override
    public void onScoreMinus(int remove) {

    }

    @Override
    public void onTimeProgress(int progress) {

    }
}
