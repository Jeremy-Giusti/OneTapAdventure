package fr.giusti.onetapadventure.gameObject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.giusti.onetapadventure.callback.OnBoardEventListener;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.gameObject.entities.Entity;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.gameObject.entities.Particule;
import fr.giusti.onetapadventure.gameObject.entities.Scenery;
import fr.giusti.onetapadventure.gameObject.rules.eConditions;
import fr.giusti.onetapadventure.repository.SpriteRepo;
import fr.giusti.onetapadventure.repository.entities.EntityDispenser;

/**
 * represente la carte du jeu avec une taille limite, des mobs
 * gere les evenement lié au jeu (update on tick/on touch/...)
 */
public class GameBoard {
    private EntityDispenser mMobDisp;
    private CopyOnWriteArrayList<GameMob> mMobs = new CopyOnWriteArrayList<GameMob>();
    private CopyOnWriteArrayList<Particule> mParticules = new CopyOnWriteArrayList<Particule>();
    private CopyOnWriteArrayList<Scenery> mSceneries = new CopyOnWriteArrayList<Scenery>();
    private CopyOnWriteArrayList<TouchPoint> mTouchPoints = new CopyOnWriteArrayList<TouchPoint>();


    /**
     * les limite du board
     */
    public Rect mBoardBounds;
    public Rect mCameraBound;
    public Rect mCameraBoundOnScreen;

    private String mBackgroundBitmapId;

    private OnBoardEventListener mEventListener = null;
    /**
     * empty border around camera (left and right)
     */
    private int borderHorz;
    /**
     * empty border around camera (above and under)
     */
    private int borderVert;
    private boolean started = false;


    /**
     * @param mobs               les entité mobile qui seront presente sur la carte
     * @param backgroundBitmapId l'image de fond
     */
    public GameBoard(ArrayList<GameMob> mobs, String backgroundBitmapId, int boardWidth, int boardHeight, Rect drawedBounds) {
        super();
        this.mMobs = new CopyOnWriteArrayList<>(mobs);
        this.mBackgroundBitmapId = backgroundBitmapId;
        mCameraBound = drawedBounds;
        mBoardBounds = new Rect(0, 0, boardWidth, boardHeight);
    }

    /**
     * @param mobsDisp           les entité mobile qui seront presente sur la carte
     * @param backgroundBitmapId l'image de fond
     */
    public GameBoard(EntityDispenser mobsDisp, String backgroundBitmapId, int boardWidth, int boardHeight, Rect drawedBounds) {
        super();
        this.mMobDisp = mobsDisp;
        initEntityLists(mobsDisp.getInitialList());
        this.mBackgroundBitmapId = backgroundBitmapId;
        mCameraBound = drawedBounds;
        mBoardBounds = new Rect(0, 0, boardWidth, boardHeight);
    }

    private void initEntityLists(ArrayList<Entity> initialList) {
        for (Entity entity : initialList) {
            if (entity instanceof GameMob) {
                mMobs.add((GameMob) entity);
            } else if (entity instanceof Particule) {
                mParticules.add((Particule) entity);
            } else if (entity instanceof Scenery) {
                mSceneries.add((Scenery) entity);
            }
        }
    }


    /**
     * @return la liste de mob de ce plateau
     */
    public List<GameMob> getMobs() {
        return mMobs;
    }

    public void setMobs(ArrayList<GameMob> mobs) {
        this.mMobs = new CopyOnWriteArrayList<>(mobs);
    }

    @Deprecated
    public void addMob(GameMob mob) {
        this.mMobs.add(mob);
    }

    public GameMob getMob(int mobIndex) {
        return mMobs.get(mobIndex);
    }

    public CopyOnWriteArrayList<Particule> getmParticules() {
        return mParticules;
    }

    public void addParticule(Particule particule) {
        this.mParticules.add(particule);
    }

    public Particule getParticule(int particuleIndex) {
        return mParticules.get(particuleIndex);
    }

    public void setmParticules(CopyOnWriteArrayList<Particule> mParticules) {
        this.mParticules = mParticules;
    }

    public String getBackgroundBitmapId() {
        return mBackgroundBitmapId;
    }

    public void setBackgroundBitmapId(String backgroundBitmapId) {
        this.mBackgroundBitmapId = backgroundBitmapId;
    }

    public int getHeight() {
        return this.mBoardBounds.height();
    }

    public int getWidth() {
        return this.mBoardBounds.width();
    }

    public Rect getmCameraBound() {
        return mCameraBound;
    }

    public void setmCameraBound(Rect mCameraBound) {
        this.mCameraBound = mCameraBound;
    }

    /**
     * @param eventListener called when a mob die
     */
    public void setBoardEventListener(OnBoardEventListener eventListener) {
        this.mEventListener = eventListener;
    }


    /**
     * met a jour la carte après un tick
     */
    public void update() {
        if (!started) {
            mEventListener.firstUpdate();
            started = true;
        }

        this.updateCameraPosition();

        for (GameMob mob : Collections.synchronizedList(mMobs)) {
            mob.update(this);
        }
        for (Particule particule : Collections.synchronizedList(mParticules)) {
            if (particule.getmAnimationState() < 0) {
                mParticules.remove(particule);
            } else {
                particule.update(this);
            }
        }


        for (TouchPoint touch : mTouchPoints) {
            if (touch.isEnded()) {
                mTouchPoints.remove(touch);
            } else {
                touch.update();
            }
        }

        for (Scenery scenery : mSceneries) {
            scenery.update(this);

        }

        if (mMobDisp != null) {
            mMobDisp.onTick(this);
        }
    }

