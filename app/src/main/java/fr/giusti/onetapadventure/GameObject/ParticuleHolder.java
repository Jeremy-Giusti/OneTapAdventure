package fr.giusti.onetapadventure.gameObject;

import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.HashMap;

import fr.giusti.onetapadventure.gameObject.entities.Particule;
import fr.giusti.onetapadventure.repository.entities.ParticuleRepo;

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
    public static Particule getAvailableParticule(String templateId, int x, int y, int width, int height, boolean reversed, PointF[] path) {
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
            result.setMovePattern(path);
            result.setmPosition(new RectF(x - width / 2, y - height / 2, x + width / 2, y + height / 2));
        }
        return result;
    }

}
