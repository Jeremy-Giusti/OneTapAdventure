package fr.giusti.onetapadventure.gameObject.entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.commons.Utils;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.entityDistribution.ParticuleHolder;
import fr.giusti.onetapadventure.repository.SpriteRepo;

/**
 * Particule are only graphicale element, they cannot interact with anything (they are result of actions, and can't be a cause)
 * Created by giusti on 20/03/2015.
 */
public class Particule extends Entity {

    private static final String TAG = Particule.class.getName();

    /**
     * portion of sprite to use
     */
    protected int mSpirteColumn = 0;
    /**
     * path if the particule move
     */
    protected PointF[] movePattern;
    protected int mAnimationState = 0;

    /**
     * position actuel dans l'array de mouvement sequentiel
     */
    protected int currentMove = 0;

    protected boolean animationReversed = false;


    /**
     * true if the particule is not used
     */
    protected boolean mAvailable = false;


    public Particule(String name, int x, int y, int width, int height, PointF[] movePattern, String mBitmapId, boolean reverseAnimation) {

        super(name, x, y, width, height, mBitmapId);
        this.movePattern = movePattern;
        this.animationReversed = reverseAnimation;
    }


    public RectF getmPosition() {
        return mPosition;
    }

    public void setmPosition(RectF mPosition) {
        this.mPosition = mPosition;
    }

    public void setmPositionFromXY(int x, int y) {
        mPosition.offsetTo(x, y);
    }

    public PointF[] getMovePattern() {
        return movePattern;
    }

    public void setMovePattern(PointF[] movePattern) {
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

    public boolean isAnimationReversed() {
        return animationReversed;
    }

    public void setAnimationReversed(boolean animationReversed) {
        this.animationReversed = animationReversed;
    }

    /**
     * met a jour le mob au terme d'un tick (orientation, position, animation)
     */
    @Override
    public void update(GameBoard board) {
        move();
        updateSprite();
    }

    /**
     * move the particule
     */
    private void move() {
        this.mPosition.offset(movePattern[currentMove].x, movePattern[currentMove].y);

        if ((currentMove + 1) < movePattern.length) currentMove++;
        else currentMove = 0;

    }

    /**
     * update the portion of the spritesheet to draw accordingly to the particule state
     */
    private void updateSprite() {
        // check l'etat du mob et son stade dans l'animation
        if (mAnimationState >= Constants.PARTICULE_COMPLETE_ANIMATION_DURATION || mAnimationState == -1) {
            mAnimationState = -1;
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
    @Override
    public void draw(Canvas canvas, Paint mBrush, Rect cameraPosition) {
        if (Utils.doRectIntersect(cameraPosition, mPosition)) {//On screen
            RectF positionOnSceen = new RectF(mPosition);
            positionOnSceen.offset(-cameraPosition.left, -cameraPosition.top);
            canvas.drawBitmap(SpriteRepo.getSpriteBitmap(mBitmapId, mSpirteColumn, 0), null, positionOnSceen, mBrush);
        }

    }

    @Override
    public void resize(float ratio) {
        //? no need ?
    }

    @Override
    public Particule clone() {
        Particule clone;
        PointF[] cloneMovePattern = new PointF[movePattern.length];
        for (int i = 0; i < movePattern.length; i++) {
            cloneMovePattern[i] = new PointF(movePattern[i].x, movePattern[i].y);
        }
        clone = new Particule(
                idName,
                (int) mPosition.left,
                (int) mPosition.top,
                (int) mPosition.width(),
                (int) mPosition.height(),
                cloneMovePattern,
                mBitmapId,
                this.animationReversed);
        return clone;

    }

    public void recycle() {
        ParticuleHolder.recycle(this);
    }


    public void setAvailable(boolean available) {
        this.mAvailable = available;
    }

    public boolean isAvailable() {
        return mAvailable;
    }
}


