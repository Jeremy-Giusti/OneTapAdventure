package com.sqli.spritesheetgenerator.model;

import android.graphics.Bitmap;

/**
 * Created by jgiusti on 21/03/2017.
 */

public class Sprite {
    private Bitmap[] mLayers;
    private int mXposition;
    private int mYposition;

    public Sprite(int xPosition, int yPosition, Bitmap... layers) {
        this.mLayers = layers;
        mXposition = xPosition;
        mYposition = yPosition;
    }

    public Bitmap[] getLayers() {
        return mLayers;
    }

    public void setLayers(Bitmap[] mLayers) {
        this.mLayers = mLayers;
    }

    public int getmXposition() {
        return mXposition;
    }

    public void setmXposition(int mXposition) {
        this.mXposition = mXposition;
    }

    public int getmYposition() {
        return mYposition;
    }

    public void setmYposition(int mYposition) {
        this.mYposition = mYposition;
    }
}
