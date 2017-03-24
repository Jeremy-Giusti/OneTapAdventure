package fr.giusti.onetapengine.repository;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.GameBoard;
import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.entity.Particule;
import fr.giusti.onetapengine.entity.Scenery;
import fr.giusti.onetapengine.entity.distribution.ParticuleHolder;
import fr.giusti.onetapengine.entity.moves.SpecialMove;
import fr.giusti.onetapengine.interaction.TouchPoint;

/**
 * Created by giusti on 20/03/2015.
 */
public class SpecialMoveRepo {
    //TODO limited range tp/switch
    public static final String NO_MOVE = "noMove";
    public static final String AUTO_HEAL = "autoHeal";
    public static final String AUTO_HURT_EXPLODING = "autoHurtExploding";
    public static final String MULTIPLIE = "multiplie";
    public static final String TELEPORT = "teleport";
    public static final String SWAP = "swap";
    public static final String BREAK_GLASS = "broke_glass";
    public static final String SMOKE_TRAIL = "smoke_trail";
    public static final String GHOST_MOVE = "ghost_move";


    private static SpecialMove noMove = new SpecialMove() {
        @Override
        public void doSpecialMove(GameBoard board, GameMob currentMob) {
        }

        @Override
        public String getId() {
            return NO_MOVE;
        }
    };

    private static SpecialMove autoHeal = new SpecialMove() {
        private int lastUse = 0;

        @Override
        public void doSpecialMove(GameBoard board, GameMob currentMob) {
            int currentHealth = currentMob.getHealth();
            if (currentMob.isJustMoving() && this.lastUse > 180 && currentHealth < (5 * GameConstant.TOUCH_DAMAGE)) {
                lastUse = 0;
                currentMob.setState(GameMob.eMobState.SPE1);
                currentMob.setHealth(currentHealth + GameConstant.TOUCH_DAMAGE);
                currentMob.setAnimationState(0);
            } else {
                lastUse++;
            }
        }

        @Override
        public String getId() {
            return AUTO_HEAL;
        }
    };

    private static SpecialMove smoke_trail = new SpecialMove() {

        private int lastUse = 0;

        @Override
        public void doSpecialMove(GameBoard board, GameMob currentMob) {
            if (currentMob.isJustMoving() && lastUse > 5) {
                lastUse = 0;
                Particule trailParicule = ParticuleHolder.getAvailableParticule(ParticuleRepo.SMOKE_PARTICULE, (int) currentMob.getPosition().centerX(), (int) currentMob.getPosition().centerY(), 16, 16, false, null);
                board.addParticule(trailParicule);
            } else {
                lastUse++;
            }
        }

        @Override
        public String getId() {
            return SMOKE_TRAIL;
        }
    };

    private static SpecialMove autoHurtExploding = new SpecialMove() {
        private int lastUse = 0;
        private int lastShowed = -1;

        @Override
        public void doSpecialMove(GameBoard board, GameMob currentMob) {

            if (lastUse >= Constants.FRAME_PER_SEC * 2) {
                lastUse = 0;
                int mobHealth = currentMob.getHealth();
                if (mobHealth > GameConstant.TOUCH_DAMAGE) {
                    mobHealth -= GameConstant.TOUCH_DAMAGE;
                    currentMob.setHealth(mobHealth);
                    currentMob.setState(GameMob.eMobState.HURT);
                    switch (mobHealth) {
                        case 3:
                            break;
                        case 2:
                            break;
                        case 1:
                            break;
                    }
                    if (mobHealth <= GameConstant.TOUCH_DAMAGE && lastShowed != 1) {
                        lastShowed = 1;
                        Particule numberParicule = ParticuleHolder.getAvailableParticule(ParticuleRepo.NUMBER1_PARTICULE, currentMob.getPositionX(), currentMob.getPositionY(), currentMob.mPosition.width(), currentMob.mPosition.height(), false, new PointF[]{new PointF(0, 2)});
                        board.addParticule(numberParicule);
                    } else if (mobHealth <= (2 * GameConstant.TOUCH_DAMAGE) && lastShowed != 2) {
                        lastShowed = 2;
                        Particule numberParicule = ParticuleHolder.getAvailableParticule(ParticuleRepo.NUMBER2_PARTICULE, currentMob.getPositionX(), currentMob.getPositionY(), currentMob.mPosition.width(), currentMob.mPosition.height(), false, new PointF[]{new PointF(0, 2)});
                        board.addParticule(numberParicule);
                    } else if (mobHealth <= (3 * GameConstant.TOUCH_DAMAGE) && lastShowed != 3) {
                        lastShowed = 3;
                        Particule numberParicule = ParticuleHolder.getAvailableParticule(ParticuleRepo.NUMBER3_PARTICULE, currentMob.getPositionX(), currentMob.getPositionY(), currentMob.mPosition.width(), currentMob.mPosition.height(), false, new PointF[]{new PointF(0, 2)});
                        board.addParticule(numberParicule);
                    } else {
                        lastShowed = -1;
                    }
                } else {
                    //EXPLOSION !
                    int particuleWidth = (int) currentMob.getWidth() * 2;
                    int particuleHeight = (int) currentMob.getHeight() * 2;
                    int particuleX =  currentMob.getPositionX();
                    int particuleY =  currentMob.getPositionY();

                    Particule explosionParticule = ParticuleHolder.getAvailableParticule(ParticuleRepo.EXPLOSION_PARTICULE, particuleX, particuleY, particuleWidth, particuleHeight, false, null);

                    //TODO MAKE AOE event instead of touch
                    board.addTouchEvent(new TouchPoint(currentMob.getPositionX(), currentMob.getPositionY(), (int) (currentMob.getWidth() + currentMob.getHeight())));

                    board.addParticule(explosionParticule);
                    currentMob.setHealth(-1);
                    currentMob.setState(GameMob.eMobState.DYING);
                }
                currentMob.setAnimationState(0);
            } else {
                lastUse++;
            }

        }

        @Override
        public String getId() {
            return AUTO_HURT_EXPLODING;
        }
    };

