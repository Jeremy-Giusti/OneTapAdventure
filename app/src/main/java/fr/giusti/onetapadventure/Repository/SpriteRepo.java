package fr.giusti.onetapadventure.Repository;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.commons.FileUtils;
import fr.giusti.onetapadventure.commons.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;

public class SpriteRepo {
    private static HashMap<String, Bitmap> mBitmapList = new HashMap<String, Bitmap>();
    private static HashMap<String, Point> mSpriteDimensList = new HashMap<String, Point>();


    /**
     * explicite
     *
     * @param bitmapId
     * @return
     */
    public static Bitmap getBitmap(String bitmapId) {
        synchronized (mBitmapList) {
            return mBitmapList.get(bitmapId);
        }
    }

    public static Bitmap getSpriteBitmap(String bitmapId, int column, int line) {
        Point spriteDimens = mSpriteDimensList.get(bitmapId);
        return Bitmap.createBitmap(
                mBitmapList.get(bitmapId),
                column * spriteDimens.x,
                line * spriteDimens.y,
                spriteDimens.x,
                spriteDimens.y
        );
    }

    /**
     * excplicite
     *
     * @param bmp
     * @param bitmapId
     */
    public void addBitmap(Bitmap bmp, String bitmapId, int nbColumn, int nbLine) {
        mBitmapList.put(bitmapId, bmp);
        mSpriteDimensList.put(bitmapId, new Point(bmp.getWidth() / nbColumn, bmp.getHeight() / nbLine));
    }

    /**
     * check if the id provided already exist
     * generate another one in such case
     *
     * @param bmp
     * @param bitmapId id wich would be used
     * @return id finally used
     */
    public String addSpriteSheetWithIdCheck(Bitmap bmp, String bitmapId, int nbColumn, int nbLine) {
        String idReturn = bitmapId;

        if (mBitmapList.containsKey(bitmapId)) {
            for (int i = 1; mBitmapList.containsKey(idReturn); i++) {
                idReturn = bitmapId + i;
            }
        }

        mBitmapList.put(idReturn, bmp);
        mSpriteDimensList.put(bitmapId, new Point(bmp.getWidth() / nbColumn, bmp.getHeight() / nbLine));
        return idReturn;
    }


    /**
     * @param bitmapId
     * @return true if the id is found on the spriteSheet list
     */
    public boolean addIfDoesntExist(String bitmapId, Bitmap bmp, int nbColumn, int nbLine) {
        if (!mBitmapList.containsKey(bitmapId)) {
            mBitmapList.put(bitmapId, bmp);
            mSpriteDimensList.put(bitmapId, new Point(bmp.getWidth() / nbColumn, bmp.getHeight() / nbLine));
            return true;
        } else {
            return false;
        }
    }

    /**
     * recycle all bitmap and reset the bitmapList
     */
    public static void flushCache() {

        for (Map.Entry<String, Bitmap> entry : mBitmapList.entrySet()) {
            entry.getValue().recycle();
        }

        mBitmapList = new HashMap<String, Bitmap>();
        mSpriteDimensList = new HashMap<String, Point>();

    }

    /**
     * @param backgroundBitmapId
     * @return point.x la largeur de la bitmap, point.y la hauteur ||  null si pas d'image trouv�
     */
    public Point getDimensionSpriteSheet(String backgroundBitmapId) {
        if (mBitmapList.get(backgroundBitmapId) == null) {
            return null;
        } else {
            return new Point(mBitmapList.get(backgroundBitmapId).getWidth(), mBitmapList.get(backgroundBitmapId).getHeight());
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
    public Point saveAndLoadFile(Context context, String pictureUrl, String id, int nbColumn, int nbLine) throws IOException {

        File destFile = new File(Constants.getSpriteRepoFolder(context), id);
        if (!destFile.exists()) {
            FileUtils.copyFile(pictureUrl, destFile.getAbsolutePath());
        }
        this.addIfDoesntExist(id, FileUtils.fileToBitmap(pictureUrl), nbColumn, nbLine);

        return mSpriteDimensList.get(id);

    }

    /**
     * get a bitmap from the file found from the id (and load it to the cache)
     *
     * @param context
     * @param id
     * @return the dimension of one sprite from the spritesheet
     * @throws IOException
     */
    public Point loadFromId(Context context, String id, int nbColumn, int nbLine) throws IOException {
        File pictureFile = new File(Constants.getSpriteRepoFolder(context), id);
        this.addIfDoesntExist(id, FileUtils.fileToBitmap(pictureFile.getAbsolutePath()), nbColumn, nbLine);

        return mSpriteDimensList.get(id);
    }

    ///////////////////////////////////////////Scaling/////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @param imageKey
     * @param ratio    le 'ratio brut' pour redimensionner les images
     * @return le 'ratio arrondit' utilis� pour redimensionner les images en les deformant au minimum (l'id�e est de garder un spritesheet divisible par le nb de sprites)
     */
    public double getBestRatioForSpriteSheet(String imageKey, double ratio) {
        if (mBitmapList.containsKey(imageKey)) {
            int bitmapHeight = mBitmapList.get(imageKey).getHeight();

            //SCIENCE !
            double roundedRatio = Utils.findInversOfNearestDivider(bitmapHeight, Constants.SPRITESHEETHEIGHT, ratio);
            return roundedRatio;
        } else {
            return -1;
        }
    }
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
