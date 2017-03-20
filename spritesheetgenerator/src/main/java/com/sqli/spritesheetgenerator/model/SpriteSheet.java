package com.sqli.spritesheetgenerator.model;

import android.graphics.Bitmap;

/**
 * Created by jgiusti on 20/03/2017.
 */

public class SpriteSheet {
    private double mSpriteHeight;
    private double mSpriteWidth;

    private Bitmap[][] mSpriteList;

    public SpriteSheet(double mSpriteHeight, double mSpriteWidth, Bitmap[][] spriteList) {
        this.mSpriteHeight = mSpriteHeight;
        this.mSpriteWidth = mSpriteWidth;
        this.mSpriteList = spriteList;
    }

    public SpriteSheet(double mSpriteHeight, double mSpriteWidth, int animLength, Bitmap... spriteList) {

        initWith(mSpriteHeight, mSpriteWidth, animLength, spriteList);
    }

    public SpriteSheet(int animLength, Bitmap... spriteList) {
        double height = spriteList[0].getHeight();
        double width = spriteList[0].getWidth();
        initWith(height, width, animLength, spriteList);
    }

    private void initWith(double mSpriteHeight, double mSpriteWidth, int animLength, Bitmap[] spriteList) {
        this.mSpriteHeight = mSpriteHeight;
        this.mSpriteWidth = mSpriteWidth;
        int columnNb = spriteList.length / animLength;
        this.mSpriteList = new Bitmap[animLength][columnNb];

        int row = 0;
        int column = 0;
        for (Bitmap sprite : spriteList) {
            mSpriteList[row][column] = sprite;
            row++;
            if (row == animLength) {
                row = 0;
                column++;
            }
        }
    }


    public double getSpriteHeight() {
        return mSpriteHeight;
    }

    public void setSpriteHeight(double mSpriteHeight) {
        this.mSpriteHeight = mSpriteHeight;
    }

    public double getSpriteWidth() {
        return mSpriteWidth;
    }

    public void setSpriteWidth(double mSpriteWidth) {
        this.mSpriteWidth = mSpriteWidth;
    }

    public Bitmap[][] getSpriteList() {
        return mSpriteList;
    }

    public void setSpriteList(Bitmap[][] mSpriteList) {
        this.mSpriteList = mSpriteList;
    }
}
