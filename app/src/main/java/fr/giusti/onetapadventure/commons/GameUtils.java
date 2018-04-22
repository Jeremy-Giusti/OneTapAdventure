package fr.giusti.onetapadventure.commons;

import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.commons.GameConstant;

/**
 * Created by giusti on 22/04/2018.
 */

public class GameUtils {

    public static int getSpeedInPxPerTic(int speedPXPerSeconds){
        return (int)(speedPXPerSeconds/(double) Constants.FRAME_PER_SEC);
    }
}
