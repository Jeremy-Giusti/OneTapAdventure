package fr.giusti.onetapadventure.gameObject.interactions;

import fr.giusti.onetapadventure.commons.Constants;

/**
 * Created by jgiusti on 20/09/2016.
 * Hold every information needed to create a touchPoint from the event coordinate
 */

public class TouchDispenser {
    private int touchStrock = Constants.TOUCH_STROKE;
    private String spriteId = null;
    private int damageDone = Constants.TOUCH_DAMAGE;

    public TouchDispenser(int touchStrock, String spriteId, int damageDone) {
        this.touchStrock = touchStrock;
        this.spriteId = spriteId;
        this.damageDone = damageDone;
    }

    public int getTouchStrock() {
        return touchStrock;
    }

    public void setTouchStrock(int touchStrock) {
        this.touchStrock = touchStrock;
    }

    public String getSpriteId() {
        return spriteId;
    }

    public void setSpriteId(String spriteId) {
        this.spriteId = spriteId;
    }

    public int getDamageDone() {
        return damageDone;
    }

    public void setDamageDone(int damageDone) {
        this.damageDone = damageDone;
    }

    public TouchPoint generateTouchPoint(float x, float y) {
        return new TouchPoint(x, y, spriteId, touchStrock, damageDone);
    }
}
