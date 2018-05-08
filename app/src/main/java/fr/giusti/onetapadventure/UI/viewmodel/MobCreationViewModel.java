package fr.giusti.onetapadventure.UI.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.databinding.Observable;
import android.graphics.Bitmap;
import android.text.TextUtils;

import java.io.IOException;

import fr.giusti.onetapadventure.repository.spritesheet.SpriteSheetFactory;
import fr.giusti.onetapengine.BR;
import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.repository.PathRepo;
import fr.giusti.onetapengine.repository.SpriteRepo;

/**
 * Created by jérémy on 05/05/2018.
 */

public class MobCreationViewModel extends AndroidViewModel implements SpriteSheetFactory.SpriteSheetGenerationListener {

    private GameMob.MobBuilder mGameMob;

    public MobCreationViewModel(Application application) {
        super(application);
        mGameMob = new GameMob.MobBuilder("Name", "", 10f, 10f);
        updateMobSpriteSheet();

        mGameMob.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                onMobPropertyChanged(sender, propertyId);
            }
        });
    }

    public GameMob.MobBuilder getGameMob() {
        return mGameMob;
    }

    private void onMobPropertyChanged(Observable sender, int propertyId) {

        if (propertyId == BR.alignement || propertyId == BR.specialMove1Name || propertyId == BR.touchedMoveName) {
            updateMobSpriteSheet();
        }
    }

    private void updateMobSpriteSheet() {
        SpriteSheetFactory.getInstance().generateAsyncAndIgnorePrevious(getApplication(), this, mGameMob.build(), PathRepo.ePathType.RANDOM.name);

    }

    @Override
    public void onSpriteSheetDone(Bitmap spriteSheet) {
        String spriteId = String.valueOf(System.currentTimeMillis());
        String oldSpriteId = mGameMob.getSpriteSheetId();

        SpriteRepo.addSpriteSheet(spriteSheet, spriteId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);
        mGameMob.setSpriteSheetId(spriteId);

        if (!TextUtils.isEmpty(oldSpriteId)) {
            SpriteRepo.removeSpriteSheet(oldSpriteId);
        }
    }

    @Override
    public void onError(IOException error) {
        //TODO show error ?
    }

    @Override
    public void onCancelled() {
        //nothing
    }
}
