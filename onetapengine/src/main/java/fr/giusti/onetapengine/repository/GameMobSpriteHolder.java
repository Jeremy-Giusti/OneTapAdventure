package fr.giusti.onetapengine.repository;

import android.graphics.Bitmap;
import android.graphics.Point;

/**
 * Created by jérémy on 04/09/2016.
 */
public class GameMobSpriteHolder {


    public Bitmap[][] sprites;
    public float frameWidth;
    public float frameHeight;

    public void resizeAllFrame(int frameWidth, int frameHeight) {
        if(frameWidth>this.frameWidth && frameHeight>this.frameHeight) return;//don't scale up, it will only increase memory consumption

        for (int x = 0; x < sprites.length; x++) {
            for (int y = 0; y < sprites[0].length; y++) {
                sprites[x][y] = Bitmap.createScaledBitmap(sprites[x][y], frameWidth, frameHeight, false);
            }
        }
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }

    public GameMobSpriteHolder(Bitmap spriteSheet, int nbFrame, int nbAnim) {
        sprites = new Bitmap[nbFrame][nbAnim];
        frameWidth = spriteSheet.getWidth() / (float) nbFrame;
        frameHeight = spriteSheet.getHeight() / (float) nbAnim;

        for (int x = 0; x < nbFrame; x++) {
            for (int y = 0; y < nbAnim; y++) {
                sprites[x][y] = Bitmap.createBitmap(spriteSheet, (int) (x * frameWidth), (int) (y * frameHeight),(int)frameWidth,(int) frameHeight);
            }
        }
    }

    public Point getFrameDimens() {
        return new Point((int) frameWidth, (int) frameHeight);
    }

    public void flush() {
        for (int x = 0; x < sprites.length; x++) {
            for (int y = 0; y < sprites[0].length; y++) {
                sprites[x][y].recycle();
            }
        }
    }
}
