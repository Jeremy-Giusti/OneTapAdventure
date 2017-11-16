package fr.giusti.onetapengine;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.giusti.onetapengine.callback.OnBoardEventListener;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.entity.Particule;
import fr.giusti.onetapengine.entity.Scenery;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerManager;
import fr.giusti.onetapengine.interaction.TouchDispenser;
import fr.giusti.onetapengine.interaction.TouchPoint;
import fr.giusti.onetapengine.repository.SpriteRepo;
import fr.giusti.onetapengine.rules.RulesManager;
import fr.giusti.onetapengine.rules.eConditions;


/**
 * represente la carte du jeu avec une taille limite, des mobs
 * gere les evenement lié au jeu (update on tick/on touch/...)
 */
public class GameBoard {

    /**
     * the frequency at which the elapsed time event will be dispatched
     */
    private final static long ELAPSED_TIME_GRANULARITY = 250;

    /**
     * The last value dispatched, prevent sending 2 time the same value
     */
    private long lastDispatchedElapsedTime = -1;

    private EventsHolder mEventsHolder = new EventsHolder();

    private TouchDispenser mTouchDisp;
    private EntitySpawnerManager mEntityManager;
    private RulesManager mRulesManager;

    private CopyOnWriteArrayList<GameMob> mMobs = new CopyOnWriteArrayList<GameMob>();
    private CopyOnWriteArrayList<Particule> mParticules = new CopyOnWriteArrayList<Particule>();
    private CopyOnWriteArrayList<Scenery> mSceneries = new CopyOnWriteArrayList<Scenery>();
    private CopyOnWriteArrayList<TouchPoint> mTouchPoints = new CopyOnWriteArrayList<TouchPoint>();


    /**
     * les limite du board
     */
    public Rect mBoardBounds;
    public Rect mCameraBounds;
    //   public Rect mCameraPosOnScreen;// a quoi ça sere ?

    private String mBackgroundBitmapId;

