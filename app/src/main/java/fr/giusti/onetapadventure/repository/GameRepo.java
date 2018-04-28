package fr.giusti.onetapadventure.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.repository.entities.EntityRepo;
import fr.giusti.onetapadventure.repository.entities.EntitySpawnerRepo;
import fr.giusti.onetapadventure.repository.levelsData.Lvl1Constant;
import fr.giusti.onetapadventure.repository.levelsData.Lvl2Constant;
import fr.giusti.onetapadventure.repository.levelsData.Lvl3Constant;
import fr.giusti.onetapadventure.repository.levelsData.infinitelvl.InfiniteLvlConstant;
import fr.giusti.onetapengine.GameBoard;
import fr.giusti.onetapengine.callback.OnGameEndListener;
import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.entity.distribution.EntitySpawnerManager;
import fr.giusti.onetapengine.interaction.TouchDispenser;
import fr.giusti.onetapengine.repository.ParticuleRepo;
import fr.giusti.onetapengine.repository.SpriteRepo;
import fr.giusti.onetapengine.rules.IRuleProgressListener;
import fr.giusti.onetapengine.rules.RulesManager;

public class GameRepo {
    public static final String LVL_TEST = "lvl test";
    public static final String LVL_1 = "1x1";
    public static final String LVL_2 = "1x2";
    public static final String LVL_INFINITE = "inifinite_level";
    private static final String TAG = GameRepo.class.getSimpleName();

    private int mScreenWidth;
    private int mScreenHeight;

    public GameRepo(int screenWidth, int screenHeight) {
        super();
        this.mScreenHeight = screenHeight;
        this.mScreenWidth = screenWidth;
    }


    public void getBoardByLvlIdAsync(final Context context, final String lvlId, final OnGameEndListener endListener, final IRuleProgressListener ruleProgressListener, final BoardGenerationCallback callback) {

        new Thread(new Runnable() {
            public void run() {
                getBoardByLvlId(context, callback, lvlId, endListener, ruleProgressListener);
            }
        }).start();

    }

    private void getBoardByLvlId(Context context, final BoardGenerationCallback callback, String lvlId, OnGameEndListener endListener, IRuleProgressListener ruleProgressListener) {
        switch (lvlId) {
            case LVL_TEST:
                generateSampleBoard(context, callback);
                break;
            case LVL_1:
                //FIXME /not a dynamic detection\
                generateLvl_1x1(context, callback, endListener, ruleProgressListener);
                break;
            case LVL_2:
                //FIXME /not a dynamic detection\
                generateLvl_1x2(context, callback, endListener, ruleProgressListener);
                break;
            case LVL_INFINITE:
                genereteInfiniteLvl(context, callback, endListener, ruleProgressListener);
                break;
        }
    }

    /**
     * end rule: mobcount = 25
     *
     * @param context
     * @param callback
     * @param endListener
     * @param ruleProgressListener
     * @return
     */
    private void genereteInfiniteLvl(Context context, final BoardGenerationCallback callback, OnGameEndListener endListener, IRuleProgressListener ruleProgressListener) {
        String backGameBoard = "backgroundInfiniteLvl";

        //particules
        ParticuleRepo.initCache(context);

        //TODO infinite background
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.lvl1x2_back);
        SpriteRepo.addPicture(backGameBoard, fullSizedBackground);

        callback.onGameBoardGenerationProgress(15);

        //rules
        RulesManager rulesManager = RuleRepo.getInfiniteLvlRules(endListener);

