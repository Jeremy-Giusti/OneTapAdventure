package fr.giusti.onetapadventure.UI.activity;

import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.UI.dialog.PercentLoadingDialog;
import fr.giusti.onetapadventure.repository.GameRepo;
import fr.giusti.onetapengine.GameBoard;
import fr.giusti.onetapengine.callback.OnGameEndListener;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.rules.IRuleProgressListener;
import fr.giusti.onetapengine.rules.eRuleResult;
import fr.giusti.onetapengine.ui.DrawingView;

/**
 * classe qui contient tout une "partie"
 * c'est a dire un plateau avec ses mob etc
 * et la surfaceView sur laquelle le plateau va etre affiché
 *
 * @author giusti
 */
public class GameActivity extends Activity implements OnGameEndListener, IRuleProgressListener, GameRepo.BoardGenerationCallback {
    private static final String TAG = GameActivity.class.getSimpleName();
    private static final String LOADING_DIALOG_TAG = "loadingDialog";

    private DrawingView mDrawingSurface;
    private Button mPauseButton;
    private Button mRestartButton;
    private TextView mRule1;
    private TextView mRule2;
    private boolean running = false;
    private boolean paused = false;
    private GameRepo mRepo;
    private String mSelectedLvlName;
    private PercentLoadingDialog mLoadingDialog;

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

        mSelectedLvlName = getIntent().getStringExtra(GameConstant.LEVEL_NAME);
        initViews();
        initEvents();

        if (mLoadingDialog == null) {
            mLoadingDialog = PercentLoadingDialog.newInstance(getString(R.string.generating_lvl));
        }
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

            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

            //wtf doing here ?
            if (!running) {
                if (mRepo == null) {
                    mRepo = new GameRepo(mDrawingSurface.getWidth(), mDrawingSurface.getHeight());
                }
                Log.d(TAG, "starting board init");
                startNewGameLoading();
                running = true;
            }
        }
    }

    @Override
    protected void onPause() {
        mDrawingSurface.stopGame();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mDrawingSurface.resumeGameIfPossible();
        super.onResume();
    }

    public OnClickListener onCLickStopGame = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO faire une vrai interraction avec la surface (bloquer les click listener/rester en pause si on quitte et reprend l'appli)
            if (!paused) {
                mDrawingSurface.stopGame();
                paused = true;
                ((Button) v).setText("Reprendre");
            } else {
                mDrawingSurface.resumeGameIfPossible();
                paused = false;
                ((Button) v).setText("Stop");
            }
        }
    };

    public OnClickListener onCLickRestartGame = new OnClickListener() {

        @Override
        public void onClick(View v) {
            mDrawingSurface.stopGame();
            startNewGameLoading();

        }
    };

    /**
     * launch new game on the surface
     */
    private void startNewGameLoading() {
        if (!mLoadingDialog.isAdded()) {
            mLoadingDialog.show(getFragmentManager(), LOADING_DIALOG_TAG);
        }
        mRepo.getBoardByLvlIdAsync(this, mSelectedLvlName, this, this, this);
    }

    @Override
    public void onGameEnd(final eRuleResult gameResult, String gameId, final int score) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(GameActivity.this, "end of game, result: " + gameResult + "\n Score: " + score, Toast.LENGTH_LONG).show();
            }
        });
    }


    String firstRuleid = null;

    @Override
    public void onRuleProgress(final String ruleId, final String displayableProgress, final double completionPercent) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (firstRuleid == null) firstRuleid = ruleId;
                if (firstRuleid.equals(ruleId)) {
                    //TODO ne doit pas rester ici,
                    //faire un override de game activity pour utiliser un comportment specifique a chaque cas
                    int color = Color.argb(200,(int) (0 + 255 * completionPercent), (int) (255 - 255 * completionPercent), 0);
                    mRule1.setBackgroundColor(color);
                    mRule1.setText(displayableProgress);
                } else {
                    mRule2.setText(displayableProgress);
                }
            }
        });

    }

    @Override
    public void onGameBoardGenerated(GameBoard board) {
        if (board == null) {
            Toast.makeText(this, R.string.error_level_generation, Toast.LENGTH_SHORT).show();
            this.finish();
        }

        Log.d(TAG, "Board created");

        if (mLoadingDialog != null && mLoadingDialog.isAdded()) {
            mLoadingDialog.dismiss();
        }

        mDrawingSurface.startGame(board);
    }

    @Override
    public void onGameBoardGenerationError(final String message, final Exception e) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (TextUtils.isEmpty(message)) {
                    Log.e(TAG, getString(R.string.error_board_loading), e);
                } else {
                    Log.e(TAG, message, e);
                }
            }
        });


    }

    @Override
    public void onGameBoardGenerationProgress(final int progress) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, " board generation progress: " + progress);

                if (mLoadingDialog != null && mLoadingDialog.isAdded()) {
                    mLoadingDialog.setProgress(progress);
                }

            }
        });


    }
}
