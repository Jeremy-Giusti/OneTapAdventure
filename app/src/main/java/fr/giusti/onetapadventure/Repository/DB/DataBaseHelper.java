package fr.giusti.onetapadventure.repository.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.giusti.onetapadventure.repository.DB.model.BoardDB;
import fr.giusti.onetapadventure.repository.DB.model.MobDB;
import fr.giusti.onetapadventure.repository.DB.model.PathDB;

/**
 * Created by giusti on 17/03/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    static final String dbName = "demoDB";
    static final int dbVersion = 1;


    public DataBaseHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DataBaseHelper.dbName, factory, DataBaseHelper.dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(PathDB.FeedEntry.CREATE_TABLE);

        db.execSQL(MobDB.FeedEntry.CREATE_TABLE);

        db.execSQL(BoardDB.FeedEntry.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PathDB.FeedEntry.DELETE_TABLE);
        db.execSQL(PathDB.FeedEntry.DELETE_TABLE);
        db.execSQL(PathDB.FeedEntry.DELETE_TABLE);
    }
}
