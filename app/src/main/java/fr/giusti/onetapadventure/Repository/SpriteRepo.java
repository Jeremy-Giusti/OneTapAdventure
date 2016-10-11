package fr.giusti.onetapadventure.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.giusti.onetapadventure.gameObject.GameMobSpriteHolder;
import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.commons.FileUtils;

public class SpriteRepo {
    private static HashMap<String, GameMobSpriteHolder> mMobSpriteList = new HashMap<>();
    private static HashMap<String, Bitmap> mPictureList = new HashMap<>();
    // private static HashMap<String, Point> mSpriteDimensList = new HashMap<String, Point>();


    /**
     * explicite
     *
     * @param bitmapId
     * @return
     */
    public static Bitmap getPicture(String bitmapId) {
        synchronized (mPictureList) {
            return mPictureList.get(bitmapId);
        }
    }

    public static void addPicture(String pictureId, Bitmap bitmap) {
        mPictureList.put(pictureId, bitmap);

    }


    public static Bitmap getSpriteBitmap(String bitmapId, int column, int line) {
        return mMobSpriteList.get(bitmapId).sprites[column][line];
    }

    /**
     * excplicite
     *
     * @param bmp
     * @param bitmapId
     */
    public static void addSpriteSheet(Bitmap bmp, String bitmapId, int nbColumn, int nbLine) {
        GameMobSpriteHolder newHolder = new GameMobSpriteHolder(bmp, nbColumn, nbLine);
        mMobSpriteList.put(bitmapId, newHolder);
    }

    /**
     * check if the id provided already exist
     * generate another one in such case
     *
     * @param bmp
     * @param bitmapId id wich would be used
     * @return id finally used
     */
    public static String addSpriteSheetWithIdCheck(Bitmap bmp, String bitmapId, int nbColumn, int nbLine) {
        String idReturn = bitmapId;

        if (mMobSpriteList.containsKey(bitmapId)) {
            for (int i = 1; mMobSpriteList.containsKey(idReturn); i++) {
                idReturn = bitmapId + i;
            }
        }

        addSpriteSheet(bmp, idReturn, nbColumn, nbLine);
        return idReturn;
    }


    /**
     * @param bitmapId
     * @return true if the id is found on the spriteSheet list
     */
    public static boolean addSpritesheetIfDoesntExist(String bitmapId, Bitmap bmp, int nbColumn, int nbLine) {
        if (!mMobSpriteList.containsKey(bitmapId)) {
            addSpriteSheet(bmp, bitmapId, nbColumn, nbLine);
            return true;
        } else {
            return false;
        }
    }

    /**
     * recycle all bitmap and reset the bitmapList
     */
    public static void flushCache() {

        for (Map.Entry<String, Bitmap> entry : mPictureList.entrySet()) {
            entry.getValue().recycle();
        }
        for (GameMobSpriteHolder holder : mMobSpriteList.values()) {
            holder.flush();
        }

        mPictureList = new HashMap<String, Bitmap>();
        mMobSpriteList = new HashMap<>();

    }

    /**
     * @param id
     * @return point.x la largeur de la bitmap, point.y la hauteur ||  null si pas d'image trouv�
     */
    public static Point getDimensionSprite(String id) {
        if (mMobSpriteList.get(id) == null) {
            return null;
        } else {
            return mMobSpriteList.get(id).getFrameDimens();
        }
    }

    public static Point getDimensionPicture(String pictureId) {
        if (mPictureList.get(pictureId) == null) {
            return null;
        } else {
            return new Point(mPictureList.get(pictureId).getWidth(), mPictureList.get(pictureId).getHeight());
        }
    }


