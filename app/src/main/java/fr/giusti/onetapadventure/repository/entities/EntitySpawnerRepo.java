package fr.giusti.onetapadventure.repository.entities;

import android.content.Context;

import java.io.IOException;

import fr.giusti.onetapengine.entity.distribution.EntitySpawnerInt;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerLong;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerManager;
import fr.giusti.onetapengine.entity.distribution.eEntityDistributionMode;
import fr.giusti.onetapengine.rules.eConditions;

/**
 * Created by jérémy on 10/09/2016.
 */
public class EntitySpawnerRepo {

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
        result.addSpawner(new EntitySpawnerLong.EntitySpawnerBuilder(eEntityDistributionMode.ONE_BY_ONE_ORDERED, 3000l, 0l,eConditions.TIMER)
                .setSpawnerInfinite()
                .setEntityList(EntityRepo.getLvl1x3SpecialList(context))
                .build());

        return result;
    }
}
