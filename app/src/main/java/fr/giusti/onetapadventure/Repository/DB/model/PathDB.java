package fr.giusti.onetapadventure.repository.DB.model;

import android.provider.BaseColumns;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

/**
 * Created by giusti on 16/03/2015.
 */
public class PathDB {

    private String id;
    private String MobId;
    private PointDB[] path;


    public PathDB(String id, String mobId, android.graphics.Point[] path) {
        this.id = id;
        MobId = mobId;

        this.path = new PointDB[path.length];
        for (int i = 0; i < path.length; i++) {
            this.path[i] = new PointDB(path[i].x, path[i].y);
        }
    }

    public PathDB() {
    }

    public String getId() {
        return id;
    }

    public String getMobId() {
        return MobId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMobId(String mobId) {
        MobId = mobId;
    }

    public PointDB[] getPath() {
        return path;
    }

    public void setPath(PointDB[] path) {
        this.path = path;
    }

    /**
     * return the path as a byte array
     *
     * @return
     */
    public byte[] getPathAsBytes() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out = null;
        byte[] bytePath = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(this.path);
            bytePath = bos.toByteArray();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                bos.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            return bytePath;
        }
    }

    public void setPathFromBytes(byte[] bytePath) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytePath);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            this.path = (PointDB[]) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
            } catch (IOException ex) {
                // ignore close exception
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
    }

    public static class PointDB implements Serializable {
        public float x;
        public float y;

        public PointDB(float x, float y) {
            this.x = x;
            this.y = y;
        }


    }

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "paths";
        public static final String COLUMN_NAME_PATH_ID = "id";
        public static final String COLUMN_NAME_MOB_ID = "pobId";
        public static final String COLUMN_PATH = "path";

        public static final String CREATE_TABLE = "CREATE TABLE " + PathDB.FeedEntry.TABLE_NAME + " (" +
                PathDB.FeedEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PathDB.FeedEntry.COLUMN_NAME_PATH_ID + " TEXT UNIQUE , " +
                PathDB.FeedEntry.COLUMN_NAME_MOB_ID + " TEXT , " +
                PathDB.FeedEntry.COLUMN_PATH + " BLOB )";

        /**
         * Script to delete the table.
         */
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;
    }

}
