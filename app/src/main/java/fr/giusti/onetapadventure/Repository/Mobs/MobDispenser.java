package fr.giusti.onetapadventure.Repository.Mobs;

import android.util.Pair;

import java.util.concurrent.CopyOnWriteArrayList;

import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.GameObject.Entities.GameMob;
import fr.giusti.onetapadventure.commons.Constants;

/**
 * Created by jérémy on 08/09/2016.
 */
public abstract class MobDispenser {
    protected static final int UPDATE_FREQUENCY = Constants.FRAME_PER_SEC/2;
    private int tickCount = 0;
    protected CopyOnWriteArrayList<GameMob> initList = new CopyOnWriteArrayList<>();


    public MobDispenser(CopyOnWriteArrayList<GameMob> initList, Pair<Integer, GameMob>... mobs) {
        this.initList = initList;
        for (Pair<Integer, GameMob> indexedMob : mobs) {
            addMobToList(indexedMob.second, indexedMob.first);
        }
    }

    public CopyOnWriteArrayList<GameMob> getInitialList() {
        return initList;
    }

    //index can serve as a probability of pop/time of pop/order of pop/...
    protected abstract void addMobToList(GameMob mob, int index);


    public void onTick(GameBoard board) {
        if (tickCount % 10 == 0) {
            tickCount = 0;
            updateMobs(board);
        }
    }

    protected abstract void updateMobs(GameBoard board);


}
