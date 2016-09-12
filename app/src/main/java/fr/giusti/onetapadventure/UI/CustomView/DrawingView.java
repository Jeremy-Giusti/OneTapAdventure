//http://obviam.net/index.php/the-android-game-loop/
package fr.giusti.onetapadventure.UI.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import fr.giusti.onetapadventure.gameObject.GameBoard;

public class DrawingView extends SurfaceView implements SurfaceHolder.Callback, TickingThread.OnTickListener {
    private static final String TAG = DrawingView.class.getSimpleName();
    private Context mContext;
    private TickingThread mDrawThread;
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
        // mDrawThread = new TickingThread(getHolder(), mContext, this);
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
        // mDrawThread = new TickingThread(getHolder(), mContext, this);
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

        mDrawThread = new TickingThread(this);
        mMap = map;
        this.resize();
//        mMapRatioX = this.getWidth() / mMap.mBoardWidth;
//        mMapRatioY = this.getHeight() / mMap.mBoardHeight;
        mDrawThread.setRunning(true);
        Log.d(TAG,"Drawing view ready, starting drawing thread");

        mDrawThread.start();
    }

    private void resize() {
        getLayoutParams().width = mMap.getmCameraBound().width();
        getLayoutParams().height = mMap.getmCameraBound().height();
    }

    /**
     * put an end to the thread execution
     *
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
                mDrawThread = new TickingThread(this);
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


    /**
     * draw and update map
     *
     * @return
     */
    @Override
    public void onTick() {

        Canvas canvas = null;
        // try locking the canvas for exclusive pixel editing
        // in the surface
        try {
            canvas = this.getHolder().lockCanvas();
            synchronized (this.getHolder()) {
                // update game state
                this.update();
                // render state to the screen
                // draws the canvas on the panel
                this.doDraw(canvas);
            }
        } finally {
            // in case of an exception the surface is not left in
            // an inconsistent state
            if (canvas != null) {
                this.getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    /**
     * only update map
     *
     * @return
     */
    @Override
    public void onLightTick() {
        this.update();
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
