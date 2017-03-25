package fr.giusti.onetapadventure.repository.spritesheet;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Pair;

import com.sqli.spritesheetgenerator.SpriteGenerator;
import com.sqli.spritesheetgenerator.model.SpriteSheet;

import java.io.IOException;

import fr.giusti.onetapadventure.commons.AssetsHelper;
import fr.giusti.onetapengine.entity.GameMob;

/**
 * Created by jérémy on 25/03/2017.
 */

public class SpriteSheetRepository {


    public Bitmap getMobSpriteSheet(Context context, GameMob mob, String mobMovementType) throws IOException {
        String[][][] assetString = AttributsToSpriteMapper.getInstance().getMobMap(context, mob, mobMovementType);
        int animationFrameNb = assetString.length;
        int categoryNb = assetString[0].length;
        int layerNb = assetString[0][0].length;


        Bitmap[][][] spriteList = new Bitmap[animationFrameNb][categoryNb][layerNb];
        for (int x = 0; x < animationFrameNb; x++) {
            for (int y = 0; y < categoryNb; y++) {
                for (int z = 0; z < layerNb; z++) {
                    spriteList[x][y][z] = AssetsHelper.getBitmapFromAsset(context, assetString[x][y][z]);
                }
            }
        }

        SpriteSheet spriteSheet = new SpriteSheet(spriteList);
        return SpriteGenerator.generateSpriteSheet(spriteSheet);
    }

    public void getMobListSpriteSheetAsync(final Context context, final SpriteSheetGenerationListener listener, final Pair<GameMob, String>... mobAndMovementType) throws IOException {
        new AsyncTask<String, Integer, Pair<GameMob, Bitmap>[]>() {

            @Override
            protected Pair<GameMob, Bitmap>[] doInBackground(String... strings) {
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
        }.execute();
    }

    interface SpriteSheetGenerationListener {
        public void onSpriteSheetDone(Pair<GameMob, Bitmap>... spritedMob);

        public void onSpriteSheetProgress(int progress);

        public void onError(Exception error);

    }


}
