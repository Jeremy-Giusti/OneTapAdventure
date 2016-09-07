package fr.giusti.onetapadventure.callback;

import fr.giusti.onetapadventure.GameObject.GameMob;

public interface OnBoardEventListener {

    public void OnMobDeath(GameMob deadMob);
    public void OnAllMobDead(GameMob lastMobKilled);
}
