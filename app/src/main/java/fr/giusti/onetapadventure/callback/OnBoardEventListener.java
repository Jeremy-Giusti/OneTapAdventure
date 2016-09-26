package fr.giusti.onetapadventure.callback;

import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;

public interface OnBoardEventListener {

    void firstUpdate();

    void onMobCountChange(int count, eConditions reason, GameMob mob);

    void onScorePlus(int add);

    void onScoreMinus(int remove);

    void onTimeProgress(int progress);
}