    private static SpecialMove multiplie = new SpecialMove() {
        private int lastUse = 0;

        @Override
        public void doSpecialMove(GameBoard board, GameMob currentMob) {
            if (currentMob.isJustMoving() && this.lastUse > 450) {
                lastUse = 0;
                currentMob.setState(GameMob.eMobState.SPE1);
                currentMob.setAnimationState(0);
                board.addMob(currentMob.clone());
                currentMob.setxAlteration(-currentMob.getxAlteration());
                currentMob.setyAlteration(-currentMob.getyAlteration());
            } else {
                lastUse++;
            }
        }

        @Override
        public String getId() {
            return MULTIPLIE;
        }
    };

    private static SpecialMove teleport = new SpecialMove() {
        private int lastUse = 0;

        @Override
        public String getId() {
            return TELEPORT;
        }

        @Override
        public void doSpecialMove(GameBoard board, GameMob currentMob) {
            if (currentMob.isJustMoving() && this.lastUse > Constants.FRAME_PER_SEC * 1.75) {
                lastUse = 0;
                currentMob.setState(GameMob.eMobState.SPE1);
                currentMob.setAnimationState(0);

                int newX = (int) (Math.random() * board.getWidth());
                int newY = (int) (Math.random() * board.getHeight());

                int width = (int) currentMob.getWidth() * 2;
                int height = (int) currentMob.getHeight() * 2;

                Particule firstParticule = ParticuleHolder.getAvailableParticule(ParticuleRepo.TP_PARTICULE, currentMob.getPositionX(), currentMob.getPositionY(), width, height, false, null);
                Particule secondParticule = ParticuleHolder.getAvailableParticule(ParticuleRepo.TP_PARTICULE, newX, newY, width, height, true, null);

                currentMob.setPositionFromXY(newX, newY);

                board.addParticule(firstParticule);
                board.addParticule(secondParticule);
            } else {
                lastUse++;
            }
        }


    };

    private static SpecialMove breakGlassMove = new SpecialMove() {
        private final int iteration = 8;
        int remainingIteration = iteration;
        int lastUse = 0;

        @Override
        public String getId() {
            return BREAK_GLASS;
        }

        @Override
        public void doSpecialMove(GameBoard board, GameMob currentMob) {
            if (lastUse < Constants.FRAME_PER_SEC * 4 && remainingIteration >= iteration) {
                lastUse++;
            } else {
                if (currentMob.getState() == GameMob.eMobState.HURT) {
                    remainingIteration = iteration;
                } else if (currentMob.getState() != GameMob.eMobState.SPE1) {
                    currentMob.setMovePattern(new PointF[]{new PointF(0, 0)});
                    currentMob.setState(GameMob.eMobState.SPE1);
                    currentMob.setAnimationState(0);
                    remainingIteration--;
                }
                if (remainingIteration == 0) {
                    int sceneryHeight = (int) currentMob.mPosition.height() * 2;
                    int sceneryWidth = (int) currentMob.mPosition.width() * 2;
                    board.onNewScenery(new Scenery("brokenglass" + currentMob.getIdName(), (int) currentMob.mPosition.centerX() - sceneryWidth / 2, (int) currentMob.mPosition.centerY() - sceneryHeight / 2, sceneryWidth, sceneryHeight, 1, TouchedMoveRepo.getMoveById(TouchedMoveRepo.DISAPEAR), GameConstant.HOLE_FRONT_SPRITE_ID));
                    currentMob.setHealth(0);
                    currentMob.setState(GameMob.eMobState.DYING);
                    lastUse = 0;
                    remainingIteration = iteration;
                }
            }

        }


    };


