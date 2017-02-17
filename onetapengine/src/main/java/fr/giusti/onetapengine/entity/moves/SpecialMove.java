package fr.giusti.onetapengine.entity.moves;

import fr.giusti.onetapengine.GameBoard;
import fr.giusti.onetapengine.entity.GameMob;

/**
 * Created by giusti on 20/03/2015.
 */
public interface SpecialMove {
    public String getId();
    public void doSpecialMove(GameBoard board, GameMob currentMob);
}
