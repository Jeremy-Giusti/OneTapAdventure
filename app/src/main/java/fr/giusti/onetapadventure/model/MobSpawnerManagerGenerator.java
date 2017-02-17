package fr.giusti.onetapadventure.model;

import android.content.Context;

import java.util.ArrayList;

import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.distribution.EntitySpawner;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerManager;

/**
 * Created by jgiusti on 17/02/2017.
 * <p>
 * used to generate an EntitySpawnerManager with dynamically generated mob lists (in spawners and in initialEntityList)
 */

public class MobSpawnerManagerGenerator extends EntitySpawnerManager {
    public MobSpawnerManagerGenerator(ArrayList<Entity> initList, ArrayList<Entity> sharedList) {
        super(initList, sharedList);
    }

    private ArrayList<MobGroupGenerator> mobGroupGeneratorList;
    private ArrayList<MobSpawnerGenerator> mobSpawnerGeneratorList;


    public EntitySpawnerManager generate(Context context) {
        if (initialList == null) initialList = new ArrayList<>();

        //------ generated initMobList management
        for (MobGroupGenerator generator : mobGroupGeneratorList) {
            initialList.addAll(generator.generateGroup(context)); //should only had since scenery/particule/static mob may already be in this list
        }

        //------ generated spawner management
        for (MobSpawnerGenerator spawner : mobSpawnerGeneratorList) {
            if (entitySpawnerList.containsKey(spawner.conditionType)) {
                //we put the spawn in its indexed list
                entitySpawnerList.get(spawner.conditionType).add(spawner);
            } else {
                //create an indexed list and put spawner in it
                ArrayList<EntitySpawner> spawnerList = new ArrayList<>();
                spawnerList.add(spawner);
                entitySpawnerList.put(spawner.conditionType, spawnerList);
            }
        }
        //FIXME clear generators arraylists ?

        return this;
    }
}
