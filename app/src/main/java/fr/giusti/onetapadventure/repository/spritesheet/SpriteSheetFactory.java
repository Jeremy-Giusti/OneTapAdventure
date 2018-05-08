package fr.giusti.onetapadventure.repository.spritesheet;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.sqli.spritesheetgenerator.SpriteGenerator;
import com.sqli.spritesheetgenerator.model.SpriteSheetTemplate;

import java.io.IOException;

import fr.giusti.onetapengine.entity.GameMob;

/**
 * Created by jérémy on 25/03/2017.
 * Used to generate gameMob spriteSheet based on mob features
 */

public class SpriteSheetFactory {


    private static final String TAG = SpriteSheetFactory.class.getSimpleName();
    private static SpriteSheetFactory instance;

    private SpriteSheetFactory() {
    }

    public static SpriteSheetFactory getInstance() {
        if (instance == null) {
            instance = new SpriteSheetFactory();
        }
        return instance;
    }

    /**
     * generate a spritesheet using mob features
     *
     * @param context
     * @param mob             desired mob
     * @param mobMovementType since the movement type is not held by the mob we need to pass it
     * @return the desired spritsheet
     * @throws IOException
     */
    public Bitmap generateMobSpriteSheet(Context context, GameMob mob, String mobMovementType) throws IOException {

        Log.v(TAG, "Sprite generation for " + mob.getBitmapId());
        SpriteSheetTemplate spriteSheetTemplate = AttributesToSpriteMapper.getInstance().getMobMap(context, mob, mobMovementType);
        //Log.v(TAG, "Sprite loading for " + mob.getBitmapId());
        spriteSheetTemplate.loadBitmaps(context);
        //Log.v(TAG, "SpriteSheet generation for " + mob.getBitmapId());
        Bitmap spriteSheetResult = SpriteGenerator.generateSpriteSheet(spriteSheetTemplate);
        Log.v(TAG, "SpriteSheet ready for " + mob.getBitmapId());
        return spriteSheetResult;
    }

    private long ongoingGenerationTimestamp;

    public void generateAsyncAndIgnorePrevious(final Context context, final SpriteSheetGenerationListener listener, @NonNull final GameMob mob, final String mobMovementType) {
        long generationTimestamp = System.currentTimeMillis();
        ongoingGenerationTimestamp = generationTimestamp;

        new Thread(() -> {
            try {
                Bitmap result = generateMobSpriteSheet(context, mob, mobMovementType);
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (generationTimestamp == ongoingGenerationTimestamp)
                        listener.onSpriteSheetDone(result);
                    else
                        listener.onCancelled();
                });

            } catch (IOException e) {
                Log.e(TAG, "error generating spritesheet asyn", e);
                listener.onError(e);
            }
        }).start();
    }


    public interface SpriteSheetGenerationListener {
        void onSpriteSheetDone(Bitmap spriteSheet);

        void onError(IOException error);

        void onCancelled();
    }


}
