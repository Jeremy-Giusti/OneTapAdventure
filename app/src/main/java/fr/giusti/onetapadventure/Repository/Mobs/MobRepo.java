package fr.giusti.onetapadventure.Repository.Mobs;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.giusti.onetapadventure.GameObject.GameMob;
import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.Repository.DB.ModelConverter;
import fr.giusti.onetapadventure.Repository.DB.model.MobDB;
import fr.giusti.onetapadventure.Repository.DB.model.PathDB;
import fr.giusti.onetapadventure.Repository.DB.persister.MobPersister;
import fr.giusti.onetapadventure.Repository.DB.persister.PathPersister;
import fr.giusti.onetapadventure.Repository.PathRepo;
import fr.giusti.onetapadventure.Repository.SpecialMoveRepo;
import fr.giusti.onetapadventure.Repository.SpriteRepo;
import fr.giusti.onetapadventure.Repository.TouchedMoveRepo;
import fr.giusti.onetapadventure.commons.Constants;

public class MobRepo {


    private static final String TAG = MobRepo.class.getSimpleName();

    /**
     * creer une liste de mob generé en dur (code) +les mobs créés avec l'editeur de mob
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public static CopyOnWriteArrayList<GameMob> getSampleMobList(Context mContext) throws CloneNotSupportedException {//

        CopyOnWriteArrayList<GameMob> returnList = new CopyOnWriteArrayList<GameMob>();

        PathRepo pathRepo = new PathRepo();

        String bitmapId = "spritesheetTest";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet), bitmapId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String bitmapId2 = "spritesheetTest2";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet2), bitmapId2, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String bitmapId3 = "spritesheetTest3";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet3), bitmapId3, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String bitmapId4 = "spritesheetTest4";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet4), bitmapId4, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        //work
        Point[] mob1Pattern = pathRepo.mergePaths(pathRepo.generateCurvedPath(20, 7, 0, 0, 5), pathRepo.generateCurvedPath(20, 7, 0, 0, -5));
        //work
        Point[] mob2Pattern = pathRepo.generateLinePath(20, 5, 3);
        //work
        Point[] mob3Pattern = pathRepo.speedDownPortionOfPath(pathRepo.generateLinePath(20, 5, 7), 6, 15, 2);
        //work
        //work (not perfectly but still)
        Point[] mob5Pattern = pathRepo.generateLoopedPath(Constants.FRAME_PER_SEC, new Point(0, 0), new Point(0, 5), 7, 0);//un tour a la seconde
        //seems to work
        Point[] mob6Pattern = pathRepo.generateRandomPath(100, 5, 5, 15, 15, 25);

        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();
        GameMob blueMob = new GameMob("programmedMob1", 1, 250, 32, 32, mob1Pattern, moveRepo.getMoveById(SpecialMoveRepo.TRAIL), touchedMoveRepo.getMoveById(TouchedMoveRepo.BAIT), bitmapId4, 5, 1);
        GameMob greenMob = new GameMob("programmedMob3", 100, 100, 48, 48, mob3Pattern, moveRepo.getMoveById(SpecialMoveRepo.SWAP), null, bitmapId3, 1, 1);
        GameMob yellowMob = new GameMob("programmedMob6", 250, 250, 32, 32, mob6Pattern, moveRepo.getMoveById(SpecialMoveRepo.AUTO_HEAL), touchedMoveRepo.getMoveById(TouchedMoveRepo.BLEED), bitmapId2, 2, 1);
        GameMob orangeMob = new GameMob("programmedMob5", 250, 250, 40, 40, mob5Pattern, moveRepo.getMoveById(SpecialMoveRepo.AUTO_HURT_EXPLODING), touchedMoveRepo.getMoveById(TouchedMoveRepo.HEAL), bitmapId4, 2, 1);

        returnList.add(blueMob);
        returnList.add(new GameMob("programmedMob2", 10, 10, 48, 48, mob2Pattern, moveRepo.getMoveById(SpecialMoveRepo.TELEPORT), null, bitmapId, 3, 1));

        returnList.add(greenMob);

        //    Point[] mob4Pattern = {new Point(0, 0)};
        //     returnList.add(new GameMob("programmedMob4", 256, 256, 2, 2, mob4Pattern, null, null, null, 1, 1));
        returnList.add(orangeMob);
        returnList.add(yellowMob);

        try {
            returnList.addAll(LoadMobsFromDb(mContext));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Error while loading mob from db");
        }

        return returnList;

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
    public static boolean saveGameMob(Context context, GameMob mob, String BoardId) {
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
    public static GameMob LoadGameMob(Context context, String MobId) throws IOException {
        MobDB mobDb = new MobPersister(context).loadMobFromId(MobId);
        PathDB pathDb = new PathPersister(context).loadPathForMob(MobId);

        GameMob loadedMob = ModelConverter.mobDBToMob(mobDb);
        loadedMob.setMovePattern(ModelConverter.pathDBtoPath(pathDb));

        SpriteRepo.loadSpriteSheetFromId(context, loadedMob.getBitmapId(), Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        return loadedMob;
    }

    /**
     * add all mob found on db to the unscaled cache
     *
     * @param context
     * @return
     */
    public static ArrayList<GameMob> LoadMobsFromDb(Context context) throws IOException {
        ArrayList<GameMob> returnList = new ArrayList<>();
        ArrayList<String> mobIds = new MobPersister(context).getAllMobsId();
        for (String mobId : mobIds) {
            GameMob mob = LoadGameMob(context, mobId);
            returnList.add(mob);
        }
        return returnList;
    }


}
