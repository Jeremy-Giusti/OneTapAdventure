package fr.giusti.onetapengine.entity.distribution;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import fr.giusti.onetapengine.entity.Particule;
import fr.giusti.onetapengine.repository.ParticuleRepo;

/**
 * Created by jgiusti on 21/09/2016.
 */

public class ParticuleHolder {
    private static HashMap<String, ArrayList<Particule>> availableParticules = new HashMap<>();


    public static void recycle(Particule particule) {
        String partName = particule.getIdName();
        if (availableParticules.get(partName) == null) {
            availableParticules.put(partName, new ArrayList<Particule>());
        }
        particule.setAvailable(true);
        availableParticules.get(partName).add(particule);
    }


    /**
     * generate a particule from one of the default particule
     *
     * @param templateId id of the default particule
     * @param x          new position x
     * @param y          new position y
     * @param reversed   display the reversed animation or not
     * @param path       the new path
     * @return null if not found
     */
    public static Particule getAvailableParticule(String templateId, int x, int y, float width, float height, boolean reversed, PointF[] path) {
        String customParticuleId = templateId + width + "x" + height;
        Particule result = null;
        ArrayList<Particule> availablePool = availableParticules.get(customParticuleId);
        if (availablePool != null && !availablePool.isEmpty()) {
            result = availablePool.get(0);
            availablePool.remove(result);
            result.setAvailable(false);
        } else {
            result = ParticuleRepo.getTemplateParticule(templateId).clone();
            result.setAnimationReversed(reversed);
            if (path != null) {
                result.setMovePattern(path);
            }
            result.setmPosition(new RectF(x - width / 2, y - height / 2, x + width / 2, y + height / 2));
        }
        return result;
    }

    /**
     * should be avoided when possible
     *
     * @param groupId
     * @param spawnArea
     * @param generalDirection
     * @param particulNb
     * @return
     */
    public static ArrayList<Particule> getAvailableParticuleGroupe(String groupId, RectF spawnArea, PointF generalDirection, int particulNb) {
        ArrayList<Particule> result = new ArrayList<>();
        ArrayList<String> particuleIdList = ParticuleRepo.mParticuleGroupIdList.get(groupId);

        ArrayList<Particule> availablePool;
        Particule particule = null;
        Random random = new Random();


        for (int i = 0; i < particulNb; i++) {
            String particuleId = particuleIdList.get((int) (Math.random() * particuleIdList.size()));
            availablePool = availableParticules.get(particuleId);
            if (availablePool != null && !availablePool.isEmpty()) {
                particule = availablePool.get(0);
                availablePool.remove(particule);
                particule.setAvailable(false);
            } else {
                particule = ParticuleRepo.getTemplateParticule(particuleId).clone();
            }
            float x = random.nextInt((int)(spawnArea.right - spawnArea.left)) + spawnArea.left;
            float y = random.nextInt((int)(spawnArea.bottom - spawnArea.top)) + spawnArea.top;
            particule.setmPositionFromXY(x, y);
            int xMovement = ((x % 2 == 1) ? 1 : -1) * (random.nextInt(3) + 1);
            int yMovement = ((y % 2 == 1) ? 1 : -1) * (random.nextInt(3) + 1);
            particule.setPath(new PointF(xMovement + generalDirection.x, yMovement + generalDirection.y));
            result.add(particule);
        }

        return result;
    }

}
