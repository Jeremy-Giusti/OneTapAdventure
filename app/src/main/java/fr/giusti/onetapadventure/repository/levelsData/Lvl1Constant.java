package fr.giusti.onetapadventure.repository.levelsData;

import android.graphics.Rect;

/**
 * Created by jgiusti on 15/09/2016.
 */
public class Lvl1Constant {

    public static final int MOB_SIZE = 48;
    public static final int MOB_POP_X = 950;
    public static final int MOB_POP_Y_MAX_VAlUE = 470;
   public static final Rect HOLE1_DIMENS = new Rect(32, 32, 160, 416);
    public static final int HOLE_HITBOX_MARGIN = MOB_SIZE / 2;
    public static final String ESCAPING_MOB_RULE = "escapingMob";
    public static final String LEVEL_END_RULE = "levelEnd1x1";

    public static final int MAX_MOB_AWAY = 5;

    public static final int START_SIMPLE_MOB = 1;
    public static final int MIXED_TIER_MOB_NB = 52;
    public static final int RAPIDE_MOB = 1;
    public static final int END_MOB_WAVE = 6;
    public static final int MOB_TOTAL_NB = START_SIMPLE_MOB+MIXED_TIER_MOB_NB+RAPIDE_MOB+END_MOB_WAVE;


    //

}