    ///////////////////////////////////////---FILE---//////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    /**
     * add the file to the spriteRepoFolder and load the bitmap on the cache
     *
     * @param pictureUrl
     * @param id         wanted
     * @return the dimension of one sprite from the spritesheet
     */
    public static Point saveAndLoadFile(Context context, String pictureUrl, String id, int nbColumn, int nbLine) throws IOException {

        File destFile = new File(Constants.getSpriteRepoFolder(context), id);
        if (!destFile.exists()) {
            FileUtils.copyFile(pictureUrl, destFile.getAbsolutePath());
        }
        addSpritesheetIfDoesntExist(id, FileUtils.fileToBitmap(pictureUrl), nbColumn, nbLine);

        return mMobSpriteList.get(id).getFrameDimens();

    }


    /**
     * add the file to the spriteRepoFolder and load the bitmap on the cache
     *
     * @param alsoPutOnPictureList true if the image should also be stored as a single picture
     * @return
     * @throws IOException
     */
    public static Point saveAndLoadFile(Context context, String pictureUrl, String id, int nbColumn, int nbLine, boolean alsoPutOnPictureList) throws IOException {
        File destFile = new File(Constants.getSpriteRepoFolder(context), id);
        if (!destFile.exists()) {
            FileUtils.copyFile(pictureUrl, destFile.getAbsolutePath());
        }
        Bitmap picture = FileUtils.fileToBitmap(pictureUrl);
        addSpritesheetIfDoesntExist(id, picture, nbColumn, nbLine);
        if (alsoPutOnPictureList) addPicture(id, picture);
        return mMobSpriteList.get(id).getFrameDimens();
    }


    /**
     * get a bitmap from the file found from the id (and load it to the cache)
     *
     * @param context
     * @param id
     * @return the dimension of one sprite from the spritesheet
     * @throws IOException
     */
    public static Point loadSpriteSheetFromId(Context context, String id, int nbColumn, int nbLine) throws IOException {
        File pictureFile = new File(Constants.getSpriteRepoFolder(context), id);
        addSpritesheetIfDoesntExist(id, FileUtils.fileToBitmap(pictureFile.getAbsolutePath()), nbColumn, nbLine);
        return mMobSpriteList.get(id).getFrameDimens();
    }

    public static void resizePicture(String id, float ratio) {
        Bitmap picture = mPictureList.get(id);
        picture = Bitmap.createScaledBitmap(picture,(int)(picture.getWidth()*ratio), (int)(picture.getHeight()*ratio), true);
        mPictureList.put(id,picture);
    }

    public static void resizeSprites(String mSpritesId, int width, int height) {
        mMobSpriteList.get(mSpritesId).resizeAllFrame(width,height);
    }

    ///////////////////////////////////////////Scaling/////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

//    /**
//     * @param imageKey
//     * @param ratio    le 'ratio brut' pour redimensionner les images
//     * @return le 'ratio arrondit' utilis� pour redimensionner les images en les deformant au minimum (l'id�e est de garder un spritesheet divisible par le nb de sprites)
//     */
//    public double getBestRatioForSpriteSheet(String imageKey, double ratio) {
//        if (mBitmapList.containsKey(imageKey)) {
//            int bitmapHeight = mBitmapList.get(imageKey).getHeight();
//
//            //SCIENCE !
//            double roundedRatio = Utils.findInversOfNearestDivider(bitmapHeight, Constants.SPRITESHEETHEIGHT, ratio);
//            return roundedRatio;
//        } else {
//            return -1;
//        }
//    }


//
//    /**
//     * creé une spritesheet a l'echelle des diemnsions du mob
//     * @param spritSheetId
//     * @param mobHeight
//     * @param mobWidth
//     * @return id de la nouvelle spritsheet
//     */
//    public String scaleSpritesheetXY(String spritSheetId, int mobHeight, int mobWidth) {
//        int dstWidth = mobWidth * Constants.SPRITESHEETWIDTH;
//        int dstHeight = mobHeight * Constants.SPRITESHEETHEIGHT;
//        String newBitmapId = spritSheetId + Constants.SPRITE_SCALED_XY_SUFFIXE;
//
//        this.addSpriteSheetWithIdCheck(Bitmap.createScaledBitmap(mBitmapList.get(spritSheetId), dstWidth, dstHeight, false), newBitmapId);
//        return newBitmapId;
//    }

}
