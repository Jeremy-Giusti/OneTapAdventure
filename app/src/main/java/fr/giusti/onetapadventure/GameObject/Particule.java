package fr.giusti.onetapadventure.GameObject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import fr.giusti.onetapadventure.Repository.SpriteRepo;
import fr.giusti.onetapadventure.commons.Constants;

/**
 * Particule are only graphicale element, they cannot interact with anything (they are result of actions, and can't be a cause)
 * Created by giusti on 20/03/2015.
 */
public class Particule implements Cloneable {

    private static final String TAG = Particule.class.getName();
    /**
     * unique Particule id
     */
    protected String name;

    /**
     * portion of sprite to use
     */
    protected int mSpirteColumn = 0;
    public RectF mPosition = new RectF();
    /**
     * path if the particule move
     */
    protected Point[] movePattern;
    protected int mAnimationState = 0;

    /**
     * position actuel dans l'array de mouvement sequentiel
     */
    protected int currentMove = 0;

    /**
     * id de la spriteSheet dans le repo
     */
    protected String mBitmapId;

    protected boolean animationReversed = false;

    protected boolean infinite=false;



    public Particule(String name, int x, int y, int width, int height, Point[] movePattern, String mBitmapId, boolean reverseAnimation, boolean infinite) {
        this.name = name;
        this.mPosition.set(x, y, x + width, y + height);
        this.movePattern = movePattern;
        this.mBitmapId = mBitmapId;
        this.animationReversed = reverseAnimation;
        this.infinite=infinite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RectF getmPosition() {
        return mPosition;
    }

    public float getPositionX(){
        return mPosition.centerX();
    }

    public float getPositionY(){
        return mPosition.centerY();
    }

    public void setmPosition(RectF mPosition) {
        this.mPosition = mPosition;
    }

    public void setmPositionFromXY(int x, int y) {
        mPosition.offsetTo(x, y);
    }

    public Point[] getMovePattern() {
        return movePattern;
    }

    public void setMovePattern(Point[] movePattern) {
        this.movePattern = movePattern;
    }

    public int getmAnimationState() {
        return mAnimationState;
    }

    public void setmAnimationState(int mAnimationState) {
        this.mAnimationState = mAnimationState;
    }

    public int getCurrentMove() {
        return currentMove;
    }

    public void setCurrentMove(int currentMove) {
        this.currentMove = currentMove;
    }

    public String getmBitmapId() {
        return mBitmapId;
    }

    public void setmBitmapId(String mBitmapId) {
        this.mBitmapId = mBitmapId;
    }

    public boolean isAnimationReversed() {
        return animationReversed;
    }

    public void setAnimationReversed(boolean animationReversed) {
        this.animationReversed = animationReversed;
    }

    /**
     * met a jour le mob au terme d'un tick (orientation, position, animation)
     */
    public void update() {
        move();
        //TODO map collision ?
        updateSprite();
    }

    /**
     * move the particule
     */
    private void move() {
        this.mPosition.offset(movePattern[currentMove].x, movePattern[currentMove].y);

        currentMove = 0;

    }

    /**
     * update the portion of the spritesheet to draw accordingly to the particule state
     */
    private void updateSprite() {
        // check l'etat du mob et son stade dans l'animation
        if (mAnimationState >= Constants.PARTICULE_COMPLETE_ANIMATION_DURATION || mAnimationState == -1) {
            mAnimationState = (infinite) ?  0 : -1;
        } else {
            if (animationReversed) {
                this.mSpirteColumn = (int) (((Constants.PARTICULE_COMPLETE_ANIMATION_DURATION - 1) - mAnimationState) / Constants.FRAME_DURATION);
            } else {
                // definie la column du spritesheet à decouper
                this.mSpirteColumn = (int) (mAnimationState / Constants.FRAME_DURATION);
            }
            mAnimationState++;
        }
    }

    /**
     * dessine la particule sur le canvas (utilise son skin + position x,y)
     *
     * @param canvas
     * @param mBrush
     */
    public void draw(Canvas canvas, Paint mBrush) {
        //RectF rectPositionOnScreen = new RectF(mPosition);
        //da fuck
       // rectPositionOnScreen.offsetTo(mPosition.left, mPosition.top);
        canvas.drawBitmap(SpriteRepo.getSpriteBitmap(mBitmapId, mSpirteColumn, 0), null, mPosition, mBrush);

    }

    @Override
    public Particule clone() {
        Particule clone;
        Point[] cloneMovePattern = new Point[movePattern.length];
        for (int i = 0; i < movePattern.length; i++) {
            cloneMovePattern[i] = new Point(movePattern[i].x, movePattern[i].y);
        }
        clone = new Particule(
                name,
                (int) mPosition.left,
                (int) mPosition.top,
                (int) mPosition.width(),
                (int) mPosition.height(),
                cloneMovePattern,
                mBitmapId,
                this.animationReversed,
                infinite);
        return clone;


//        try {
//            return (Particule) super.clone();
//        } catch (CloneNotSupportedException e) {
//           Log.e(TAG, "cloneNotSupported: " + e);
//        }
//        return null;
    }


}


