package com.sqli.spritesheetgenerator.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by jgiusti on 20/03/2017.
 */

public class SpriteSheet {
    private int mSpriteHeight;
    private int mSpriteWidth;
    private int mSpriteColumnNb;
    private int mSpriteRowNb;


    private ArrayList<Sprite> mSpriteList = new ArrayList<>();

    public SpriteSheet(int mSpriteHeight, int mSpriteWidth, Bitmap[][][] spriteList) {
        this.mSpriteHeight = mSpriteHeight;
        this.mSpriteWidth = mSpriteWidth;
        this.mSpriteColumnNb = spriteList.length;
        this.mSpriteRowNb = spriteList[0].length;

        for (int x = 0; x < spriteList.length; x++) {
            for (int y = 0; y < spriteList[0].length; y++) {
                mSpriteList.add(new Sprite(x, y, spriteList[x][y]));
            }
        }
    }

    public SpriteSheet(int mSpriteHeight, int mSpriteWidth, int animLength, Bitmap[]... spriteList) {

        initWith(mSpriteHeight, mSpriteWidth, animLength, spriteList);
    }

    public SpriteSheet(int animLength, Bitmap[]... spriteList) {
        int height = spriteList[0][0].getHeight();
        int width = spriteList[0][0].getWidth();
        initWith(height, width, animLength, spriteList);
    }

    private void initWith(int mSpriteHeight, int mSpriteWidth, int animLength, Bitmap[][] spriteList) {
        this.mSpriteHeight = mSpriteHeight;
        this.mSpriteWidth = mSpriteWidth;

        int row = 0;
        int column = -1;
        for (Bitmap[] sprite : spriteList) {
            if (column == animLength) {
                column = 0;
                row++;
            }

            mSpriteList.add(new Sprite(column, row, sprite));
            column++;
        }

        this.mSpriteColumnNb = column;
        this.mSpriteRowNb = row;
    }


    public int getSpriteHeight() {
        return mSpriteHeight;
    }

    public void setSpriteHeight(int mSpriteHeight) {
        this.mSpriteHeight = mSpriteHeight;
    }

    public int getSpriteWidth() {
        return mSpriteWidth;
    }

    public void setSpriteWidth(int mSpriteWidth) {
        this.mSpriteWidth = mSpriteWidth;
    }

    public ArrayList<Sprite> getSpriteList() {
        return mSpriteList;
    }

    public void setSpriteList(ArrayList<Sprite> mSpriteList) {
        this.mSpriteList = mSpriteList;
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
}
