package fr.giusti.onetapadventure.repository.spritesheet;

import android.content.Context;
import android.support.annotation.NonNull;

import com.sqli.spritesheetgenerator.model.SpriteSheetTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.entity.GameMob;

/**
 * Created by jgiusti on 22/03/2017.<br>
 * used to make correspondences between mob features and sprite parts
 */

public class AttributesToSpriteMapper {

    private static final String TAG = AttributesToSpriteMapper.class.getSimpleName();

    private final static int LAYER_NUMBER = 5;
    private final static String SPRITE_ASSET_FILE_SUFFIX = ".png";
    private final static String SPRITE_BASE_ASSET_PATH = "sprites/";

    private final static String SPRITE_WINGS_ASSET_PATH = "wings";
    private final static String SPRITE_BODY_COLOR_ASSET_PATH = "bodycolor";
    private final static String SPRITE_BODY_OVERLAY_ASSET_PATH = "bodyoverlay1";
    private final static String SPRITE_BODY_OVERLAY_2_ASSET_PATH = "bodyoverlay2";
    private final static String SPRITE_BODY_OVERLAY_3_ASSET_PATH = "bodyoverlay3";

    private HashMap<Integer, String> healthMap = getHealthMap();

    private HashMap<Integer, String> getHealthMap() {
        HashMap<Integer, String> healthMap = new HashMap<>();
        healthMap.put(2, BODY3_health2);
        healthMap.put(4, BODY3_health4);
        healthMap.put(6, BODY3_health6);
        healthMap.put(8, BODY3_health8);
        healthMap.put(HEALTH_MAP_DEFAULT_VALUE, BODY3_health10);
        return healthMap;
    }

    private static final Integer HEALTH_MAP_DEFAULT_VALUE = 10;

    private final static String BODY3_health2 = "health2/";
    private final static String BODY3_health4 = "health4/";
    private final static String BODY3_health6 = "health6/";
    private final static String BODY3_health8 = "health8/";
    private final static String BODY3_health10 = "health10/";


    private AttributesToSpriteMapper() {

    }

    private static AttributesToSpriteMapper instance;

    public static AttributesToSpriteMapper getInstance() {
        if (instance == null) {
            instance = new AttributesToSpriteMapper();
        }
        return instance;
    }

    /**
     * generate three-dimensional array of assets based on mob features
     *
     * @param context
     * @param mob              mob used to determine which sprite will be used
     * @param mobMovementhType since the movement type is not held by the mob we need to pass it
     * @return not yet loaded SpriteSheetTemplate (only contain sprite assets
     * @throws IOException
     */
    public SpriteSheetTemplate getMobMap(Context context, GameMob mob, String mobMovementhType) throws IOException {

        ArrayList<GameMob.eMobState> mobAnimatedState = GameMob.eMobState.getAnimatedState();
        SpriteSheetTemplate spriteSheetTemplate = new SpriteSheetTemplate(Constants.NB_FRAME_ON_ANIMATION, mobAnimatedState.size(), LAYER_NUMBER);

        String[][] animationLayers = new String[LAYER_NUMBER][];//animation list (x) ordered by layer (z) so string[z][x]
        for (int y = 0; y < mobAnimatedState.size(); y++) {
            GameMob.eMobState mobState = mobAnimatedState.get(y);

            if (mobState != GameMob.eMobState.MOVING_UP) {
//                Log.v(TAG, "Sprite mapping 1" + mob.getBitmapId());
                animationLayers[0] = getMovementSpritesAssetPath(context, mobState, mobMovementhType);
//                Log.v(TAG, "Sprite mapping 2" + mob.getBitmapId());
                animationLayers[1] = getAlignementSpritesAsRessource(context, mobState, mob.getmAlignement());
//                Log.v(TAG, "Sprite mapping 3" + mob.getBitmapId());
                animationLayers[2] = getSpecialSpritesAsRessource(context, mobState, mob.getmSpecialMove1().getId());
//                  Log.v(TAG, "Sprite mapping 4" + mob.getBitmapId());
                animationLayers[3] = getTouchSpritesAsRessource(context, mobState, mob.getmTouchedMove().getId());
//                 removed health sprite indication to put it back set LAYER_NUMBER++
//                  Log.v(TAG, "Sprite mapping 5" + mob.getBitmapId());
                animationLayers[4] = getHealthSpritesAsRessource(context, mobState, mob.getHealth());
//                   Log.v(TAG, "Sprite mapping end" + mob.getBitmapId());
            } else {
                animationLayers[0] = getAlignementSpritesAsRessource(context, mobState, mob.getmAlignement());
                animationLayers[1] = getSpecialSpritesAsRessource(context, mobState, mob.getmSpecialMove1().getId());
                animationLayers[2] = getTouchSpritesAsRessource(context, mobState, mob.getmTouchedMove().getId());
                animationLayers[3] = getHealthSpritesAsRessource(context, mobState, mob.getHealth());
                animationLayers[4] = getMovementSpritesAssetPath(context, mobState, mobMovementhType);
            }

            for (int z = 0; z < animationLayers.length; z++) {
                for (int x = 0; x < Constants.NB_FRAME_ON_ANIMATION; x++) {
                    spriteSheetTemplate.setSpriteAsset(x, y, z, animationLayers[z][x]);
                }
            }
        }
        return spriteSheetTemplate;
    }


    public String[] getMovementSpritesAssetPath(Context context, GameMob.eMobState state, String movementType) throws IOException {

        String assetCategoryFolder = SPRITE_BASE_ASSET_PATH + SPRITE_WINGS_ASSET_PATH;
        return getAssetsStrings(context, state, movementType, assetCategoryFolder);
    }

