package com.sqli.spritesheetgenerator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;

import com.sqli.spritesheetgenerator.model.SpriteSheetTemplate;

/**
 * Created by jgiusti on 20/03/2017.
 */

public class SpriteGenerator {

    /**
     * generate a single bitmap spritesheet from the spriteSheet model data <br>
     *
     * @param spritSheetModel model with all data needed to make the bitmap
     * @return
     */
    public static Bitmap generateSpriteSheet(SpriteSheetTemplate spritSheetModel) {

        int spriteHeight = spritSheetModel.getSpriteHeight();
        int spriteWidth = spritSheetModel.getSpriteWidth();

        Bitmap result = Bitmap.createBitmap(spritSheetModel.getmSpriteColumnNb() * spriteWidth, spritSheetModel.getmSpriteRowNb() * spriteHeight, Bitmap.Config.ARGB_8888);
        result.eraseColor(Color.TRANSPARENT);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();

        Bitmap[][][] spriteBitmaps = spritSheetModel.getSpriteBitmap();
        Bitmap sprite;
        for (int x = 0; x < spriteBitmaps.length; x++) {
            for (int y = 0; y < spriteBitmaps[0].length; y++) {
                for (int z = 0; z < spriteBitmaps[0][0].length; z++) {
                    sprite = spriteBitmaps[x][y][z];
                    if (sprite != null)
                        canvas.drawBitmap(sprite, x * spriteWidth, y * spriteHeight, paint);
                }
            }
        }
        return result;
    }

    /**
     * generate spritesheet(s) in an async task
     *
     * @param spriteSheetTemplateModelList
     * @param callback                     for progress update and result (in main thread)
     */
    public static void generateSpriteSheetAsync(final SpriteSheetGenerationListener callback, final SpriteSheetTemplate... spriteSheetTemplateModelList) {
        new AsyncTask<String, Integer, Bitmap[]>() {
            @Override
            protected Bitmap[] doInBackground(String... params) {
                Bitmap[] spritesheetResults = new Bitmap[spriteSheetTemplateModelList.length];
                for (int i = 0; i < spriteSheetTemplateModelList.length; i++) {
                    spritesheetResults[i] = generateSpriteSheet(spriteSheetTemplateModelList[i]);
                    publishProgress((int) ((i / (float) (spriteSheetTemplateModelList.length)) * 100));
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
