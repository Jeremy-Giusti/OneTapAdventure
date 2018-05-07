package fr.giusti.onetapadventure.UI.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.repository.SpriteRepo;

/**
 * Created by giusti on 31/03/2015.
 */
public class SpriteView extends android.support.v7.widget.AppCompatImageView {

    private int row = 0;
    private int column = 0;
    private int timeShowed = 0;

    public SpriteView(Context context) {
        super(context);
    }

    public SpriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttr(attrs, context);
    }

    public SpriteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttr(attrs, context);
    }

    private void handleAttr(AttributeSet attrs, Context context) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SpriteView, 0, 0);
        String spriteSheetId;
        try {
            spriteSheetId = ta.getString(R.styleable.SpriteView_spriteName);
        } finally {
            ta.recycle();
        }
        if (!TextUtils.isEmpty(spriteSheetId)) {
            setSpriteSheet(spriteSheetId);
        }
    }

    public void setSpriteSheet(@NonNull final String spriteId) {
        if (TextUtils.isEmpty(spriteId)) return;
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
