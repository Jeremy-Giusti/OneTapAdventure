package fr.giusti.onetapengine.callback;

import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.rules.eConditions;

public interface OnBoardEventListener {

    void firstUpdate();

    void onMobCountChange(int count, eConditions reason, GameMob mob);

    void onScorePlus(int add);

    void onScoreMinus(int remove);

    void onTimeProgress(int progress);
}
