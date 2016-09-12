package fr.giusti.onetapadventure.repository.entities;

import android.content.Context;

import java.util.ArrayList;

import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.commons.Constants;

/**
 * Created by jérémy on 10/09/2016.
 */
public class EntityDispenserRepo {

    public static EntityDispenser getLvl1_1MobDispenser(Context context) {

        return new EntityDispenser(MobRepo.getLvl1x1InitList(context),MobRepo.getLvl1x1BackupList()) {

            private final int mobPopTickFrequency = Constants.FRAME_PER_SEC * 2;
            private final int minMobOnBoard = 2;
            private long currentTick = 0;
            private ArrayList<GameMob> tier1Mobs;
            private ArrayList<GameMob> tier2Mobs;
            private ArrayList<GameMob> tier3Mobs;
            private GameMob lastMob;
            boolean lastMobPoped = false;


            @Override
            public void addMobToList(GameMob mob, int index) {
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
                if ((currentTick % mobPopTickFrequency == 0 || board.getMobs().size() < minMobOnBoard) && !lastMobPoped) {
                    board.addMob(getConcernedMob());
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
                } else {
                    concernedMob = lastMob;
                    lastMobPoped = true;
                }
                return concernedMob;
            }
        };
    }
}
