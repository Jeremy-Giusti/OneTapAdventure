package fr.giusti.onetapadventure.GameObject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.view.MotionEvent;

import fr.giusti.onetapadventure.GameObject.moves.SpecialMove;
import fr.giusti.onetapadventure.GameObject.moves.TouchedMove;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.Repository.SpriteRepo;

public class GameMob implements Cloneable {
    private static final String TAG = GameMob.class.getName();

    public RectF mPosition = new RectF();

    private int mSpriteCurrentColumn = 0;
    private int mSpriteCurrentLine = 0;

    protected Point[] movePattern;

    /**
     * unique mob id
     */
    private String name;

    /**
     * alignement of the mob, used to differenciate teams
     */
    private int alignement = 0;
    /**
     * interface conataining the special move of this mob
     */
    private SpecialMove mSpecialMove1 = null;

    /**
     * interface containing the methode to execute if the mob is touched
     */
    private TouchedMove mTouchedMove = null;
    /**
     * multiplcateur pour les mouvements horizontaux (permet de ralentire/accelerer/inverser)
     */
    public double xAlteration = 1;

    /**
     * multiplcateur pour les mouvements verticaux (permet de ralentire/accelerer/inverser)
     */
    public double yAlteration = 1;

    /**
     * position actuel dans l'array de mouvement sequentiel
     */
    protected int currentMove = 0;

    /**
     * id de la spriteSheet dans le repo
     */
    private String mBitmapId;

    /**
     * mob life
     */
    private int mHealth = 1;

    /**
     * stat<4 = normal,
     * 0=bas, 1 haut, 2 droite, 3 gauche
     * 4= touché, 5 mourant, 6 spe1, 7 spe2
     */
    protected int mState = 3;

    public final static int STATE_ORIENTATION_LEFT = 0;
    public final static int STATE_ORIENTATION_UP = 1;
    public final static int STATE_ORIENTATION_RIGHT = 2;
    public final static int STATE_ORIENTATION_DOWN = 3;
    public final static int STATE_HURT = 4;
    public final static int STATE_DYING = 5;
    public final static int STATE_SPEMOVE1 = 6;
    public final static int STATE_SPEMOVE2 = 7;

    /**
     *
     */
//    private int mOrientation = 0;

    /**
     * 0-10,10-20,20-30 utilisé pour selectioner les sprite en sequance (joue l'animation)
     */
    private int mAnimationState = 0;

    // ========================================================| BEAN |==================================================

