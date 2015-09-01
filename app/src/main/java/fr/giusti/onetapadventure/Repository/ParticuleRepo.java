package fr.giusti.onetapadventure.Repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.RectF;

import java.util.HashMap;

import fr.giusti.onetapadventure.GameObject.Particule;
import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.commons.Constants;

/**
 * Created by giusti on 25/03/2015.
 */
public class ParticuleRepo {

    public final static String TP_PARTICULE = "TpParticle";
    public final static String SWAP_PARTICULE = "swapParticule";
    public final static String BLOOD_PARTICULE = "bloodParticule";
    public final static String EXPLOSION_PARTICULE = "explosionParticule";
    public final static String SMOKE_PARTICULE = "smokeParticule";
    public final static String NUMBER1_PARTICULE = "number1Particule";
    public final static String NUMBER2_PARTICULE = "number2Particule";
    public final static String NUMBER3_PARTICULE = "number3Particule";


    //Must be created with the board
    private static HashMap<String, Particule> mParticuleList = new HashMap<String, Particule>();


    private Context mContext;
    private Double mRatio = 1.0;

    /**
     * populate the cache with the default particules
     *
     * @param context
     * @param ratio
     */
    public void initCache(Context context, double ratio) {
        this.mRatio = ratio;
        this.mContext = context;

        if (mParticuleList.isEmpty()) {
            mParticuleList.put(TP_PARTICULE, initParticule(R.drawable.tp_particle, TP_PARTICULE));
            mParticuleList.put(SWAP_PARTICULE, initParticule(R.drawable.swap_particule, SWAP_PARTICULE));
            mParticuleList.put(BLOOD_PARTICULE, initParticule(R.drawable.blood_particule, BLOOD_PARTICULE));
            mParticuleList.put(EXPLOSION_PARTICULE, initParticule(R.drawable.explosion_particule, EXPLOSION_PARTICULE));
            mParticuleList.put(SMOKE_PARTICULE, initParticule(R.drawable.smoke_particule, SMOKE_PARTICULE));

            mParticuleList.put(NUMBER1_PARTICULE, initParticule(R.drawable.particule_1, NUMBER1_PARTICULE));
            mParticuleList.put(NUMBER2_PARTICULE, initParticule(R.drawable.particule_2, NUMBER2_PARTICULE));
            mParticuleList.put(NUMBER3_PARTICULE, initParticule(R.drawable.particule_3, NUMBER3_PARTICULE));

        }
    }

    /**
     * create a particule from the given ressource(used as spriteSheet) with the given id
     *
     * @param ressourceId
     * @param id
     * @return
     */
    private Particule initParticule(int ressourceId, String id) {

        Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(), ressourceId);

        int particuleWidth = bm.getWidth() / Constants.PARTICULE_NB_FRAME_ON_ANIMATION;
        int particuleHeight = bm.getHeight();

        new SpriteRepo().addIfDoesntExist(id, bm, Constants.PARTICULE_NB_FRAME_ON_ANIMATION, 1);

        return new Particule(id, 0, 0, (int) (particuleWidth * mRatio), (int) (particuleHeight * mRatio), new Point[]{new Point(0, 0)}, id, false,false);
    }

    public Particule getParticuleById(String id) {
        return mParticuleList.get(id);
    }

    /**
     * generate a particule from one of the default particule
     *
     * @param id       id of the default particule
     * @param x        new position x
     * @param y        new position y
     * @param reversed display the reversed animation or not
     * @param path     the new path
     * @return null if not found
     */
    public Particule generateOrGetCustomParticule(String id, int x, int y, int width, int height, boolean reversed, Point[] path) {
        String customParticuleId = id+width+"x"+height+reversed;
        //TODO optimise
        //if(mParticuleList.containsKey(customParticuleId))
        if (mParticuleList.containsKey(id)) {
            Particule result = mParticuleList.get(id).clone();
            result.setAnimationReversed(reversed);
            if (path != null) {
                result.setMovePattern(path);
            }
            if (width > 0 && height > 0) {
                result.setmPosition(new RectF(x - width / 2, y - height / 2, x + width / 2, y + height / 2));
            } else {
                result.setmPositionFromXY((int) (x - result.getmPosition().width() / 2), (int) (y - result.getmPosition().height() / 2));
            }
         //   mParticuleList.put(customParticuleId,result);
            return result;
        } else {
            return null;
        }
    }
}
