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
import java.util.Random;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.commons.GameConstant;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.entity.Particule;
import fr.giusti.onetapengine.entity.Scenery;
import fr.giusti.onetapadventure.repository.DB.ModelConverter;
import fr.giusti.onetapadventure.repository.DB.model.MobDB;
import fr.giusti.onetapadventure.repository.DB.model.PathDB;
import fr.giusti.onetapadventure.repository.DB.persister.MobPersister;
import fr.giusti.onetapadventure.repository.DB.persister.PathPersister;
import fr.giusti.onetapengine.entity.distribution.ParticuleHolder;
import fr.giusti.onetapengine.entity.moves.TouchedMove;
import fr.giusti.onetapengine.repository.SpecialMoveRepo;
import fr.giusti.onetapengine.repository.ParticuleRepo;
import fr.giusti.onetapengine.repository.SpriteRepo;
import fr.giusti.onetapengine.repository.TouchedMoveRepo;
import fr.giusti.onetapadventure.repository.levelsData.Lvl1Constant;
import fr.giusti.onetapadventure.repository.levelsData.Lvl2Constant;
import fr.giusti.onetapengine.repository.PathRepo;

public class EntityRepo {


    private static final String TAG = EntityRepo.class.getSimpleName();

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
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.fly_spritesheet_yellow), bitmapId2, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
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
        //PointF[] mob5Pattern = PathRepo.generateLoopedPath(Constants.FRAME_PER_SEC, new Point(0, 0), new Point(0, 5), 7, 0);//un tour a la seconde
        Pair<PointF,PointF[]> mob5Pattern = PathRepo.generateSpiralePath(Constants.FRAME_PER_SEC*5, new PointF(512, 256),240, 0,4);//un tour a la seconde
       //  Pair<PointF,PointF[]> mob5Pattern = PathRepo.generateCirclePath(Constants.FRAME_PER_SEC, new PointF(512, 256),240, 0);//un tour a la seconde

        //seems to work
        PointF[] mob6Pattern = PathRepo.generateLinePath(1, 4, 4);

        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();
        GameMob.MobBuilder mobBuilder = new GameMob.MobBuilder("programmedMob1", bitmapId4, 1, 250);
        GameMob blueMob = mobBuilder.setSize(32).
                setDefaultHealth(50).
                setMovePattern(mob1Pattern).
                setSpecialMove(moveRepo.getMoveById(SpecialMoveRepo.SMOKE_TRAIL)).
                setTouchedMove(touchedMoveRepo.getMoveById(TouchedMoveRepo.BAIT))
                .build();
        mobBuilder = new GameMob.MobBuilder("programmedMob3", bitmapId3, 100, 100);
        GameMob greenMob = mobBuilder.setMovePattern(mob3Pattern).
                setSpecialMove(moveRepo.getMoveById(SpecialMoveRepo.SWAP)).
                setTouchedMove(touchedMoveRepo.getMoveById(TouchedMoveRepo.BLEED))
                .build();

        mobBuilder = new GameMob.MobBuilder("programmedMob6", bitmapId2, 250, 250);
        GameMob yellowMob = mobBuilder.setMovePattern(mob6Pattern).
                setSpecialMove(moveRepo.getMoveById(SpecialMoveRepo.MULTIPLIE))
                .build();
        mobBuilder = new GameMob.MobBuilder("programmedMob5", bitmapId4, mob5Pattern.first.x, mob5Pattern.first.y);
        GameMob orangeMob = mobBuilder.setMovePattern(mob5Pattern.second).
                setSize(64).
                setDefaultHealth(20).
                setSpecialMove(moveRepo.getMoveById(SpecialMoveRepo.AUTO_HURT_EXPLODING)).
                setTouchedMove(touchedMoveRepo.getMoveById(TouchedMoveRepo.HEAL))
                .build();


        returnList.add(blueMob);
        returnList.add(greenMob);
        returnList.add(orangeMob);
        returnList.add(yellowMob);

        GameMob ghostMob = getGhostMob("ghost", mContext, new Point(1000, 500), new Point(0, 250), Constants.FRAME_PER_SEC * 5, 50);
        returnList.add(ghostMob);
        GameMob holeMob = getHoleMakerMob("holemob", mContext, new Point(1000, 16), new Point(0, 250), Constants.FRAME_PER_SEC * 5, 10);
        returnList.add(holeMob);
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

        String bitmapId = "broken_glass_side";
        SpriteRepo.addPicture(bitmapId, BitmapFactory.decodeResource(context.getResources(), R.drawable.brokenglass_left_side));

        PointF posDest = new PointF(Lvl1Constant.HOLE1_DIMENS.left + (Lvl1Constant.HOLE1_DIMENS.width() / 2), Lvl1Constant.HOLE1_DIMENS.top + (Lvl1Constant.HOLE1_DIMENS.height() / 2));
        PointF startPos = new PointF(Lvl1Constant.MOB_POP_X, Lvl1Constant.MOB_POP_Y_MAX_VAlUE / 2);

        int hitboxLeft = Lvl1Constant.HOLE1_DIMENS.left - Lvl1Constant.HOLE_HITBOX_MARGIN;
        int hitboxTop = Lvl1Constant.HOLE1_DIMENS.top - Lvl1Constant.HOLE_HITBOX_MARGIN;
        int hitboxRight = Lvl1Constant.HOLE1_DIMENS.right - Lvl1Constant.HOLE_HITBOX_MARGIN;
        int hitboxBottom = Lvl1Constant.HOLE1_DIMENS.bottom - Lvl1Constant.HOLE_HITBOX_MARGIN;
        RectF hitbox = new RectF(hitboxLeft, hitboxTop, hitboxRight, hitboxBottom);

        Scenery hole1 = new Scenery("holes1", Lvl1Constant.HOLE1_DIMENS.left, Lvl1Constant.HOLE1_DIMENS.top, Lvl1Constant.HOLE1_DIMENS.width(), Lvl1Constant.HOLE1_DIMENS.height(), hitbox, touchedMoveRepo.getMoveById(TouchedMoveRepo.DISAPEAR), bitmapId);
        entityList.add(hole1);

        Particule glassParticule1 = ParticuleHolder.getAvailableParticule(ParticuleRepo.GLASS_PARTICULE, Lvl1Constant.HOLE1_DIMENS.centerX(), Lvl1Constant.HOLE1_DIMENS.centerY() - Lvl1Constant.HOLE1_DIMENS.width() / 2, Lvl1Constant.HOLE1_DIMENS.width(), Lvl1Constant.HOLE1_DIMENS.width(), false, new PointF[]{new PointF(0, 6)});
        Particule glassParticule2 = ParticuleHolder.getAvailableParticule(ParticuleRepo.GLASS_PARTICULE, Lvl1Constant.HOLE1_DIMENS.centerX(), Lvl1Constant.HOLE1_DIMENS.centerY() + Lvl1Constant.HOLE1_DIMENS.width() / 2, Lvl1Constant.HOLE1_DIMENS.width(), Lvl1Constant.HOLE1_DIMENS.width(), false, new PointF[]{new PointF(0, 6)});
        entityList.add(glassParticule1);
        entityList.add(glassParticule2);

        String mob1sptsheetId = "tier1Mob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet), mob1sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);

        GameMob mob1 = generateSimpleRandomizedMob("firstMob1", context, new RectF(startPos.x - 5, startPos.y + 45, startPos.x + 5, startPos.y + 55), hitbox, (int) (Constants.FRAME_PER_SEC * 4.5));
        GameMob mob2 = generateSimpleRandomizedMob("firstMob2", context, new RectF(startPos.x - 5, startPos.y - 55, startPos.x + 5, startPos.y - 45), hitbox, (int) (Constants.FRAME_PER_SEC * 4.5));

        entityList.add(mob1);
        entityList.add(mob2);

        return entityList;
    }

    /**
     * create all non initial mob for lvl 1 (3 difficulty tier and 1 last wich as different behavior)
     *
     * @param context
     * @return
     */
    public static ArrayList<Entity> getLvl1x1BackupList(Context context) {
        ArrayList<Entity> backupList = new ArrayList<>();
        int tierMobNb = Lvl1Constant.MIXED_TIER_MOB_NB;

        String mobaseNameID = "mob";
        int currentTier = 1;
        int tier1Added = 0;
        PointF posDest = new PointF(Lvl1Constant.HOLE1_DIMENS.left + Lvl1Constant.HOLE1_DIMENS.width() / 2, Lvl1Constant.HOLE1_DIMENS.top + Lvl1Constant.HOLE1_DIMENS.height() / 2);

        for (int i = 0; i < tierMobNb; i++) {
            if (tier1Added < 2 || i < (tierMobNb / 3)) {
                tier1Added++;
                currentTier = 1;
            } else {
                currentTier = (i < (tierMobNb / 3) * 2) ? 2 : 3;
                tier1Added = 0;
            }


            int seed = (int) (Math.random() * (float) Lvl1Constant.MOB_POP_Y_MAX_VAlUE);
            GameMob mob = getMobFromSeed(context, currentTier, seed, posDest, mobaseNameID + i, Lvl1Constant.MOB_POP_X, seed, Lvl1Constant.MOB_SIZE, Lvl1Constant.MOB_SIZE);
            mob.setAlignement(currentTier);
            backupList.add(mob);
        }

        //--------------------Last mob --------------------------
        Point startPos = new Point(Lvl1Constant.MOB_POP_X, Lvl1Constant.MOB_POP_Y_MAX_VAlUE / 2);
        GameMob lastMob = getRapidScanMob("lastMob", context, startPos, 30);
        lastMob.setAlignement(4);
        backupList.add(lastMob);
        // -----------------------------------------------------

        return backupList;
    }

    public static ArrayList<Entity> getLvl1x1LastWave(Context context) {
        ArrayList<Entity> result = new ArrayList<>();
        for (int i = 0; i < Lvl1Constant.END_MOB_WAVE; i++) {
            result.add(generateSimpleRandomizedMob("wave" + i, context, new RectF(1010, 5, 1019, 506), new RectF(Lvl1Constant.HOLE1_DIMENS), Constants.FRAME_PER_SEC * 3));
        }
        return result;
    }


    public static ArrayList<Entity> getLvl1x2InitList(Context context) {
        ArrayList<Entity> entityList = new ArrayList<>();

        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();

        String bitmapId = GameConstant.HOLE_FRONT_SPRITE_ID;
        SpriteRepo.addPicture(bitmapId, BitmapFactory.decodeResource(context.getResources(), R.drawable.brokenglass_front));

        PointF posDest = new PointF(Lvl2Constant.HOLE1_DIMENS.left + (Lvl2Constant.HOLE1_DIMENS.width() / 2), Lvl2Constant.HOLE1_DIMENS.top + (Lvl2Constant.HOLE1_DIMENS.height() / 2));

        int hitboxLeft = Lvl2Constant.HOLE1_DIMENS.left + Lvl2Constant.HOLE_HITBOX_MARGIN;
        int hitboxTop = Lvl2Constant.HOLE1_DIMENS.top + Lvl2Constant.HOLE_HITBOX_MARGIN;
        int hitboxRight = Lvl2Constant.HOLE1_DIMENS.right - Lvl2Constant.HOLE_HITBOX_MARGIN;
        int hitboxBottom = Lvl2Constant.HOLE1_DIMENS.bottom - Lvl2Constant.HOLE_HITBOX_MARGIN;
        RectF hitbox = new RectF(hitboxLeft, hitboxTop, hitboxRight, hitboxBottom);

        Scenery hole1 = new Scenery("holes1", Lvl2Constant.HOLE1_DIMENS.left, Lvl2Constant.HOLE1_DIMENS.top, Lvl2Constant.HOLE1_DIMENS.width(), Lvl2Constant.HOLE1_DIMENS.height(), hitbox, touchedMoveRepo.getMoveById(TouchedMoveRepo.DISAPEAR), bitmapId);
        entityList.add(hole1);

        Particule glassParticule = ParticuleHolder.getAvailableParticule(ParticuleRepo.GLASS_PARTICULE, Lvl2Constant.HOLE1_DIMENS.centerX(), Lvl2Constant.HOLE1_DIMENS.centerY(), Lvl2Constant.HOLE1_DIMENS.width(), Lvl2Constant.HOLE1_DIMENS.height(), false, new PointF[]{new PointF(0, 6)});
        entityList.add(glassParticule);

        String mob1sptsheetId = "tier1Mob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet), mob1sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);

        RectF rectDest = new RectF(posDest.x, posDest.y, posDest.x+1, posDest.y+1);
        // PointF[] mob1Pattern = PathRepo.generateLineToDest(new PointF(10, 10), posDest, Constants.FRAME_PER_SEC * 4);
        GameMob mob1 = generateSimpleRandomizedMob("firstMob", context, new RectF(10, 10, 11, 11), rectDest, Constants.FRAME_PER_SEC * 4);
        //      new GameMob("firstMob", 10, 10, Lvl1Constant.MOB_SIZE, Lvl1Constant.MOB_SIZE, mob1Pattern, moveRepo.getMoveById(SpecialMoveRepo.NO_MOVE), touchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE), mob1sptsheetId, 10, 1);

        // PointF[] mob2Pattern = PathRepo.generateLineToDest(new PointF(10, 500), posDest, Constants.FRAME_PER_SEC * 4);
        GameMob mob2 = generateSimpleRandomizedMob("secondMob", context, new RectF(10, 500, 11, 501), rectDest, Constants.FRAME_PER_SEC * 4);
        //new GameMob("secondMob", 10, 500, Lvl1Constant.MOB_SIZE, Lvl1Constant.MOB_SIZE, mob2Pattern, moveRepo.getMoveById(SpecialMoveRepo.NO_MOVE), touchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE), mob1sptsheetId, 10, 1);

        // PointF[] mob3Pattern = PathRepo.generateLineToDest(new PointF(1000, 10), posDest, Constants.FRAME_PER_SEC * 4);
        GameMob mob3 = generateSimpleRandomizedMob("thirdMob", context, new RectF(1000, 10, 1001, 11), rectDest, Constants.FRAME_PER_SEC * 4);

        // PointF[] mob4Pattern = PathRepo.generateLineToDest(new PointF(1000, 500), posDest, Constants.FRAME_PER_SEC * 4);
        GameMob mob4 = generateSimpleRandomizedMob("fourthMob", context, new RectF(1000, 500, 1001, 501), rectDest, Constants.FRAME_PER_SEC * 4);

        entityList.add(mob1);
        entityList.add(mob2);
        entityList.add(mob3);
        entityList.add(mob4);

        return entityList;
    }

    public static ArrayList<Entity> getLvl1x2BackupList(Context context) {

        ArrayList<Entity> result = new ArrayList<>();

        RectF dest = new RectF(Lvl2Constant.HOLE1_DIMENS.left, 64, Lvl2Constant.HOLE1_DIMENS.right, 458);
        int startx;
        int startYmin;
        int startYmax;

        for (int i = 0; i < Lvl2Constant.MOB_NB; i++) {
            startx = (i < Lvl2Constant.MOB_NB / 2) ? 5 : 1014;
            startYmin = (i % 10 < 5) ? 5 : 256;
            startYmax = (i % 10 < 5) ? 256 : 507;
            result.add(generateSimpleRandomizedMob("mobNb" + i, context, new RectF(startx, startYmin, startx + 5, startYmax), dest, Constants.FRAME_PER_SEC * 3));
        }

        return result;
    }

    public static ArrayList<Entity> getLvl1x2SpecialList(Context context) {

        ArrayList<Entity> result = new ArrayList<>();

        RectF dest = new RectF(Lvl2Constant.HOLE1_DIMENS.left, 0, Lvl2Constant.HOLE1_DIMENS.right, 512);
        int startx;
        int startYmin;
        int startYmax;

        for (int i = 0; i < 6; i++) {
            startx = (i < Lvl2Constant.MOB_NB / 2) ? 5 : 1014;
            startYmin = 5;
            startYmax = 507;
            result.add(generateSimpleRandomizedMob("mobNb" + i, context, new RectF(startx, startYmin, startx + 5, startYmax), dest, Constants.FRAME_PER_SEC * 3));
        }

        Point startPoint = (Math.random() < 0.5) ? new Point(5, 232) : new Point(1019, 232);
        result.add(getRapidScanMob("rapideMob", context, startPoint, 10));

        PointF startPoint2 = (Math.random() < 0.5) ? new PointF(5, 232) : new PointF(1019, 232);
        PointF destPoint = new PointF(dest.centerX(), dest.centerY());
        result.add(getTeleportMob("teleportMob", context, startPoint2, destPoint, 30));


        return result;
    }

    public static ArrayList<Entity> getLvl1x3SpecialList(Context context) {
        ArrayList<Entity> result = new ArrayList<>();


        return null;
    }


    public static GameMob getMobFromSeed(Context context, int difficulty, int seed, PointF posDest, String id, int x, int y, int width, int height) {
        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();
        TouchedMove touchedMove = touchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE);
        PointF[] path;
        String spriteId;

        String mob1sptsheetId = "tier1Mob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet), mob1sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String mob2sptsheetId = "tier2Mob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet_yellow), mob2sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        String mob3sptsheetId = "tier3Mob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet4), mob3sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);


        if (difficulty < 2) {
            spriteId = mob1sptsheetId;
            path = PathRepo.generateLineToDest(new PointF(x, y), posDest, Constants.FRAME_PER_SEC * 3);
        } else {
            if (seed % 4 > 2) {
                path = PathRepo.generateLineToDest(new PointF(x, y), posDest, (int) (Constants.FRAME_PER_SEC * 3.50f));
                touchedMove = touchedMoveRepo.getMoveById(TouchedMoveRepo.BAIT);
            } else {
                path = PathRepo.generateCurvedPath(new PointF(x, y), posDest, 200, (seed % 2 == 1), (int) (Constants.FRAME_PER_SEC * 3.75));
            }
            spriteId = (difficulty == 2) ? mob2sptsheetId : mob3sptsheetId;
        }
        return new GameMob.MobBuilder(id, spriteId, x, y)
                .setWidth(width)
                .setHeight(height)
                .setMovePattern(path)
                .setTouchedMove(touchedMove)
                .setDefaultHealth(difficulty * GameConstant.TOUCH_DAMAGE)
                .build();
        //    GameMob(id, x, y, width, height, path, moveRepo.getMoveById(SpecialMoveRepo.NO_MOVE), touchedMove, spriteId, difficulty * GameConstant.TOUCH_DAMAGE, 1);
    }

    public static GameMob generateSimpleRandomizedMob(String id, Context context, RectF startPos, RectF destPos, int travelTimeOnTick) {
        String mob1sptsheetId = "tier1Mob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet), mob1sptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);

        Random r = new Random();

        float startX = r.nextInt((int) (startPos.right - startPos.left)) + startPos.left;
        float startY = r.nextInt((int) (startPos.bottom - startPos.top)) + startPos.top;
        float destX = r.nextInt((int) (destPos.right - destPos.left)) + destPos.left;
        float destY = r.nextInt((int) (destPos.bottom - destPos.top)) + destPos.top;

        PointF[] path = PathRepo.generateLineToDest(new PointF(startX, startY), new PointF(destX, destY), travelTimeOnTick);

        return new GameMob.MobBuilder(id, mob1sptsheetId, startX, startY).setMovePattern(path).build();
        //  GameMob(id, startX, startY, GameConstant.DEFAULT_MOB_SIZE, 48, path, SpecialMoveRepo.getMoveById(SpecialMoveRepo.NO_MOVE), TouchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE), mob1sptsheetId, GameConstant.TOUCH_DAMAGE, 1);
    }

    public static GameMob getRapidScanMob(String id, Context context, Point startPos, int health) {
        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();
        int lastMobDirection = (Math.random() < 0.5) ? -1 : 1;
        String lastMobsptsheetId = "rapideMob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet3), lastMobsptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        return new GameMob.MobBuilder(id, lastMobsptsheetId, startPos.x, startPos.y)
                .setMovePattern(new PointF[]{new PointF(-4, 15 * lastMobDirection)})
                .setDefaultHealth(health)
                .build();
        //     GameMob(id, startPos.x, startPos.y, GameConstant.DEFAULT_MOB_SIZE, GameConstant.DEFAULT_MOB_SIZE, new PointF[]{new PointF(-4, 15 * lastMobDirection)}, moveRepo.getMoveById(SpecialMoveRepo.NO_MOVE), touchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE), lastMobsptsheetId, health, 1);
    }

    private static GameMob getTeleportMob(String id, Context context, PointF startPoint, PointF destPoint, int health) {
        SpecialMoveRepo moveRepo = new SpecialMoveRepo();
        TouchedMoveRepo touchedMoveRepo = new TouchedMoveRepo();

        PointF[] path = PathRepo.generateLineToDest(startPoint, destPoint, 3 * Constants.FRAME_PER_SEC);
        String mobsptsheetId = "tpMob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet_purple), mobsptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        return new GameMob.MobBuilder(id, mobsptsheetId, startPoint.x, startPoint.y)
                .setMovePattern(path)
                .setTouchedMove(touchedMoveRepo.getMoveById(TouchedMoveRepo.TELEPORT))
                .setHealth(health)
                .build();
        //    GameMob(id, startPoint.x, startPoint.y, GameConstant.DEFAULT_MOB_SIZE, GameConstant.DEFAULT_MOB_SIZE, path, moveRepo.getMoveById(SpecialMoveRepo.NO_MOVE), touchedMoveRepo.getMoveById(TouchedMoveRepo.TELEPORT), mobsptsheetId, health, 1);

    }

    private static GameMob getHoleMakerMob(String id, Context context, Point startPoint, Point destPoint, int tickToDest, int health) {


        String bitmapId = GameConstant.HOLE_FRONT_SPRITE_ID;
        SpriteRepo.addPicture(bitmapId, BitmapFactory.decodeResource(context.getResources(), R.drawable.brokenglass_front));

        PointF[] path = PathRepo.generateLineToDest(new PointF(startPoint), new PointF(destPoint), tickToDest);
        String mobsptsheetId = "holemakermob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet2), mobsptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        return new GameMob.MobBuilder(id, mobsptsheetId, startPoint.x, startPoint.y).setMovePattern(path).setSpecialMove(SpecialMoveRepo.getMoveById(SpecialMoveRepo.BREAK_GLASS)).setDefaultHealth(health).build();
        //       new GameMob(id, startPoint.x, startPoint.y, GameConstant.DEFAULT_MOB_SIZE, GameConstant.DEFAULT_MOB_SIZE, path, SpecialMoveRepo.getMoveById(SpecialMoveRepo.BREAK_GLASS), TouchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE), mobsptsheetId, health, 1);

    }

    private static GameMob getGhostMob(String id, Context context, Point startPoint, Point destPoint, int tickToDest, int health) {

        PointF[] path = PathRepo.generateLineToDest(new PointF(startPoint), new PointF(destPoint), tickToDest);
        String mobsptsheetId = "ghostmob";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheetghost), mobsptsheetId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        return new GameMob.MobBuilder(id, mobsptsheetId, startPoint.x, startPoint.y).setMovePattern(path).setSpecialMove(SpecialMoveRepo.getMoveById(SpecialMoveRepo.GHOST_MOVE)).setDefaultHealth(health).build();
        //     GameMob(id, startPoint.x, startPoint.y, GameConstant.DEFAULT_MOB_SIZE, GameConstant.DEFAULT_MOB_SIZE, path, SpecialMoveRepo.getMoveById(SpecialMoveRepo.GHOST_MOVE), TouchedMoveRepo.getMoveById(TouchedMoveRepo.DEFAULT_MOVE), mobsptsheetId, health, 1);

    }


}
