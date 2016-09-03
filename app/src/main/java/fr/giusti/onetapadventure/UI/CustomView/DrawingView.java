//http://obviam.net/index.php/the-android-game-loop/
package fr.giusti.onetapadventure.UI.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import fr.giusti.onetapadventure.GameObject.GameBoard;

public class DrawingView extends SurfaceView implements SurfaceHolder.Callback {
    private Context mContext;
    private DrawingThread mDrawThread;
    private Paint mBrush = new Paint();
    private GameBoard mMap;
//    private float mMapRatioX = 0.5f;
//    private float mMapRatioY = 0.5f;
    final Handler handler = new Handler();

    public DrawingView(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        // make the GamePanel focusable so it can handle events
        mContext = context;
        setFocusable(true);
       // mDrawThread = new DrawingThread(getHolder(), mContext, this);
        mBrush.setColor(mContext.getResources().getColor(android.R.color.darker_gray));
        mBrush.setStrokeWidth(5);
        initEvent();

    }

    public DrawingView(Context context, AttributeSet attr) {
        super(context, attr);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        // make the GamePanel focusable so it can handle events
        setFocusable(true);
        mContext = context;
       // mDrawThread = new DrawingThread(getHolder(), mContext, this);
        mBrush.setColor(mContext.getResources().getColor(android.R.color.darker_gray));
        mBrush.setStrokeWidth(5);
        initEvent();

    }

    /**
     * event listeners
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {
        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
           //     event.setLocation(event.getX() / mMapRatioX, event.getY() / mMapRatioY);
                mMap.touchEvent(event);
                return false;
            }
        });
    }

    public void startGame(GameBoard map) {
        
        mDrawThread=new DrawingThread(getHolder(), this);
        mMap = map;
//        mMapRatioX = this.getWidth() / mMap.mBoardWidth;
//        mMapRatioY = this.getHeight() / mMap.mBoardHeight;
        mDrawThread.setRunning(true);
        mDrawThread.start();
    }

    /**
     * put an end to the thread execution
     * @throws InterruptedException 
     */
    public void stopGame() throws InterruptedException {
        mDrawThread.setRunning(false);
    }
    

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mMap != null) {
            // restart after being destroyed
            if (mDrawThread.getState() == Thread.State.TERMINATED) {
                mDrawThread = new DrawingThread(getHolder(), this);
                mDrawThread.setRunning(true);
                mDrawThread.start();
            }
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Auto-generated method stub

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mDrawThread.setRunning(false);
        // thread.join()
    }

    public void doDraw(Canvas canvas) {
        mMap.draw(canvas, mBrush);

    }

    /**
     *
     */
    public void update() {
        mMap.update();

    }

    public void onSystemePause() throws InterruptedException {
        mDrawThread.onPause();

    }

    public void onSystemeResume() {
        if (mMap != null) {
            // restart after just pausing
            mDrawThread.onResume();
        }

    }

}
