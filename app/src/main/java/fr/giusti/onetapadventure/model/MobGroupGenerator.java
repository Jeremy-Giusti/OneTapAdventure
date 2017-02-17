package fr.giusti.onetapadventure.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.Random;

import fr.giusti.onetapadventure.R;
import fr.giusti.onetapengine.commons.Constants;
import fr.giusti.onetapengine.entity.Entity;
import fr.giusti.onetapengine.entity.GameMob;
import fr.giusti.onetapengine.entity.moves.SpecialMove;
import fr.giusti.onetapengine.entity.moves.TouchedMove;
import fr.giusti.onetapengine.repository.PathRepo;
import fr.giusti.onetapengine.repository.SpriteRepo;

/**
 * Created by jgiusti on 17/02/2017.
 * model that should hold enough data to generate list of randomly slightly different mob
 * TODO size management?
 * TODO sprite generation base on attributs
 */

public class MobGroupGenerator {
    private String baseName;
    private int nbMob;
    private RectF dest;
    private RectF origin;
    private SpecialMove smov;
    private TouchedMove tmov;
    private int msToDest;//millisecondes spend to reach dest
    private int alignement;
    private int health;
    private int scoreValue;


    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public int getNbMob() {
        return nbMob;
    }

    public void setNbMob(int nbMob) {
        this.nbMob = nbMob;
    }

    public RectF getDest() {
        return dest;
    }

    public void setDest(RectF dest) {
        this.dest = dest;
    }

    public RectF getOrigin() {
        return origin;
    }

    public void setOrigin(RectF origin) {
        this.origin = origin;
    }

    public SpecialMove getSmov() {
        return smov;
    }

    public void setSmov(SpecialMove smov) {
        this.smov = smov;
    }

    public TouchedMove getTmov() {
        return tmov;
    }

    public void setTmov(TouchedMove tmov) {
        this.tmov = tmov;
    }

    public int getMsToDest() {
        return msToDest;
    }

    public void setMsToDest(int msToDest) {
        this.msToDest = msToDest;
    }

    public int getAlignement() {
        return alignement;
    }

    public void setAlignement(int alignement) {
        this.alignement = alignement;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public ArrayList<Entity> generateGroup(Context context) {
        ArrayList<Entity> group = new ArrayList<>(nbMob);

        double msToTickFactor = Constants.FRAME_PER_SEC / 1000;//used to convert the time to dest to a pathlength in order to generate mob path

        String spriteId = baseName + "sprite";
        SpriteRepo.addSpriteSheet(BitmapFactory.decodeResource(context.getResources(), R.drawable.fly_spritesheet), spriteId, Constants.SPRITESHEETWIDTH, Constants.SPRITESHEETHEIGHT);

        GameMob.MobBuilder mobBuilder;
        Random r = new Random();

        for (int i = 0; i < nbMob; i++) {

            float startX = r.nextInt((int) (origin.right - origin.left)) + origin.left;
            float startY = r.nextInt((int) (origin.bottom - origin.top)) + origin.top;
            float destX = r.nextInt((int) (dest.right - dest.left)) + dest.left;
            float destY = r.nextInt((int) (dest.bottom - dest.top)) + dest.top;
            PointF[] path = PathRepo.generateLineToDest(new PointF(startX, startY), new PointF(destX, destY), (int) (msToDest * msToTickFactor));

            mobBuilder = new GameMob.MobBuilder(baseName + i, spriteId, startX, startY)
                    .setMovePattern(path)
                    .setHealth(health)
                    .setAlignement(alignement)
                    .setSpecialMove(smov)
                    .setTouchedMove(tmov)
                    .setScore(scoreValue);

            group.add(mobBuilder.build());
        }
        return group;
    }
}
