package com.sqli.spritesheetgenerator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.sqli.spritesheetgenerator.model.Sprite;
import com.sqli.spritesheetgenerator.model.SpriteSheet;

import java.util.ArrayList;

/**
 * Created by jgiusti on 20/03/2017.
 */

public class SpriteGenerator {

    /**
     * generate a single bitmap spritesheet from the spriteSheet model data <br>
     * TODO optimizations if needed
     *
     * @param spritSheetModel model with all data needed to make the bitmap
     * @return
     */
    public static Bitmap generateSpriteSheet(SpriteSheet spritSheetModel) {

        int spriteHeight = spritSheetModel.getSpriteHeight();
        int spriteWidth = spritSheetModel.getSpriteWidth();

        Bitmap result = Bitmap.createBitmap(spritSheetModel.getmSpriteColumnNb() * spriteWidth, spritSheetModel.getmSpriteRowNb() * spriteHeight, Bitmap.Config.ARGB_8888);
        result.eraseColor(Color.TRANSPARENT);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();

        ArrayList<Sprite> spriteList = spritSheetModel.getSpriteList();


        for (Sprite sprite : spriteList) {
            Bitmap[] spriteLayers = sprite.getLayers();
            for (Bitmap bitmap : spriteLayers) {
                canvas.drawBitmap(bitmap, sprite.getmXposition() * spriteWidth, sprite.getmYposition() * spriteHeight, paint);
            }
        }


        return result;
    }

    /**
     * generate spritesheet(s) in an async task
     *
     * @param spriteSheetModelList
     * @param callback             for progress update and result (in main thread)
     */
    public static void generateSpriteSheetAsync(final SpriteSheetGenerationListener callback, final SpriteSheet... spriteSheetModelList) {
        new AsyncTask<String, Integer, Bitmap[]>() {
            @Override
            protected Bitmap[] doInBackground(String... params) {
                Bitmap[] spritesheetResults = new Bitmap[spriteSheetModelList.length];
                for (int i = 0; i < spriteSheetModelList.length; i++) {
                    spritesheetResults[i] = generateSpriteSheet(spriteSheetModelList[i]);
                    publishProgress((int) ((i / (float) (spriteSheetModelList.length)) * 100));
                }
                return spritesheetResults;
            }

            @Override
            protected void onPostExecute(Bitmap... bitmap) {
                callback.onSpriteSheetReady(bitmap);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                callback.onProgressUpdate(values[0]);
            }

        }.execute();
    }


    public interface SpriteSheetGenerationListener {
        void onSpriteSheetReady(Bitmap... bitmap);

        void onProgressUpdate(int percentValue);
    }


}
