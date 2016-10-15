package fr.giusti.onetapadventure.repository.DB.model;

import android.provider.BaseColumns;

/**
 * Created by giusti on 16/03/2015.
 */
public class MobDB {

    private String id;
    private String boardId;
    private String specialMoveId;
    private String touchedMoveId;
    private int health;
    private int posX;
    private int posY;
    private int height;
    private int width;
    private String spriteSheetUrl;

    public MobDB() {
        super();
    }


    public MobDB(String id, String boardId, String specialMoveId, String touchedMoveId, int health, int posX, int posY, int height, int width, String spriteSheetUrl) {
        this.id = id;
        this.boardId = boardId;
        this.specialMoveId = specialMoveId;
        this.touchedMoveId = touchedMoveId;
        this.health = health;
        this.posX = posX;
        this.posY = posY;
        this.height = height;
        this.width = width;
        this.spriteSheetUrl = spriteSheetUrl;
    }


    public String getId() {
        return id;
    }

    public String getBoardId() {
        return boardId;
    }

    public String getSpecialMoveId() {
        return specialMoveId;
    }

    public void setSpecialMoveId(String specialMoveId) {
        this.specialMoveId = specialMoveId;
    }

    public String getTouchedMoveId() {
        return touchedMoveId;
    }

    public void setTouchedMoveId(String touchedMoveId) {
        this.touchedMoveId = touchedMoveId;
    }

    public int getHealth() {
        return health;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getSpriteSheetUrl() {
        return spriteSheetUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setSpriteSheetUrl(String spriteSheetUrl) {
        this.spriteSheetUrl = spriteSheetUrl;
    }

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "mobs";
        public static final String COLUMN_NAME_MOB_ID = "id";
        public static final String COLUMN_NAME_BOARD_ID = "boardId";
        public static final String COLUMN_NAME_SPEMOVE_ID = "speMoveId";
        public static final String COLUMN_NAME_TOUCHMOVE_ID = "touchMoveId";
        public static final String COLUMN_HEALTH = "health";
        public static final String COLUMN_POS_X = "posX";
        public static final String COLUMN_POS_Y = "posY";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WEIGHT = "width";
        public static final String COLUMN_SPRITE_URL = "spriteSheetUrl";

        public static final String CREATE_TABLE = "CREATE TABLE " + MobDB.FeedEntry.TABLE_NAME + " (" +
                MobDB.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MobDB.FeedEntry.COLUMN_NAME_MOB_ID + " TEXT UNIQUE , " +
                MobDB.FeedEntry.COLUMN_NAME_BOARD_ID + " TEXT , " +
                MobDB.FeedEntry.COLUMN_NAME_SPEMOVE_ID + " TEXT , " +
                MobDB.FeedEntry.COLUMN_NAME_TOUCHMOVE_ID + " TEXT , " +
                MobDB.FeedEntry.COLUMN_HEALTH + " INTEGER , " +
                MobDB.FeedEntry.COLUMN_WEIGHT + " INTEGER , " +
                MobDB.FeedEntry.COLUMN_HEIGHT + " INTEGER , " +
                MobDB.FeedEntry.COLUMN_POS_X + " INTEGER , " +
                MobDB.FeedEntry.COLUMN_POS_Y + " INTEGER , " +
                MobDB.FeedEntry.COLUMN_SPRITE_URL + " TEXT )";

        /**
         * Script to delete the table.
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    }
}
