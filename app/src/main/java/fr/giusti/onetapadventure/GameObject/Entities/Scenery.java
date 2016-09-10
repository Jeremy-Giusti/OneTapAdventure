package fr.giusti.onetapadventure.GameObject.Entities;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.GameObject.moves.TouchedMove;
import fr.giusti.onetapadventure.Repository.SpriteRepo;

/**
 * Created by jérémy on 10/09/2016.
 */
public class Scenery extends GameBoardEntity {

    private RectF hitbox;
    private TouchedMove touchedByMob;

    /**
     * @param idName
     * @param x         position horizontale initial
     * @param y         position verticale initial
     * @param width
     * @param height
     * @param mBitmapId un string qui sert d'id pour aller piocher le skin de l'entity dans le bitmapRepo (cache bitmap)
     */
    public Scenery(String idName, int x, int y, int width, int height, RectF hitbox, TouchedMove onColiision, String mBitmapId) {
        super(idName, x, y, width, height, mBitmapId);
        touchedByMob = onColiision;
        this.hitbox = hitbox;
    }

    public Scenery(String idName, int x, int y, int width, int height, float hitboxRatio, TouchedMove onColiision, String mBitmapId) {
        super(idName, x, y, width, height, mBitmapId);
        touchedByMob = onColiision;

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
        for(GameMob mob : board.getMobs()){
            if(hitbox.intersect(mob.mPosition)){
                touchedByMob.doTouchedMove(board,mob,null);
            }
        }
    }

    @Override
    public void draw(Canvas canvas, Paint mBrush) {
        canvas.drawBitmap(SpriteRepo.getPicture(mBitmapId), null, mPosition, mBrush);
    }

    @Override
    public void resize(float ratio) {
        float oldWidth = getWidth();
        float newWidth = oldWidth * ratio;
        float oldHeight = getHeight();
        float newHeight = oldHeight * ratio;
        float diffHeight = (newHeight - oldHeight) / 2;
        float diffWidth = (newWidth - oldWidth) / 2;

        mPosition = new RectF(mPosition.left - diffWidth, mPosition.top - diffHeight, mPosition.right + diffWidth, mPosition.bottom + diffHeight);

        SpriteRepo.resizePicture(mBitmapId, ratio);

        float oldWidthHB = hitbox.width();
        float newWidthHB = oldWidthHB * ratio;
        float oldHeightHB = hitbox.height();
        float newHeightHB = oldHeightHB * ratio;
        float diffHeightHB = (newHeightHB - oldHeightHB) / 2;
        float diffWidthHB = (newWidthHB - oldWidthHB) / 2;

        hitbox = new RectF(hitbox.left - diffWidthHB, hitbox.top - diffHeightHB, hitbox.right + diffWidthHB, hitbox.bottom + diffHeightHB);

    }
}
