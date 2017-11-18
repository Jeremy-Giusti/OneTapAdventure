package fr.giusti.onetapengine.callback;

import fr.giusti.onetapengine.rules.eRuleResult;

/**
 * Created by jérémy on 09/09/2016.
 */
public interface OnGameEndListener{
    void  onGameEnd(eRuleResult gameResult, String gameId, int score);
}
