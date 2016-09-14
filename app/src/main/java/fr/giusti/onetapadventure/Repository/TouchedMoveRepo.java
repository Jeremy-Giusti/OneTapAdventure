package fr.giusti.onetapadventure.repository;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.gameObject.entities.Particule;
import fr.giusti.onetapadventure.gameObject.moves.TouchedMove;
import fr.giusti.onetapadventure.repository.entities.ParticuleRepo;

/**
 * Created by giusti on 30/03/2015.
 */
public class TouchedMoveRepo {

    public static final String DEFAULT_MOVE = "touched";
    public static final String BLEED = "bleed";
    public static final String HEAL = "heal";
    public static final String BAIT = "bait";
    public static final String MOB_AWAY_MOVE = "mob_away";


    public static TouchedMove default_touched_move = new TouchedMove() {
        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, Point touchPoint) {
            int mobHealth = currentMob.getHealth();
            if (mobHealth > 1) {
                mobHealth--;
                currentMob.setHealth(mobHealth);
                currentMob.setState(GameMob.eMobState.HURT);
            } else {
                currentMob.setHealth(0);
                currentMob.setState(GameMob.eMobState.DYING);
            }
            currentMob.setAnimationState(0);
        }

        @Override
        public String getId() {
            return DEFAULT_MOVE;
        }
    };
    public static TouchedMove bleedMove = new TouchedMove() {
        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, Point touchPoint) {
            int mobHealth = currentMob.getHealth();
            if (mobHealth > 1) {
                mobHealth--;
                currentMob.setHealth(mobHealth);
                currentMob.setState(GameMob.eMobState.HURT);
            } else {
                currentMob.setHealth(0);
                currentMob.setState(GameMob.eMobState.DYING);
            }
            currentMob.setAnimationState(0);
            board.getmParticules().add(new ParticuleRepo().generateOrGetCustomParticule(ParticuleRepo.BLOOD_PARTICULE, touchPoint.x, touchPoint.y,(int) currentMob.getWidth(),(int) currentMob.getHeight(), false, new PointF[]{currentMob.getCurrentMove()}));

        }

        @Override
        public String getId() {
            return BLEED;
        }
    };
    public static TouchedMove healMove = new TouchedMove() {
        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, Point touchPoint) {
            int mobHealth = currentMob.getHealth();
            if (mobHealth < 9) {//9 de vie max
                currentMob.setHealth(mobHealth + 1);
            }
            currentMob.setState(GameMob.eMobState.SPE1);
            currentMob.setAnimationState(0);
        }

        @Override
        public String getId() {
            return HEAL;
        }
    };

    public static TouchedMove baitMove = new TouchedMove() {
        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, Point touchPoint) {
            int mobHealth = currentMob.getHealth();
            if (mobHealth > 1) {
                mobHealth--;
                currentMob.setHealth(mobHealth);
                currentMob.setState(GameMob.eMobState.HURT);
                //creer des leurres
                int particuleWidth =(int) currentMob.getWidth() * 2;
                int particuleHeight =(int) currentMob.getHeight() * 2;
                int particuleX = currentMob.getPositionX();
                int particuleY = currentMob.getPositionY();

                PointF[] mobMovePatern = currentMob.getMovePattern();



                //Change la direction du mob
                currentMob.setCurrentMove(0);
                switch ((int) (Math.random() * 4)) {
                    case 1:
                        currentMob.setxAlteration(-1);
                        currentMob.setyAlteration(1);
                        break;
                    case 2:
                        currentMob.setxAlteration(1);
                        currentMob.setyAlteration(-1);
                        break;
                    case 3:
                        currentMob.setxAlteration(-1);
                        currentMob.setyAlteration(-1);
                        break;
                    default:
                        currentMob.setxAlteration(1);
                        currentMob.setyAlteration(1);
                        break;
                }
                Particule particule1 = new ParticuleRepo().generateOrGetCustomParticule(ParticuleRepo.SMOKE_PARTICULE,
                        particuleX,
                        particuleY,
                        particuleWidth,
                        particuleHeight,
                        false,
                        null);
                board.addParticule(particule1);

            } else {
                currentMob.setHealth(0);
                currentMob.setState(GameMob.eMobState.DYING);
            }
            currentMob.setAnimationState(0);
        }

        @Override
        public String getId() {
            return BAIT;
        }
    };


    public static TouchedMove mobAwayMove = new TouchedMove() {
        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, Point touchPoint) {
           board.onMobAway(currentMob);
        }

        @Override
        public String getId() {
            return MOB_AWAY_MOVE;
        }
    };


    private final static HashMap<String, TouchedMove> touchedMoveList;

    static {
        touchedMoveList = new HashMap<String, TouchedMove>();
        touchedMoveList.put(baitMove.getId(), baitMove);
        touchedMoveList.put(bleedMove.getId(), bleedMove);
        touchedMoveList.put(healMove.getId(), healMove);
        touchedMoveList.put(mobAwayMove.getId(), mobAwayMove);
        touchedMoveList.put(default_touched_move.getId(), default_touched_move);
    }


    public TouchedMove getMoveById(String id) {
        return TouchedMoveRepo.touchedMoveList.get(id);
    }

    public ArrayList<String> getMoveIdList() {
        return new ArrayList<String>(touchedMoveList.keySet());
    }


}
