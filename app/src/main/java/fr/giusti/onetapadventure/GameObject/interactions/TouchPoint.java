package fr.giusti.onetapadventure.gameObject.interactions;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.List;

import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.commons.GameConstant;
import fr.giusti.onetapadventure.commons.Utils;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.gameObject.entities.entityDistribution.ParticuleHolder;
import fr.giusti.onetapadventure.repository.SpriteRepo;
import fr.giusti.onetapadventure.repository.entities.ParticuleRepo;

public class TouchPoint {

    public RectF mPosition;
    private int state = 0;
    private final static int LENGTH_ANIMATION_FRAME = Constants.FRAME_DURATION;
    private boolean actionDone = false;
    private String spriteId = null;
    private int stroke = GameConstant.TOUCH_STROKE;
    private int damage = GameConstant.TOUCH_DAMAGE;


    public TouchPoint(float x, float y, int stroke) {
        super();
        this.stroke = stroke;
        float halfReach = stroke / 2;
        mPosition = new RectF(x - halfReach, y - halfReach, x + halfReach, y + halfReach);
    }

    public TouchPoint(float x, float y, String spriteId, int stroke, int damage) {
        float halfReach = stroke / 2;
        mPosition = new RectF(x - halfReach, y - halfReach, x + halfReach, y + halfReach);

        this.spriteId = spriteId;
        this.stroke = stroke;
        this.damage = damage;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void update(GameBoard board) {
        if (!actionDone) {
            actionDone = true;
            List<GameMob> mobList = board.getMobs();
            for (GameMob mob : mobList) {
                if (Utils.doRectIntersect(mPosition, mob.mPosition) && mob.isActive()) {
                    mob.manageTouchEvent(board, damage);
                }
            }
        }
        if(state<1){
            board.addParticules(ParticuleHolder.getAvailableParticuleGroupe(ParticuleRepo.GROUPE_SPARK_PARTICULE,mPosition,new PointF(0,0),5));
        }

        state++;
    }

    public void draw(Canvas canvas, Paint brush, Rect cameraPostion) {

        if (Utils.doRectIntersect(cameraPostion, mPosition)) {//On screen
            RectF positionOnSceen = new RectF(mPosition);
            positionOnSceen.offset(-cameraPostion.left, -cameraPostion.top);
            if (spriteId == null) {
                if (state < 5) {
                    canvas.drawRect(positionOnSceen, brush);
                    // canvas.drawCircle(mPosition.centerX(), mPosition.centerY(), Constants.TOUCH_STROKE * 2 / 2, brush);
                } else if (state > 10) {
                    canvas.drawRect(positionOnSceen, brush);
                    // canvas.drawCircle(mPosition.centerX(), mPosition.centerY(), Constants.TOUCH_STROKE * 2 / 8, brush);
                } else {
                    canvas.drawRect(positionOnSceen, brush);
                    // canvas.drawCircle(mPosition.centerX(), mPosition.centerY(), Constants.TOUCH_STROKE * 2 / 4, brush);
                }
            } else if (!isEnded()) {
                int currentFrame = state / LENGTH_ANIMATION_FRAME;
                canvas.drawBitmap(SpriteRepo.getSpriteBitmap(spriteId, currentFrame, 0), null, positionOnSceen, brush);
            }
        }
    }

    public boolean isEnded() {
        return state >= (LENGTH_ANIMATION_FRAME * Constants.PARTICULE_NB_FRAME_ON_ANIMATION);
    }

    public static Paint GetPaint(Paint paint) {
        paint.setStrokeWidth(1);
        paint.setColor(Color.argb(100, 255, 0, 0));
        return paint;
    }
}
