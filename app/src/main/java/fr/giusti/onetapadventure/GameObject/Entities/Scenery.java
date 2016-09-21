package fr.giusti.onetapadventure.gameObject.entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.commons.Utils;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.moves.TouchedMove;
import fr.giusti.onetapadventure.repository.SpriteRepo;

/**
 * Created by jérémy on 10/09/2016.
 */
public class Scenery extends Entity {

    public RectF hitbox;
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
            if (Utils.doRectIntersect(hitbox,mob.mPosition)) {
                touchedByMob.doTouchedMove(board, mob, Constants.TOUCH_DAMAGE);
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint mBrush, Rect cameraPostion) {
        if (Utils.doRectIntersect(cameraPostion, mPosition)) {//On screen
            RectF positionOnSceen = new RectF(mPosition);
            positionOnSceen.offset(-cameraPostion.left, -cameraPostion.top);
            canvas.drawBitmap(SpriteRepo.getPicture(mBitmapId), null, mPosition, mBrush);
        }
    }

    @Override
    public void resize(float ratio) {

        mPosition = new RectF(mPosition.left * ratio, mPosition.top * ratio, mPosition.right * ratio, mPosition.bottom * ratio);

        SpriteRepo.resizePicture(mBitmapId, ratio);

        hitbox = new RectF(hitbox.left * ratio, hitbox.top * ratio, hitbox.right * ratio, hitbox.bottom * ratio);
    }
}
