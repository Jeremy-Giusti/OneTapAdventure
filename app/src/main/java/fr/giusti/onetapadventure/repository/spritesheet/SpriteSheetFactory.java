package fr.giusti.onetapadventure.repository.spritesheet;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

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

    /**
     * generate a spritesheet using mob features
     *
     * @param context
     * @param mob             desired mob
     * @param mobMovementType since the movement type is not held by the mob we need to pass it
     * @return the desired spritsheet
     * @throws IOException
     */
    public static Bitmap getMobSpriteSheet(Context context, GameMob mob, String mobMovementType) throws IOException {

        Log.v(TAG, "Sprite generation for " + mob.getBitmapId());
        SpriteSheetTemplate spriteSheetTemplate = AttributesToSpriteMapper.getInstance().getMobMap(context, mob, mobMovementType);
        //Log.v(TAG, "Sprite loading for " + mob.getBitmapId());
        spriteSheetTemplate.loadBitmaps(context);
        //Log.v(TAG, "SpriteSheet generation for " + mob.getBitmapId());
        Bitmap spriteSheetResult = SpriteGenerator.generateSpriteSheet(spriteSheetTemplate);
        Log.v(TAG, "SpriteSheet ready for " + mob.getBitmapId());
        return spriteSheetResult;
    }

    public static void getMobListSpriteSheetAsync(final Context context, final SpriteSheetGenerationListener listener, final Pair<GameMob, String>... mobAndMovementType) throws IOException {
        new AsyncSpriteGeneratorTask(context, listener).execute(mobAndMovementType);
    }


    interface SpriteSheetGenerationListener {
        public void onSpriteSheetDone(Pair<GameMob, Bitmap>... spritedMob);

        public void onSpriteSheetProgress(int progress);

        public void onError(Exception error);

    }

    private static class AsyncSpriteGeneratorTask extends AsyncTask<Pair<GameMob, String>, Integer, Pair<GameMob, Bitmap>[]> {

        private Context context;
        private SpriteSheetGenerationListener listener;

        AsyncSpriteGeneratorTask(final Context context, final SpriteSheetGenerationListener listener) {
            this.context = context;
            this.listener = listener;
        }

        @Override
        protected Pair<GameMob, Bitmap>[] doInBackground(Pair<GameMob, String>... mobAndMovementType) {
            Pair<GameMob, Bitmap>[] result = new Pair[mobAndMovementType.length];
            try {
                for (int i = 0; i < mobAndMovementType.length; i++) {
                    Bitmap sprisheet = getMobSpriteSheet(context, mobAndMovementType[i].first, mobAndMovementType[i].second);
                    result[i] = new Pair<GameMob, Bitmap>(mobAndMovementType[i].first, sprisheet);
                }
            } catch (IOException e) {
                listener.onError(e);
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            listener.onSpriteSheetProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Pair<GameMob, Bitmap>[] result) {
            listener.onSpriteSheetDone(result);
        }
    }


}
