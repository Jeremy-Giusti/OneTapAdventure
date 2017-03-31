package fr.giusti.onetapadventure.repository.spritesheet;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.entity.GameMob;

/**
 * Created by jgiusti on 22/03/2017.
 */

public class AttributsToSpriteMapper {

    private final static String SPRITE_BASE_ASSET_PATH = "sprites/";

    private final static String SPRITE_WINGS_ASSET_PATH = "wings/";
    private final static String SPRITE_BODY_COLOR_ASSET_PATH = "bodycolor/";
    private final static String SPRITE_BODY_OVERLAY_ASSET_PATH = "bodyoverlay1/";
    private final static String SPRITE_BODY_OVERLAY_2_ASSET_PATH = "bodyoverlay2/";
    private final static String SPRITE_BODY_OVERLAY_3_ASSET_PATH = "bodyoverlay3/";

    private final static String WINGS_LINE = "line/";
    private final static String WINGS_CURVE = "curve/";
    private final static String WINGS_CIRCLE = "circle/";
    private final static String WINGS_LOOP = "loop/";
    private final static String WINGS_RANDOM = "random/";
    private final static String WINGS_SPIRAL = "spiral/";

    private final static String BODY1_BREAKGLASS = "breakglass/";
    private final static String BODY1_EXPLODE = "explode/";
    private final static String BODY1_GHOST = "ghost/";
    private final static String BODY1_HEAL = "heal/";
    private final static String BODY1_MULTIPLIE = "multiplie/";
    private final static String BODY1_SMOKE = "smoke/";
    private final static String BODY1_SWAPE = "swape/";
    private final static String BODY1_TP = "teleport/";

    private final static String BODY2_TP = "teleport/";
    private final static String BODY2_BAIT = "bait/";
    private final static String BODY2_BLEED = "bleed/";
    private final static String BODY2_DISAPEAR = "disapear/";
    private final static String BODY2_HEAL = "heal/";
    private final static String BODY2_HURT = "hurt/";


    private HashMap<Integer, String> healthMap = getHealthMap();

    private HashMap<Integer, String> getHealthMap() {
        HashMap<Integer, String> healthMap = new HashMap<>();
        healthMap.put(2, BODY3_health2);
        healthMap.put(4, BODY3_health4);
        healthMap.put(6, BODY3_health6);
        healthMap.put(8, BODY3_health8);
        healthMap.put(10, BODY3_health10);
        return healthMap;
    }

    private final static String BODY3_health2 = "health2/";
    private final static String BODY3_health4 = "health4/";
    private final static String BODY3_health6 = "health6/";
    private final static String BODY3_health8 = "health8/";
    private final static String BODY3_health10 = "health10/";


    private AttributsToSpriteMapper() {

    }

    private static AttributsToSpriteMapper instance;

    public static AttributsToSpriteMapper getInstance() {
        if (instance == null) {
            instance = new AttributsToSpriteMapper();
        }
        return instance;
    }

    /**
     * @param context
     * @param mob
     * @param mobMovementhType
     * @return
     * @throws IOException
     */
    public String[][][] getMobMap(Context context, GameMob mob, String mobMovementhType) throws IOException {
        ArrayList<GameMob.eMobState> mobAnimatedState = GameMob.eMobState.getAnimatedState();
        String[][][] result = new String[Constants.NB_FRAME_ON_ANIMATION][mobAnimatedState.size()][5];
        String[][] animationLayers = new String[5][];//animation list (x) ordered by layer (z) so string[z][x]
        for (int y = 0; y < mobAnimatedState.size(); y++) {
            GameMob.eMobState mobState = mobAnimatedState.get(y);
            if (mobState != GameMob.eMobState.MOVING_UP) {
                animationLayers[0] = getMovementSpritesAssetPath(context, mobState, mobMovementhType);
                animationLayers[1] = getAlignementSpritesAsRessource(context, mobState, mob.getAlignement());
                animationLayers[2] = getSpecialSpritesAsRessource(context, mobState, mob.getmSpecialMove1().getId());
                animationLayers[3] = getTouchSpritesAsRessource(context, mobState, mob.getmTouchedMove().getId());
                animationLayers[4] = getHealthSpritesAsRessource(context, mobState, mob.getHealth());
            } else {
                animationLayers[0] = getAlignementSpritesAsRessource(context, mobState, mob.getAlignement());
                animationLayers[1] = getSpecialSpritesAsRessource(context, mobState, mob.getmSpecialMove1().getId());
                animationLayers[2] = getTouchSpritesAsRessource(context, mobState, mob.getmTouchedMove().getId());
                animationLayers[3] = getHealthSpritesAsRessource(context, mobState, mob.getHealth());
                animationLayers[4] = getMovementSpritesAssetPath(context, mobState, mobMovementhType);
            }

            for (int z = 0; z < animationLayers.length; z++) {
                for (int x = 0; x < Constants.NB_FRAME_ON_ANIMATION; x++) {
                    result[x][y][z] = animationLayers[z][x];
                }
            }
        }
        return result;
    }


