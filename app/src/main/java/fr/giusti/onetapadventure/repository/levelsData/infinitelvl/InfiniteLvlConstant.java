package fr.giusti.onetapadventure.repository.levelsData.infinitelvl;

/**
 * Created by jérémy on 09/04/2018.
 */

public class InfiniteLvlConstant {
    public static final String MASTER_RULE = "mob count limit";
    public static final String SCORE_RULE = "score rule";

    public static final int MAX_NUMBER_OF_MOB = 20;
    public static final int DEFAULT_MOB_SIZE = 48;
    public static final int BOARD_WITDH = 1024;
    public static final int BOARD_HEIGHT = 512;


    // ---------------------------- pool 1 ----------------------------//

    public static final int POOL1_MOB_SPEED = 160;
    public static final int POOL1_MOB_SCORE = 10;
    // ------- SPAWNER VALUES
    public static final long LONG_INTEVALE_OF_SPAWN = 1200;
    public static final long SHORT_INTEVALE_OF_SPAWN = 600;
    public static final int MOB_COUNT_FOR_INTERVAL_SELECTION = 15;


    // ---------------------------- pool 2 ----------------------------//

    public static final int POOL2_MOB_SPEED = 200;
    public static final int POOL2_MOB_SCORE = 75;
    // ------- SPAWNER VALUES
    /**
     * value at which the MIN_INTEVALE_OF_SPAWN is used.
     */
    public static final long SCORE_INTERVAL_CAP = 15000;
    public static final long MIN_INTEVALE_OF_SPAWN = 750;
    public static final long MAX_INTERVALE_OF_SPAWN = 1250;
    public static final int MAX_SPAWN_GROUP_SIZE = 6;


}

