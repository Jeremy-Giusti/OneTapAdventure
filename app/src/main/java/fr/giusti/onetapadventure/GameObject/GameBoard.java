package fr.giusti.onetapadventure.GameObject;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import fr.giusti.onetapadventure.callback.OnAllMobDeadListener;
import fr.giusti.onetapadventure.callback.OnMobDeathListener;
import fr.giusti.onetapadventure.callback.OnScrollingEndListener;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.Repository.SpriteRepo;

/**
 * represente la carte du jeu avec une taille limite, des mobs
 * gere les evenement lié au jeu (update on tick/on touch/...)
 */
public class GameBoard {
    private CopyOnWriteArrayList<GameMob> mMobs = new CopyOnWriteArrayList<GameMob>();
    private CopyOnWriteArrayList<Particule> mParticules = new CopyOnWriteArrayList<Particule>();
    private CopyOnWriteArrayList<TouchPoint> mTouchPoints = new CopyOnWriteArrayList<TouchPoint>();
    /**
     * la partie de l'image qui va etre dessinée en fond)
     */
    private Rect mDrawedBackgroundPortionBounds;
    /**
     * les limite du board
     */
    public Rect mBoardBounds;

    private String mBackgroundBitmapId;
    private int mBackgroundBitmapWidth;
    private int mBackgroundBitmapHeight;

    private OnMobDeathListener mMobDeathListener = null;
    private OnAllMobDeadListener mAllMobDeadListener = null;
    private OnScrollingEndListener mScrollingEndListener = null;


    /**
     * @param mobs               les entité mobile qui seront presente sur la carte
     * @param backgroundBitmapId l'image de fond
     */
    public GameBoard(CopyOnWriteArrayList<GameMob> mobs, String backgroundBitmapId, int boardWidth, int boardHeight) {
        super();
        this.mMobs = mobs;
        this.mBackgroundBitmapId = backgroundBitmapId;
        Point backgroundDimens = new SpriteRepo().getDimensionSpriteSheet(backgroundBitmapId);
        mBackgroundBitmapWidth = backgroundDimens.x;
        mBackgroundBitmapHeight = backgroundDimens.y;
        mDrawedBackgroundPortionBounds = new Rect(0, 0, boardWidth, boardHeight);
        mBoardBounds = new Rect(0, 0, boardWidth, boardHeight);
    }

    /**
     * @return la liste de mob de ce plateau
     */
    public List<GameMob> getMobs() {
        return mMobs;
    }

    public void setMobs(CopyOnWriteArrayList<GameMob> mobs) {
        this.mMobs = mobs;
    }

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

    /**
     * @param mMobDeathListener called when a mob die
     */
    public void setMobDeathListener(OnMobDeathListener mMobDeathListener) {
        this.mMobDeathListener = mMobDeathListener;
    }

    /**
     * @param mAllMobDeadListener called when all mob are dead
     */
    public void setAllMobDeadListener(OnAllMobDeadListener mAllMobDeadListener) {
        this.mAllMobDeadListener = mAllMobDeadListener;
    }

    /**
     * @param mScrollingEndListener called when the scroll reach the background end
     */
    public void setScrollingEndListener(OnScrollingEndListener mScrollingEndListener) {
        this.mScrollingEndListener = mScrollingEndListener;
    }

    /**
     * met a jour la carte après un tick
     */
    public void update() {
        for (GameMob mob : Collections.synchronizedList(mMobs)) {
            if (mob.isDead()) {
                if (mMobs.size() < 2 && mAllMobDeadListener != null) {
                    mAllMobDeadListener.OnAllMobDead(mob.clone());
                } else if (mMobDeathListener != null) {
                    mMobDeathListener.OnMobDeath(mob.clone());
                }
                mMobs.remove(mob);
            } else {
                mob.update(this);
            }
        }
        for (Particule particule : Collections.synchronizedList(mParticules)) {
            if (particule.getmAnimationState() < 0) {
                mParticules.remove(particule);
            } else {
                particule.update();
            }
        }

        for (TouchPoint touch : mTouchPoints) {
            if (touch.isEnded()) {
                mTouchPoints.remove(touch);
            } else {
                touch.update();
            }

        }

        // TODO gerer les depassement dans les deux sens (sortie de la map par la gauche ou par le haut +callback avec limit touché (enum ?)
        if (mDrawedBackgroundPortionBounds.left > mBackgroundBitmapWidth)
            this.mDrawedBackgroundPortionBounds.offsetTo(0, mDrawedBackgroundPortionBounds.top);
        if (mDrawedBackgroundPortionBounds.top > mBackgroundBitmapHeight)
            this.mDrawedBackgroundPortionBounds.offsetTo(mDrawedBackgroundPortionBounds.left, 0);

        // TODO creer une vitesse de deplacement x et y
        this.mDrawedBackgroundPortionBounds.offset(1, 0);
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

    /**
     * dessine la map puis les mob
     *
     * @param canvas
     * @param mBrush
     */
    public void draw(Canvas canvas, Paint mBrush) {

        Bitmap backgroundBitmap = SpriteRepo.getBitmap(mBackgroundBitmapId);

        int diffX = mDrawedBackgroundPortionBounds.right - backgroundBitmap.getWidth();
        int diffY = mDrawedBackgroundPortionBounds.bottom - backgroundBitmap.getHeight();

        if (diffX > 0) {
            // partie gauche de l'ecran
            Rect boardHalf = new Rect(mBoardBounds.left, mBoardBounds.top, mBoardBounds.right - diffX, mBoardBounds.bottom);
            // partie droite de l'image ("fin" de l'image")
            Rect backgroundHalf = new Rect(backgroundBitmap.getWidth() - boardHalf.width(), mDrawedBackgroundPortionBounds.top, backgroundBitmap.getWidth(), mDrawedBackgroundPortionBounds.bottom);
            canvas.drawBitmap(backgroundBitmap, backgroundHalf, boardHalf, mBrush);

            // partie droite de l'ecran
            boardHalf = new Rect(boardHalf.right, mBoardBounds.top, mBoardBounds.right, mBoardBounds.bottom);
            // partie gauche de l'image (debut)
            backgroundHalf = new Rect(0, mDrawedBackgroundPortionBounds.top, diffX, mDrawedBackgroundPortionBounds.bottom);

            canvas.drawBitmap(backgroundBitmap, backgroundHalf, boardHalf, mBrush);

        } else if (diffY > 0) {
            // TODO scrolling vertical
        } else {
            canvas.drawBitmap(backgroundBitmap, mDrawedBackgroundPortionBounds, mBoardBounds, mBrush);

        }
        Log.d("GAMEBOARD", "Number of mob to draw : " + mMobs.size());
        mBrush = GameMob.GetPaint(mBrush);
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
        // restore default brush ?
        mBrush.setAlpha(255);
    }

    public int getHeight() {
        return this.mBoardBounds.height();
    }

    public int getWidth() {
        return this.mBoardBounds.width();
    }

}
