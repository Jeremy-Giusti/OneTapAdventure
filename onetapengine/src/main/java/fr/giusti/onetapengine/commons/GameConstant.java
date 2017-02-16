package fr.giusti.onetapengine.commons;

import fr.giusti.onetapengine.R;


/**
 * Created by jérémy on 22/09/2016.
 */

public class GameConstant {


    public static final String LEVEL_NAME = "level name";
    public static final int AREA_1_LVL_COUNT = 10;
    public static final int AREA_1_BACKGROUND = R.drawable.area1_bkg;
    public static final String BACKGROUND = "background";
    public static final String LVL_COUNT = "lvl count";
    public static final String AREA = "area";
    public static final int DEFAULT_MOB_SIZE = 48;
    public static final int SHORT_TP_MAX_RANGE = 144;
    public static final int SHORT_TP_MIN_RANGE = 72;
    public static final String HOLE_FRONT_SPRITE_ID = "hole front";
    public static final int TOUCH_DAMAGE = 10;
    public static final int TOUCH_STROKE = 25;

    public static final String getLevelId(int world, int lvl) {
        return "" + world + "x" + lvl;
    }


}
