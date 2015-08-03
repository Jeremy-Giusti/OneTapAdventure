package fr.giusti.onetapadventure.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.RectF;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.Repository.DB.ModelConverter;
import fr.giusti.onetapadventure.Repository.DB.model.MobDB;
import fr.giusti.onetapadventure.Repository.DB.model.PathDB;
import fr.giusti.onetapadventure.Repository.DB.persister.MobPersister;
import fr.giusti.onetapadventure.Repository.DB.persister.PathPersister;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.GameObject.GameMob;

public class MobRepo {

    /**
     * the variable used to scale every game element to the screen height (implying the width will be scaled by the same variable but everything too long will be cropped on display)
     */
    private double mRatio;

    private int mBoardWidth;
    private int mBoardHeight;

    private static ArrayList<GameMob> mStaticUnscaledMobList = new ArrayList<GameMob>();
    private static ArrayList<GameMob> mStaticScaledMobList = new ArrayList<GameMob>();

    public MobRepo(double ratio, int boardWidth, int boardHeight) {
        super();
        this.mRatio = ratio;
        this.mBoardWidth = boardWidth;
        this.mBoardHeight = boardHeight;
    }

    /**
     * creer une liste de mob generé en dur (code) +les mobs créés avec l'editeur de mob
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public CopyOnWriteArrayList<GameMob> getSampleMobList(Context mContext) throws CloneNotSupportedException {//

        CopyOnWriteArrayList<GameMob> returnList = new CopyOnWriteArrayList<GameMob>();

        if (mStaticScaledMobList.size() == 0) {
            PathRepo pathRepo = new PathRepo();
            SpriteRepo spriteRepo = new SpriteRepo();

            String bitmapId = "spritesheetTest";
            spriteRepo.addBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet), bitmapId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
            String bitmapId2 = "spritesheetTest2";
            spriteRepo.addBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet2), bitmapId2, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
            String bitmapId3 = "spritesheetTest3";
            spriteRepo.addBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet3), bitmapId3, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
            String bitmapId4 = "spritesheetTest4";
            spriteRepo.addBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet4), bitmapId4, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
            //work
            Point[] mob1Pattern = pathRepo.mergePaths(pathRepo.generateCurvedPath(20, 7, 0, 0, 5), pathRepo.generateCurvedPath(20, 7, 0, 0, -5));
            //work
            Point[] mob2Pattern = pathRepo.generateLinePath(20, 5, 3);
            //work
            Point[] mob3Pattern = pathRepo.speedDownPortionOfPath(pathRepo.generateLinePath(20, 5, 7), 6, 15, 2);
            //work
            Point[] mob4Pattern = {new Point(0, 0)};
            //work (not perfectly but still)
            Point[] mob5Pattern = pathRepo.generateLoopedPath(Constants.FRAME_PER_SEC, new Point(0, 0), new Point(0, 5), 7, 0);//un tour a la seconde
            //seems to work
            Point[] mob6Pattern = pathRepo.generateRandomPath(100, 5, 5, 15, 15, 25);

            ArrayList<GameMob> unscaledList = new ArrayList<GameMob>();
            SpecialMoveRepo moveRepo = new SpecialMoveRepo();
            TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();
            GameMob blueMob = new GameMob("programmedMob1", 1, 250, 32, 32, mob1Pattern, moveRepo.getMoveById(SpecialMoveRepo.TRAIL), touchedMoveRepo.getMoveById(TouchedMoveRepo.BAIT), bitmapId4, 5, 1);
            GameMob greenMob = new GameMob("programmedMob3", 100, 100, 48, 48, mob3Pattern, moveRepo.getMoveById(SpecialMoveRepo.SWAP), null, bitmapId3, 1, 1);
            GameMob yellowMob = new GameMob("programmedMob6", 250, 250, 32, 32, mob6Pattern, moveRepo.getMoveById(SpecialMoveRepo.AUTO_HEAL), touchedMoveRepo.getMoveById(TouchedMoveRepo.BLEED), bitmapId2, 2, 1);
            GameMob orangeMob = new GameMob("programmedMob5", 250, 250, 40, 40, mob5Pattern, moveRepo.getMoveById(SpecialMoveRepo.AUTO_HURT_EXPLODING), touchedMoveRepo.getMoveById(TouchedMoveRepo.HEAL), bitmapId4, 2, 1);

            unscaledList.add(blueMob);
            unscaledList.add(new GameMob("programmedMob2", 10, 10, 48, 48, mob2Pattern, moveRepo.getMoveById(SpecialMoveRepo.TELEPORT), null, bitmapId, 3, 1));

            unscaledList.add(greenMob);
            unscaledList.add(new GameMob("programmedMob4", 256, 256, 2, 2, mob4Pattern, null, null, null, 1, 1));
            unscaledList.add(orangeMob);
            unscaledList.add(yellowMob);
            unscaledList.addAll(mStaticUnscaledMobList);

            mStaticScaledMobList = scaleList(unscaledList);

        }
        for (GameMob mob : mStaticScaledMobList) {
            returnList.add(mob.clone());
        }
        return returnList;

    }

    /**
     * retourne la liste de mob redimmensionn� pour correspondre a l'affichage actuel de l'appareil </br>
     * on part du pricipe que la liste initial est au dimension par default (Constants.DEFAULT_GAME_WIDTH et Constants.DEFAULT_GAME_HEIGHT)
     *
     * @param unsclaledMobList
     * @return
     */
    public ArrayList<GameMob> scaleList(ArrayList<GameMob> unsclaledMobList) {
        ArrayList<GameMob> scaledList = new ArrayList<GameMob>();
        //utilisé pour la position du mob
        double ratioX = (double) mBoardWidth / Constants.DEFAULT_GAME_WIDTH;
        double ratioY = (double) mBoardHeight / Constants.DEFAULT_GAME_HEIGHT;
        ////////////////

        SpriteRepo spritesRepo = new SpriteRepo();
        for (GameMob unscaledMob : unsclaledMobList) {
            double definitiveRatio = mRatio;//ratio par default pour height/weight du mob

            // sprite scaling
            String spriteSheetID = unscaledMob.getBitmapId();

            if (spriteSheetID != null) {
                definitiveRatio = spritesRepo.getBestRatioForSpriteSheet(spriteSheetID, mRatio);// on redimensionnne l'image plus genere un ratio ideal
            }

            // path scaling
            Point[] scaledPath = new PathRepo().createScalePath(definitiveRatio, unscaledMob.getMovePattern());

            // mob scaling
            GameMob scaledMob = new GameMob(//
                    unscaledMob.getName(),//
                    (int) (unscaledMob.mPosition.left * ratioX),//
                    (int) (unscaledMob.mPosition.top * ratioY),//
                    (int) (unscaledMob.mPosition.width() * definitiveRatio + 0.5),//
                    (int) (unscaledMob.mPosition.height() * definitiveRatio + 0.5),//
                    scaledPath,//
                    unscaledMob.getmSpecialMove1(),//
                    unscaledMob.getmTouchedMove(),//
                    spriteSheetID,//
                    unscaledMob.getHealth(),//
                    unscaledMob.getState());
            scaledList.add(scaledMob);

        }

        return scaledList;

    }

