package fr.giusti.onetapadventure.GameObject.moves;

import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.GameObject.Entities.GameMob;

/**
 * Created by giusti on 20/03/2015.
 */
public interface SpecialMove {
    public void doSpecialMove(GameBoard board, GameMob currentMob);
    public String getId();
}