    public String[] getAlignementSpritesAsRessource(Context context, GameMob.eMobState state, Integer alignement) throws IOException {
        String[] selectedSpriteAssets = new String[Constants.NB_FRAME_ON_ANIMATION];
        String assetCategoryFolder = SPRITE_BASE_ASSET_PATH + SPRITE_BODY_COLOR_ASSET_PATH;

        if (GameMob.eMobState.HURT == state) {
            String alignementAssetPath = assetCategoryFolder + "/" + state.index + "0" + SPRITE_ASSET_FILE_SUFFIX;
            for (int frame = 0; frame < Constants.NB_FRAME_ON_ANIMATION; frame++) {
                selectedSpriteAssets[frame] = alignementAssetPath; //yep always the same
            }
        } else {
            selectedSpriteAssets = getAssetsStrings(context, state, "" + alignement, assetCategoryFolder);
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


    /**
     * dumbed down version of health sprite mapping (don't change depending of health qty)<br>
     * (see commented code for full feature implementation)
     *
     * @param context
     * @param state
     * @param health
     * @return
     * @throws IOException
     */
    public String[] getHealthSpritesAsRessource(Context context, GameMob.eMobState state, Integer health) throws IOException {

        String assetCategoryFolder = SPRITE_BASE_ASSET_PATH + SPRITE_BODY_OVERLAY_3_ASSET_PATH;
//        if (healthMap.containsKey(health)) {
//            return getAssetsStrings(context, state, healthMap.get(health), assetCategoryFolder);
//        } else {
//            for (Integer category : healthMap.keySet()) {
//                if (health < category) {
//                    return getAssetsStrings(context, state, healthMap.get(category), assetCategoryFolder);
//                }
//            }
//        }
// return getAssetsStrings(context, state, healthMap.get(HEALTH_MAP_DEFAULT_VALUE), assetCategoryFolder);
        if (state.index != 5) {
            String selectedAsset = assetCategoryFolder + "/" + state.index + "0.png";
            return new String[]{
                    selectedAsset, selectedAsset, selectedAsset
            };
        } else {
            return new String[]{
                    assetCategoryFolder + "/00.png", assetCategoryFolder + "/51.png", assetCategoryFolder + "/52.png"
            };
        }


    }

    /**
     * @param context
     * @param state               mob state concerned by the requested assets sprites
     * @param type                assets ruleResult => for wings ruleResult are: line/curve/loop etc...
     * @param assetCategoryFolder asset category folder "/sprite/wings" for exemple
     * @return
     * @throws IOException
     */
    @NonNull
    private String[] getAssetsStrings(Context context, GameMob.eMobState state, String type, String assetCategoryFolder) throws IOException {
        String assetFolder = assetCategoryFolder + "/" + type;

        List<String> assetList = Arrays.asList(context.getAssets().list(assetFolder));


        String[] selectedSpriteAssets = new String[Constants.NB_FRAME_ON_ANIMATION];
        String fileName;
        for (int frame = 0; frame < Constants.NB_FRAME_ON_ANIMATION; frame++) {
            fileName = "" + state.index + frame + SPRITE_ASSET_FILE_SUFFIX; //for the sake of folder readability x and y is inversed in filename (so 01 is the file with frame number 1 and state index 0)
            if (assetList.contains(fileName)) {
                selectedSpriteAssets[frame] = assetFolder + "/" + fileName;
            } else if (assetList.contains("" + 0 + frame + SPRITE_ASSET_FILE_SUFFIX)) {
                selectedSpriteAssets[frame] = assetFolder + "/" + "" + 0 + frame + SPRITE_ASSET_FILE_SUFFIX;//default (no mob state consideration)
            } else if (assetList.contains("" + state.index + 0 + SPRITE_ASSET_FILE_SUFFIX)) {
                selectedSpriteAssets[frame] = assetFolder + "/" + "" + state.index + 0 + SPRITE_ASSET_FILE_SUFFIX;//default (no mob frame consideration)
            } else if (assetList.contains("00" + SPRITE_ASSET_FILE_SUFFIX)) {
                selectedSpriteAssets[frame] = assetFolder + "/00" + SPRITE_ASSET_FILE_SUFFIX;//default (no mob state/frame consideration)
            } else {
                selectedSpriteAssets[frame] = findDefaultAssetSprit(context, state, frame, assetCategoryFolder);//default (no ruleResult/state consideration)
            }
        }
        return selectedSpriteAssets;
    }

    private String findDefaultAssetSprit(Context context, GameMob.eMobState state, int frame, String assetCategoryFolder) throws IOException {
        List<String> defaultAssetList = Arrays.asList(context.getAssets().list(assetCategoryFolder));
        if (defaultAssetList.contains("" + state.index + frame + SPRITE_ASSET_FILE_SUFFIX)) {
            return assetCategoryFolder + "/" + state.index + frame + SPRITE_ASSET_FILE_SUFFIX; //default
        } else if (defaultAssetList.contains("0" + frame + SPRITE_ASSET_FILE_SUFFIX)) {
            return assetCategoryFolder + "/" + "0" + frame + SPRITE_ASSET_FILE_SUFFIX; //default no state
        } else if (defaultAssetList.contains("" + state.index + 0 + SPRITE_ASSET_FILE_SUFFIX)) {
            return assetCategoryFolder + "/" + state.index + 0 + SPRITE_ASSET_FILE_SUFFIX; //default no frame
        }
        return assetCategoryFolder + "/" + "00" + SPRITE_ASSET_FILE_SUFFIX;//no frame no state
    }
}
