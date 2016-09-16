package fr.giusti.onetapadventure.UI.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * surface de dessin light,
 * trace un chemin en suivant le touché de l'utilisateur
 * @author giusti
 *
 */
public class PathDrawingView extends View {

    private static Path mTouchPath = new Path();
    private Paint mTouchPaint;

    public PathDrawingView(Context context) {
        super(context);

        init();
    }

    public PathDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public PathDrawingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    /**
     * settup the brush and empty the drawing cache
     */
    private void init() {
        mTouchPaint = new Paint();
        mTouchPaint.setStrokeWidth(4);
        mTouchPaint.setColor(Color.WHITE);
        mTouchPaint.setStyle(Style.STROKE);
        mTouchPaint.setStrokeCap(Cap.ROUND);
        mTouchPaint.setAntiAlias(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                reset();
                mTouchPath.moveTo(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_MOVE:
                mTouchPath.lineTo(event.getX(), event.getY());
                break;

            default:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mTouchPath, mTouchPaint);

    }
    
    /**
     * supprime le dessin precedent
     */
    public void reset(){
        mTouchPath=new Path();
    }

}
