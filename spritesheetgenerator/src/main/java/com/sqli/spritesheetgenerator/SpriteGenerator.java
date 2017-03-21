package com.sqli.spritesheetgenerator;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.sqli.spritesheetgenerator.model.Sprite;
import com.sqli.spritesheetgenerator.model.SpriteSheet;

import java.util.ArrayList;

/**
 * Created by jgiusti on 20/03/2017.
 */

public class SpriteGenerator {

    public static Bitmap generateSpriteSheet(SpriteSheet spritSheetModel) {
        Bitmap result = Bitmap.createBitmap(spritSheetModel.getmSpriteColumnNb(), spritSheetModel.getmSpriteRowNb(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();

        ArrayList<Sprite> spriteList = spritSheetModel.getSpriteList();
        int spriteHeight = spritSheetModel.getSpriteHeight();
        int spriteWidth = spritSheetModel.getSpriteWidth();


        for(Sprite sprite:spriteList){
            Bitmap[] spriteLayers = sprite.getLayers();
            for(Bitmap bitmap:spriteLayers){
                canvas.drawBitmap(bitmap, sprite.getmXposition()* spriteWidth,sprite.getmYposition()* spriteHeight,paint);
            }
        }


        return result;
    }


}
