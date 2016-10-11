package fr.giusti.onetapadventure.gameObject.moves;

import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;

/**
 * Created by giusti on 20/03/2015.
 */
public interface SpecialMove {
    public String getId();
    public void doSpecialMove(GameBoard board, GameMob currentMob);
}
