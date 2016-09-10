package fr.giusti.onetapadventure.callback;

import fr.giusti.onetapadventure.GameObject.Entities.GameMob;
import fr.giusti.onetapadventure.GameObject.Rules.eConditions;

public interface OnBoardEventListener {

    void firstUpdate();

    void onMobCountChange(int count, eConditions reason, GameMob mob);

    void onScorePlus(int add);

    void onScoreMinus(int remove);
}
