package fr.giusti.onetapadventure.Repository.Mobs;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.GameObject.GameMob;

/**
 * Created by jérémy on 08/09/2016.
 */
public abstract class MobDispenser {
    private static final int UPDATE_FREQUENCY = 10;
    private int tickCount = 0;


    //index can serve as a probability of pop/time of pop/order of pop/...
    public abstract void addMobToList(GameMob mob, int index);

    public abstract void addMobToList(GameMob mob,int index,boolean randomPos);

    public abstract void addMultipleMobToList(GameMob mob,int index,int number);

    public abstract void addMultipleMobToList(GameMob mob,int index,int number,boolean randomPos);


    public void onTick(GameBoard board){
        if(tickCount%10 == 0){
            tickCount=0;
            updateMobs(board);
        }
    }

    protected abstract void updateMobs(GameBoard board);




}
