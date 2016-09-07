package fr.giusti.onetapadventure.Repository.DB.model;

import android.graphics.Rect;
import android.provider.BaseColumns;

/**
 * Created by giusti on 16/03/2015.
 */
public class BoardDB {

    private String id;
    private String backgroundUrl;
    private int height;
    private int width;
    private int camLeft;
    private int camTop;
    private int camRight;
    private int camBottom;

    public BoardDB() {
    }

    public BoardDB(String id, String backgroundUrl, int height, int width, Rect camBound) {
        this.id = id;
        this.backgroundUrl = backgroundUrl;
        this.height = height;
        this.width = width;
        this.camLeft = camBound.left;
        this.camTop = camBound.top;
        this.camRight = camBound.right;
        this.camBottom = camBound.bottom;

    }

    public  Rect getCamRect() {
        return new Rect(camLeft,camTop,camRight,camBottom);
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

    public int getCamLeft() {
        return camLeft;
    }

    public void setCamLeft(int camLeft) {
        this.camLeft = camLeft;
    }

    public int getCamTop() {
        return camTop;
    }

    public void setCamTop(int camTop) {
        this.camTop = camTop;
    }

    public int getCamRight() {
        return camRight;
    }

    public void setCamRight(int camRight) {
        this.camRight = camRight;
    }

    public int getCamBottom() {
        return camBottom;
    }

    public void setCamBottom(int camBottom) {
        this.camBottom = camBottom;
    }

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "boards";
        public static final String COLUMN_NAME_BOARD_ID = "boardId";
        public static final String COLUMN_BACKGROUND_URL = "backgroundUrl";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WIDTH = "width";

        public static final String COLUMN_CAMLEFT = "camTop";
        public static final String COLUMN_CAMTOP = "camLeft";
        public static final String COLUMN_CAMRIGHT = "camRight";
        public static final String COLUMN_CAMBOTTOM = "camBottom";

        /** used to create the table*/
        public static final String CREATE_TABLE = "CREATE TABLE " + BoardDB.FeedEntry.TABLE_NAME + " (" +
                BoardDB.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                BoardDB.FeedEntry.COLUMN_NAME_BOARD_ID + " TEXT UNIQUE , " +
                BoardDB.FeedEntry.COLUMN_HEIGHT + " INTEGER , " +
                BoardDB.FeedEntry.COLUMN_WIDTH + " INTEGER , " +
                BoardDB.FeedEntry.COLUMN_CAMLEFT + " INTEGER , " +
                BoardDB.FeedEntry.COLUMN_CAMTOP + " INTEGER , " +
                BoardDB.FeedEntry.COLUMN_CAMRIGHT + " INTEGER , " +
                BoardDB.FeedEntry.COLUMN_CAMBOTTOM + " INTEGER , " +
                BoardDB.FeedEntry.COLUMN_BACKGROUND_URL + " TEXT )";

        /** Script to delete the table. */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }
}
