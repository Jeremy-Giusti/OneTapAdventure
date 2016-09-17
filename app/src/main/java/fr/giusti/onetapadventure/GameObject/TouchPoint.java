package fr.giusti.onetapadventure.gameObject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.List;

import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.commons.Utils;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;

public class TouchPoint {

    public RectF mPosition;
    private int state = 0;
    private final static int LENGTH_ANIMATION_FRAME = Constants.FRAME_DURATION;
    private boolean actionDone = false;


    public TouchPoint(float x, float y, int reach) {
        super();
        float halfReach = reach / 2;
        mPosition = new RectF(x - halfReach, y - halfReach, x + halfReach, y + halfReach);
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
                if (Utils.doRectIntersect(mPosition, mob.mPosition) && ! mob.isDying()) {
                    mob.manageTouchEvent(board);
                }
            }
        }
        state++;
    }

    public void draw(Canvas canvas, Paint brush, Rect cameraPostion) {

        if (Utils.doRectIntersect(cameraPostion, mPosition)) {//On screen
            RectF positionOnSceen = new RectF(mPosition);
            positionOnSceen.offset(-cameraPostion.left, -cameraPostion.top);
            if (state < 5) {
                canvas.drawRect(positionOnSceen,brush);
               // canvas.drawCircle(mPosition.centerX(), mPosition.centerY(), Constants.TOUCH_STROKE * 2 / 2, brush);
            } else if (state > 10) {
                canvas.drawRect(positionOnSceen,brush);
               // canvas.drawCircle(mPosition.centerX(), mPosition.centerY(), Constants.TOUCH_STROKE * 2 / 8, brush);
            } else {
                canvas.drawRect(positionOnSceen,brush);
                // canvas.drawCircle(mPosition.centerX(), mPosition.centerY(), Constants.TOUCH_STROKE * 2 / 4, brush);
            }
        }
    }

    public boolean isEnded() {
        return state >= (LENGTH_ANIMATION_FRAME * Constants.NB_FRAME_ON_ANIMATION);
    }

    public static Paint GetPaint(Paint paint) {
        paint.setStrokeWidth(1);
        paint.setColor(Color.argb(100, 255, 0, 0));
        return paint;
    }
}
