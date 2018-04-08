package fr.giusti.onetapengine.callback;


import fr.giusti.onetapengine.entity.distribution.EntitySpawner;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerLong;
import fr.giusti.onetapengine.entity.distribution.eEntityDistributionMode;

/**
 * Created by jgiusti on 26/09/2016.
 */

public interface SpawnerListener {
    void onSpawnerEmpty(EntitySpawner popper);

    /**
     * used when we desire to spawn entity from a shared list
     * @param infinitePop true if we don't want to remove the spawned entity from the shared pool
     * @param distribMode {@link eEntityDistributionMode}
     * @param groupSize if distibution mode is a grouped one, this is the size of the desired group
     */
    void onSpawnRequested(boolean infinitePop, eEntityDistributionMode distribMode, int groupSize);
}
