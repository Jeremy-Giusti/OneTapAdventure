package fr.giusti.onetapadventure.repository.entities;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

import fr.giusti.onetapadventure.repository.levelsData.infinitelvl.spawners.Pool1Spawner;
import fr.giusti.onetapadventure.repository.levelsData.infinitelvl.spawners.Pool2Spawner;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerFactory;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerInt;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerLong;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerManager;
import fr.giusti.onetapengine.entity.distribution.eEntityDistributionMode;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jérémy on 10/09/2016.
 */
public class EntitySpawnerRepo {

    public static EntitySpawnerManager getInfiniteSpawnerManager(Context context) throws IOException {
        EntitySpawnerManager result = new EntitySpawnerManager(EntityRepo.getInfiniteLvlInitList(context), EntityRepo.getInfiniteLvlBackupList(context));

        // simple randomized mob (1pv, simple paths,no capacity,
        // spawn every 1.5sec or 0.75 if count<10)
        ArrayList<Entity> pool1 = EntityRepo.getInfiniteLvlPool1(context);
        result.addSpawner(new Pool1Spawner(pool1));
        //TODO END POOL 2
        //multiplying mobs (duplication, egg flies, heal on touch, multiplie into smaller version on touch, shield ?)
        //Spawn each 10 kill OR make them spawn more often with bigger group as time pass (with a capping) ?
        //spawn on group with this calcul groupSize = ((25(=maxmobsNb) - count)/maxmob) * 7 (maxgroupsize)
        ArrayList<Entity> pool2 = EntityRepo.getInfiniteLvlPool2(context);
        result.addSpawner(new Pool2Spawner(pool2));
        //TODO POOL 3
        //helping mobs (zombie, eating, gravity?)
        //spawn onMobdeath
        // chance to spawn  = count/maxmobNb
        //result.addSpawner(new EntitySpawnerInt.EntitySpawnerBuilder(eEntityDistributionMode.ALL_AT_ONCE, 0, -1, eConditions.MOB_COUNT).setEntityList(EntityRepo.getLvl1x1LastWave(context)).build());
        //TODO POOL 4
        //Avancement mobs (golden, tp, touch tp, quick, swap, ...)
        //spawn onScore
        //spawn on modulo of score (to define) => spawn more often as score advance ?
       // result.addSpawner(new EntitySpawnerInt.EntitySpawnerBuilder(eEntityDistributionMode.ALL_AT_ONCE, 0, -1, eConditions.MOB_COUNT).setEntityList(EntityRepo.getLvl1x1LastWave(context)).build());

        return result;
    }

    public static EntitySpawnerManager getLvl1_1SpawnerManager(Context context) throws IOException {
        EntitySpawnerManager result = new EntitySpawnerManager(EntityRepo.getLvl1x1InitList(context), EntityRepo.getLvl1x1BackupList(context));


        result.addSpawner(new EntitySpawnerInt.EntitySpawnerBuilder(eEntityDistributionMode.ONE_BY_ONE_ORDERED, 1, 0, eConditions.MOB_COUNT).setUseSharedList().build());

        result.addSpawner(new EntitySpawnerLong.EntitySpawnerBuilder(eEntityDistributionMode.ONE_BY_ONE_ORDERED, 3000l, 0l, eConditions.TIMER).setUseSharedList().build());
        result.addSpawner(new EntitySpawnerInt.EntitySpawnerBuilder(eEntityDistributionMode.ALL_AT_ONCE, 0, -1, eConditions.MOB_COUNT).setEntityList(EntityRepo.getLvl1x1LastWave(context)).build());

        return result;
    }

    public static EntitySpawnerManager getLvl1_2SpawnerManager(Context context) throws IOException {
        EntitySpawnerManager result = new EntitySpawnerManager(EntityRepo.getLvl1x2InitList(context), EntityRepo.getLvl1x2BackupList(context));

        result.addSpawner(new EntitySpawnerLong.EntitySpawnerBuilder(eEntityDistributionMode.GROUPED_SEMIRANDOM, 2000l, 0l, eConditions.TIMER)
                .setSpawnerInfinite()
                .setUseSharedList()
                .build());

        result.addSpawner(new EntitySpawnerInt.EntitySpawnerBuilder(eEntityDistributionMode.ONE_BY_ONE_RANDOM, 0, 0, eConditions.MOB_COUNT)
                .setSpawnerInfinite()
                .setEntityList(EntityRepo.getLvl1x2SpecialList(context))
                .build());

        return result;
    }

    public static EntitySpawnerManager getLvl1_3SpawnerManager(Context context) throws IOException {
        //TODO
        EntitySpawnerManager result = new EntitySpawnerManager(EntityRepo.getLvl1x2InitList(context), EntityRepo.getLvl1x2BackupList(context));

//        result.addSpawner(new EntitySpawnerLong.EntitySpawnerBuilder(eEntityDistributionMode.GROUPED_SEMIRANDOM, eConditions.TIMER, 2000, 0)
//                .setSpawnerInfinite()
//                .setUseSharedList()
//                .build());
//
        result.addSpawner(new EntitySpawnerLong.EntitySpawnerBuilder(eEntityDistributionMode.ONE_BY_ONE_ORDERED, 3000l, 0l, eConditions.TIMER)
                .setSpawnerInfinite()
                .setEntityList(EntityRepo.getLvl1x3SpecialList(context))
                .build());

        return result;
    }
}
