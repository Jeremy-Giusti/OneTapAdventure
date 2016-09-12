package fr.giusti.onetapadventure.repository.entities;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import fr.giusti.onetapadventure.gameObject.entities.Entity;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.gameObject.entities.Scenery;
import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.repository.DB.ModelConverter;
import fr.giusti.onetapadventure.repository.DB.model.MobDB;
import fr.giusti.onetapadventure.repository.DB.model.PathDB;
import fr.giusti.onetapadventure.repository.DB.persister.MobPersister;
import fr.giusti.onetapadventure.repository.DB.persister.PathPersister;
import fr.giusti.onetapadventure.repository.PathRepo;
import fr.giusti.onetapadventure.repository.SpecialMoveRepo;
import fr.giusti.onetapadventure.repository.SpriteRepo;
import fr.giusti.onetapadventure.repository.TouchedMoveRepo;
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

        String bitmapId = "spritesheetTest";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet), bitmapId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String bitmapId2 = "spritesheetTest2";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet2), bitmapId2, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String bitmapId3 = "spritesheetTest3";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet3), bitmapId3, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String bitmapId4 = "spritesheetTest4";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet4), bitmapId4, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        //work
        Point[] mob1Pattern = PathRepo.mergePaths(PathRepo.generateCurvedPath(20, 7, 0, 0, 5), PathRepo.generateCurvedPath(20, 7, 0, 0, -5));
        //work
        Point[] mob2Pattern = PathRepo.generateLinePath(20, 5, 3);
        //work
        Point[] mob3Pattern = PathRepo.speedDownPortionOfPath(PathRepo.generateLinePath(20, 5, 7), 6, 15, 2);
        //work
        //work (not perfectly but still)
        Point[] mob5Pattern = PathRepo.generateLoopedPath(Constants.FRAME_PER_SEC, new Point(0, 0), new Point(0, 5), 7, 0);//un tour a la seconde
        //seems to work
        Point[] mob6Pattern = PathRepo.generateRandomPath(100, 5, 5, 15, 15, 25);

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


    public static ArrayList<Entity> getLvl1x1InitList(Context context) {
        ArrayList<Entity> entityList = new ArrayList<>();

        String bitmapId = "hole1pict";
        SpriteRepo.addPicture(bitmapId,BitmapFactory.decodeResource(context.getResources(), R.drawable.brokenglass_front));
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();
        RectF hitbox = new RectF(326,132,392,272);
        Scenery hole1 = new Scenery("holes1",263,64,266,263,hitbox,touchedMoveRepo.getMoveById(TouchedMoveRepo.MOB_AWAY_MOVE),bitmapId);
        entityList.add(hole1);

        String mob1sptsheetId = "mob1";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet), mob1sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);



        return null;
    }

    public static Pair<Integer, GameMob> getLvl1x1BackupList() {
        return null;
    }
}
