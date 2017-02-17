package fr.giusti.onetapadventure.UI.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapengine.ui.DrawingView;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.GameBoard;
import fr.giusti.onetapengine.rules.IRuleProgressListener;
import fr.giusti.onetapengine.callback.OnGameEndListener;
import fr.giusti.onetapengine.rules.eConditionType;
import fr.giusti.onetapadventure.repository.GameRepo;

/**
 * classe qui contient tout une "partie"
 * c'est a dire un plateau avec ses mob etc
 * et la surfaceView sur laquelle le plateau va etre affiché
 *
 * @author giusti
 */
public class GameActivity extends Activity implements OnGameEndListener, IRuleProgressListener {
    private static final String TAG = GameActivity.class.getSimpleName();
    private DrawingView mDrawingSurface;
    private Button mPauseButton;
    private Button mRestartButton;
    private TextView mRule1;
    private TextView mRule2;
    private boolean running = false;
    private boolean paused = false;
    private GameRepo mRepo;
    private String currentLvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "GameActivity created");
        setContentView(R.layout.activity_game);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        currentLvl = getIntent().getStringExtra(GameConstant.LEVEL_NAME);
        initViews();
        initEvents();
    }

    private void initViews() {
        mDrawingSurface = (DrawingView) findViewById(R.id.GameBoard);
        mPauseButton = (Button) findViewById(R.id.ag_b_Stop);
        mRestartButton = (Button) findViewById(R.id.ag_b_restart);
        mRule1 = (TextView) findViewById(R.id.ag_defeat_rule_tv);
        mRule2 = (TextView) findViewById(R.id.ag_end_rule_tv);
    }

    private void initEvents() {
        mPauseButton.setOnClickListener(onCLickStopGame);
        mRestartButton.setOnClickListener(onCLickRestartGame);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            if (!running) {
                if (mRepo == null) {
                    mRepo = new GameRepo(mDrawingSurface.getWidth(), mDrawingSurface.getHeight());
                }
                Log.d(TAG, "starting board init");
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
                startNewGame();
//
//                    }
//                },1000);
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
            //GameBoard board=mRepo.generateSampleBoard(this);
            //board.setBoardEventListener(this);

//           GameBoard board = mRepo.generateLvl_1x1(this, this);
//            board.getRulesManager().setRuleListener(Lvl1Constant.ESCAPING_MOB_RULE, this);
//            board.getRulesManager().setRuleListener(Lvl1Constant.LEVEL_END_RULE, this);

            GameBoard board = mRepo.getBoardByLvlId(this, currentLvl, this, this);

            if (board == null) {
                Toast.makeText(this, R.string.error_level_generation, Toast.LENGTH_SHORT).show();
                this.finish();
            }

            Log.d(TAG, "Board created");

            mDrawingSurface.startGame(board);
        } catch (CloneNotSupportedException e) {
            Toast.makeText(this, "error clonning sample mob list", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onGameEnd(final eConditionType gameResult, String gameId, final int score) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivity.this, "end of game, result: " + gameResult + "\n Score: " + score, Toast.LENGTH_LONG).show();
            }
        });
    }


    String firstRuleid = null;
    @Override
    public void onRuleProgress(final String ruleId, final String displayableProgress) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(firstRuleid==null) firstRuleid=ruleId;
                if (firstRuleid.equals(ruleId)) {
                    mRule1.setText(displayableProgress);
                } else {
                    mRule2.setText(displayableProgress);
                }
            }
        });

    }
}