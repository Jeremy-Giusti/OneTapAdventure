package fr.giusti.onetapadventure.repository.spritesheet;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Pair;

import com.sqli.spritesheetgenerator.SpriteGenerator;
import com.sqli.spritesheetgenerator.model.SpriteSheetTemplate;

import java.io.IOException;

import fr.giusti.onetapengine.entity.GameMob;

/**
 * Created by jérémy on 25/03/2017.
 * Used to generate gameMob spriteSheet based on mob features
 *
 */

public class SpriteSheetFactory {


    /**
     *
     * @param context
     * @param mob
     * @param mobMovementType
     * @return
     * @throws IOException
     */
    public static Bitmap getMobSpriteSheet(Context context, GameMob mob, String mobMovementType) throws IOException {
        SpriteSheetTemplate spriteSheetTemplate = AttributsToSpriteMapper.getInstance().getMobMap(context, mob, mobMovementType);

        spriteSheetTemplate.loadBitmaps(context);
        return SpriteGenerator.generateSpriteSheet(spriteSheetTemplate);
    }

    public static void getMobListSpriteSheetAsync(final Context context, final SpriteSheetGenerationListener listener, final Pair<GameMob, String>... mobAndMovementType) throws IOException {
        new AsyncSpriteGeneratorTask(context,listener).execute(mobAndMovementType);
    }




    interface SpriteSheetGenerationListener {
        public void onSpriteSheetDone(Pair<GameMob, Bitmap>... spritedMob);

        public void onSpriteSheetProgress(int progress);

        public void onError(Exception error);

    }

    private static class AsyncSpriteGeneratorTask extends AsyncTask<Pair<GameMob, String>, Integer, Pair<GameMob, Bitmap>[]>{

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