    @Deprecated
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
        mCameraBounds = drawedBounds;
        mBoardBounds = new Rect(0, 0, boardWidth, boardHeight);
    }


    /**
     * @param backgroundBitmapId l'image de fond
     */
    public GameBoard(EntitySpawnerManager entityManager, String backgroundBitmapId, int boardWidth, int boardHeight, Rect drawedBounds, RulesManager rulesManager, TouchDispenser touchDisp) {
        super();
        onNewEntities(entityManager.getInitialList());//should be called first to prevent board event while game isn't running
        this.mEntityManager = entityManager;
        this.mTouchDisp = touchDisp;
        entityManager.setBoard(this);
        this.mBackgroundBitmapId = backgroundBitmapId;
        mCameraBounds = drawedBounds;
        mBoardBounds = new Rect(0, 0, boardWidth, boardHeight);
        this.mRulesManager = rulesManager;
    }


    // ------------------------------ ACCESSORS -------------------------------//

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

    public void addParticules(ArrayList<Particule> particules) {
        this.mParticules.addAll(particules);
    }

    public Particule getParticule(int particuleIndex) {
        return mParticules.get(particuleIndex);
    }

    public void setmParticules(CopyOnWriteArrayList<Particule> mParticules) {
        this.mParticules = mParticules;
    }

    public void addTouchEvent(TouchPoint touchPoint) {
        this.mTouchPoints.add(touchPoint);
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

    public Rect getmCameraBounds() {
        return mCameraBounds;
    }

    public void setmCameraBounds(Rect mCameraBounds) {
        this.mCameraBounds = mCameraBounds;
    }

    /**
     * @param eventListener called when a mob die
     */
    @Deprecated
    public void setBoardEventListener(OnBoardEventListener eventListener) {
        this.mEventListener = eventListener;
    }

    public RulesManager getRulesManager() {
        return mRulesManager;
    }

    public void setRulesManager(RulesManager mRulesManager) {
        this.mRulesManager = mRulesManager;
    }


    // ------------------------------ LIFECYCLE -------------------------------//


    /**
     * met a jour la carte après un tick
     */
    public void update(long timeSinceStart) {

        if (!started && getRulesManager() != null) {
            mRulesManager.firstUpdate();
            mEntityManager.firstUpdate();
            started = true;
        }

        mEventsHolder.elapsedTime = timeSinceStart - (timeSinceStart % ELAPSED_TIME_GRANULARITY);
        dispatchEvents();


        for (GameMob mob : Collections.synchronizedList(mMobs)) {
            mob.update(this);
        }
        for (Particule particule : Collections.synchronizedList(mParticules)) {
            if (particule.getmAnimationState() < 0) {
                mParticules.remove(particule);
                particule.recycle();
            } else {
                particule.update(this);
            }
        }

        for (TouchPoint touch : mTouchPoints) {
            if (touch.isEnded()) {
                mTouchPoints.remove(touch);
            } else {
                touch.update(this);
            }
        }

        for (Scenery scenery : mSceneries) {
            scenery.update(this);

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


        canvas.drawBitmap(backgroundBitmap, mCameraBounds, mCameraBounds, mBrush);

        Log.d("GAMEBOARD", "Number of mob to draw : " + mMobs.size());
        //mBrush = GameMob.GetPaint(mBrush);
        for (Scenery scenery : mSceneries) {
            scenery.draw(canvas, mBrush, mCameraBounds);
            //   canvas.drawRect(scenery.hitbox,mBrush);
        }

        for (GameMob mob : mMobs) {
            mob.draw(canvas, mBrush, mCameraBounds);
        }
        for (Particule particule : mParticules) {
            particule.draw(canvas, mBrush, mCameraBounds);
        }

        mBrush = TouchPoint.GetPaint(mBrush);
        for (TouchPoint touch : mTouchPoints) {
            touch.draw(canvas, mBrush, mCameraBounds);
        }

        mBrush.setAlpha(255);
    }

    /**
     * communicate the events to the rule manager and the entityManager
     */
    public void dispatchEvents() {


        /*--SETTUP_EVENT--*/
        mEventsHolder.mobCount = mMobs.size();


        boolean dispatchTime = lastDispatchedElapsedTime != mEventsHolder.elapsedTime;
        if (dispatchTime) lastDispatchedElapsedTime = mEventsHolder.elapsedTime;

        //prevent conflict if mEntityManager add mob
        mEventsHolder.oldMobEvents = mEventsHolder.mobEvents;
        mEventsHolder.mobEvents = new ArrayList<>();

        /*--BASE_EVENTS--*/
        if (mRulesManager != null) {
            mRulesManager.onMobCountChange(mEventsHolder.mobCount);
            mRulesManager.onScoreChange(mEventsHolder.score);
            if (dispatchTime) mRulesManager.onTimeProgress(mEventsHolder.elapsedTime);
        }
        if (mEntityManager != null) {
            mEntityManager.onMobCountChange(mEventsHolder.mobCount);
            mEntityManager.onScoreChange(mEventsHolder.score);
            if (dispatchTime) mEntityManager.onTimeProgress(mEventsHolder.elapsedTime);
        }

        /*--MOB_EVENT--*/
        for (EventsHolder.MobEvent mobEvent : mEventsHolder.oldMobEvents) {
            if (mEntityManager != null) {
                mEntityManager.onMobEvent(mobEvent.reason, mobEvent.mob);
            }
            if (mRulesManager != null) {
                mRulesManager.onMobEvent(mobEvent.reason, mobEvent.mob);
            }
        }

    }

    // ------------------------------ EVENT HANDLING -------------------------------//

    /**
     * action touch
     */
    public void touchEvent(MotionEvent event) {
        // get pointer index from the event object
        int pointerIndex = event.getActionIndex();

        // get masked (not specific to a pointer) action
        int maskedAction = event.getActionMasked();

        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (mTouchDisp == null)
                    mTouchPoints.add(new TouchPoint(event.getX(pointerIndex), event.getY(pointerIndex), GameConstant.TOUCH_STROKE));
                else
                    mTouchPoints.add(mTouchDisp.generateTouchPoint(event.getX(pointerIndex), event.getY(pointerIndex)));
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

        mEventsHolder.mobEvents.add(new EventsHolder.MobEvent(mob, eConditions.MOB_DEATH));
        mEventsHolder.score += mob.getScoreValue();
        mMobs.remove(mob);
    }

    public void onMobAway(GameMob mob) {
        mEventsHolder.mobEvents.add(new EventsHolder.MobEvent(mob, eConditions.MOB_AWAY));
        mEventsHolder.score -= mob.getScoreValue();
        mMobs.remove(mob);
    }

    public void onNewMob(GameMob mob) {
        mMobs.add(mob);
        mEventsHolder.mobEvents.add(new EventsHolder.MobEvent(mob, eConditions.NEW_MOB));

    }

    public void onNewMobs(List<GameMob> mobList) {
        for (GameMob mob : mobList) {
            mEventsHolder.mobEvents.add(new EventsHolder.MobEvent(mob, eConditions.NEW_MOB));
        }
        mMobs.addAll(mobList);
    }

    public void onNewEntities(ArrayList<Entity> entities) {
        if (entities == null) return;
        for (Entity entity : entities) {
            if (entity instanceof GameMob) {
                onNewMob((GameMob) entity);
            } else if (entity instanceof Particule) {
                mParticules.add((Particule) entity);
            } else if (entity instanceof Scenery) {
                mSceneries.add((Scenery) entity);
            }
        }
    }

    public void onNewScenery(Scenery blab) {
        mSceneries.add(blab);
    }

    public void onScore(int score) {
        mEventsHolder.score += score;
    }

    // ------------------------------ RESIZE -------------------------------//

    /**
     * first calculate how to adapt the camera bound to the screen size (border are the empty border that will result)<br>
     * then calculate a ratio wich correspond to a scalling of everything to have a correct size on screen
     *
     * @param screenWidth
     * @param screenHeight
     */
    public void resize(int screenWidth, int screenHeight) {
        float screanHWRatio = screenHeight / (float) screenWidth;
        float camHWRatio = mCameraBounds.height() / (float) mCameraBounds.width();

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
            ratio = screenHeight / (float) mCameraBounds.height();
            cameraWidth = (int) (mCameraBounds.width() * ratio);
            cameraHeight = (int) (mCameraBounds.height() * ratio);
            borderHorz = (screenWidth - cameraWidth) / 2;
        } else {
            //screen more hight than wide compared to camera so we change camera width to match screen and add border on top and bottom
            ratio = screenWidth / (float) mCameraBounds.width();
            cameraWidth = (int) (mCameraBounds.width() * ratio);
            cameraHeight = (int) (mCameraBounds.height() * ratio);
            borderVert = (screenHeight - cameraHeight) / 2;
        }

        mCameraBounds.set(mCameraBounds.left, mCameraBounds.top, mCameraBounds.left + cameraWidth, mCameraBounds.top + cameraHeight);

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

        if (mEntityManager != null) {
            mEntityManager.resize(ratio);
        }

    }
}
