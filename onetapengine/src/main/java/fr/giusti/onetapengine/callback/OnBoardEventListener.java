package fr.giusti.onetapengine.callback;

import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.rules.eConditions;

public interface OnBoardEventListener {

    void firstUpdate();

    void onMobEvent(eConditions reason, GameMob mob);

    void onMobCountChange(int count);

    void onScoreChange(int score);

    void onTimeProgress(long progress);
}
