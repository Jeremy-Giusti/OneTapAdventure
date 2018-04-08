//http://obviam.net/index.php/the-android-game-loop/
package fr.giusti.onetapengine.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import fr.giusti.onetapengine.GameBoard;

public class DrawingView extends SurfaceView implements SurfaceHolder.Callback, TickingThread.OnTickListener {
    private static final String TAG = DrawingView.class.getSimpleName();
    private Context mContext;
    private TickingThread mDrawThread;
    private Paint mBrush = new Paint();

    /**
     * used to calculate in game time since game start
     */
    private Long mStoppingTime = null;
    private long mStartingTime = System.currentTimeMillis();

    private GameBoard mMap;


    // --------------------------- LifeCycle ---------------------------------//
    public DrawingView(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        // make the GamePanel focusable so it can handle events
        mContext = context;
        setFocusable(true);
        mBrush.setColor(mContext.getResources().getColor(android.R.color.darker_gray));
        mBrush.setStrokeWidth(5);

    }

    public DrawingView(Context context, AttributeSet attr) {
        super(context, attr);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);
        // make the GamePanel focusable so it can handle events
        setFocusable(true);
        mContext = context;
        mBrush.setColor(mContext.getResources().getColor(android.R.color.darker_gray));
        mBrush.setStrokeWidth(5);

    }

    /**
     * start back a game if a board is found
     *
     * @param holder
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        resumeGameIfPossible();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Auto-generated method stub
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mDrawThread != null) mDrawThread.setRunning(false);
    }

    // --------------------------- start/stop events ---------------------------------//

    /**
     * start the board continuous updates
     *
     * @param map
     */
    public void startGame(GameBoard map) {

        mDrawThread = new TickingThread(this);
        mMap = map;
        this.resize();
        mDrawThread.setRunning(true);
        Log.d(TAG, "Drawing view ready, starting drawing thread");

        mDrawThread.start();
    }

//    /**
//     * pause the thread execution <br>
//     * beware, it don't allow background pausing
//     *
//     * @throws InterruptedException
//     */
//    public void pauseGame() throws InterruptedException {
//        mDrawThread.onPause();
//        mStoppingTime = System.currentTimeMillis();
//    }

    /**
     * put an end to the thread execution
     *
     * @throws InterruptedException
     */
    public void stopGame() {
        if (mDrawThread != null) {
            mDrawThread.setRunning(false);
            mStoppingTime = System.currentTimeMillis();
        }
    }

    /**
     * resume the game if a board is found
     */
    public void resumeGameIfPossible() {
        if (mMap != null) {

            //prevent taking paused time a game time
            long pausedTime = System.currentTimeMillis() - mStoppingTime;
            mStartingTime += pausedTime;
            mStoppingTime = null;

            // restart after just pausing
            if (mDrawThread != null && mDrawThread.getState() != Thread.State.TERMINATED)
                mDrawThread.onResume();
            else startGame(mMap);
        }
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

    /**
     * draw map on the canvas
     *
     * @param canvas
     */

    public void doDraw(Canvas canvas) {
        mMap.draw(canvas, mBrush);
    }

    /**
     * update board (make a logic tick)
     */
    public void update() {
        long time = System.currentTimeMillis();
        time -= mStartingTime;
        mMap.update(time);
    }

    /**
     * Touch event handling
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mMap != null) mMap.touchEvent(event);

        return true;
    }

    /**
     * update view size depending on board camera size
     */
    private void resize() {
        getLayoutParams().width = mMap.getmCameraBounds().width();
        getLayoutParams().height = mMap.getmCameraBounds().height();
    }


}
