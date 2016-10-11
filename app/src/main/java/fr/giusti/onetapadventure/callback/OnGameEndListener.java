package fr.giusti.onetapadventure.callback;

import fr.giusti.onetapadventure.gameObject.rules.eConditionType;

/**
 * Created by jérémy on 09/09/2016.
 */
public interface OnGameEndListener{
    void  onGameEnd(eConditionType gameResult, String gameId, int score);
}
