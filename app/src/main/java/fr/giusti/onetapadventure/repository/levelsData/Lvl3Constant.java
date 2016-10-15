package fr.giusti.onetapadventure.repository.levelsData;

import android.graphics.Rect;

/**
 * Created by jérémy on 03/10/2016.
 */

public class Lvl3Constant {
    public static final int MAX_MOB_AWAY = 10;
    public static final int MOB_NB = 100;
    public static final int TIMER_END_VALUE = 60*1000;
    public static final int MOB_SIZE = 48;
    public static final Rect HOLE1_DIMENS = new Rect(448, 192, 572, 320);
    public static final int HOLE_HITBOX_MARGIN  = MOB_SIZE / 2;

    public static final String SUCCESS_SCORE_RULE = "success score rule";
    public static final String DEFEAT_SCORE_RULE = "defeat score rule";
    public static final int SUCCESS_SCORE_RULE_VALUE = 10000;
    public static final int DEFEAT_SCORE_RULE_VALUE = -5000;
}