    /**
     * @param x           position horizontale initial
     * @param y           position verticale initial
     * @param movePattern une liste de point, chaque point represente la distance x et y qu'il va parcourir à un tick
     * @param mBitmapId   un string qui sert d'id pour aller piocher le skin du mob dans le bitmapRepo (cache bitmap)
     * @param state       l'etat du mob (inutilisé pour le moment)
     */
    public GameMob(String name, int x, int y, int width, int height, Point[] movePattern, SpecialMove specialMove1, TouchedMove touchedmove, String mBitmapId, int health, int state) {
        super();
        this.name = name;
        mPosition.set(x, y, x + width, y + height);
        this.movePattern = movePattern;
        this.mSpecialMove1 = specialMove1;
        this.mTouchedMove = touchedmove;
        this.mBitmapId = mBitmapId;
        this.mHealth = health;
        this.mState = state;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAlignement() {
        return alignement;
    }

    public void setAlignement(int alignement) {
        this.alignement = alignement;
    }

    public RectF getPosition() {
        return mPosition;
    }

    public int getPositionX() {
        return (int) (mPosition.left);
    }

    public int getPositionY() {
        return (int) (mPosition.top);
    }

    /**
     * methode potentielement lourde
     *
     * @param tickInFuture
     * @return
     */
    public Point getFuturePositionCenter(int tickInFuture) {
        int futureX = (int) mPosition.centerX();
        int futureY = (int) mPosition.centerY();
        int futureMove = currentMove;
        for (int i = 0; i < tickInFuture; i++) {

            futureX += movePattern[futureMove].x * xAlteration;
            futureY += movePattern[futureMove].y * yAlteration;
            if (futureMove < (movePattern.length - 1)) {
                futureMove++;
            } else {
                futureMove = 0;
            }
        }

        return new Point(futureX, futureY);
    }

    public int getWidth() {
        return (int) mPosition.width();
    }

    public int getHeight() {
        return (int) mPosition.height();
    }


    public void setPosition(RectF position) {
        this.mPosition = position;
    }

    public void setPositionFromXY(int x, int y) {
        mPosition.offsetTo(x, y);
    }

    public SpecialMove getmSpecialMove1() {
        return mSpecialMove1;
    }

    public void setmSpecialMove1(SpecialMove mSpecialMove1) {
        this.mSpecialMove1 = mSpecialMove1;
    }

    public TouchedMove getmTouchedMove() {
        return mTouchedMove;
    }

    public void setmTouchedMove(TouchedMove mTouchedMove) {
        this.mTouchedMove = mTouchedMove;
    }

    public double getxAlteration() {
        return xAlteration;
    }

    public void setxAlteration(double xAlteration) {
        this.xAlteration = xAlteration;
    }

    public double getyAlteration() {
        return yAlteration;
    }

    public void setyAlteration(double yAlteration) {
        this.yAlteration = yAlteration;
    }

    /**
     * @return id du skin du mob (voir {@link SpriteRepo})
     */
    public String getBitmapId() {
        return mBitmapId;
    }

    /**
     * @param mBitmapId id du skin du mob (voir {@link SpriteRepo})
     */
    public void setBitmapId(String mBitmapId) {
        this.mBitmapId = mBitmapId;
    }

    public int getHealth() {
        return mHealth;
    }

    public void setHealth(int mHealth) {
        this.mHealth = mHealth;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        this.mState = state;
    }

    public int getAnimationState() {
        return mAnimationState;
    }

    public void setAnimationState(int mAnimationState) {
        this.mAnimationState = mAnimationState;
    }

    public Point[] getMovePattern() {
        return movePattern;
    }

    public Point getCurrentMove() {
        return movePattern[currentMove];
    }

    public void setCurrentMove(int newCurrentMove) {
        currentMove = newCurrentMove;
    }


    public void setMovePattern(Point[] movePattern) {
        this.movePattern = movePattern;
    }

    // ======================================================| UPDATE |===================================================

    /**
     * met a jour le mob au terme d'un tick (orientation, position, animation)
     */
    public void update(GameBoard board) {

        int deplacementX = (int) (movePattern[currentMove].x * xAlteration);
        int deplacementY = (int) (movePattern[currentMove].y * yAlteration);

        move(deplacementX, deplacementY);
        manageMapLimitCollision(board);
        if (mState < STATE_HURT) {
            updateOrientation(deplacementX, deplacementY);
        }

        doSpecialMove(board);

        updateSprite();
    }

    /**
     * move the mob (check the map limit too
     *
     * @param deplacementX
     * @param deplacementY
     */
    protected void move(int deplacementX, int deplacementY) {
        this.mPosition.offset(deplacementX, deplacementY);
        if (mState != STATE_DYING) {//si il est mort il ne change plus de direction
            if (currentMove < (movePattern.length - 1)) {
                currentMove++;
            } else {
                currentMove = 0;
            }
        }

    }

    /**
     * check if the mob is touching the map limit
     * change the mob orientation
     */
    protected void manageMapLimitCollision(GameBoard board) {
        if (mPosition.left < 0) {
            xAlteration = -xAlteration;
            mPosition.offsetTo(0, mPosition.top);
        } else if (mPosition.right >= board.mBoardBounds.right) {
            xAlteration = -xAlteration;
            mPosition.offsetTo((board.mBoardBounds.right - 1) - mPosition.width(), mPosition.top);
        }
        if (mPosition.top < 0) {
            yAlteration = -yAlteration;
            mPosition.offsetTo(mPosition.left, 0);
        } else if (mPosition.bottom >= board.mBoardBounds.bottom) {
            yAlteration = -yAlteration;
            mPosition.offsetTo(mPosition.left, (board.mBoardBounds.bottom - 1) - mPosition.height());

        }
    }

    /**
     * calculate the orientation based on the current move x and y value
     *
     * @param deplacementX
     * @param deplacementY
     */
    protected void updateOrientation(int deplacementX, int deplacementY) {
        //calcule en valeur absolus
        if (deplacementX * deplacementX > deplacementY * deplacementY) {
            //on se deplace principalement de facon horizontale
            if (deplacementX > 0) {
                mState = STATE_ORIENTATION_RIGHT;
            } else {
                mState = STATE_ORIENTATION_LEFT;
            }

        } else {
            if (deplacementY > 0) {
                mState = STATE_ORIENTATION_DOWN;
            } else {
                mState = STATE_ORIENTATION_UP;
            }
        }
    }

    /**
     * update the portion of the spritesheet to draw accordingly to the mob state + animationstate
     */
    protected void updateSprite() {
        // check l'etat du mob et son stade dans l'animation
        if (mAnimationState == Constants.COMPLETE_ANIMATION_DURATION) {
            if (mState != STATE_DYING) {
                mAnimationState = 0;
                if (mState >= STATE_HURT) {
                    mState = 3;
                }
            }
        } else {
            mAnimationState++;
        }
        this.mSpriteCurrentColumn = (int) (mAnimationState / Constants.FRAME_DURATION);
        this.mSpriteCurrentLine = mState;

    }

    private void doSpecialMove(GameBoard board) {
        if (this.mSpecialMove1 != null) {
            this.mSpecialMove1.doSpecialMove(board, this);
        }
    }

    // =========================================================| DRAW |===================================================

    /**
     * dessine le mob sur le canvas (utilise son skin + position x,y)
     *
     * @param canvas
     * @param mBrush
     */
    public void draw(Canvas canvas, Paint mBrush) {
        if (mBitmapId == null) {

            if (mState == STATE_DYING) {
                canvas.drawCircle(mPosition.left, mPosition.top, 6, mBrush);
            } else {
                canvas.drawCircle(mPosition.left, mPosition.top, 3, mBrush);
            }
        } else {

            RectF rectPositionOnScreen = new RectF(mPosition);
            rectPositionOnScreen.offsetTo(mPosition.left, mPosition.top);
            canvas.drawBitmap(SpriteRepo.getSpriteBitmap(mBitmapId, mSpriteCurrentColumn, mSpriteCurrentLine), null, mPosition, mBrush);

            //canvas.drawBitmap(SpriteRepo.getBitmap(mBitmapId), mMobSprite, rectPositionOnScreen, mBrush);

        }

    }

    public static Paint GetPaint(Paint paint) {
        paint.setStrokeWidth(3);
        paint.setColor(Color.argb(255, 150, 150, 150));
        return paint;
    }

    // =======================================================| EVENTS |===================================================

    /**
     * gere un clique et met a jour le status du mob en fonction
     *
     * @param touchStroke
     * @param event
     */
    public void manageTouchEvent(Point event, int touchStroke, GameBoard board) {
        if (isDead())
            return;// deja mort
        float eventX = event.x;
        float eventY = event.y;

        if (mPosition.intersects((eventX - touchStroke / 2), (eventY - touchStroke / 2), (eventX + touchStroke / 2), (eventY + touchStroke / 2))) {
            if (this.mTouchedMove != null) {
                this.mTouchedMove.doTouchedMove(board, this, new Point((int) eventX, (int) eventY));
            } else if (mHealth > 1) {
                mHealth--;
                mState = STATE_HURT;
            } else {
                mHealth = 0;
                mState = STATE_DYING;
            }
            mAnimationState = 0;

        }
    }

    /**
     * mState=5 (dead) + mAnimationState = complete (already looped one time, showing the death animation)
     *
     * @return
     */
    public boolean isDead() {
        return (mState == STATE_DYING && mAnimationState == Constants.COMPLETE_ANIMATION_DURATION) ? true : false;
    }

    @Override
    public GameMob clone() {
        GameMob clone;
        Point[] cloneMovePattern = new Point[movePattern.length];
        for (int i = 0; i < movePattern.length; i++) {
            cloneMovePattern[i] = new Point(movePattern[i].x, movePattern[i].y);
        }
        clone = new GameMob(
                name,
                (int) mPosition.left,
                (int) mPosition.top,
                (int) mPosition.width(),
                (int) mPosition.height(),
                cloneMovePattern,
                this.mSpecialMove1,
                mTouchedMove,
                mBitmapId,
                mHealth,
                mState);

        return clone;
//        try {
//            return (GameMob) super.clone();
//        } catch (CloneNotSupportedException e) {
//            Log.e(TAG, "cloneNotSupported: " + e);
//        }
//        return null;

    }
}
