package fr.giusti.onetapadventure.UI.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.Bindable;

import fr.giusti.onetapengine.entity.GameMob;

/**
 * Created by jérémy on 05/05/2018.
 */

public class MobCreationViewModel extends ViewModel {

    private GameMob.MobBuilder mGameMob;

    public MobCreationViewModel() {
        mGameMob = new GameMob.MobBuilder("", "", 10f, 10f);
    }

    public GameMob.MobBuilder getGameMob() {
        return mGameMob;
    }



}
