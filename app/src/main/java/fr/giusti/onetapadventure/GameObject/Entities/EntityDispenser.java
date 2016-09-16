package fr.giusti.onetapadventure.gameObject.entities;

import android.util.Pair;

import java.util.ArrayList;

import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.Entity;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;

/**
 * Created by jérémy on 08/09/2016.
 */
public abstract class EntityDispenser {
    protected static final int UPDATE_FREQUENCY = Constants.FRAME_PER_SEC / 2;
    protected long tickCount = 0;
    protected ArrayList<Entity> initList = new ArrayList<>();


    public EntityDispenser(ArrayList<Entity> initList, Pair<Integer, GameMob>... mobs) {
        this.initList = initList;
        for (Pair<Integer, GameMob> indexedMob : mobs) {
            addMobToList(indexedMob.second, indexedMob.first);
        }
    }

    public EntityDispenser(ArrayList<Entity> initList, ArrayList<Pair<Integer, GameMob>> mobs) {
        this.initList = initList;
        for (Pair<Integer, GameMob> indexedMob : mobs) {
            addMobToList(indexedMob.second, indexedMob.first);
        }
    }

    public ArrayList<Entity> getInitialList() {
        return initList;
    }

    //index can serve as a probability of pop/time of pop/order of pop/...
    protected abstract void addMobToList(GameMob mob, int index);


    public void onTick(GameBoard board) {
        tickCount++;
        if (tickCount % UPDATE_FREQUENCY == 0) {
            updateMobs(board);
        }

    }

    protected abstract void updateMobs(GameBoard board);


    public abstract void resize(float ratio);
}
