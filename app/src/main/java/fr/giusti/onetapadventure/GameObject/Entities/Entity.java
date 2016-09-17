package fr.giusti.onetapadventure.gameObject.entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.repository.SpriteRepo;

/**
 * Created by jérémy on 10/09/2016.
 */
public abstract class Entity implements Cloneable {
    private static final String TAG = Entity.class.getName();

    public RectF mPosition = new RectF();

    /**
     * unique entity id
     */
    protected String idName;

    /**
     * id de la spriteSheet dans le repo
     */
    protected String mBitmapId;

    // ========================================================| BEAN |==================================================

    /**
     * @param x         position horizontale initial
     * @param y         position verticale initial
     * @param mBitmapId un string qui sert d'id pour aller piocher le skin de l'entity dans le bitmapRepo (cache bitmap)
     */
    public Entity(String idName, int x, int y, int width, int height, String mBitmapId) {
        super();
        this.idName = idName;
        mPosition.set(x, y, x + width, y + height);
        this.mBitmapId = mBitmapId;

    }

    public String getIdName() {
        return idName;
    }

    public void setIdName(String idName) {
        this.idName = idName;
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

    public float getWidth() {
        return mPosition.width();
    }

    public float getHeight() {
        return mPosition.height();
    }


    public void setPosition(RectF position) {
        this.mPosition = position;
    }

    public void setPositionFromXY(int x, int y) {
        mPosition.offsetTo(x, y);
    }

    /**
     * @return id du skin de l'entité (voir {@link SpriteRepo})
     */
    public String getBitmapId() {
        return mBitmapId;
    }

    /**
     * @param mBitmapId id du skin de l'entité (voir {@link SpriteRepo})
     */
    public void setBitmapId(String mBitmapId) {
        this.mBitmapId = mBitmapId;
    }


    // ======================================================| UPDATE |===================================================

    /**
     * met a jour l'entité au terme d'un tick (orientation, position, animation)
     */
    public abstract void update(GameBoard board);


    // =========================================================| DRAW |===================================================

    /**
     * dessine le mob sur le canvas (utilise son skin + position x,y)
     *
     * @param canvas
     * @param mBrush
     */
    public abstract void draw(Canvas canvas, Paint mBrush, Rect cameraBound);



    public abstract void resize(float ratio);


}
