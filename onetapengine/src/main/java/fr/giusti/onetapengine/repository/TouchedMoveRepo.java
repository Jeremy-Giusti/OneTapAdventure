package fr.giusti.onetapengine.repository;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import fr.giusti.onetapengine.GameBoard;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.entity.Particule;
import fr.giusti.onetapengine.entity.distribution.ParticuleHolder;
import fr.giusti.onetapengine.entity.moves.TouchedMove;


/**
 * Created by giusti on 30/03/2015.
 */
public class TouchedMoveRepo {

    public static final String DEFAULT_MOVE = "hurt";
    public static final String BLEED = "bleed";
    public static final String TELEPORT = "teleport";
    public static final String HEAL = "heal";
    public static final String BAIT = "bait";
    public static final String DISAPEAR = "disapear";


    public static TouchedMove default_touched_move = new TouchedMove() {

        @Override
        public String getId() {
            return DEFAULT_MOVE;
        }

        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, int damage) {
            currentMob.hurt(damage);
        }

    };
    public static TouchedMove bleedMove = new TouchedMove() {

        @Override
        public String getId() {
            return BLEED;
        }

        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, int damage) {
            currentMob.hurt(damage);
            board.getmParticules().add(ParticuleHolder.getAvailableParticule(ParticuleRepo.BLOOD_PARTICULE, currentMob.getPositionX(), currentMob.getPositionY(), currentMob.getWidth(), currentMob.getHeight(), false, new PointF[]{currentMob.getCurrentMove()}));

        }

    };
    public static TouchedMove healMove = new TouchedMove() {
        @Override
        public String getId() {
            return HEAL;
        }

        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, int damage) {
            int mobHealth = currentMob.getHealth();
            if (mobHealth < (9 * GameConstant.BASE_DAMAGE)) {//9 de vie max
                currentMob.setHealth(mobHealth + GameConstant.BASE_DAMAGE);
            }
            currentMob.setState(GameMob.eMobState.HURT);
            currentMob.setAnimationState(0);
        }


    };

    public static TouchedMove baitMove = new TouchedMove() {
        @Override
        public String getId() {
            return BAIT;
        }

        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, int damage) {
            if (!currentMob.hurt(damage)) {
                //creer des leurres
                int particuleWidth = (int) currentMob.getWidth() * 2;
                int particuleHeight = (int) currentMob.getHeight() * 2;
                int particuleX = currentMob.getPositionX();
                int particuleY = currentMob.getPositionY();

                PointF[] mobMovePatern = currentMob.getMovePattern();


                //Change la direction du mob
                currentMob.setCurrentMove(0);

                //just  inverse direction
                currentMob.setxAlteration(currentMob.getxAlteration() * -1);
                currentMob.setyAlteration(currentMob.getyAlteration() * -1);

                Particule particule1 = ParticuleHolder.getAvailableParticule(ParticuleRepo.SMOKE_PARTICULE,
                        particuleX,
                        particuleY,
                        particuleWidth,
                        particuleHeight,
                        false,
                        null);
                board.addParticule(particule1);

            }

        }


    };


    public static TouchedMove mobAwayMove = new TouchedMove() {
        @Override
        public String getId() {
            return DISAPEAR;
        }

        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, int damage) {
            if (GameMob.eMobState.DYING != currentMob.getState())
                //board.onMobAway(currentMob);
                currentMob.setDisappering();
        }


    };

    public static TouchedMove tpMove = new TouchedMove() {
        @Override
        public String getId() {
            return TELEPORT;
        }

        @Override
        public void doTouchedMove(GameBoard board, GameMob currentMob, int damage) {
            if (GameMob.eMobState.DYING != currentMob.getState()) {

                int mobHealth = currentMob.getHealth();
                if (mobHealth > damage) {
                    mobHealth -= damage;
                    currentMob.setHealth(mobHealth);
                    currentMob.setState(GameMob.eMobState.HURT);
                } else {
                    currentMob.setHealth(0);
                    currentMob.setState(GameMob.eMobState.DYING);
                    return;
                }

                currentMob.setAnimationState(0);

                int xDIrection = (Math.random() < 0.5) ? 1 : -1;
                int yDIrection = (Math.random() < 0.5) ? 1 : -1;

                Random r = new Random(System.currentTimeMillis());

                int XTP = r.nextInt(GameConstant.SHORT_TP_MAX_RANGE - GameConstant.SHORT_TP_MIN_RANGE) + GameConstant.SHORT_TP_MIN_RANGE;
                int YTP = r.nextInt(GameConstant.SHORT_TP_MAX_RANGE - GameConstant.SHORT_TP_MIN_RANGE) + GameConstant.SHORT_TP_MIN_RANGE;


                int width = (int) currentMob.getWidth() * 2;
                int height = (int) currentMob.getHeight() * 2;

                Particule firstParticule = ParticuleHolder.getAvailableParticule(ParticuleRepo.TP_PARTICULE, currentMob.getPositionX(), currentMob.getPositionY(), width, height, false, null);

                currentMob.setPositionFromXY(currentMob.getPositionX() + (XTP * xDIrection), currentMob.getPositionY() + (YTP * yDIrection));

                Particule secondParticule = ParticuleHolder.getAvailableParticule(ParticuleRepo.TP_PARTICULE, currentMob.getPositionX(), currentMob.getPositionY(), width, height, true, null);

                board.addParticule(firstParticule);
                board.addParticule(secondParticule);
            }
        }


    };


    private final static HashMap<String, TouchedMove> touchedMoveList;

    static {
        touchedMoveList = new HashMap<String, TouchedMove>();
        touchedMoveList.put(baitMove.getId(), baitMove);
        touchedMoveList.put(bleedMove.getId(), bleedMove);
        touchedMoveList.put(healMove.getId(), healMove);
        touchedMoveList.put(mobAwayMove.getId(), mobAwayMove);
        touchedMoveList.put(tpMove.getId(), tpMove);
        touchedMoveList.put(default_touched_move.getId(), default_touched_move);
    }


    public static TouchedMove getMoveById(String id) {
        return TouchedMoveRepo.touchedMoveList.get(id);
    }

    public static ArrayList<String> getMoveIdList() {
        return new ArrayList<>(touchedMoveList.keySet());
    }


}
