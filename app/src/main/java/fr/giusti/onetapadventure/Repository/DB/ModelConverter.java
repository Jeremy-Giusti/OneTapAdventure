package fr.giusti.onetapadventure.repository.DB;

import android.graphics.PointF;

import java.util.ArrayList;

import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;
import fr.giusti.onetapadventure.repository.DB.model.BoardDB;
import fr.giusti.onetapadventure.repository.DB.model.MobDB;
import fr.giusti.onetapadventure.repository.DB.model.PathDB;
import fr.giusti.onetapadventure.repository.SpecialMoveRepo;
import fr.giusti.onetapadventure.repository.TouchedMoveRepo;

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
        if (mob.getmSpecialMove1() != null) {
            mobDb.setSpecialMoveId(mob.getmSpecialMove1().getId());
        }
        if (mob.getmTouchedMove() != null) {
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


    public static PointF[] pathDBtoPath(PathDB pathDb) {
        PathDB.PointDB[] pointListDB = pathDb.getPath();
        PointF[] path = new PointF[pointListDB.length];

        for (int i = 0; i < pointListDB.length; i++) {
            path[i] = new PointF(pointListDB[i].x, pointListDB[i].y);
        }

        return path;
    }

    public static PathDB pathToPathDB(PointF[] path, String pathDbId, String mobId) {
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
        return new GameBoard(new ArrayList<GameMob>(), boardDB.getBackgroundUrl(), boardDB.getWidth(), boardDB.getHeight(), boardDB.getCamRect());
    }

    public static BoardDB boardDbToBoard(GameBoard board, String boardId) {
        return new BoardDB(boardId, board.getBackgroundBitmapId(), board.getHeight(), board.getWidth(), board.getmCameraBounds());
    }

}