    private static SpecialMove swap = new SpecialMove() {
        private int lastUse = 0;
        private int swapingMob = -1;
        private Point nextPosition;
        private Point nextPositionOtherMob;
        private int delayBeforeTp = Constants.PARTICULE_NB_FRAME_ON_ANIMATION * Constants.FRAME_DURATION / 2;


        @Override
        public void doSpecialMove(GameBoard board, GameMob currentMob) {
            if (currentMob.isJustMoving() && this.lastUse > 120) {
                List<GameMob> mobList = board.getMobs();
                lastUse = 0;
                ParticuleRepo particuleRepo = new ParticuleRepo();
                currentMob.setState(GameMob.eMobState.SPE1);
                currentMob.setAnimationState(0);

                swapingMob = (int) (Math.random() * board.getMobs().size());

                if (mobList.get(swapingMob).equals(currentMob)) {
                    if (swapingMob + 1 >= mobList.size()) {
                        swapingMob--;
                    } else {
                        swapingMob++;
                    }
                }

                nextPosition = mobList.get(swapingMob).getFuturePositionCenter(delayBeforeTp);
                nextPositionOtherMob = currentMob.getFuturePositionCenter(delayBeforeTp);


                Particule firstParticule = ParticuleHolder.getAvailableParticule(ParticuleRepo.SWAP_PARTICULE, nextPosition.x, nextPosition.y, currentMob.getWidth(), currentMob.getHeight(), false, null);
                Particule secondParticule = ParticuleHolder.getAvailableParticule(ParticuleRepo.SWAP_PARTICULE, nextPositionOtherMob.x, nextPositionOtherMob.y, currentMob.getWidth(), currentMob.getHeight(), false, null);

                //add particule inverse and not

                board.addParticule(firstParticule);
                board.addParticule(secondParticule);
            } else if (this.lastUse == delayBeforeTp && swapingMob != -1) {
                List<GameMob> mobList = board.getMobs();
                if (mobList.size() > swapingMob && mobList.get(swapingMob) != null) {
                    currentMob.setPositionFromXY(nextPosition.x, nextPosition.y);
                    mobList.get(swapingMob).setPositionFromXY(nextPositionOtherMob.x, nextPositionOtherMob.y);
                }
                swapingMob = -1;
            } else {
                lastUse++;
            }
        }

        @Override
        public String getId() {
            return SWAP;
        }
    };

    private static SpecialMove ghostMove = new SpecialMove() {
        int currentTick = 0;
        int frequency = Constants.FRAME_PER_SEC * 3;

        @Override
        public void doSpecialMove(GameBoard board, GameMob currentMob) {
            currentTick++;
            int periode = currentTick % (frequency * 2);
            if (periode > frequency) {
                if (periode == (frequency + 1)) {
                    currentMob.setState(GameMob.eMobState.SPE1);
                    currentMob.setAnimationState(0);
                }
                if (currentMob.isJustMoving() || (GameMob.eMobState.SPE1 == currentMob.getState() && currentMob.getAnimationState() == Constants.FRAME_DURATION * 3 - 1)) {
                    currentMob.setAlpha(50);
                } else {
                    currentMob.setAlpha(255);
                }
            } else if (periode < 1) {
                board.addParticules(ParticuleHolder.getAvailableParticuleGroupe(ParticuleRepo.GROUPE_PLASMA_SPARK_PARTICULE, currentMob.mPosition, new PointF(0, 0), 10));
                currentMob.setAlpha(255);
                //currentMob.setState(GameMob.eMobState.MOVING_DOWN);
                // currentMob.setAnimationState(0);
            }
        }

        @Override
        public String getId() {
            return GHOST_MOVE;
        }
    };


    private final static HashMap<String, SpecialMove> specialeMoveList;

    static {
        specialeMoveList = new HashMap<String, SpecialMove>();
        specialeMoveList.put(autoHeal.getId(), autoHeal);
        specialeMoveList.put(autoHurtExploding.getId(), autoHurtExploding);
        specialeMoveList.put(multiplie.getId(), multiplie);
        specialeMoveList.put(noMove.getId(), noMove);
        specialeMoveList.put(teleport.getId(), teleport);
        specialeMoveList.put(swap.getId(), swap);
        specialeMoveList.put(smoke_trail.getId(), smoke_trail);
        specialeMoveList.put(breakGlassMove.getId(), breakGlassMove);
        specialeMoveList.put(ghostMove.getId(), ghostMove);


    }


    public static SpecialMove getMoveById(String id) {
        try {
            return SpecialMoveRepo.specialeMoveList.get(id).getClass().newInstance();
        } catch (InstantiationException e) {
            return null;
        } catch (IllegalAccessException e) {
            return null;
        }
    }

    public static ArrayList<String> getMoveIdList() {
        return new ArrayList<String>(specialeMoveList.keySet());
    }
}
