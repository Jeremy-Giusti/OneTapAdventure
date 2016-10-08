package fr.giusti.onetapadventure.commons;

import fr.giusti.onetapadventure.R;

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

    public static final String getLevelId(int world, int lvl) {
        return "" + world + "x" + lvl;
    }



}
