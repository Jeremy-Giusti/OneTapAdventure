package fr.giusti.onetapengine.callback;

import fr.giusti.onetapengine.rules.eConditionType;

/**
 * Created by jérémy on 09/09/2016.
 */
public interface OnGameEndListener{
    void  onGameEnd(eConditionType gameResult, String gameId, int score);
}