        //touch event
        String touchSpriteID = "touchSprite";
        Bitmap touchSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch1);
        SpriteRepo.addSpriteSheet(touchSprite, touchSpriteID, Constants.PARTICULE_NB_FRAME_ON_ANIMATION, 1);
        TouchDispenser touchDisp = new TouchDispenser(GameConstant.TOUCH_STROKE * 2, touchSpriteID, GameConstant.BASE_DAMAGE);

        callback.onGameBoardGenerationProgress(66);

        EntitySpawnerManager infiniteLvlSpawnerManager = null;
        try {
            infiniteLvlSpawnerManager = EntitySpawnerRepo.getInfiniteSpawnerManager(context);
        } catch (IOException e) {
            Log.e(TAG, "generateInfiniteLvl: ", e);
            callback.onGameBoardGenerationError("", e);
        }
        callback.onGameBoardGenerationProgress(90);

        //board
        GameBoard board = new GameBoard(infiniteLvlSpawnerManager, backGameBoard, InfiniteLvlConstant.BOARD_WITDH, InfiniteLvlConstant.BOARD_HEIGHT, new Rect(0, 0, InfiniteLvlConstant.BOARD_WITDH, InfiniteLvlConstant.BOARD_HEIGHT), rulesManager, touchDisp);
        board.resize(mScreenWidth, mScreenHeight);

        //board listeners binding
        board.getRulesManager().setRuleListener(InfiniteLvlConstant.MASTER_RULE, ruleProgressListener);
        board.getRulesManager().setRuleListener(InfiniteLvlConstant.SCORE_RULE, ruleProgressListener);

        if (board != null) callback.onGameBoardGenerated(board);
        else callback.onGameBoardGenerationError("generateInfiniteLvl", null);
    }


    /**
     * create a sample gameBoard from the application datas
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public void generateSampleBoard(Context context, BoardGenerationCallback callback) {
        ParticuleRepo.initCache(context);
        String backGameBoard = "background";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.grid512);

        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();

        SpriteRepo.addPicture(backGameBoard, fullSizedBackground);
        GameBoard board = null;

        callback.onGameBoardGenerationProgress(66);

        try {
            board = new GameBoard(EntityRepo.getSampleMobList(context), backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight));
        } catch (CloneNotSupportedException e) {
            Log.e(TAG, "generateSampleBoard: ", e);
            callback.onGameBoardGenerationError("", e);
        } catch (IOException e) {
            Log.e(TAG, "generateSampleBoard: ", e);
            callback.onGameBoardGenerationError("", e);
        }

        callback.onGameBoardGenerationProgress(90);


        board.resize(mScreenWidth, mScreenHeight);

        if (board != null) callback.onGameBoardGenerated(board);
        else callback.onGameBoardGenerationError("generateSampleBoard", null);
    }

    public void generateLvl_1x1(Context context, BoardGenerationCallback callback, OnGameEndListener gameListener, IRuleProgressListener ruleProgressListener) {
        String backGameBoard = "background1x1";

        //particules
        ParticuleRepo.initCache(context);

        //background
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.lvl1x1_back);
        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();
        SpriteRepo.addPicture(backGameBoard, fullSizedBackground);

        callback.onGameBoardGenerationProgress(15);

        //rules
        RulesManager rulesManager = RuleRepo.getLvl_1x1_Rules(gameListener);

        //touch event
        String touchSpriteID = "touchSprite";
        Bitmap touchSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch1);
        SpriteRepo.addSpriteSheet(touchSprite, touchSpriteID, Constants.PARTICULE_NB_FRAME_ON_ANIMATION, 1);
        TouchDispenser touchDisp = new TouchDispenser(GameConstant.TOUCH_STROKE * 2, touchSpriteID, GameConstant.BASE_DAMAGE);

        callback.onGameBoardGenerationProgress(66);

        //Entity
        EntitySpawnerManager lvl1_1SpawnerManager = null;
        try {
            lvl1_1SpawnerManager = EntitySpawnerRepo.getLvl1_1SpawnerManager(context);
        } catch (IOException e) {
            Log.e(TAG, "generateLvl_1x1: ", e);
            callback.onGameBoardGenerationError("", e);
        }
        callback.onGameBoardGenerationProgress(90);

        //board
        GameBoard board = new GameBoard(lvl1_1SpawnerManager, backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight), rulesManager, touchDisp);
        board.resize(mScreenWidth, mScreenHeight);

        //board listeners binding
        board.getRulesManager().setRuleListener(Lvl1Constant.ESCAPING_MOB_RULE, ruleProgressListener);
        board.getRulesManager().setRuleListener(Lvl1Constant.LEVEL_END_RULE, ruleProgressListener);

        if (board != null) callback.onGameBoardGenerated(board);
        else callback.onGameBoardGenerationError("generateLvl_1x1", null);
    }

    private void generateLvl_1x2(Context context, BoardGenerationCallback callback, OnGameEndListener endListener, IRuleProgressListener ruleProgressListener) {
        ParticuleRepo.initCache(context);
        String backGameBoard = "background1x2";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.lvl1x2_back);

        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();
        SpriteRepo.addPicture(backGameBoard, fullSizedBackground);

        callback.onGameBoardGenerationProgress(15);

        RulesManager rulesManager = RuleRepo.getLvl_1x2_Rules(endListener);

        String touchSpriteID = "touchSprite";
        Bitmap touchSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch1);
        SpriteRepo.addSpriteSheet(touchSprite, touchSpriteID, Constants.PARTICULE_NB_FRAME_ON_ANIMATION, 1);
        TouchDispenser touchDisp = new TouchDispenser(GameConstant.TOUCH_STROKE * 2, touchSpriteID, GameConstant.BASE_DAMAGE);

        callback.onGameBoardGenerationProgress(66);

        GameBoard board = null;
        try {
            board = new GameBoard(EntitySpawnerRepo.getLvl1_2SpawnerManager(context), backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight), rulesManager, touchDisp);
        } catch (IOException e) {
            Log.e(TAG, "generateLvl_1x2: ", e);
            callback.onGameBoardGenerationError("", e);
        }

        callback.onGameBoardGenerationProgress(90);

        board.resize(mScreenWidth, mScreenHeight);
        board.getRulesManager().setRuleListener(Lvl2Constant.ESCAPING_MOB_RULE, ruleProgressListener);
        board.getRulesManager().setRuleListener(Lvl2Constant.TIMER_RULE, ruleProgressListener);

        if (board != null) callback.onGameBoardGenerated(board);
        else callback.onGameBoardGenerationError("generateLvl_1x2", null);
    }

    private GameBoard generateLvl_1x3(Context context, OnGameEndListener endListener, IRuleProgressListener ruleProgressListener) throws IOException {
        ParticuleRepo.initCache(context);
        String backGameBoard = "background1x3";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.lvl1x2_back);//TODO change background

        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();
        SpriteRepo.addPicture(backGameBoard, fullSizedBackground);

        RulesManager rulesManager = RuleRepo.getLvl_1x3_Rules(endListener);

        String touchSpriteID = "touchSprite";
        Bitmap touchSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch1);
        SpriteRepo.addSpriteSheet(touchSprite, touchSpriteID, Constants.PARTICULE_NB_FRAME_ON_ANIMATION, 1);
        TouchDispenser touchDisp = new TouchDispenser(GameConstant.TOUCH_STROKE * 2, touchSpriteID, GameConstant.BASE_DAMAGE);

        GameBoard board = new GameBoard(EntitySpawnerRepo.getLvl1_3SpawnerManager(context), backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight), rulesManager, touchDisp);
        board.resize(mScreenWidth, mScreenHeight);
        board.getRulesManager().setRuleListener(Lvl3Constant.SUCCESS_SCORE_RULE, ruleProgressListener);
        board.getRulesManager().setRuleListener(Lvl3Constant.DEFEAT_SCORE_RULE, ruleProgressListener);
        return board;
    }

    public interface BoardGenerationCallback {
        void onGameBoardGenerated(GameBoard board);

        void onGameBoardGenerationError(String message, Exception e);

        void onGameBoardGenerationProgress(int progress);
    }


}
