package fr.giusti.onetapadventure.repository;

import java.util.HashMap;
import java.util.Map;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapengine.repository.SpecialMoveRepo;

/**
 * Created by jgiusti on 22/03/2017.
 */

public class AttributsToSpriteMapper {

    // ------------------------------------------------- Movement Sprite / Wings -------------------------------------------------------//

    private static final Map<String, Integer[]> movementSpriteList = createMovementSpriteList();

    private static Map<String, Integer[]> createMovementSpriteList() {
        Map<String, Integer[]> myMap = new HashMap<String, Integer[]>();
        myMap.put("line", {R.drawable.wing_line1, R.drawable.wing_line2, R.drawable.wing_line3});
        myMap.put("curve", {R.drawable.wing_curve1, R.drawable.wing_curve2, R.drawable.wing_curve3});
        myMap.put("loop", {R.drawable.wing_loop1, R.drawable.wing_loop2, R.drawable.wing_loop3});
        myMap.put("circle", {R.drawable.wing_circle1, R.drawable.wing_circle2, R.drawable.wing_circle3});
        myMap.put("spiral", {R.drawable.wing_spiral1, R.drawable.wing_spiral2, R.drawable.wing_spiral3});
        myMap.put("random", {R.drawable.wing_random1, R.drawable.wing_random2, R.drawable.wing_random3});
        return myMap;
    }

    public static Integer[] getMovementSpritesAsRessource(String movementType) {
        return movementSpriteList.get(movementType);
    }

    // ------------------------------------------------- Alignement Sprite / Body  color -------------------------------------------------------//

    private static final Map<Integer, Integer[]> alignementSpriteList = createAlignementSpriteList();

    private static Map<Integer, Integer[]> createAlignementSpriteList() {
        Map<Integer, Integer[]> myMap = new HashMap<Integer, Integer[]>();
        myMap.put(1, {R.drawable.body_1color1, R.drawable.body_1color2, R.drawable.body_1color3});
        myMap.put(2, {R.drawable.body_2color1, R.drawable.body_2color2, R.drawable.body_2color3});
        myMap.put(3, {R.drawable.body_3color1, R.drawable.body_3color2, R.drawable.body_3color3});
        myMap.put(4, {R.drawable.body_4color1, R.drawable.body_4color2, R.drawable.body_4color3});
        myMap.put(5, {R.drawable.body_5color1, R.drawable.body_5color2, R.drawable.body_5color3});
        myMap.put(6, {R.drawable.body_6color1, R.drawable.body_6color2, R.drawable.body_6color3});
        myMap.put(7, {R.drawable.body_7color1, R.drawable.body_7color2, R.drawable.body_7color3});
        myMap.put(8, {R.drawable.body_8color1, R.drawable.body_8color2, R.drawable.body_8color3});
        return myMap;
    }

    public static Integer[] getAlignementSpritesAsRessource(Integer alignement) {
        return specialSpriteList.containsKey(alignement) ? specialSpriteList.get(alignement) : {R.drawable.body_0color1, R.drawable.body_0color2, R.drawable.body_0color3};

    }

    // ------------------------------------------------- Special Sprite / Body2 overlay -------------------------------------------------------//

    private static final Map<String, Integer[]> specialSpriteList = createSpecialSpriteList();

    private static Map<String, Integer[]> createSpecialSpriteList() {
        Map<String, Integer[]> myMap = new HashMap<String, Integer[]>();
        myMap.put(SpecialMoveRepo.AUTO_HEAL, {R.drawable.body2_heal1, R.drawable.body2_heal2, R.drawable.body2_heal3});
        myMap.put(SpecialMoveRepo.AUTO_HURT_EXPLODING, {R.drawable.body2_explode1, R.drawable.body2_explode2, R.drawable.body2_explode3});
        myMap.put(SpecialMoveRepo.BREAK_GLASS, {R.drawable.body2_breakglass1, R.drawable.body2_breakglass2, R.drawable.body2_breakglass3});
        myMap.put(SpecialMoveRepo.GHOST_MOVE, {R.drawable.body2_ghost1, R.drawable.body2_ghost2, R.drawable.body2_ghost3});
        myMap.put(SpecialMoveRepo.MULTIPLIE, {R.drawable.body2_multiplie1, R.drawable.body2_multiplie2, R.drawable.body2_multiplie3});
        myMap.put(SpecialMoveRepo.SMOKE_TRAIL, {R.drawable.body2_smoke1, R.drawable.body2_smoke2, R.drawable.body2_smoke3});
        myMap.put(SpecialMoveRepo.SWAP, {R.drawable.body2_swape1, R.drawable.body2_swape2, R.drawable.body2_swape3});
        myMap.put(SpecialMoveRepo.TELEPORT, {R.drawable.body2_tp1, R.drawable.body2_tp2, R.drawable.body2_tp3});
        myMap.put(SpecialMoveRepo.NO_MOVE, new Integer[0]);
        return myMap;
    }

    public static Integer[] getSpecialSpritesAsRessource(String specialType) {
        return specialSpriteList.get(specialType);
    }


    // ------------------------------------------------- touch Sprite / Body2 overlay -------------------------------------------------------//

    private static final Map<String, Integer[]> specialSpriteList = createSpecialSpriteList();

    private static Map<String, Integer[]> createSpecialSpriteList() {
        Map<String, Integer[]> myMap = new HashMap<String, Integer[]>();
        myMap.put(SpecialMoveRepo.AUTO_HEAL, {R.drawable.body2_heal1, R.drawable.body2_heal2, R.drawable.body2_heal3});
        myMap.put(SpecialMoveRepo.AUTO_HURT_EXPLODING, {R.drawable.body2_explode1, R.drawable.body2_explode2, R.drawable.body2_explode3});
        myMap.put(SpecialMoveRepo.BREAK_GLASS, {R.drawable.body2_breakglass1, R.drawable.body2_breakglass2, R.drawable.body2_breakglass3});
        myMap.put(SpecialMoveRepo.GHOST_MOVE, {R.drawable.body2_ghost1, R.drawable.body2_ghost2, R.drawable.body2_ghost3});
        myMap.put(SpecialMoveRepo.MULTIPLIE, {R.drawable.body2_multiplie1, R.drawable.body2_multiplie2, R.drawable.body2_multiplie3});
        myMap.put(SpecialMoveRepo.SMOKE_TRAIL, {R.drawable.body2_smoke1, R.drawable.body2_smoke2, R.drawable.body2_smoke3});
        myMap.put(SpecialMoveRepo.SWAP, {R.drawable.body2_swape1, R.drawable.body2_swape2, R.drawable.body2_swape3});
        myMap.put(SpecialMoveRepo.TELEPORT, {R.drawable.body2_tp1, R.drawable.body2_tp2, R.drawable.body2_tp3});
        myMap.put(SpecialMoveRepo.NO_MOVE, new Integer[0]);
        return myMap;
    }

    public static Integer[] getSpecialSpritesAsRessource(String specialType) {
        return specialSpriteList.get(specialType);
    }


}
