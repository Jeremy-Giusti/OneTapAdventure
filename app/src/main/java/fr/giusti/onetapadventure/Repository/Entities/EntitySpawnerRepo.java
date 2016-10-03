package fr.giusti.onetapadventure.repository.entities;

import android.content.Context;

import fr.giusti.onetapadventure.gameObject.entities.entityDistribution.EntitySpawner;
import fr.giusti.onetapadventure.gameObject.entities.entityDistribution.EntitySpawnerManager;
import fr.giusti.onetapadventure.gameObject.entities.entityDistribution.eEntityDistributionMode;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;

/**
 * Created by jérémy on 10/09/2016.
 */
public class EntitySpawnerRepo {

    public static EntitySpawnerManager getLvl1_1SpawnerManager(Context context) {
        EntitySpawnerManager result = new EntitySpawnerManager(EntityRepo.getLvl1x1InitList(context), EntityRepo.getLvl1x1BackupList(context));

      //  result.addSpawner(new EntitySpawner(eConditions.MOB_COUNT, 2, 0, false, eEntityDistributionMode.ONE_BY_ONE_ORDERED, null));
        result.addSpawner(new EntitySpawner(eConditions.MOB_COUNT, 1, 0, false, eEntityDistributionMode.ONE_BY_ONE_ORDERED, null));
        result.addSpawner(new EntitySpawner(eConditions.TIMER, 3000, 0, false, eEntityDistributionMode.ONE_BY_ONE_ORDERED, null));
        return result;
    }

    public static EntitySpawnerManager getLvl1_2SpawnerManager(Context context) {
        EntitySpawnerManager result = new EntitySpawnerManager(EntityRepo.getLvl1x2InitList(context), EntityRepo.getLvl1x1BackupList(context));

        result.addSpawner(new EntitySpawner(eConditions.TIMER, 3000, 0, false, eEntityDistributionMode.ONE_BY_ONE_ORDERED, null));
        return result;
    }
}
