package fr.giusti.onetapengine.entity;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Arrays;

import fr.giusti.onetapengine.GameBoard;
import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.commons.Utils;
import fr.giusti.onetapengine.entity.moves.SpecialMove;
import fr.giusti.onetapengine.entity.moves.TouchedMove;
import fr.giusti.onetapengine.repository.PathRepo;
import fr.giusti.onetapengine.repository.SpecialMoveRepo;
import fr.giusti.onetapengine.repository.SpriteRepo;
import fr.giusti.onetapengine.repository.TouchedMoveRepo;

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
     * multiplcateur pour les mouvements horizontaux (permet d'amplifier/diminuer/inverser l'amplitude des mouvements)
     */
    public float xAlteration = 1;

    /**
     * multiplcateur pour les mouvements verticaux (permet d'amplifier/diminuer/inverser l'amplitude des mouvements)
     */
    public float yAlteration = 1;

    /**
     * position actuel dans l'array de mouvement sequentiel
     */
    protected int currentMove = 0;

    /**
     * mob life
     */
    private int mHealth = 1;

    /**
     * mob default life
     */
    private final int mDefaultHealth;

    /**
     * stat<4 = normal,
     * 0=left, 1 haut, 2 bas, 3 droite
     * 4= touché, 5 mourant, 6 spe1, 7 spe2
     */
    protected eMobState mState = eMobState.MOVING_UP;

    private final int mScoreValue;

    /**
     * 0-10,10-20,20-30 utilisé pour selectioner les sprite en sequence (joue l'animation)
     */
    private int mAnimationState = 0;

    private Paint paint = new Paint();

    // ========================================================| BEAN |==================================================


