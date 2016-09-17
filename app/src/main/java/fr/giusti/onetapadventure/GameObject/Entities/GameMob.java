package fr.giusti.onetapadventure.gameObject.entities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.commons.Utils;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.moves.SpecialMove;
import fr.giusti.onetapadventure.gameObject.moves.TouchedMove;
import fr.giusti.onetapadventure.repository.PathRepo;
import fr.giusti.onetapadventure.repository.SpriteRepo;

public class GameMob extends Entity {
    private static final String TAG = GameMob.class.getName();


    private int mSpriteCurrentColumn = 0;
    //   private int mSpriteCurrentLine = 0;

    protected PointF[] movePattern;

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
     * mob life
     */
    private int mHealth = 1;

    /**
     * stat<4 = normal,
     * 0=left, 1 haut, 2 bas, 3 droite
     * 4= touché, 5 mourant, 6 spe1, 7 spe2
     */
    protected eMobState mState = eMobState.MOVING_UP;


    /**
     * 0-10,10-20,20-30 utilisé pour selectioner les sprite en sequence (joue l'animation)
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
    public GameMob(String idName, int x, int y, int width, int height, PointF[] movePattern, SpecialMove specialMove1, TouchedMove touchedmove, String mBitmapId, int health, int state) {
        super(idName, x, y, width, height, mBitmapId);
        this.movePattern = movePattern;
        this.mSpecialMove1 = specialMove1;
        this.mTouchedMove = touchedmove;
        this.mHealth = health;
        this.mState = eMobState.values()[state];
    }

    public GameMob(String idName, int x, int y, int width, int height, PointF[] movePattern, SpecialMove specialMove1, TouchedMove touchedmove, String mBitmapId, int health, eMobState state) {
        super(idName, x, y, width, height, mBitmapId);
        this.movePattern = movePattern;
        this.mSpecialMove1 = specialMove1;
        this.mTouchedMove = touchedmove;
        this.mHealth = health;
        this.mState = state;
    }


    public int getAlignement() {
        return alignement;
    }

    public void setAlignement(int alignement) {
        this.alignement = alignement;
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
            //TODO optimizable
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

    public int getHealth() {
        return mHealth;
    }

    public void setHealth(int mHealth) {
        this.mHealth = mHealth;
    }

    public eMobState getState() {
        return mState;
    }

    public void setState(eMobState mState) {
        this.mState = mState;
    }

    public int getAnimationState() {
        return mAnimationState;
    }

    public void setAnimationState(int mAnimationState) {
        this.mAnimationState = mAnimationState;
    }

    public PointF[] getMovePattern() {
        return movePattern;
    }

    public PointF getCurrentMove() {
        return movePattern[currentMove];
    }

    public void setCurrentMove(int newCurrentMove) {
        currentMove = newCurrentMove;
    }


    public void setMovePattern(PointF[] movePattern) {
        this.movePattern = movePattern;
    }

    // ======================================================| UPDATE |===================================================

    /**
     * met a jour le mob au terme d'un tick (orientation, position, animation)
     */
    @Override
    public void update(GameBoard board) {

        if (isDead()) {
            board.onMobDeath(this);
            return;
        }

        float deplacementX = (float) (movePattern[currentMove].x * xAlteration);
        float deplacementY = (float) (movePattern[currentMove].y * yAlteration);

        move(deplacementX, deplacementY);
        manageMapLimitCollision(board);
        if (isJustMoving()) {
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
    protected void move(float deplacementX, float deplacementY) {
        this.mPosition.offset(deplacementX, deplacementY);
        if (mState != eMobState.DYING) {//si il est mort il ne change plus de direction
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
    protected void updateOrientation(float deplacementX, float deplacementY) {
        //calcule en valeur absolus
        if (deplacementX * deplacementX > deplacementY * deplacementY) {
            //on se deplace principalement de facon horizontale
            if (deplacementX > 0) {
                mState = eMobState.MOVING_RIGHT;
            } else {
                mState = eMobState.MOVING_LEFT;
            }

        } else {
            if (deplacementY > 0) {
                mState = eMobState.MOVING_DOWN;
            } else {
                mState = eMobState.MOVING_UP;
            }
        }
    }

    /**
     * update the portion of the spritesheet to draw accordingly to the mob state + animationstate
     */
    protected void updateSprite() {
        // check l'etat du mob et son stade dans l'animation
        if (mAnimationState == Constants.COMPLETE_ANIMATION_DURATION) {
            if (mState != eMobState.DYING) {
                mAnimationState = 0;
                if (!isJustMoving()) {
                    mState = eMobState.MOVING_DOWN;
                }
            }
        } else {
            mAnimationState++;
        }
        this.mSpriteCurrentColumn = (int) (mAnimationState / Constants.FRAME_DURATION);
//        this.mSpriteCurrentLine = mState;

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
    @Override
    public void draw(Canvas canvas, Paint mBrush, Rect cameraPosition) {
        if (mBitmapId != null && Utils.doRectIntersect(cameraPosition, mPosition)) {
            RectF positionOnSceen = new RectF(mPosition);
            positionOnSceen.offset(-cameraPosition.left, -cameraPosition.top);
            //    Log.i(TAG, "drawing: " + idName + "\n column:" + mSpriteCurrentColumn + " line:" + mState.index);
            canvas.drawBitmap(SpriteRepo.getSpriteBitmap(mBitmapId, mSpriteCurrentColumn, mState.index), null, positionOnSceen, mBrush);

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
     */
    public void manageTouchEvent(GameBoard board) {
        if (isDead())
            return;// deja mort

        if (this.mTouchedMove != null) {
            this.mTouchedMove.doTouchedMove(board, this);
        } else if (mHealth > 1) {
            mHealth--;
            mState = eMobState.HURT;
        } else {
            mHealth = 0;
            mState = eMobState.DYING;
        }
        mAnimationState = 0;

    }

    /**
     * mState=5 (dead) + mAnimationState = complete (already looped one time, showing the death animation)
     *
     * @return
     */
    public boolean isDead() {
        return (mState == eMobState.DYING && mAnimationState == Constants.COMPLETE_ANIMATION_DURATION) ? true : false;
    }

    @Override
    public GameMob clone() {
        GameMob clone;
        PointF[] cloneMovePattern = new PointF[movePattern.length];
        for (int i = 0; i < movePattern.length; i++) {
            cloneMovePattern[i] = new PointF(movePattern[i].x, movePattern[i].y);
        }
        clone = new GameMob(
                idName,
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

    }

    @Override
    public void resize(float ratio) {
//        float oldWidth = getWidth();
//        float newWidth = oldWidth * ratio;
//        float oldHeight = getHeight();
//        float newHeight = oldHeight * ratio;
//        float diffHeight = (oldHeight - newHeight) / 2;
//        float diffWidth = (oldWidth -newWidth ) / 2;

        mPosition = new RectF(mPosition.left * ratio, mPosition.top * ratio, mPosition.right * ratio, mPosition.bottom * ratio);
        movePattern = PathRepo.createScalePath(ratio, movePattern);
        SpriteRepo.resizeSprites(mBitmapId, (int) getWidth(), (int) getHeight());
    }

    public boolean isJustMoving() {
        return mState == eMobState.MOVING_LEFT || mState == eMobState.MOVING_UP || mState == eMobState.MOVING_RIGHT || mState == eMobState.MOVING_DOWN;
    }

    public boolean isDying() {
        return eMobState.DYING == mState;
    }

    public enum eMobState {

        MOVING_LEFT(0),
        MOVING_UP(1),
        MOVING_RIGHT(2),
        MOVING_DOWN(3),
        HURT(4),
        DYING(5),
        SPE1(6),
        SPE2(7);

        public final int index;

        eMobState(int index) {
            this.index = index;
        }
    }

}
