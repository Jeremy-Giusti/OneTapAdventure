package fr.giusti.onetapadventure.repository.DB.persister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import fr.giusti.onetapadventure.repository.DB.model.PathDB;

/**
 * Created by giusti on 17/03/2015.
 */
public class PathPersister extends AbstractPersister {


    private static final String TAG = PathPersister.class.getName();

    public PathPersister(Context context) {
        super(context);
    }

    /**
     * insert the path in the BD
     *
     * @param path
     * @return
     */
    public boolean savePath(PathDB path) {
        final SQLiteDatabase database = this.dataBaseHelper.getWritableDatabase();
        try {
            final ContentValues values = new ContentValues();

            values.put(PathDB.FeedEntry.COLUMN_NAME_PATH_ID, path.getId());
            if (!TextUtils.isEmpty(path.getMobId())) {
                values.put(PathDB.FeedEntry.COLUMN_NAME_MOB_ID, path.getMobId());
            }
            values.put(PathDB.FeedEntry.COLUMN_PATH, path.getPathAsBytes());


            // Insert the new row, returning the primary key value of the new row
            final long newRowId = database.insert(PathDB.FeedEntry.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Log.e(PathPersister.TAG, "path saving failed: " + path.getId());
                return false;
            } else {
                Log.i(PathPersister.TAG, "path saved " + path.getId());
            }

        } finally {
            database.close();
        }
        return true;
    }

    /**
     * @param mobId
     * @return the path found for a mobId
     */
    public PathDB loadPathForMob(String mobId) {
        PathDB pathFound = new PathDB();

        final SQLiteDatabase database = this.dataBaseHelper.getReadableDatabase();

        final String selection = PathDB.FeedEntry.COLUMN_NAME_MOB_ID + " LIKE ?";
        final String[] selectionArgs = {mobId};

        final Cursor cursor = database.query(PathDB.FeedEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);


        try {
            if (cursor.getCount() <= 0 || cursor.getCount() > 1) {
                Log.d(TAG, "no or to much path for mob = " + mobId);
                pathFound = null;
            } else {

                cursor.moveToFirst();
                pathFound.setPathFromBytes(cursor.getBlob(cursor.getColumnIndexOrThrow(PathDB.FeedEntry.COLUMN_PATH)));
                pathFound.setId(cursor.getString(cursor.getColumnIndexOrThrow(PathDB.FeedEntry.COLUMN_NAME_PATH_ID)));
                pathFound.setMobId(mobId);

            }
        } finally {
            cursor.close();
            database.close();
        }

        return pathFound;
    }

    /**
     * @param pathId
     * @return a
     */
    public PathDB loadPathFromId(String pathId) {
        PathDB pathFound = null;

        final SQLiteDatabase database = this.dataBaseHelper.getReadableDatabase();

        final String selection = PathDB.FeedEntry.COLUMN_NAME_PATH_ID + " LIKE ?";
        final String[] selectionArgs = {pathId};

        final Cursor cursor = database.query(PathDB.FeedEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);


        try {
            if (cursor.getCount() <= 0 || cursor.getCount() > 1) {
                Log.d(TAG, "no or to much path for = " + pathId);
                pathFound = null;
            } else {

                cursor.moveToFirst();
                pathFound = new PathDB();
                pathFound.setPathFromBytes(cursor.getBlob(cursor.getColumnIndexOrThrow(PathDB.FeedEntry.COLUMN_PATH)));
                pathFound.setMobId(cursor.getString(cursor.getColumnIndexOrThrow(PathDB.FeedEntry.COLUMN_NAME_MOB_ID)));
                pathFound.setId(pathId);

            }
        } finally {
            cursor.close();
            database.close();
        }

        return pathFound;
    }


}
