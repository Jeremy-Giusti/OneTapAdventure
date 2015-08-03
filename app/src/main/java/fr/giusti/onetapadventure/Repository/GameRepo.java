package fr.giusti.onetapadventure.Repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.R;

public class GameRepo {
    /** the variable used to scale every game element to the screen height (implying the width will be scaled by the same variable but everything too long will be cropped on display)  */
    private double mRatioY;
    
    private int mScreenWidth;
    private int mScreenHeight;

    public GameRepo(int mDefaultGameHeight, int screenWidth, int screenHeight) {
        super();
        this.mRatioY = (double) screenHeight / mDefaultGameHeight;
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
        new ParticuleRepo().initCache(context,mRatioY);
        String backGameBoard = "background";
        Bitmap fullSizedBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.grid512);
        
        int backgroundHeight = (int) (fullSizedBackground.getHeight() * mRatioY);
        int backgroundWidth = (int) (fullSizedBackground.getWidth() * mRatioY);
        
        new SpriteRepo().addBitmap(Bitmap.createScaledBitmap(fullSizedBackground, backgroundWidth, backgroundHeight, false), backGameBoard,1,1);
        return new GameBoard(new MobRepo(mRatioY,mScreenWidth,mScreenHeight).getSampleMobList(context), backGameBoard, mScreenWidth,mScreenHeight);
    }

    public GameBoard generateBoardFromBd(String boardId) {
        // TODO utiliser une bd + liste de mob id?
        return null;
    }

    public double getRatioY() {
        return mRatioY;
    }

    public void setRatioY(double mRatioY) {
        this.mRatioY = mRatioY;
    }

}
