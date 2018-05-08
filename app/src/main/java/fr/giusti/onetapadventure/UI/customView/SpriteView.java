package fr.giusti.onetapadventure.UI.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewTreeObserver;

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

    private int spriteSize = -1;

    private final Handler handler = new Handler();
    private Runnable currentRunnable;

    public SpriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttr(attrs, context);

        //update size when view is loaded
        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setSpriteSize(spriteSize);
                getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
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

    /**
     * set padding depending en sprite desired size
     *
     * @param spriteSize
     */
    public void setSpriteSize(int spriteSize) {
        this.spriteSize = spriteSize;
        int viewSize = this.getMeasuredHeight();
        if (viewSize > this.getMeasuredWidth())
            viewSize = this.getMeasuredWidth();

        int padding = viewSize / 2 - (spriteSize);

        if (padding > 0) {
            setPadding(padding, padding, padding, padding);
        } else {
            setPadding(0, 0, 0, 0);
        }
    }

    public void setSpriteSheet(@NonNull final String spriteId) {
        if (TextUtils.isEmpty(spriteId)) return;
        if (currentRunnable != null) {
            handler.removeCallbacks(currentRunnable);
        }

        currentRunnable = new Runnable() {
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
                Bitmap sprite = SpriteRepo.getSpriteBitmapWithNullCheck(spriteId, column, row);
                if (sprite != null) setImageBitmap(sprite);
                handler.postDelayed(this, Constants.FRAME_DURATION * 20);
            }
        };
        handler.post(currentRunnable);
    }
}
