package fr.giusti.onetapadventure.repository.entities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.HashMap;

import fr.giusti.onetapadventure.gameObject.entities.Particule;
import fr.giusti.onetapadventure.R;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.repository.SpriteRepo;

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
    private static HashMap<String, Particule> mTemplateParticuleList = new HashMap<String, Particule>();

    /**
     * populate the cache with the default particules
     *
     * @param context
     */
    public static void initCache(Context context) {

        if (mTemplateParticuleList.isEmpty()) {
            mTemplateParticuleList.put(TP_PARTICULE, initParticule(context,R.drawable.tp_particle, TP_PARTICULE));
            mTemplateParticuleList.put(SWAP_PARTICULE, initParticule(context,R.drawable.swap_particule, SWAP_PARTICULE));
            mTemplateParticuleList.put(BLOOD_PARTICULE, initParticule(context,R.drawable.blood_particule, BLOOD_PARTICULE));
            mTemplateParticuleList.put(EXPLOSION_PARTICULE, initParticule(context,R.drawable.explosion_particule, EXPLOSION_PARTICULE));
            mTemplateParticuleList.put(SMOKE_PARTICULE, initParticule(context,R.drawable.smoke_particule, SMOKE_PARTICULE));
            mTemplateParticuleList.put(NUMBER1_PARTICULE, initParticule(context,R.drawable.particule_1, NUMBER1_PARTICULE));
            mTemplateParticuleList.put(NUMBER2_PARTICULE, initParticule(context,R.drawable.particule_2, NUMBER2_PARTICULE));
            mTemplateParticuleList.put(NUMBER3_PARTICULE, initParticule(context,R.drawable.particule_3, NUMBER3_PARTICULE));
        }
    }

    /**
     * create a particule from the given ressource(used as spriteSheet) with the given id
     *
     * @param ressourceId
     * @param id
     * @return
     */
    private static Particule initParticule(Context context,int ressourceId, String id) {

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), ressourceId);

        int particuleWidth = bm.getWidth() / Constants.PARTICULE_NB_FRAME_ON_ANIMATION;
        int particuleHeight = bm.getHeight();

       SpriteRepo.addSpritesheetIfDoesntExist(id, bm, Constants.PARTICULE_NB_FRAME_ON_ANIMATION, 1);

        return new Particule(id, 0, 0, particuleWidth, particuleHeight, new PointF[]{new PointF(0, 0)}, id, false);
    }

    public static Particule getTemplateParticule(String id) {
        return mTemplateParticuleList.get(id);
    }

}
