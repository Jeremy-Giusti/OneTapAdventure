package fr.giusti.onetapadventure.commons;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jérémy on 25/03/2017.
 */

public class AssetsHelper {

    public static Bitmap getBitmapFromAsset(Context context, String assetPath) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream istr = null;
        Bitmap bitmap = null;
        try {
            istr = assetManager.open(assetPath);
            bitmap = BitmapFactory.decodeStream(istr);
        } finally {
            if (istr != null) istr.close();
        }

        return bitmap;
    }

}
