package fr.giusti.onetapadventure.repository.entities;

import android.util.Pair;

import java.util.ArrayList;

import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.Entity;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.repository.levelsData.Lvl1Constant;

/**
 * Created by jérémy on 13/09/2016.
 */
public class ThreeTierEntityDispenser extends EntityDispenser {
    private final int mobPopTickFrequency =(int) (Constants.FRAME_PER_SEC * Lvl1Constant.MOB_POP_FREQUENCY_SEC);
    private final int minMobOnBoard = Lvl1Constant.MIN_MOB_ON_SCREEN;
    private long currentTick = 0;
    private ArrayList<GameMob> tier1Mobs;
    private ArrayList<GameMob> tier2Mobs;
    private ArrayList<GameMob> tier3Mobs;
    private GameMob lastMob;
    boolean lastMobPoped = false;

    public ThreeTierEntityDispenser(ArrayList<Entity> initList, Pair<Integer, GameMob>... mobs) {
        super(initList, mobs);
    }

    public ThreeTierEntityDispenser(ArrayList<Entity> initList, ArrayList<Pair<Integer, GameMob>> mobs) {
        super(initList, mobs);
    }


    @Override
    public void addMobToList(GameMob mob, int index) {
        if (tier1Mobs == null) tier1Mobs = new ArrayList<>();
        if (tier2Mobs == null) tier2Mobs = new ArrayList<>();
        if (tier3Mobs == null) tier3Mobs = new ArrayList<>();
        switch (index) {
            case 1:
                tier1Mobs.add(mob);
                break;
            case 2:
                tier2Mobs.add(mob);
                break;
            case 3:
                tier3Mobs.add(mob);
                break;
            default:
                lastMob = mob;
                break;
        }
    }

    @Override
    protected void updateMobs(GameBoard board) {
        currentTick += UPDATE_FREQUENCY;
        if ((/*currentTick % mobPopTickFrequency == 0 ||*/ board.getMobs().size() < minMobOnBoard) && !lastMobPoped) {
            GameMob concernedMob = getConcernedMob();
            if (concernedMob != null) board.onNewMob(concernedMob);
        }
    }


    public GameMob getConcernedMob() {
        GameMob concernedMob = null;

        if (tier1Mobs.size() > 0) {
            concernedMob = tier1Mobs.get(0);
            tier1Mobs.remove(concernedMob);
        } else if (tier2Mobs.size() > 0) {
            concernedMob = tier2Mobs.get(0);
            tier2Mobs.remove(concernedMob);
        } else if (tier3Mobs.size() > 0) {
            concernedMob = tier3Mobs.get(0);
            tier3Mobs.remove(concernedMob);
        } else if (!lastMobPoped) {
            concernedMob = lastMob;
            lastMobPoped = true;
        }
        return concernedMob;
    }

    @Override
    public void resize(float ratio) {
//        for (Entity ent : initList) {
//            ent.resize(ratio);
//        }
        for (GameMob mob : tier1Mobs) {
            mob.resize(ratio);
        }
        for (GameMob mob : tier2Mobs) {
            mob.resize(ratio);
        }
        for (GameMob mob : tier3Mobs) {
            mob.resize(ratio);
        }
    }

}
