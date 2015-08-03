package fr.giusti.onetapadventure.Repository.DB.model;

import android.provider.BaseColumns;

/**
 * Created by giusti on 16/03/2015.
 */
public class BoardDB {

    private String id;
    private String backgroundUrl;
    private int height;
    private int width;

    public BoardDB() {
    }

    public BoardDB(String id, String backgroundUrl, int height, int width) {
        this.id = id;
        this.backgroundUrl = backgroundUrl;
        this.height = height;
        this.width = width;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "boards";
        public static final String COLUMN_NAME_BOARD_ID = "boardId";
        public static final String COLUMN_BACKGROUND_URL = "backgroundUrl";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WIDTH = "width";

        /** used to create the table*/
        public static final String CREATE_TABLE = "CREATE TABLE " + BoardDB.FeedEntry.TABLE_NAME + " (" +
                BoardDB.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BoardDB.FeedEntry.COLUMN_NAME_BOARD_ID + " TEXT UNIQUE , " +
                BoardDB.FeedEntry.COLUMN_HEIGHT + " INTEGER , " +
                BoardDB.FeedEntry.COLUMN_WIDTH + " INTEGER , " +
                BoardDB.FeedEntry.COLUMN_BACKGROUND_URL + " TEXT )";

        /** Script to delete the table. */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }
}