    private void updateCameraPosition() {
        mCameraBoundOnScreen = new Rect(mCameraBound.left + borderHorz, mCameraBound.top + borderVert, mCameraBound.right + borderHorz, mCameraBound.bottom + borderVert);
    }

    /**
     * action touch
     */
    public void touchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point eventPoint = new Point((int) event.getX(), (int) event.getY());
                for (GameMob mob : mMobs) {
                    // TODO gerer l'envergure du touché (constants)
                    mob.manageTouchEvent(eventPoint, Constants.TOUCH_STROKE, this);
                }
                mTouchPoints.add(new TouchPoint((int) event.getX(), (int) event.getY()));
                break;
            case MotionEvent.ACTION_MOVE:
                // todo
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // todo
                break;

            default:
                break;

        }
    }

    public void onMobDeath(GameMob mob) {
        if (mEventListener != null) {
            mEventListener.onMobCountChange(mMobs.size() - 1, eConditions.MOB_DEATH, mob.clone());//FIXME mob.clone ?
        }
        mMobs.remove(mob);
    }

    public void onMobAway(GameMob mob) {
        if (mEventListener != null) {
            mEventListener.onMobCountChange(mMobs.size() - 1, eConditions.MOB_AWAY, mob.clone());
        }
        mMobs.remove(mob);
    }

    public void onNewMob(GameMob mob) {
        if (mEventListener != null) {
            mEventListener.onMobCountChange(mMobs.size() + 1, eConditions.NEW_MOB, mob.clone());
        }
        mMobs.add(mob);
    }

    public void onScore(int score) {//FIXME improve score events
        if (mEventListener != null) {
            if (score < 0) {
                mEventListener.onScoreMinus(-score);
            } else {
                mEventListener.onScorePlus(score);
            }
        }
    }

    /**
     * dessine la map puis les mob
     *
     * @param canvas
     * @param mBrush
     */
    public void draw(Canvas canvas, Paint mBrush) {

        Bitmap backgroundBitmap = SpriteRepo.getPicture(mBackgroundBitmapId);


        canvas.drawBitmap(backgroundBitmap, mCameraBound, mCameraBound, mBrush);

        Log.d("GAMEBOARD", "Number of mob to draw : " + mMobs.size());
        //mBrush = GameMob.GetPaint(mBrush);

        for (Scenery scenery : mSceneries) {
            scenery.draw(canvas, mBrush);
        }

        for (GameMob mob : mMobs) {
            mob.draw(canvas, mBrush);
        }
        for (Particule particule : mParticules) {
            particule.draw(canvas, mBrush);
        }

        mBrush = TouchPoint.GetPaint(mBrush);
        for (TouchPoint touch : mTouchPoints) {
            touch.draw(canvas, mBrush);
        }

        mBrush.setAlpha(255);
    }


    /**
     * first calculate how to adapt the camera bound to the screen size (border are the empty border that will result)<br>
     * then calculate a ratio wich correspond to a scalling of everything to have a correct size on screen
     *
     * @param screenWidth
     * @param screenHeight
     */
    public void resize(int screenWidth, int screenHeight) {
        float screanHWRatio = screenHeight / (float) screenWidth;
        float camHWRatio = mCameraBound.height() / (float) mCameraBound.width();

        float ratio = 0;
        borderHorz = 0;
        borderVert = 0;
        int cameraWidth = 0;
        int cameraHeight = 0;


        /**
         * updating camera scale and borders depending on screen dimens
         */
        if (screanHWRatio < camHWRatio) {
            //screen more wide than hight compared to camera so we change camera height to match screen and add border on left and right
            ratio = screenHeight / (float) mCameraBound.height();
            cameraWidth = (int) (mCameraBound.width() * ratio);
            cameraHeight = (int) (mCameraBound.height() * ratio);
            borderHorz = (screenWidth - cameraWidth) / 2;
        } else {
            //screen more hight than wide compared to camera so we change camera width to match screen and add border on top and bottom
            ratio = screenWidth / (float) mCameraBound.width();
            cameraWidth = (int) (mCameraBound.width() * ratio);
            cameraHeight = (int) (mCameraBound.height() * ratio);
            borderVert = (screenHeight - cameraHeight) / 2;
        }

        mCameraBound.set(mCameraBound.left, mCameraBound.top, mCameraBound.left + cameraWidth, mCameraBound.top + cameraHeight);

        /**
         * redimensionning the board
         */
        int boardWidth = (int) (mBoardBounds.width() * ratio);
        int boardHeight = (int) (mBoardBounds.height() * ratio);
        mBoardBounds.set(mBoardBounds.left, mBoardBounds.top, mBoardBounds.left + boardWidth, mBoardBounds.top + boardHeight);
        SpriteRepo.resizePicture(mBackgroundBitmapId, ratio);

        for (GameMob mob : mMobs) {
            mob.resize(ratio);
        }

        for (Scenery scen : mSceneries) {
            scen.resize(ratio);
        }

        for (Particule part : mParticules) {
            part.resize(ratio);
        }

        if (mMobDisp != null) {
            mMobDisp.resize(ratio);
        }

    }

}
