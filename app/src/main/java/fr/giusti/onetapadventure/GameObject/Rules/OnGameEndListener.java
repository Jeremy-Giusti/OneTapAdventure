package fr.giusti.onetapadventure.gameObject.rules;

/**
 * Created by jérémy on 09/09/2016.
 */
public interface OnGameEndListener{
    void  onGameEnd(eConditionType gameResult,String gameId,int score);
}
