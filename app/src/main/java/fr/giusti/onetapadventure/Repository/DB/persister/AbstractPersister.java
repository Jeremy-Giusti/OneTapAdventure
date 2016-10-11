package fr.giusti.onetapadventure.repository.DB.persister;

import android.content.Context;

import fr.giusti.onetapadventure.repository.DB.DataBaseHelper;

/**
 * Created by giusti on 17/03/2015.
 */
public class AbstractPersister {

    protected Context context;
    protected DataBaseHelper dataBaseHelper;

    public AbstractPersister(Context context) {
        this.context = context;
        this.dataBaseHelper = new DataBaseHelper(context,null);
    }
}
