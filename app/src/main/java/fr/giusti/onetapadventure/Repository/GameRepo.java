package fr.giusti.onetapadventure.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.callback.OnGameEndListener;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.interactions.TouchDispenser;
import fr.giusti.onetapadventure.gameObject.rules.IRuleProgressListener;
import fr.giusti.onetapadventure.gameObject.rules.RulesManager;
import fr.giusti.onetapadventure.repository.entities.EntityRepo;
import fr.giusti.onetapadventure.repository.entities.EntitySpawnerRepo;
import fr.giusti.onetapadventure.repository.entities.ParticuleRepo;
import fr.giusti.onetapadventure.repository.levelsData.Lvl1Constant;
import fr.giusti.onetapadventure.repository.levelsData.Lvl2Constant;

public class GameRepo {
    public static final String LVL_TEST = "lvl test";
    public static final String LVL_1 = "1x1";
    public static final String LVL_2 = "1x2";

    private int mScreenWidth;
    private int mScreenHeight;

    public GameRepo(int screenWidth, int screenHeight) {
        super();
        this.mScreenHeight = screenHeight;
        this.mScreenWidth = screenWidth;
    }

    public GameBoard getBoardByLvlId(Context context, String lvlId, OnGameEndListener endListener, IRuleProgressListener ruleProgressListener) throws CloneNotSupportedException {
        GameBoard result = null;
        switch (lvlId) {
            case LVL_TEST:
                result = generateSampleBoard(context);
                break;
            case LVL_1:
                //FIXME /not a dynamic detection\
                result = generateLvl_1x1(context, endListener, ruleProgressListener);
                break;
            case LVL_2:
                //FIXME /not a dynamic detection\
                result = generateLvl_1x2(context, endListener, ruleProgressListener);
                break;
        }

        return result;
    }


    /**
     * create a sample gameBoard from the application datas
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public GameBoard generateSampleBoard(Context context) throws CloneNotSupportedException {
        ParticuleRepo.initCache(context);
        String backGameBoard = "background";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.grid512);

        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();

        SpriteRepo.addPicture(backGameBoard, fullSizedBackground);
        GameBoard board = new GameBoard(EntityRepo.getSampleMobList(context), backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight));
        board.resize(mScreenWidth, mScreenHeight);
        return board;
    }

    public GameBoard generateLvl_1x1(Context context, OnGameEndListener gameListener, IRuleProgressListener ruleProgressListener) throws CloneNotSupportedException {
        ParticuleRepo.initCache(context);
        String backGameBoard = "background1x1";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.lvl1x1_back);

        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();
        SpriteRepo.addPicture(backGameBoard, fullSizedBackground);

        RulesManager rulesManager = RuleRepo.getLvl_1x1_Rules(gameListener);

        String touchSpriteID = "touchSprite";
        Bitmap touchSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch1);
        SpriteRepo.addSpriteSheet(touchSprite, touchSpriteID, Constants.PARTICULE_NB_FRAME_ON_ANIMATION, 1);
        TouchDispenser touchDisp = new TouchDispenser(Constants.TOUCH_STROKE*2, touchSpriteID, Constants.TOUCH_DAMAGE);

        GameBoard board = new GameBoard(EntitySpawnerRepo.getLvl1_1SpawnerManager(context), backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight), rulesManager, touchDisp);
        board.resize(mScreenWidth, mScreenHeight);
        board.getRulesManager().setRuleListener(Lvl1Constant.ESCAPING_MOB_RULE, ruleProgressListener);
        board.getRulesManager().setRuleListener(Lvl1Constant.LEVEL_END_RULE, ruleProgressListener);
        return board;
    }

    private GameBoard generateLvl_1x2(Context context, OnGameEndListener endListener, IRuleProgressListener ruleProgressListener) {
        ParticuleRepo.initCache(context);
        String backGameBoard = "background1x2";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.lvl1x2_back);

        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();
        SpriteRepo.addPicture(backGameBoard, fullSizedBackground);

        RulesManager rulesManager = RuleRepo.getLvl_1x2_Rules(endListener);

        String touchSpriteID = "touchSprite";
        Bitmap touchSprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.touch1);
        SpriteRepo.addSpriteSheet(touchSprite, touchSpriteID, Constants.PARTICULE_NB_FRAME_ON_ANIMATION, 1);
        TouchDispenser touchDisp = new TouchDispenser(Constants.TOUCH_STROKE*2, touchSpriteID, Constants.TOUCH_DAMAGE);
       // TouchDispenser touchDisp = new TouchDispenser(Constants.TOUCH_STROKE, null, Constants.TOUCH_DAMAGE);

        GameBoard board = new GameBoard(EntitySpawnerRepo.getLvl1_2SpawnerManager(context), backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight), rulesManager, touchDisp);
        board.resize(mScreenWidth, mScreenHeight);
        board.getRulesManager().setRuleListener(Lvl2Constant.ESCAPING_MOB_RULE, ruleProgressListener);
        board.getRulesManager().setRuleListener(Lvl2Constant.TIMER_RULE, ruleProgressListener);
        return board;
    }

}
