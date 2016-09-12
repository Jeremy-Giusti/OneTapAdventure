package fr.giusti.onetapadventure.gameObject.moves;

import android.graphics.Point;

import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;

/**
 * Created by giusti on 30/03/2015.
 */
public interface TouchedMove {
    public void doTouchedMove(GameBoard board, GameMob currentMob, Point touchPoint);
    public String getId();
}
