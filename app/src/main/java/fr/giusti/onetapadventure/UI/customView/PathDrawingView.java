package fr.giusti.onetapadventure.UI.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import fr.giusti.onetapadventure.R;

/**
 * trace/affiche un chemin en suivant le touché de l'utilisateur
 *
 * @author giusti
 */
public class PathDrawingView extends View {

    private static Path mTouchPath = new Path();
    private Paint mTouchPaint;
    private boolean mReadOnly = false;

    public PathDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttr(attrs, context);
        init();
    }

    private void handleAttr(AttributeSet attrs, Context context) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PathDrawingView, 0, 0);
        try {
            mReadOnly = ta.getBoolean(R.styleable.PathDrawingView_readOnly, false);
        } finally {
            ta.recycle();
        }
    }

    /**
     * settup the brush and empty the drawing cache
     */
    private void init() {
        mTouchPaint = new Paint();
        mTouchPaint.setStrokeWidth(4);
        mTouchPaint.setColor(Color.BLACK);
        mTouchPaint.setStyle(Style.STROKE);
        mTouchPaint.setStrokeCap(Cap.ROUND);
        mTouchPaint.setAntiAlias(true);
    }

    /**
     * set the path to draw when/if the view is ready
     * @param rawPath it's not a list of point but a list of deplacement (pointf.x => horizontale movement)
     */
    public void setPath(@NonNull final PointF[] rawPath) {
        if (getMeasuredHeight() == 0) {
            //displayPath view is loaded
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    displayPath(rawPath);
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        } else {
            displayPath(rawPath);
        }
    }

    /**
     *  the path will be centered but not scaled, so it may exceed view bounds
     * @param rawPath it's not a list of point but a list of movement (pointf.x => horizontal movement)
     */
    private void displayPath(@NonNull final PointF[] rawPath) {
        float minX = 0;
        float maxX = 0;
        float minY = 0;
        float maxY = 0;

        PointF currentPosition = new PointF(0, 0);
        for (PointF rawPoint : rawPath) {
            currentPosition.x += rawPoint.x;
            currentPosition.y += rawPoint.y;
            if (minX > currentPosition.x) minX = currentPosition.x;
            else if (maxX < currentPosition.x) maxX = currentPosition.x;
            if (minY > currentPosition.y) minY = currentPosition.y;
            else if (maxY < currentPosition.y) maxY = currentPosition.y;
        }

        float midX = (maxX + minX) / 2;
        float midY = (maxY + minY) / 2;

        float midWidth = getMeasuredWidth() / 2;
        float midHeight = getMeasuredHeight() / 2;

        float xModificator = midWidth - midX;
        float yModificator = midHeight - midY;

        mTouchPath = new Path();
        currentPosition = new PointF(xModificator, yModificator);
        mTouchPath.moveTo(currentPosition.x, currentPosition.y);
        for (PointF rawPoint : rawPath) {
            currentPosition.x += rawPoint.x;
            currentPosition.y += rawPoint.y;
            mTouchPath.lineTo(currentPosition.x, currentPosition.y);
        }

        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mReadOnly) {
            //still allow onClick
            if (MotionEvent.ACTION_UP == event.getActionMasked()) {
                this.performClick();
            }
            return true;
        }
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                reset();
                mTouchPath.moveTo(event.getX(), event.getY());
                break;

            case MotionEvent.ACTION_UP:
                this.performClick();
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
    public void reset() {
        mTouchPath = new Path();
    }

}
