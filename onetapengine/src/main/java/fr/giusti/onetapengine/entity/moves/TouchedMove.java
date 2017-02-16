package fr.giusti.onetapengine.entity.moves;

import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapengine.entity.GameMob;

/**
 * Created by giusti on 30/03/2015.
 */
public interface TouchedMove {
    public void doTouchedMove(GameBoard board, GameMob currentMob, int damage);
    public String getId();
}
