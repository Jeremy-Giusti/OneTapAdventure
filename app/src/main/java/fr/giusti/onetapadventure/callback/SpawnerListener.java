package fr.giusti.onetapadventure.callback;

import fr.giusti.onetapadventure.gameObject.entities.entityDistribution.EntitySpawner;
import fr.giusti.onetapadventure.gameObject.entities.entityDistribution.eEntityDistributionMode;

/**
 * Created by jgiusti on 26/09/2016.
 */

public interface SpawnerListener {
    void onSpawnerEmpty(EntitySpawner popper);
    void onSpawnRequested(boolean infinitePop, eEntityDistributionMode distribMode, int groupSize);
}
