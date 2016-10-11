package fr.giusti.onetapadventure.commons;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;

public class Utils {

    /**
     * exemple: notre inverse de diviseur est 0.4 on voudrait trouver le chiffre le plus proche de 0.4 pour que lorsqu'on fasse 8*chiffreproche on obtienne un resultat entier divisible par 2
     *
     * @param numberToDivide    8 dans l'exemple
     * @param dividerGoal       2 dans l'exemple
     * @param nearInversDivider 0.4 dans l'exemple
     * @return 0.5 dans l'exemple
     */
    public static double findInversOfNearestDivider(int numberToDivide, int dividerGoal, double nearInversDivider) {
        double nearDividerNumber = numberToDivide * nearInversDivider;
        double difference = nearDividerNumber % dividerGoal;
        difference = (difference < (dividerGoal / 2) ? -difference : difference);//on recupere le coté positif
        return (nearDividerNumber + difference) / numberToDivide;
    }

    public static boolean doRectIntersect(RectF r1, RectF r2) {
        return !(r2.left > r1.right
                || r2.right < r1.left
                || r2.top > r1.bottom
                || r2.bottom < r1.top);
    }

    public static boolean doRectIntersect(Rect r1, RectF r2) {
        return !(r2.left > r1.right
                || r2.right < r1.left
                || r2.top > r1.bottom
                || r2.bottom < r1.top);
    }

    public static boolean doRectIntersect(Rect r1, Rect r2) {
        return !(r2.left > r1.right
                || r2.right < r1.left
                || r2.top > r1.bottom
                || r2.bottom < r1.top);
    }

    public boolean isPointOnRect(Point point, Rect rect) {
        return (point.x > rect.left && point.x < rect.right) && (point.y > rect.top && point.y < rect.bottom);
    }


}
