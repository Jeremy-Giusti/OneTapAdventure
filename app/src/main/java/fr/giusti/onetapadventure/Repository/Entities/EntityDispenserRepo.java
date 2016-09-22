package fr.giusti.onetapadventure.repository.entities;

import android.content.Context;

import fr.giusti.onetapadventure.gameObject.entities.EntityDispenser;
import fr.giusti.onetapadventure.gameObject.entities.ThreeTierEntityDispenser;

/**
 * Created by jérémy on 10/09/2016.
 */
public class EntityDispenserRepo {

    public static EntityDispenser getLvl1_1MobDispenser(Context context) {
        ThreeTierEntityDispenser result = new ThreeTierEntityDispenser(EntityRepo.getLvl1x1InitList(context), EntityRepo.getLvl1x1BackupList(context));
        return result;
    }
}
