package fr.giusti.onetapadventure.Repository;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.GameObject.GameMob;
import fr.giusti.onetapadventure.GameObject.Particule;
import fr.giusti.onetapadventure.GameObject.moves.TouchedMove;

/**
 * Created by giusti on 30/03/2015.
 */
public class TouchedMoveRepo {

    public static final String DEFAULT_MOVE = "touched";
    public static final String BLEED = "bleed";
    public static final String HEAL = "heal";
    public static final String BAIT = "bait";


    public static TouchedMove default_touched_move = new TouchedMove() {
        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, Point touchPoint) {
            int mobHealth = currentMob.getHealth();
            if (mobHealth > 1) {
                mobHealth--;
                currentMob.setHealth(mobHealth);
                currentMob.setState(GameMob.STATE_HURT);
            } else {
                currentMob.setHealth(0);
                currentMob.setState(GameMob.STATE_DYING);
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
                currentMob.setState(GameMob.STATE_HURT);
            } else {
                currentMob.setHealth(0);
                currentMob.setState(GameMob.STATE_DYING);
            }
            currentMob.setAnimationState(0);
            board.getmParticules().add(new ParticuleRepo().generateOrGetCustomParticule(ParticuleRepo.BLOOD_PARTICULE, touchPoint.x, touchPoint.y, currentMob.getWidth(), currentMob.getHeight(), false, new Point[]{currentMob.getCurrentMove()}));

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
            currentMob.setState(GameMob.STATE_SPEMOVE1);
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
                currentMob.setState(GameMob.STATE_HURT);
                //creer des leurres
                int particuleWidth = currentMob.getWidth() * 2;
                int particuleHeight = currentMob.getHeight() * 2;
                int particuleX = currentMob.getPositionX();
                int particuleY = currentMob.getPositionY();

                Point[] mobMovePatern = currentMob.getMovePattern();



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
                currentMob.setState(GameMob.STATE_DYING);
            }
            currentMob.setAnimationState(0);
        }

        @Override
        public String getId() {
            return BAIT;
        }
    };

    private final static HashMap<String, TouchedMove> touchedMoveList;

    static {
        touchedMoveList = new HashMap<String, TouchedMove>();
        touchedMoveList.put(baitMove.getId(), baitMove);
        touchedMoveList.put(bleedMove.getId(), bleedMove);
        touchedMoveList.put(healMove.getId(), healMove);
        touchedMoveList.put(default_touched_move.getId(), default_touched_move);
    }


    public TouchedMove getMoveById(String id) {
        return TouchedMoveRepo.touchedMoveList.get(id);
    }

    public ArrayList<String> getMoveIdList() {
        return new ArrayList<String>(touchedMoveList.keySet());
    }


}
