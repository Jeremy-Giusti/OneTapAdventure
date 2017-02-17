package fr.giusti.onetapadventure.repository.DB.persister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fr.giusti.onetapadventure.repository.DB.model.MobDB;

/**
 * Created by giusti on 17/03/2015.
 */
public class MobPersister extends AbstractPersister {

    private static final String TAG = MobPersister.class.getName();

    public MobPersister(Context context) {
        super(context);
    }

    /**
     * insert the mob in the BD
     *
     * @param mob
     * @return
     */
    public boolean saveMob(MobDB mob) {
        final SQLiteDatabase database = this.dataBaseHelper.getWritableDatabase();
        try {
            final ContentValues values = new ContentValues();

            values.put(MobDB.FeedEntry.COLUMN_NAME_MOB_ID, mob.getId());
            if (!TextUtils.isEmpty(mob.getBoardId())) {
                values.put(MobDB.FeedEntry.COLUMN_NAME_BOARD_ID, mob.getBoardId());
            }
            values.put(MobDB.FeedEntry.COLUMN_NAME_SPEMOVE_ID, mob.getSpecialMoveId());
            values.put(MobDB.FeedEntry.COLUMN_NAME_TOUCHMOVE_ID, mob.getTouchedMoveId());


            values.put(MobDB.FeedEntry.COLUMN_HEALTH, mob.getHealth());

            values.put(MobDB.FeedEntry.COLUMN_HEIGHT, mob.getHeight());
            values.put(MobDB.FeedEntry.COLUMN_WEIGHT, mob.getWidth());

            values.put(MobDB.FeedEntry.COLUMN_POS_X, mob.getPosX());
            values.put(MobDB.FeedEntry.COLUMN_POS_Y, mob.getPosY());

            values.put(MobDB.FeedEntry.COLUMN_SPRITE_URL, mob.getSpriteSheetUrl());

            // Insert the new row, returning the primary key value of the new row
            final long newRowId = database.insert(MobDB.FeedEntry.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Log.e(TAG, "mob saving failed: " + mob.getId());
                return false;
            } else {
                Log.i(TAG, "mob saved:" + mob.getId());
            }

        } finally {
            database.close();
        }
        return true;
    }

    /**
     * @param boardId
     * @return a liste of path found for a mobId
     */
    public List<MobDB> loadMobForBoard(String boardId) {
        final List<MobDB> mobFoundList = new ArrayList<MobDB>();

        final SQLiteDatabase database = this.dataBaseHelper.getReadableDatabase();

        final String selection = MobDB.FeedEntry.COLUMN_NAME_BOARD_ID + " LIKE ?";
        final String[] selectionArgs = {boardId};

        final Cursor cursor = database.query(MobDB.FeedEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);


        try {
            if (cursor.getCount() <= 0) {
                Log.d(TAG, "No mob found for = " + boardId);
            } else {

                while (cursor.moveToNext()) {
                    final MobDB mobFound = new MobDB();

                    mobFound.setId(cursor.getString(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_NAME_MOB_ID)));
                    mobFound.setSpecialMoveId(cursor.getString(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_NAME_SPEMOVE_ID)));
                    mobFound.setTouchedMoveId(cursor.getString(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_NAME_TOUCHMOVE_ID)));


                    mobFound.setHealth(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_HEALTH)));

                    mobFound.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_HEIGHT)));
                    mobFound.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_WEIGHT)));

                    mobFound.setPosX(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_POS_X)));
                    mobFound.setPosY(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_POS_Y)));

                    mobFound.setSpriteSheetUrl(cursor.getString(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_SPRITE_URL)));

                    mobFound.setBoardId(boardId);
                    mobFoundList.add(mobFound);
                }
            }
        } finally {
            cursor.close();
            database.close();
        }

        return mobFoundList;
    }

    /**
     * @param mobId
     * @return
     */
    public MobDB loadMobFromId(String mobId) {
        MobDB mob = null;

        final SQLiteDatabase database = this.dataBaseHelper.getReadableDatabase();

        final String selection = MobDB.FeedEntry.COLUMN_NAME_MOB_ID + " LIKE ?";
        final String[] selectionArgs = {mobId};

        final Cursor cursor = database.query(MobDB.FeedEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);


        try {
            if (cursor.getCount() <= 0 || cursor.getCount() > 1) {
                Log.d(TAG, "no or to much mob for = " + mobId);
            } else {

                cursor.moveToFirst();
                mob = new MobDB();

                mob.setBoardId(cursor.getString(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_NAME_BOARD_ID)));

                mob.setHealth(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_HEALTH)));
                mob.setSpecialMoveId(cursor.getString(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_NAME_SPEMOVE_ID)));
                mob.setTouchedMoveId(cursor.getString(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_NAME_TOUCHMOVE_ID)));



                mob.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_HEIGHT)));
                mob.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_WEIGHT)));

                mob.setPosX(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_POS_X)));
                mob.setPosY(cursor.getInt(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_POS_Y)));

                mob.setSpriteSheetUrl(cursor.getString(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_SPRITE_URL)));

                mob.setId(mobId);

            }
        } finally {
            cursor.close();
            database.close();
        }

        return mob;
    }

    public ArrayList<String> getAllMobsId(){
        ArrayList<String> result = new ArrayList<String>();
        final SQLiteDatabase database = this.dataBaseHelper.getReadableDatabase();

        final Cursor cursor = database.query(MobDB.FeedEntry.TABLE_NAME, new String[]{MobDB.FeedEntry.COLUMN_NAME_MOB_ID}, null, null, null, null, null);

        while (cursor.moveToNext()){
            result.add(cursor.getString(cursor.getColumnIndexOrThrow(MobDB.FeedEntry.COLUMN_NAME_MOB_ID)));
        }

        return result;
    }
}