    /**
     * genere des dimension directement depuis le spritesheet, ce qui veux dire que les dimension ne seron a l'echelle que si le spritesheet est a l'echelle
     *
     * @param spritSheetId
     * @return
     */
    public RectF generateDimensionFromSpriteSheet(String spritSheetId) {
        Point spritDimens = new SpriteRepo().getDimensionSpriteSheet(spritSheetId);
        return new RectF(0, 0, spritDimens.x / Constants.SPRITESHEETWIDTH, spritDimens.y / Constants.SPRITESHEETHEIGHT);
    }

    /**
     * add a mob to the staci mob list wich will be used during all the current app execution
     *
     * @param mob
     */
    public void addMobToUnscaledStaticList(GameMob mob) {
        MobRepo.mStaticUnscaledMobList.add(mob);
    }

    public static void fluchScaledMobCache() {
        mStaticScaledMobList.clear();
    }

    public static void fluchAllCache() {
        mStaticScaledMobList.clear();
        mStaticUnscaledMobList.clear();
    }

    //////////////////////////---DB----////////////////////////
    ///////////////////////////////////////////////////////////

    /**
     * save the mob to make it independant of the application execution
     *
     * @param context
     * @param mob
     * @param BoardId
     * @return
     */
    public boolean saveGameMob(Context context, GameMob mob, String BoardId) {
        boolean result = true;
        MobDB mobDb = ModelConverter.mobToMobDB(mob, BoardId);
        PathDB pathDb = ModelConverter.mobToPathDB(mob);

        if (new MobPersister(context).saveMob(mobDb)) {
            result = new PathPersister(context).savePath(pathDb);
        } else {
            result = false;
        }
        return result;
    }

    /**
     * load a mob from the db (with it path)
     *
     * @param context
     * @param MobId
     * @return
     */
    public GameMob LoadGameMob(Context context, String MobId) throws IOException {
        MobDB mobDb = new MobPersister(context).loadMobFromId(MobId);
        PathDB pathDb = new PathPersister(context).loadPathForMob(MobId);

        GameMob loadedMob = ModelConverter.mobDBToMob(mobDb);
        loadedMob.setMovePattern(ModelConverter.pathDBtoPath(pathDb));

        new SpriteRepo().loadFromId(context, loadedMob.getBitmapId(), Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        return loadedMob;
    }

    /**
     * add all mob found on db to the unscaled cache
     *
     * @param context
     * @return
     */
    public int LoadMobsFromDb(Context context) throws IOException {
        ArrayList<String> mobIds = new MobPersister(context).getAllMobsId();
        for (String mobId : mobIds) {
            GameMob mob = LoadGameMob(context, mobId);
            MobRepo.mStaticUnscaledMobList.add(mob);
        }
        return mobIds.size();
    }



}
