package fr.giusti.onetapengine.callback;


import fr.giusti.onetapengine.entity.distribution.EntitySpawner;
import fr.giusti.onetapengine.entity.distribution.eEntityDistributionMode;

/**
 * Created by jgiusti on 26/09/2016.
 */

public interface SpawnerListener {
    void onSpawnerEmpty(EntitySpawner popper);
    void onSpawnRequested(boolean infinitePop, eEntityDistributionMode distribMode, int groupSize);
}
