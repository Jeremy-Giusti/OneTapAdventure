package fr.giusti.onetapadventure.Repository.DB;

import android.graphics.Point;

import fr.giusti.onetapadventure.GameObject.GameBoard;
import fr.giusti.onetapadventure.GameObject.GameMob;
import fr.giusti.onetapadventure.Repository.DB.model.BoardDB;
import fr.giusti.onetapadventure.Repository.DB.model.MobDB;
import fr.giusti.onetapadventure.Repository.DB.model.PathDB;
import fr.giusti.onetapadventure.Repository.SpecialMoveRepo;
import fr.giusti.onetapadventure.Repository.TouchedMoveRepo;

/**
 * Created by giusti on 17/03/2015.
 */
public class ModelConverter {

    //------------------------Mob Converter------------------------
    ///////////////////////////////////////////////////////////////

    public static MobDB mobToMobDB(GameMob mob, String boardId) {
        MobDB mobDb = new MobDB();

        mobDb.setId(mob.getIdName());
        mobDb.setBoardId(boardId);
        if(mob.getmSpecialMove1()!=null) {
            mobDb.setSpecialMoveId(mob.getmSpecialMove1().getId());
        }
        if(mob.getmTouchedMove()!=null) {
            mobDb.setTouchedMoveId(mob.getmTouchedMove().getId());
        }
        mobDb.setHealth(mob.getHealth());

        int x = (int) mob.getPosition().left;
        int y = (int) mob.getPosition().top;
        int width = (int) mob.getPosition().width();
        int height = (int) mob.getPosition().height();

        mobDb.setPosX(x);
        mobDb.setPosY(y);
        mobDb.setWidth(width);
        mobDb.setHeight(height);
        mobDb.setSpriteSheetUrl(mob.getBitmapId());
        return mobDb;
    }

    public static GameMob mobDBToMob(MobDB mobdb) {

        return new GameMob(
                mobdb.getId(),
                mobdb.getPosX(),
                mobdb.getPosY(),
                mobdb.getWidth(),
                mobdb.getHeight(),
                null,
                new SpecialMoveRepo().getMoveById(mobdb.getSpecialMoveId()),
                new TouchedMoveRepo().getMoveById(mobdb.getTouchedMoveId()),
                mobdb.getSpriteSheetUrl(),
                mobdb.getHealth(),
                0);
    }

    //--------------------------Path converter------------------------
    //////////////////////////////////////////////////////////////////


    public static Point[] pathDBtoPath(PathDB pathDb) {
        PathDB.PointDB[] pointListDB = pathDb.getPath();
        Point[] path = new Point[pointListDB.length];

        for (int i = 0; i < pointListDB.length; i++) {
            path[i] = new Point(pointListDB[i].x, pointListDB[i].y);
        }

        return path;
    }

    public static PathDB pathToPathDB(Point[] path, String pathDbId, String mobId) {
        PathDB.PointDB[] pointListDb = new PathDB.PointDB[path.length];
        PathDB pathDb = new PathDB();

        pathDb.setMobId(mobId);
        pathDb.setId(pathDbId);

        for (int i = 0; i < path.length; i++) {
            pointListDb[i] = new PathDB.PointDB(path[i].x, path[i].y);
        }
        pathDb.setPath(pointListDb);
        return pathDb;
    }

    public static PathDB mobToPathDB(GameMob mob) {

        return pathToPathDB(mob.getMovePattern(), mob.getIdName(), mob.getIdName());

    }

    //-----------------------------Board converter-------------------------
    ///////////////////////////////////////////////////////////////////////

    public static GameBoard boardDbToBoard(BoardDB boardDB) {
        return new GameBoard(null, boardDB.getBackgroundUrl(), boardDB.getWidth(), boardDB.getHeight(),boardDB.getCamRect());
    }

    public static BoardDB boardDbToBoard(GameBoard board, String boardId) {
        return new BoardDB(boardId, board.getBackgroundBitmapId(), board.getHeight(), board.getWidth(),board.getmCameraBound());
    }

}
