package fr.giusti.onetapadventure.GameObject.moves;

import android.graphics.Point;

import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.GameObject.GameMob;

/**
 * Created by giusti on 30/03/2015.
 */
public interface TouchedMove {
    public void doTouchedMove(GameBoard board, GameMob currentMob, Point touchPoint);
    public String getId();
}