//    public GameMob(String idName, int x, int y, int width, int height, PointF[] movePattern, SpecialMove specialMove1, TouchedMove touchedmove, String mBitmapId, int health, int state) {
//        super(idName, x, y, width, height, mBitmapId);
//        this.movePattern = movePattern;
//        this.mSpecialMove1 = specialMove1;
//        this.mTouchedMove = touchedmove;
//        this.mHealth = health;
//        this.mDefaultHealth = health;
//        this.mState = eMobState.values()[state];
//    }

    /**
     * @param x           position horizontale initial
     * @param y           position verticale initial
     * @param movePattern une liste de point, chaque point represente la distance x et y qu'il va parcourir à un tick
     * @param mBitmapId   un string qui sert d'id pour aller piocher le skin du mob dans le bitmapRepo (cache bitmap)
     * @param state       l'etat du mob (inutilisé pour le moment)
     */
    private GameMob(String idName, int x, int y, int width, int height, PointF[] movePattern, SpecialMove specialMove1, TouchedMove touchedmove, String mBitmapId, int health, eMobState state, int score) {
        super(idName, x, y, width, height, mBitmapId);
        this.movePattern = movePattern;
        this.mSpecialMove1 = specialMove1;
        this.mTouchedMove = touchedmove;
        this.mHealth = health;
        this.mDefaultHealth = health;
        this.mState = state;
        this.mScoreValue = score;
    }

    private GameMob(MobBuilder mobBuilder) {
        super(mobBuilder.idName, mobBuilder.mSpriteSheetId, mobBuilder.mPosition);
        this.movePattern = mobBuilder.movePattern;
        this.mSpecialMove1 = mobBuilder.mSpecialMove1;
        this.mTouchedMove = mobBuilder.mTouchedMove;
        this.mHealth = mobBuilder.mHealth;
        this.mDefaultHealth = mobBuilder.mDefaultHealth;
        this.mState = mobBuilder.mState;
        this.mScoreValue = mobBuilder.scoreValue;
        this.alignement = mobBuilder.alignement;
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

    public float getxAlteration() {
        return xAlteration;
    }

    public void setxAlteration(float xAlteration) {
        this.xAlteration = xAlteration;
    }

    public float getyAlteration() {
        return yAlteration;
    }

    public void setyAlteration(float yAlteration) {
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

    public int getScoreValue() {
        return mScoreValue;
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

    public PointF getAlteredCurrentMove() {
        return new PointF(movePattern[currentMove].x * xAlteration, movePattern[currentMove].y * yAlteration);
    }


    public void setCurrentMove(int newCurrentMove) {
        currentMove = newCurrentMove;
    }


    public void setMovePattern(PointF[] movePattern) {
        this.movePattern = movePattern;
        currentMove = 0;
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

        if (hasDisapperred()) {
            board.onMobAway(this);
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
        if (mState != eMobState.GOING_AWAY) {//ne bouge plus quand il disparait
            this.mPosition.offset(deplacementX, deplacementY);
            if (mState != eMobState.DYING) {//si il est mort il ne change plus de direction
                if (currentMove < (movePattern.length - 1)) {
                    currentMove++;
                } else {
                    currentMove = 0;
                }
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
            if (isActive()) {
                mAnimationState = 0;
                if (!isJustMoving()) {
                    mState = eMobState.MOVING_DOWN;
                }
            }
        } else {
            mAnimationState++;
        }
        this.mSpriteCurrentColumn = mAnimationState / Constants.FRAME_DURATION;

    }

    private void doSpecialMove(GameBoard board) {
        if (isActive()) {
            if (this.mSpecialMove1 != null) {
                this.mSpecialMove1.doSpecialMove(board, this);
            }
        }
    }

    // =========================================================| DRAW |===================================================

    /**
     * dessine le mob sur le canvas (utilise son skin + position x,y)
     *
     * @param canvas
     * @param brush
     */
    @Override
    public void draw(Canvas canvas, Paint brush, Rect cameraPosition) {
        if (mBitmapId != null && Utils.doRectIntersect(cameraPosition, mPosition)) {
            RectF positionOnSceen = new RectF(mPosition);
            positionOnSceen.offset(-cameraPosition.left, -cameraPosition.top);

            if (mState == eMobState.GOING_AWAY) {
                paint.setAlpha((int) (255 - ((mAnimationState / (float) Constants.COMPLETE_ANIMATION_DURATION) * 255)));
                canvas.drawBitmap(SpriteRepo.getSpriteBitmap(mBitmapId, mSpriteCurrentColumn, 3), null, positionOnSceen, paint);
            } else {
                if (eMobState.HURT == mState) {
                    drawLife(canvas, paint, positionOnSceen);
                }
                canvas.drawBitmap(SpriteRepo.getSpriteBitmap(mBitmapId, mSpriteCurrentColumn, mState.index), null, positionOnSceen, paint);
            }
        }

    }

    private void drawLife(Canvas canvas, Paint brush, RectF positionOnSceen) {
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeWidth(4);
        RectF heathBarPos = new RectF(positionOnSceen);
        //TODO use percentage of mob height for offset ?
        heathBarPos.offset(0, 20);

        brush.setARGB(255, 255, 0, 0);
        canvas.drawArc(heathBarPos, 0, 180, false, brush);

        brush.setARGB(255, 0, 255, 0);
        canvas.drawArc(heathBarPos, 0, 180 * (mHealth / (float) mDefaultHealth), false, brush);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public void setAlpha(int alpha) {
        this.paint.setAlpha(alpha);
    }


    // =======================================================| EVENTS |===================================================

    /**
     * gere un clique et met a jour le status du mob en fonction
     */
    public void manageTouchEvent(GameBoard board, int damage) {
        if (isDead())
            return;// deja mort

        if (this.mTouchedMove != null) {
            this.mTouchedMove.doTouchedMove(board, this, damage);
        } else if (mHealth > damage) {
            mHealth -= damage;
            mState = eMobState.HURT;
        } else {
            mHealth = 0;
            mState = eMobState.DYING;
        }
        mAnimationState = 0;

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
                mState,
                mScoreValue);

        return clone;

    }

    @Override
    public void resize(float ratio) {

        mPosition = new RectF(mPosition.left * ratio, mPosition.top * ratio, mPosition.right * ratio, mPosition.bottom * ratio);
        movePattern = PathRepo.createScalePath(ratio, movePattern);
        SpriteRepo.resizeSprites(mBitmapId, (int) getWidth(), (int) getHeight());
    }

    public boolean isJustMoving() {
        return mState == eMobState.MOVING_LEFT || mState == eMobState.MOVING_UP || mState == eMobState.MOVING_RIGHT || mState == eMobState.MOVING_DOWN;
    }

    /**
     * mState=5 (dead) + mAnimationState = complete (already looped one time, showing the death animation)
     *
     * @return
     */
    public boolean isDying() {
        return eMobState.DYING == mState;
    }


    public boolean hasDisapperred() {
        return (mState == eMobState.GOING_AWAY && mAnimationState == Constants.COMPLETE_ANIMATION_DURATION);
    }

    /**
     * mState=5 (dead) + mAnimationState = complete (already looped one time, showing the death animation)
     *
     * @return
     */
    public boolean isDead() {
        return (mState == eMobState.DYING && mAnimationState == Constants.COMPLETE_ANIMATION_DURATION);
    }

    public boolean isActive() {
        return eMobState.DYING != mState && eMobState.GOING_AWAY != mState;
    }

    public void setDisappering() {
        if (eMobState.GOING_AWAY != mState) {
            mState = eMobState.GOING_AWAY;
            mAnimationState = 1;
        }
    }


    public static class MobBuilder {
        protected String idName;
        protected String mSpriteSheetId;
        protected PointF[] movePattern = new PointF[]{new PointF(0, 0)};
        private int alignement = 0;
        private SpecialMove mSpecialMove1 = SpecialMoveRepo.getMoveById(SpecialMoveRepo.NO_MOVE);
        private TouchedMove mTouchedMove = TouchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE);
        private int mHealth = GameConstant.TOUCH_DAMAGE;
        private int mDefaultHealth = GameConstant.TOUCH_DAMAGE;
        public RectF mPosition = new RectF();
        public eMobState mState = eMobState.MOVING_DOWN;
        public int scoreValue = 50;

        public MobBuilder(String idName, String mSpriteSheetId, float x, float y) {
            this.idName = idName;
            this.mSpriteSheetId = mSpriteSheetId;
            mPosition = new RectF(x - (GameConstant.DEFAULT_MOB_SIZE / 2), y - (GameConstant.DEFAULT_MOB_SIZE / 2), x + (GameConstant.DEFAULT_MOB_SIZE / 2), y + (GameConstant.DEFAULT_MOB_SIZE / 2));
        }

        public MobBuilder setMovePattern(PointF[] movePattern) {
            this.movePattern = movePattern;
            return this;
        }

        /**
         * set default health AND health attribut
         *
         * @param health
         * @return
         */
        public MobBuilder setDefaultHealth(int health) {
            this.mDefaultHealth = health;
            this.mHealth = health;
            return this;
        }

        public MobBuilder setHealth(int health) {
            this.mHealth = health;
            return this;
        }

        public MobBuilder setAlignement(int alignement) {
            this.alignement = alignement;
            return this;
        }

        public MobBuilder setSpecialMove(SpecialMove specialMove1) {
            this.mSpecialMove1 = specialMove1;
            return this;
        }

        public MobBuilder setTouchedMove(TouchedMove touchedMove) {
            this.mTouchedMove = touchedMove;
            return this;
        }

        public MobBuilder setState(eMobState state) {
            this.mState = state;
            return this;
        }

        public MobBuilder setScore(int score) {
            this.scoreValue = score;
            return this;
        }

        public MobBuilder setSize(int size) {
            mPosition = new RectF(mPosition.centerX() - size / 2, mPosition.centerY() - size / 2, mPosition.centerX() + size / 2, mPosition.centerY() + size / 2);
            return this;
        }

        public MobBuilder setHeight(int height) {
            mPosition = new RectF(mPosition.left, mPosition.centerY() - height / 2, mPosition.right, mPosition.centerY() + height / 2);
            return this;
        }

        public MobBuilder setWidth(int width) {
            mPosition = new RectF(mPosition.centerX() - width / 2, mPosition.top, mPosition.centerX() + width / 2, mPosition.bottom);
            return this;
        }

        public GameMob build() {
            return new GameMob(this);
        }

    }

    public enum eMobState {

        MOVING_LEFT(0, true),
        MOVING_UP(1, true),
        MOVING_RIGHT(2, true),
        MOVING_DOWN(3, true),
        HURT(4, true),
        DYING(5, true),
        SPE1(6, true),
        SPE2(7, true),
        GOING_AWAY(8, false);

        public final int index;
        public final boolean animated;

        private static ArrayList<eMobState> animatedState = null;


        eMobState(int index, boolean animated) {
            this.index = index;
            this.animated = animated;
        }

        /**
         * @return the list of state that can be displayed with sprites.
         */
        public static ArrayList<eMobState> getAnimatedState() {
            if (animatedState == null) {
                animatedState = new ArrayList<eMobState>(Arrays.asList(eMobState.values()));
                for (int i = 0; i < animatedState.size(); i++) {
                    if (!animatedState.get(i).animated) {
                        animatedState.remove(i);
                        i--;
                    }
                }
            }
            return animatedState;
        }
    }

}
