//http://obviam.net/index.php/the-android-game-loop/
package fr.giusti.onetapadventure.UI.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import fr.giusti.onetapadventure.commons.Constants;

public class DrawingThread extends Thread {

    // desired fps
    private final static int MAX_FPS = Constants.FRAME_PER_SEC;
    // maximum number of frames to be skipped
    private final static int MAX_FRAME_SKIPS = 5;
    // the frame period
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    private static final String TAG = DrawingView.class.getName();

    private DrawingView mSurfacePanel;
    private boolean running = false;
    private boolean pause = false;

    private Canvas mCanvas;

    private SurfaceHolder mSurfaceHolder;

    private Context mContext;

    private Object mPauseLock = new Object();

    public DrawingThread(SurfaceHolder sholder, Context ctx, DrawingView spanel) {

        mSurfaceHolder = sholder;

        mContext = ctx;

        mSurfacePanel = spanel;

    }

    @Override
    public void run() {
        Canvas canvas;
        Log.d(TAG, "Starting game loop");

        long beginTime; // the time when the cycle begun
        long timeDiff; // the time it took for the cycle to execute
        int sleepTime; // ms to sleep (<0 if we're behind)
        int framesSkipped; // number of frames being skipped

        sleepTime = 0;

        while (running) {
            canvas = null;
            // try locking the canvas for exclusive pixel editing
            // in the surface
            try {
                canvas = this.mSurfaceHolder.lockCanvas();
                synchronized (mSurfaceHolder) {
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0; // resetting the frames skipped
                    // update game state
                    this.mSurfacePanel.update();
                    // render state to the screen
                    // draws the canvas on the panel
                    this.mSurfacePanel.doDraw(canvas);
                    // calculate how long did the cycle take
                    timeDiff = System.currentTimeMillis() - beginTime;
                    // calculate sleep time
                    sleepTime = (int) (FRAME_PERIOD - timeDiff);

                    if (sleepTime > 0) {
                        // if sleepTime > 0 we're OK
                        try {
                            // send the thread to sleep for a short period
                            // very useful for battery saving
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {}
                    }

                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS) {
                        // we need to catch up
                        // update without rendering
                        this.mSurfacePanel.update();
                        // add frame period to check if in next frame
                        sleepTime += FRAME_PERIOD;
                        framesSkipped++;
                        Log.d("Drawing thread", "frames skipped : "+framesSkipped);
                    }
                }
            } finally {
                // in case of an exception the surface is not left in
                // an inconsistent state
                if (canvas != null) {
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
                }
            } // end finally

            synchronized (mPauseLock) {
                if (pause) {
                    try {
                        mPauseLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }

    /**
     * Call this on pause.
     */
    public void onPause() {
        synchronized (mPauseLock) {
            pause = true;
        }
    }

    /**
     * Call this on resume.
     */
    public void onResume() {
        synchronized (mPauseLock) {
            pause = false;
            mPauseLock.notifyAll();
        }
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isRunning() {
        return running;
    }
}
