package fr.giusti.onetapadventure.UI.CustomView;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ImageView;

import fr.giusti.onetapadventure.Repository.SpriteRepo;
import fr.giusti.onetapadventure.commons.Constants;

/**
 * Created by giusti on 31/03/2015.
 */
public class SpriteView extends ImageView {

    private int row = 0;
    private int column = 0;
    private int timeShowed = 0;

    public SpriteView(Context context) {
        super(context);
    }

    public SpriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpriteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSpriteSheet(final String spriteId) {
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (timeShowed == 1) {
                    column--;
                } else {
                    column++;
                }

                if (column >= Constants.SPRITESHEETWIDTH || column < 0) {

                    column = (column >= Constants.SPRITESHEETWIDTH) ? column - 1 : column + 1;

                    timeShowed++;
                    if (timeShowed >= 2) {
                        column = 0;
                        timeShowed = 0;
                        row++;
                        if (row >= Constants.SPRITESHEETHEIGHT) {
                            row = 0;
                        }
                    }
                }
                setImageBitmap(SpriteRepo.getSpriteBitmap(spriteId, column, row));
                handler.postDelayed(this, Constants.FRAME_DURATION * 20);
            }
        });
    }
}
