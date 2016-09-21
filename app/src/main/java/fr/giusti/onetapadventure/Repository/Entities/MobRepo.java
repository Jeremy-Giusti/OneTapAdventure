package fr.giusti.onetapadventure.repository.entities;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.gameObject.entities.Entity;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.gameObject.entities.Scenery;
import fr.giusti.onetapadventure.gameObject.moves.TouchedMove;
import fr.giusti.onetapadventure.repository.DB.ModelConverter;
import fr.giusti.onetapadventure.repository.DB.model.MobDB;
import fr.giusti.onetapadventure.repository.DB.model.PathDB;
import fr.giusti.onetapadventure.repository.DB.persister.MobPersister;
import fr.giusti.onetapadventure.repository.DB.persister.PathPersister;
import fr.giusti.onetapadventure.repository.PathRepo;
import fr.giusti.onetapadventure.repository.SpecialMoveRepo;
import fr.giusti.onetapadventure.repository.SpriteRepo;
import fr.giusti.onetapadventure.repository.TouchedMoveRepo;
import fr.giusti.onetapadventure.repository.levelsData.Lvl1Constant;

public class MobRepo {


    private static final String TAG = MobRepo.class.getSimpleName();

    /**
     * creer une liste de mob generé en dur (code) +les mobs créés avec l'editeur de mob
     *
     * @return
     * @throws CloneNotSupportedException
     */
    public static ArrayList<GameMob> getSampleMobList(Context mContext) throws CloneNotSupportedException {//

        ArrayList<GameMob> returnList = new ArrayList<GameMob>();

        String bitmapId = "spritesheetTest";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet), bitmapId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String bitmapId2 = "spritesheetTest2";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet2), bitmapId2, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String bitmapId3 = "spritesheetTest3";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet3), bitmapId3, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String bitmapId4 = "spritesheetTest4";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet4), bitmapId4, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        //work
        PointF[] mob1Pattern = PathRepo.mergePaths(PathRepo.generateCurvedPath(20, 7, 0, 0, 5), PathRepo.generateCurvedPath(20, 7, 0, 0, -5));
        //work
        PointF[] mob2Pattern = PathRepo.generateLinePath(20, 5, 3);
        //work
        PointF[] mob3Pattern = PathRepo.speedDownPortionOfPath(PathRepo.generateLinePath(20, 5, 7), 6, 15, 2);
        //work
        //work (not perfectly but still)
        PointF[] mob5Pattern = PathRepo.generateLoopedPath(Constants.FRAME_PER_SEC, new Point(0, 0), new Point(0, 5), 7, 0);//un tour a la seconde
        //seems to work
        PointF[] mob6Pattern = PathRepo.generateLinePath(1, 4,4);

        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();
        GameMob blueMob = new GameMob("programmedMob1", 1, 250, 32, 32, mob1Pattern, moveRepo.getMoveById(SpecialMoveRepo.SMOKE_TRAIL), touchedMoveRepo.getMoveById(TouchedMoveRepo.BAIT), bitmapId4, 50, 1);
        GameMob greenMob = new GameMob("programmedMob3", 100, 100, 48, 48, mob3Pattern, moveRepo.getMoveById(SpecialMoveRepo.SWAP), touchedMoveRepo.getMoveById(TouchedMoveRepo.BLEED), bitmapId3, 10, 1);
        GameMob yellowMob = new GameMob("programmedMob6", 250, 250, 48, 48, mob6Pattern, moveRepo.getMoveById(SpecialMoveRepo.MULTIPLIE), null, bitmapId2, 10, 1);
        GameMob orangeMob = new GameMob("programmedMob5", 250, 250, 40, 40, mob5Pattern, moveRepo.getMoveById(SpecialMoveRepo.AUTO_HURT_EXPLODING), touchedMoveRepo.getMoveById(TouchedMoveRepo.HEAL), bitmapId4, 20, 1);

        returnList.add(blueMob);
        returnList.add(new GameMob("programmedMob2", 10, 10, 48, 48, mob2Pattern, moveRepo.getMoveById(SpecialMoveRepo.TELEPORT), null, bitmapId, 30, 1));

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

        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();

        String bitmapId = "hole1pict";
        SpriteRepo.addPicture(bitmapId, BitmapFactory.decodeResource(context.getResources(), R.drawable.brokenglass_front));

        PointF posDest = new PointF(Lvl1Constant.HOLE1_DIMENS.left + (Lvl1Constant.HOLE1_DIMENS.width() / 2), Lvl1Constant.HOLE1_DIMENS.top + (Lvl1Constant.HOLE1_DIMENS.height() / 2));
        PointF startPos = new PointF(Lvl1Constant.MOB_POP_X, Lvl1Constant.MOB_POP_Y_MAX_VAlUE / 2);

        int hitboxLeft = Lvl1Constant.HOLE1_DIMENS.left - Lvl1Constant.HOLE_HITBOX_MARGIN;
        int hitboxTop = Lvl1Constant.HOLE1_DIMENS.top - Lvl1Constant.HOLE_HITBOX_MARGIN;
        int hitboxRight = Lvl1Constant.HOLE1_DIMENS.right - Lvl1Constant.HOLE_HITBOX_MARGIN;
        int hitboxBottom = Lvl1Constant.HOLE1_DIMENS.bottom - Lvl1Constant.HOLE_HITBOX_MARGIN;
        RectF hitbox = new RectF(hitboxLeft, hitboxTop, hitboxRight, hitboxBottom);

        Scenery hole1 = new Scenery("holes1", Lvl1Constant.HOLE1_DIMENS.left, Lvl1Constant.HOLE1_DIMENS.top, Lvl1Constant.HOLE1_DIMENS.width(), Lvl1Constant.HOLE1_DIMENS.height(), hitbox, touchedMoveRepo.getMoveById(TouchedMoveRepo.MOB_AWAY_MOVE), bitmapId);
        entityList.add(hole1);

        String mob1sptsheetId = "tier1Mob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet), mob1sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String mob2sptsheetId = "tier2Mob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet_yellow), mob2sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String mob3sptsheetId = "tier3Mob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet4), mob3sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String lastMobsptsheetId = "lastMob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet3), lastMobsptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);

        PointF[] mob1Pattern = PathRepo.generateLineToDest(startPos, posDest, Constants.FRAME_PER_SEC * 3);

        GameMob mob1 = new GameMob("firstMob", (int) startPos.x, (int) startPos.y, Lvl1Constant.MOB_SIZE, Lvl1Constant.MOB_SIZE, mob1Pattern, moveRepo.getMoveById(SpecialMoveRepo.NO_MOVE), touchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE), mob1sptsheetId, 10, 1);
        mob1.setAlignement(1);
        entityList.add(mob1);

        return entityList;
    }

    /**
     * create all non initial mob for lvl 1 (3 difficulty tier and 1 last wich as different behavior)
     *
     * @param context
     * @return
     */
    public static ArrayList<Pair<Integer, GameMob>> getLvl1x1BackupList(Context context) {
        ArrayList<Pair<Integer, GameMob>> backupList = new ArrayList<Pair<Integer, GameMob>>();
        int mobNb = Lvl1Constant.MOB_NB - 2;

        String mobaseNameID = "mob";
        int currentTier = 1;
        PointF posDest = new PointF(Lvl1Constant.HOLE1_DIMENS.left + Lvl1Constant.HOLE1_DIMENS.width() / 2, Lvl1Constant.HOLE1_DIMENS.top + Lvl1Constant.HOLE1_DIMENS.height() / 2);
        for (int i = 0; i < mobNb; i++) {
            if (i < Lvl1Constant.TIERS1_MOB_OCCURENCE) currentTier = 1;
            else if (i < Lvl1Constant.TIERS1_MOB_OCCURENCE + Lvl1Constant.TIERS2_MOB_OCCURENCE)
                currentTier = 2;
            else currentTier = 3;
            int seed = (int) (Math.random() * (float) Lvl1Constant.MOB_POP_Y_MAX_VAlUE);
            GameMob mob = getMobFromSeed(context, currentTier, seed, posDest, mobaseNameID + i, Lvl1Constant.MOB_POP_X, seed, Lvl1Constant.MOB_SIZE, Lvl1Constant.MOB_SIZE);
            mob.setAlignement(currentTier);
            backupList.add(new Pair<>(currentTier, mob));
        }

        //--------------------Last mob --------------------------
        PointF startPos = new PointF(Lvl1Constant.MOB_POP_X, Lvl1Constant.MOB_POP_Y_MAX_VAlUE / 2);
        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();
        int lastMobDirection = (Math.random() < 0.5) ? -1 : 1;
        GameMob lastMob = new GameMob("lastMob", (int) startPos.x, (int) startPos.y, Lvl1Constant.MOB_SIZE, Lvl1Constant.MOB_SIZE, new PointF[]{new PointF(-4, 15 * lastMobDirection)}, moveRepo.getMoveById(SpecialMoveRepo.NO_MOVE), touchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE), "lastMob", 30, 1);
        lastMob.setAlignement(4);
        backupList.add(new Pair<>(0, lastMob));
        // -----------------------------------------------------

        return backupList;
    }

    public static GameMob getMobFromSeed(Context context, int difficulty, int seed, PointF posDest, String id, int x, int y, int width, int height) {
        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();
        TouchedMove touchedMove = touchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE);
        PointF[] path;
        String spriteId;
        if (difficulty < 2) {
            spriteId = "tier1Mob";
            path = PathRepo.generateLineToDest(new PointF(x, y), posDest, Constants.FRAME_PER_SEC * 3);
        } else {
            if (seed % 4 > 2) {
                path = PathRepo.generateLineToDest(new PointF(x, y), posDest, (int) (Constants.FRAME_PER_SEC * 3.50f));
                touchedMove = touchedMoveRepo.getMoveById(TouchedMoveRepo.BAIT);
            } else {
                path = PathRepo.generateCurvedPath(new PointF(x, y), posDest, 200, (seed % 2 == 1), (int) (Constants.FRAME_PER_SEC * 3.75));
            }
            spriteId = (difficulty == 2) ? "tier2Mob" : "tier3Mob";
        }
        return new GameMob(id, x, y, width, height, path, moveRepo.getMoveById(SpecialMoveRepo.NO_MOVE), touchedMove, spriteId, difficulty * Constants.TOUCH_DAMAGE, 1);

    }
}
