package fr.giusti.onetapadventure.repository.entities;

import android.content.Context;

import java.util.ArrayList;

import fr.giusti.onetapadventure.commons.Constants;
import fr.giusti.onetapadventure.gameObject.GameBoard;
import fr.giusti.onetapadventure.gameObject.entities.GameMob;

/**
 * Created by jérémy on 10/09/2016.
 */
public class EntityDispenserRepo {

    public static EntityDispenser getLvl1_1MobDispenser(Context context) {
        ThreeTierEntityDispenser result = new ThreeTierEntityDispenser(MobRepo.getLvl1x1InitList(context), MobRepo.getLvl1x1BackupList(context));
        return result;
    }
}
