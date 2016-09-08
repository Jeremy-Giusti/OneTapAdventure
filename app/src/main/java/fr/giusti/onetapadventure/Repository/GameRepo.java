package fr.giusti.onetapadventure.Repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Pair;

import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.GameObject.Rules.Rules;
import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.Repository.Mobs.MobRepo;

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

    public Pair<Rules,GameBoard> generateLvl_1x1(Context context) throws CloneNotSupportedException {
        new ParticuleRepo().initCache(context);
        String backGameBoard = "background1x1";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.grid512);//FIXME add true background

        int boardHeight = fullSizedBackground.getHeight();
        int boardWidth = fullSizedBackground.getWidth();
        SpriteRepo.addPicture(backGameBoard,fullSizedBackground);

        Rules rules = null;
        //TODO create Rules and set it as board listener
        //create mob dispenser
        //add hole to board as immobile neutral mob with collision

        GameBoard board = new GameBoard(MobRepo.getSampleMobList(context), backGameBoard, boardWidth, boardHeight, new Rect(0, 0, boardWidth, boardHeight));
        board.resize(mScreenWidth,mScreenHeight);
        return new Pair<>(rules,board);
    }

    public GameBoard generateBoardFromBd(String boardId) {
        // TODO utiliser une bd + liste de mob id?
        return null;
    }

}
