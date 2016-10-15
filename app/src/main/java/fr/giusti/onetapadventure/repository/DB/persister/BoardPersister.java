package fr.giusti.onetapadventure.repository.DB.persister;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import fr.giusti.onetapadventure.repository.DB.model.BoardDB;

/**
 * Created by giusti on 17/03/2015.
 */
public class BoardPersister extends AbstractPersister {

    private static final String TAG = BoardPersister.class.getName();

    public BoardPersister(Context context) {
        super(context);
    }

    /**
     * insert the path in the BD
     *
     * @param board
     * @return
     */
    public boolean saveBoard(BoardDB board) {
        final SQLiteDatabase database = this.dataBaseHelper.getWritableDatabase();
        try {
            final ContentValues values = new ContentValues();

            values.put(BoardDB.FeedEntry.COLUMN_NAME_BOARD_ID, board.getId());
            values.put(BoardDB.FeedEntry.COLUMN_HEIGHT, board.getHeight());
            values.put(BoardDB.FeedEntry.COLUMN_WIDTH, board.getWidth());
            values.put(BoardDB.FeedEntry.COLUMN_BACKGROUND_URL, board.getBackgroundUrl());

            // Insert the new row, returning the primary key value of the new row
            final long newRowId = database.insert(BoardDB.FeedEntry.TABLE_NAME, null, values);

            if (newRowId == -1) {
                Log.e(TAG, "board saving failed: " + board.getId());
                return false;
            } else {
                Log.i(TAG, "board saved: " + board.getId());
            }

        } finally {
            database.close();
        }
        return true;
    }

    /**
     * @param boardId
     * @return a
     */
    public BoardDB loadBoardFromId(String boardId) {
        BoardDB boardFound =null ;

        final SQLiteDatabase database = this.dataBaseHelper.getReadableDatabase();

        final String selection = BoardDB.FeedEntry.COLUMN_NAME_BOARD_ID + " LIKE ?";
        final String[] selectionArgs = {boardId};

        final Cursor cursor = database.query(BoardDB.FeedEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);


        try {
            if (cursor.getCount() <= 0 || cursor.getCount() > 1) {
                Log.d(TAG, "no or to much path for = " + boardId);
            } else {

                cursor.moveToFirst();
                boardFound = new BoardDB();
                boardFound.setId(boardId);
                boardFound.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow(BoardDB.FeedEntry.COLUMN_HEIGHT)));
                boardFound.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow(BoardDB.FeedEntry.COLUMN_WIDTH)));

                boardFound.setBackgroundUrl(cursor.getString(cursor.getColumnIndexOrThrow(BoardDB.FeedEntry.COLUMN_BACKGROUND_URL)));

            }
        } finally {
            cursor.close();
            database.close();
        }

        return boardFound;
    }
}
