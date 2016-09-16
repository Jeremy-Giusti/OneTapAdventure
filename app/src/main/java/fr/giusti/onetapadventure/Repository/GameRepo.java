package fr.giusti.onetapadventure.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.rules.OnGameEndListener;
import fr.giusti.onetapadventure.gameObject.rules.RulesManager;
import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.repository.entities.EntityDispenserRepo;
import fr.giusti.onetapadventure.repository.entities.MobRepo;
import fr.giusti.onetapadventure.repository.entities.ParticuleRepo;

public class GameRepo {
//    /** the variable used to scale every game element to the screen height (implying the width will be scaled by the same variable but everything too long will be cropped on display)  */
//    private double mRatioY;

    private int mScreenWidth;
    private int mScreenHeight;

    public GameRepo( int screenWidth, int screenHeight) {
        super();
        this.mScreenHeight = screenHeight;
        this.mScreenWidth = screenWidth;
    }

    /**
     * create a sample gameBoard from the application datas
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public GameBoard generateSampleBoard(Context context) throws CloneNotSupportedException {
        new ParticuleRepo().initCache(context);
        String backGameBoard = "background";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.grid512);

        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();

        SpriteRepo.addPicture(backGameBoard,fullSizedBackground);
        GameBoard board = new GameBoard(MobRepo.getSampleMobList(context), backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight));
        board.resize(mScreenWidth,mScreenHeight);
        return board;
    }

    public GameBoard generateLvl_1x1(Context context, OnGameEndListener gameListener) throws CloneNotSupportedException {
        new ParticuleRepo().initCache(context);
        String backGameBoard = "background1x1";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.lvl1x1_back);

        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();
        SpriteRepo.addPicture(backGameBoard,fullSizedBackground);

        RulesManager rulesManager = RuleRepo.getLvl_1x1_Rules(gameListener);
        GameBoard board = new GameBoard(EntityDispenserRepo.getLvl1_1MobDispenser(context), backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight),rulesManager);
        board.resize(mScreenWidth,mScreenHeight);
//        board.setBoardEventListener(rulesManager);
        return board;
    }


}
