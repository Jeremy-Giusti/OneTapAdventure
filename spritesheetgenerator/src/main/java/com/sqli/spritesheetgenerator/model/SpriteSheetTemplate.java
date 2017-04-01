package com.sqli.spritesheetgenerator.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.sqli.spritesheetgenerator.commons.AssetsHelper;

import java.io.IOException;

/**
 * Created by jgiusti on 20/03/2017.
 */

public class SpriteSheetTemplate {
    private int mSpriteHeight;
    private int mSpriteWidth;

    private int mSpriteColumnNb;
    private int mSpriteRowNb;
    private int mSpriteLayerNb;

    private String[][][] spriteAssets;
    private Bitmap[][][] spriteBitmap;


    public SpriteSheetTemplate(int columnNb, int rowNb, int layerNb) {
        this.mSpriteColumnNb = columnNb;
        this.mSpriteRowNb = rowNb;
        this.mSpriteLayerNb = layerNb;
        this.spriteAssets = new String[columnNb][rowNb][layerNb];
        this.spriteBitmap = new Bitmap[columnNb][rowNb][layerNb];
    }

    public void loadBitmaps(Context context) throws IOException {
        for (int x = 0; x < mSpriteColumnNb; x++) {
            for (int y = 0; y < mSpriteRowNb; y++) {
                for (int z = 0; z < mSpriteLayerNb; z++) {
                    spriteBitmap[x][y][z] = AssetsHelper.getBitmapFromAsset(context, spriteAssets[x][y][z]);
                }
            }
        }
        this.mSpriteHeight = spriteBitmap[0][0][0].getHeight();
        this.mSpriteWidth = spriteBitmap[0][0][0].getWidth();
    }

    public String[][][] getSpriteAssets() {
        return spriteAssets;
    }

    public void setSpriteAssets(String[][][] spriteAssets) {
        this.spriteAssets = spriteAssets;
    }

    public Bitmap[][][] getSpriteBitmap() {
        return spriteBitmap;
    }

    public void setSpriteBitmap(Bitmap[][][] spriteBitmap) {
        this.spriteBitmap = spriteBitmap;
    }

    public void setSpriteAsset(int x, int y, int z, String asset) {
        spriteAssets[x][y][z] = asset;
    }

    public void setSpriteBitmap(int x, int y, int z, Bitmap bitmap) {
        spriteBitmap[x][y][z] = bitmap;
    }

    public String getSpriteAsset(int x, int y, int z) {
        return spriteAssets[x][y][z];
    }

    public Bitmap getSpriteBitmap(int x, int y, int z) {
        return spriteBitmap[x][y][z];
    }

    public int getSpriteHeight() {
        return mSpriteHeight;
    }

    public void setmSpriteHeight(int mSpriteHeight) {
        this.mSpriteHeight = mSpriteHeight;
    }

    public int getSpriteWidth() {
        return mSpriteWidth;
    }

    public void setmSpriteWidth(int mSpriteWidth) {
        this.mSpriteWidth = mSpriteWidth;
    }

    public int getmSpriteColumnNb() {
        return mSpriteColumnNb;
    }

    public void setmSpriteColumnNb(int mSpriteColumnNb) {
        this.mSpriteColumnNb = mSpriteColumnNb;
    }

    public int getmSpriteRowNb() {
        return mSpriteRowNb;
    }

    public void setmSpriteRowNb(int mSpriteRowNb) {
        this.mSpriteRowNb = mSpriteRowNb;
    }

    public int getmSpriteLayerNb() {
        return mSpriteLayerNb;
    }

    public void setmSpriteLayerNb(int mSpriteLayerNb) {
        this.mSpriteLayerNb = mSpriteLayerNb;
    }
}