    public String[] getMovementSpritesAssetPath(Context context, GameMob.eMobState state, String movementType) throws IOException {

        String assetCategoryFolder = SPRITE_BASE_ASSET_PATH + SPRITE_WINGS_ASSET_PATH;
        return getAssetsStrings(context, state, movementType, assetCategoryFolder);
    }

    public String[] getAlignementSpritesAsRessource(Context context, GameMob.eMobState state, Integer alignement) throws IOException {
        String[] selectedSpriteAssets = new String[Constants.NB_FRAME_ON_ANIMATION];
        String assetCategoryFolder = SPRITE_BASE_ASSET_PATH + SPRITE_BODY_COLOR_ASSET_PATH;

        List<String> color = Arrays.asList(context.getAssets().list(assetCategoryFolder));
        for (int frame = 0; frame < Constants.NB_FRAME_ON_ANIMATION; frame++) {
            selectedSpriteAssets[frame] = assetCategoryFolder + "/" + alignement; //yep always the same
        }
        return selectedSpriteAssets;
    }

    public String[] getSpecialSpritesAsRessource(Context context, GameMob.eMobState state, String specialType) throws IOException {
        String assetCategoryFolder = SPRITE_BASE_ASSET_PATH + SPRITE_BODY_OVERLAY_ASSET_PATH;
        return getAssetsStrings(context, state, specialType, assetCategoryFolder);
    }

    public String[] getTouchSpritesAsRessource(Context context, GameMob.eMobState state, String touchType) throws IOException {
        String assetCategoryFolder = SPRITE_BASE_ASSET_PATH + SPRITE_BODY_OVERLAY_2_ASSET_PATH;
        return getAssetsStrings(context, state, touchType, assetCategoryFolder);
    }


    public String[] getHealthSpritesAsRessource(Context context, GameMob.eMobState state, Integer health) throws IOException {

        String assetCategoryFolder = SPRITE_BASE_ASSET_PATH + SPRITE_BODY_OVERLAY_3_ASSET_PATH;
        if (healthMap.containsKey(health)) {
            return getAssetsStrings(context, state, healthMap.get(health), assetCategoryFolder);
        } else {
            for (Integer category : healthMap.keySet()) {
                if (health < category) {
                    return getAssetsStrings(context, state, healthMap.get(category), assetCategoryFolder);
                }
            }
        }
        return new String[0];
    }

    /**
     * @param context
     * @param state               mob state concerned by the requested assets sprites
     * @param type                assets type => for wings type are: line/curve/loop etc...
     * @param assetCategoryFolder asset category folder "/sprite/wings" for exemple
     * @return
     * @throws IOException
     */
    @NonNull
    private String[] getAssetsStrings(Context context, GameMob.eMobState state, String type, String assetCategoryFolder) throws IOException {
        String assetRepo = assetCategoryFolder + "/" + type;

        List<String> assetList = Arrays.asList(context.getAssets().list(assetRepo));


        String[] selectedSpriteAssets = new String[Constants.NB_FRAME_ON_ANIMATION];
        String fileName;
        for (int frame = 0; frame < Constants.NB_FRAME_ON_ANIMATION; frame++) {
            fileName = "" + state.index + frame; //for the sake of folder readability x and y is inversed in filename (so 01 is the file with frame number 1 and state index 0)
            if (assetList.contains(fileName)) {
                selectedSpriteAssets[frame] = assetRepo + "/" + fileName;
            } else if (assetList.contains("" + 0 + frame)) {
                selectedSpriteAssets[frame] = assetRepo + "/" + "" + 0 + frame;//default (no mob state consideration)
            } else {
                selectedSpriteAssets[frame] = findDefaultAssetSprit(context, state, frame, assetCategoryFolder);//default (no type/state consideration)
            }
        }
        return selectedSpriteAssets;
    }

    private String findDefaultAssetSprit(Context context, GameMob.eMobState state, int frame, String assetCategoryFolder) throws IOException {
        List<String> defaultAssetList = Arrays.asList(context.getAssets().list(assetCategoryFolder));
        if (defaultAssetList.contains("" + state.index + frame)) {
            return assetCategoryFolder + "/" + state.index + frame; //default
        } else if (defaultAssetList.contains("0" + frame)) {
            return assetCategoryFolder + "0" + frame; //default no state
        } else if (defaultAssetList.contains("" + state.index + 0)) {
            return assetCategoryFolder + "/" + state + 0; //default no frame
        }
        return assetCategoryFolder + "00";//no frame no state
    }
}
