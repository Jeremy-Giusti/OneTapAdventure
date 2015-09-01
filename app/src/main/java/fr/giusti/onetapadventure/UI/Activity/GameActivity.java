package fr.giusti.onetapadventure.UI.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.GameObject.GameMob;
import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.Repository.GameRepo;
import fr.giusti.onetapadventure.Repository.MobRepo;
import fr.giusti.onetapadventure.UI.CustomView.DrawingView;
import fr.giusti.onetapadventure.callback.OnAllMobDeadListener;
import fr.giusti.onetapadventure.callback.OnMobDeathListener;
import fr.giusti.onetapadventure.callback.OnScrollingEndListener;
import fr.giusti.onetapadventure.commons.Constants;

/**
 * classe qui contient tout une "partie"
 * c'est a dire un plateau avec ses mob etc
 * et la surfaceView sur laquelle le plateau va etre affiché
 *
 * @author giusti
 */
public class GameActivity extends Activity implements OnAllMobDeadListener, OnMobDeathListener, OnScrollingEndListener {
    private DrawingView mDrawingSurface;
    private Button mPauseButton;
    private Button mRestartButton;
    private boolean running = false;
    private boolean paused = false;
    private GameRepo mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initViews();
        initEvents();
    }

    private void initViews() {
        mDrawingSurface = (DrawingView) findViewById(R.id.GameBoard);
        mPauseButton = (Button) findViewById(R.id.ag_b_Stop);
        mRestartButton = (Button) findViewById(R.id.ag_b_restart);
    }

    private void initEvents() {
        mPauseButton.setOnClickListener(onCLickStopGame);
        mRestartButton.setOnClickListener(onCLickRestartGame);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            if (!running) {
                if(mRepo==null){
                    MobRepo.fluchScaledMobCache();
                    mRepo= new GameRepo(Constants.DEFAULT_GAME_HEIGHT, mDrawingSurface.getWidth(), mDrawingSurface.getHeight());
                }

                startNewGame();
                running = true;
            }
        }
    }

    @Override
    protected void onPause() {
        try {
            mDrawingSurface.onSystemePause();
        } catch (InterruptedException e) {
            Toast.makeText(this, "thread interuption exception", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mDrawingSurface.onSystemeResume();
        super.onResume();
    }

    public OnClickListener onCLickStopGame = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO faire une vrai interraction avec la surface (bloquer les click listener/rester en pause si on quitte et reprend l'appli)
            if (!paused) {
                onPause();
                paused = true;
                ((Button) v).setText("Reprendre");
            } else {

                onResume();
                paused = false;
                ((Button) v).setText("Stop");
            }
        }
    };

    public OnClickListener onCLickRestartGame = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO test
            try {
                mDrawingSurface.stopGame();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startNewGame();

        }
    };

    /**
     * launch new game on the surface
     */
    private void startNewGame() {
        try {
            GameBoard board=mRepo.generateSampleBoard(this);

            board.setAllMobDeadListener(this);
            board.setMobDeathListener(this);
            //  board.setScrollingEndListener(this);

            mDrawingSurface.startGame(board);
        } catch (CloneNotSupportedException e) {
            Toast.makeText(this, "error clonning sample mob list",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void OnScrollingEnd() {

        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(GameActivity.this, "scolling end", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void OnMobDeath(final GameMob deadMob) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(GameActivity.this, "mob dead at "+deadMob.mPosition.centerX()+":"+deadMob.mPosition.centerY(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void OnAllMobDead(final GameMob lastMobKilled) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(GameActivity.this, "Last mob killed at "+lastMobKilled.mPosition.centerX()+":"+lastMobKilled.mPosition.centerY(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}
