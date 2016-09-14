package fr.giusti.onetapadventure.gameObject.entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.moves.TouchedMove;
import fr.giusti.onetapadventure.repository.SpriteRepo;

/**
 * Created by jérémy on 10/09/2016.
 */
public class Scenery extends Entity {

    public RectF hitbox;
    private RectF hitboxTemp;
    private TouchedMove touchedByMob;

    /**
     * @param idName
     * @param x         position horizontale initial
     * @param y         position verticale initial
     * @param width
     * @param height
     * @param mBitmapId un string qui sert d'id pour aller piocher le skin de l'entity dans le bitmapRepo (cache bitmap)
     */
    public Scenery(String idName, int x, int y, int width, int height, RectF hitbox, TouchedMove onCollision, String mBitmapId) {
        super(idName, x, y, width, height, mBitmapId);
        touchedByMob = onCollision;
        this.hitbox = hitbox;
        hitboxTemp = new RectF(hitbox);
    }

    public Scenery(String idName, int x, int y, int width, int height, float hitboxRatio, TouchedMove onCollision, String mBitmapId) {
        super(idName, x, y, width, height, mBitmapId);
        touchedByMob = onCollision;

        float widthHB = width * hitboxRatio;
        float heightHB = height * hitboxRatio;

        int leftMargin = (int) ((width - widthHB) / 2);
        int topMargin = (int) ((height - heightHB) / 2);

        float xHB = x + leftMargin;
        float yHB = y + topMargin;

        this.hitbox = new RectF(xHB, yHB, xHB + widthHB, yHB + heightHB);
    }

    @Override
    public void update(GameBoard board) {
        for (GameMob mob : board.getMobs()) {
            if (hitboxTemp.intersect(mob.mPosition)) {
                hitboxTemp = new RectF(hitbox);
                touchedByMob.doTouchedMove(board, mob, null);
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint mBrush) {
        canvas.drawBitmap(SpriteRepo.getPicture(mBitmapId), null, mPosition, mBrush);
    }

    @Override
    public void resize(float ratio) {
//        float oldWidth = getWidth();
//        float newWidth = oldWidth * ratio;
//        float oldHeight = getHeight();
//        float newHeight = oldHeight * ratio;
//        float diffHeight = (newHeight - oldHeight) / 2;
//        float diffWidth = (newWidth - oldWidth) / 2;

        mPosition = new RectF(mPosition.left * ratio, mPosition.top * ratio, mPosition.right * ratio, mPosition.bottom * ratio);

        SpriteRepo.resizePicture(mBitmapId, ratio);

//        float oldWidthHB = hitbox.width();
//        float newWidthHB = oldWidthHB * ratio;
//        float oldHeightHB = hitbox.height();
//        float newHeightHB = oldHeightHB * ratio;
//        float diffHeightHB = (newHeightHB - oldHeightHB) / 2;
//        float diffWidthHB = (newWidthHB - oldWidthHB) / 2;

        hitbox = new RectF(hitbox.left * ratio, hitbox.top * ratio, hitbox.right * ratio, hitbox.bottom * ratio);
        hitboxTemp = new RectF(hitbox);
    }
}
