package fr.giusti.onetapengine.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.HashMap;


import fr.giusti.onetapengine.R;
import fr.giusti.onetapengine.commons.Constants;

import fr.giusti.onetapengine.entity.Particule;


/**
 * Created by giusti on 25/03/2015.
 */
public class ParticuleRepo {

    public final static String GLASS_PARTICULE = "glassParticule";
    public final static String TP_PARTICULE = "TpParticle";
    public final static String SWAP_PARTICULE = "swapParticule";
    public final static String BLOOD_PARTICULE = "bloodParticule";
    public final static String EXPLOSION_PARTICULE = "explosionParticule";
    public final static String SMOKE_PARTICULE = "smokeParticule";
    public final static String NUMBER1_PARTICULE = "number1Particule";
    public final static String NUMBER2_PARTICULE = "number2Particule";
    public final static String NUMBER3_PARTICULE = "number3Particule";

    public final static String SPARK0_PARTICULE = "spark0Particule";
    public final static String SPARK1_PARTICULE = "spark1Particule";
    public final static String SPARK2_PARTICULE = "spark2Particule";
    public final static String SPARK3_PARTICULE = "spark3Particule";
    public final static String SPARK4_PARTICULE = "spark4Particule";

    public final static String PLASMA_SPARK0_PARTICULE = "plasmaSpark0Particule";
    public final static String PLASMA_SPARK1_PARTICULE = "plasmaSpark1Particule";
    public final static String PLASMA_SPARK2_PARTICULE = "plasmaSpark2Particule";
    public final static String PLASMA_SPARK3_PARTICULE = "plasmaSpark3Particule";
    public final static String PLASMA_SPARK4_PARTICULE = "plasmaSpark4Particule";

    public final static String TP_SPARK0_PARTICULE = "tpSpark0Particule";
    public final static String TP_SPARK1_PARTICULE = "tpSpark1Particule";
    public final static String TP_SPARK2_PARTICULE = "tpSpark2Particule";
    public final static String TP_SPARK3_PARTICULE = "tpSpark3Particule";
    public final static String TP_SPARK4_PARTICULE = "tpSpark4Particule";

    public final static String GROUPE_SPARK_PARTICULE = "groupspark";
    public final static String GROUPE_PLASMA_SPARK_PARTICULE = "groupPlasmaSpark";
    public final static String GROUPE_TP_SPARK_PARTICULE = "groupTpSpark";


    private static HashMap<String, Particule> mTemplateParticuleList = new HashMap<String, Particule>();
    public static HashMap<String, ArrayList<String>> mParticuleGroupIdList = new HashMap<>();

