package fr.giusti.onetapadventure.gameObject.entities.entityDistribution;

import java.util.ArrayList;

import fr.giusti.onetapadventure.gameObject.entities.Entity;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;

/**
 * Created by jgiusti on 06/10/2016.
 */

public class EntitySpawnerBuilder {
    private final EntitySpawner spawner;

    public EntitySpawnerBuilder(eEntityDistributionMode distibutionMode, eConditions conditionType, int conditionGoalValue, int initialProgressValue) {
        this.spawner = new EntitySpawner(distibutionMode, conditionType, conditionGoalValue, initialProgressValue);
    }

    public EntitySpawnerBuilder setEntityList(ArrayList<Entity> entityList) {
        if (entityList == null || entityList.isEmpty()) {
            setUseSharedList();
        }

        spawner.setEntityList(entityList);
        spawner.setUseSharedMobList(false);
        return this;
    }

    public EntitySpawnerBuilder setUseSharedList() {
        spawner.setUseSharedMobList(true);
        return this;
    }

    public EntitySpawnerBuilder setSpawnerInfinit() {
        spawner.setInfinitePop(true);
        return this;
    }

    public EntitySpawnerBuilder setSpawnerLimited() {
        spawner.setInfinitePop(false);
        return this;
    }

    public EntitySpawnerBuilder setSpawnGroupSize(int size) {
        spawner.setGroupeSize(size);
        return this;
    }


}
