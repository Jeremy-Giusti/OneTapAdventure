package fr.giusti.onetapadventure.gameObject;

import fr.giusti.onetapadventure.commons.Constants;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class TouchPoint {

    private int x;
    private int y;
    private int state = 0;
    private final static int LENGTH_ANIMATION_FRAME = Constants.FRAME_DURATION;

    public TouchPoint(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    
    public void update(){
        state++;
    }

    public void draw(Canvas canvas, Paint brush) {
        //TODO gerer l'etat
        if (state < 5) {
          canvas.drawCircle(x, y, Constants.TOUCH_STROKE / 2, brush);
        } else if (state > 10) {
            canvas.drawCircle(x, y, Constants.TOUCH_STROKE / 8, brush);
        } else {
            canvas.drawCircle(x, y, Constants.TOUCH_STROKE / 4, brush);
        }
    }
    
    public boolean isEnded(){
        return state>=(LENGTH_ANIMATION_FRAME*Constants.NB_FRAME_ON_ANIMATION);
    }
    
    public static Paint GetPaint(Paint paint){
        paint.setStrokeWidth(1);
        paint.setColor(Color.argb(100,255, 0, 0));
        return paint;
    }
}
