package fr.giusti.onetapadventure.commons;

import android.content.Context;

import java.io.File;

public class Constants {


    public final static int SPRITESHEETWIDTH = 3;
    public final static int SPRITESHEETHEIGHT = 8;
    public static final int FRAME_PER_SEC = 50;
    public static final int NB_FRAME_ON_ANIMATION = SPRITESHEETWIDTH;
    public static final int FRAME_DURATION = FRAME_PER_SEC/10;
    public static final int COMPLETE_ANIMATION_DURATION = (NB_FRAME_ON_ANIMATION * FRAME_DURATION) - 1;
    public final static int PARTICULE_NB_FRAME_ON_ANIMATION = 6;
    public final static int PARTICULE_COMPLETE_ANIMATION_DURATION = PARTICULE_NB_FRAME_ON_ANIMATION * FRAME_DURATION;
    public static final int TOUCH_STROKE = 25;
    public static final int DEFAULT_GAME_HEIGHT = 512;
    public static final int DEFAULT_GAME_WIDTH = 512;
    public static final String SPRITE_REPO_FOLDER_NAME = "SpriteFolder/";


    public static String getSpriteRepoFolder(Context context) {
        return new File(context.getFilesDir(), SPRITE_REPO_FOLDER_NAME).getAbsolutePath();
    }

    public static String getLevelId(int world,int lvl){
        return ""+world+"x"+lvl;
    }


}
