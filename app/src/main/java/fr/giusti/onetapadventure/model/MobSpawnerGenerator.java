package fr.giusti.onetapadventure.model;

import android.content.Context;

import java.util.ArrayList;

import fr.giusti.onetapengine.entity.distribution.EntitySpawner;

/**
 * Created by jgiusti on 17/02/2017.
 */

public class MobSpawnerGenerator extends EntitySpawner {

    private ArrayList<MobGroupGenerator> mobGroupGeneratorList;

    private MobSpawnerGenerator(EntitySpawnerBuilder builder) {
        super(builder);
    }

    /**
     * launch all mob generation and populate this spawner storage
     *
     * @param context
     */
    public EntitySpawner generate(Context context) {
        entityList = new ArrayList<>();
        for (MobGroupGenerator generator : mobGroupGeneratorList) {
            entityList.addAll(generator.generateGroup(context));
        }
        return this;
    }
}