    /**
     * populate the cache with the default particules
     *
     * @param context
     */
    public static void initCache(Context context) {

        if (mTemplateParticuleList.isEmpty()) {
            mTemplateParticuleList.put(GLASS_PARTICULE, initParticule(context, R.drawable.broken_glass_particle, GLASS_PARTICULE));
            mTemplateParticuleList.put(TP_PARTICULE, initParticule(context, R.drawable.tp_particle, TP_PARTICULE));
            mTemplateParticuleList.put(SWAP_PARTICULE, initParticule(context, R.drawable.swap_particule, SWAP_PARTICULE));
            mTemplateParticuleList.put(BLOOD_PARTICULE, initParticule(context, R.drawable.blood_particule, BLOOD_PARTICULE));
            mTemplateParticuleList.put(EXPLOSION_PARTICULE, initParticule(context, R.drawable.explosion_particule, EXPLOSION_PARTICULE));
            mTemplateParticuleList.put(SMOKE_PARTICULE, initParticule(context, R.drawable.smoke_particule, SMOKE_PARTICULE));
            mTemplateParticuleList.put(NUMBER1_PARTICULE, initParticule(context, R.drawable.particule_1, NUMBER1_PARTICULE));
            mTemplateParticuleList.put(NUMBER2_PARTICULE, initParticule(context, R.drawable.particule_2, NUMBER2_PARTICULE));
            mTemplateParticuleList.put(NUMBER3_PARTICULE, initParticule(context, R.drawable.particule_3, NUMBER3_PARTICULE));
            mTemplateParticuleList.put(SPARK0_PARTICULE, initParticule(context, R.drawable.spark_particule, SPARK0_PARTICULE));
            mTemplateParticuleList.put(SPARK1_PARTICULE, initParticule(context, R.drawable.spark_particule1, SPARK1_PARTICULE));
            mTemplateParticuleList.put(SPARK2_PARTICULE, initParticule(context, R.drawable.spark_particule2, SPARK2_PARTICULE));
            mTemplateParticuleList.put(SPARK3_PARTICULE, initParticule(context, R.drawable.spark_particule3, SPARK3_PARTICULE));
            mTemplateParticuleList.put(SPARK4_PARTICULE, initParticule(context, R.drawable.spark_particule4, SPARK4_PARTICULE));
            mTemplateParticuleList.put(PLASMA_SPARK0_PARTICULE, initParticule(context, R.drawable.plasma_spark_particule, PLASMA_SPARK0_PARTICULE));
            mTemplateParticuleList.put(PLASMA_SPARK1_PARTICULE, initParticule(context, R.drawable.plasma_spark_particule1, PLASMA_SPARK1_PARTICULE));
            mTemplateParticuleList.put(PLASMA_SPARK2_PARTICULE, initParticule(context, R.drawable.plasma_spark_particule2, PLASMA_SPARK2_PARTICULE));
            mTemplateParticuleList.put(PLASMA_SPARK3_PARTICULE, initParticule(context, R.drawable.plasma_spark_particule3, PLASMA_SPARK3_PARTICULE));
            mTemplateParticuleList.put(PLASMA_SPARK4_PARTICULE, initParticule(context, R.drawable.plasma_spark_particule4, PLASMA_SPARK4_PARTICULE));
            mTemplateParticuleList.put(TP_SPARK0_PARTICULE, initParticule(context, R.drawable.tp_spark_particule, TP_SPARK0_PARTICULE));
            mTemplateParticuleList.put(TP_SPARK1_PARTICULE, initParticule(context, R.drawable.tp_spark_particule1, TP_SPARK1_PARTICULE));
            mTemplateParticuleList.put(TP_SPARK2_PARTICULE, initParticule(context, R.drawable.tp_spark_particule2, TP_SPARK2_PARTICULE));
            mTemplateParticuleList.put(TP_SPARK3_PARTICULE, initParticule(context, R.drawable.tp_spark_particule3, TP_SPARK3_PARTICULE));
            mTemplateParticuleList.put(TP_SPARK4_PARTICULE, initParticule(context, R.drawable.tp_spark_particule4, TP_SPARK4_PARTICULE));
        }

        ArrayList<String> sparkPartIdList = new ArrayList<>();
        sparkPartIdList.add(SPARK0_PARTICULE);
        sparkPartIdList.add(SPARK1_PARTICULE);
        sparkPartIdList.add(SPARK2_PARTICULE);
        sparkPartIdList.add(SPARK3_PARTICULE);
        sparkPartIdList.add(SPARK4_PARTICULE);

        mParticuleGroupIdList.put(GROUPE_SPARK_PARTICULE, sparkPartIdList);

        ArrayList<String> plasmaSparkPartIdList = new ArrayList<>();
        plasmaSparkPartIdList.add(PLASMA_SPARK0_PARTICULE);
        plasmaSparkPartIdList.add(PLASMA_SPARK1_PARTICULE);
        plasmaSparkPartIdList.add(PLASMA_SPARK2_PARTICULE);
        plasmaSparkPartIdList.add(PLASMA_SPARK3_PARTICULE);
        plasmaSparkPartIdList.add(PLASMA_SPARK4_PARTICULE);

        mParticuleGroupIdList.put( GROUPE_PLASMA_SPARK_PARTICULE, plasmaSparkPartIdList);

        ArrayList<String> tpSparkPartIdList = new ArrayList<>();
        tpSparkPartIdList.add(TP_SPARK0_PARTICULE);
        tpSparkPartIdList.add(TP_SPARK1_PARTICULE);
        tpSparkPartIdList.add(TP_SPARK2_PARTICULE);
        tpSparkPartIdList.add(TP_SPARK3_PARTICULE);
        tpSparkPartIdList.add(TP_SPARK4_PARTICULE);

        mParticuleGroupIdList.put( GROUPE_TP_SPARK_PARTICULE, tpSparkPartIdList);

    }

    /**
     * create a particule from the given ressource(used as spriteSheet) with the given id
     *
     * @param ressourceId
     * @param id
     * @return
     */
    private static Particule initParticule(Context context, int ressourceId, String id) {

        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), ressourceId);

        int particuleWidth = bm.getWidth() / Constants.PARTICULE_NB_FRAME_ON_ANIMATION;
        int particuleHeight = bm.getHeight();

        SpriteRepo.addSpritesheetIfDoesntExist( bm,id, Constants.PARTICULE_NB_FRAME_ON_ANIMATION, 1);

        return new Particule(id, 0, 0, particuleWidth, particuleHeight, new PointF[]{new PointF(0, 0)}, id, false);
    }

    public static Particule getTemplateParticule(String id) {
        return mTemplateParticuleList.get(id);
    }

}
