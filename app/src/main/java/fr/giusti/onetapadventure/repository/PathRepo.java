package fr.giusti.onetapadventure.repository;

import android.graphics.Point;
import android.graphics.PointF;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;

public class PathRepo {

    /**
     * @param originalPath the original path
     * @param start        beginning of the new path from the original
     * @param end          end of the new path from the original
     * @param Xalteration  mulpicator for X move
     * @param Yalteration  multiplicator for Y move
     * @return a new altered subPath
     */
    public static PointF[] generateSubPath(PointF[] originalPath, int start, int end, int Xalteration, int Yalteration) {
        if (start < 0) {
            return null;
        }

        PointF[] result = (end < originalPath.length) ? new PointF[end - start] : new PointF[originalPath.length - start];
        for (int i = start; i < end && i < originalPath.length; i++) {
            result[i] = new PointF(originalPath[i].x * Xalteration, originalPath[i].y * Yalteration);
        }
        return result;
    }


    /**
     * shorten or extend the path depending on the ratio
     *
     * @param definitiveRatio
     * @param unscaledPath
     * @return
     */
    public static PointF[] createScalePath(double definitiveRatio, PointF[] unscaledPath) {
        PointF[] scaledPath = new PointF[unscaledPath.length];
        for (int i = 0; i < unscaledPath.length; i++) {
            scaledPath[i] = new PointF((float) (unscaledPath[i].x * definitiveRatio), (float) (unscaledPath[i].y * definitiveRatio));
        }
        return scaledPath;
    }

    /**
     * generate a line path (useless when used alone since a Point[1] would be enougth to go in one direction)
     *
     * @param pathLength
     * @param xSpeed
     * @param ySpeed
     * @return
     */
    public static PointF[] generateLinePath(int pathLength, int xSpeed, int ySpeed) {
        PointF[] returnPath = new PointF[pathLength];

        for (int i = 0; i < pathLength; i++) {
            returnPath[i] = new PointF(xSpeed, ySpeed);
        }

        return returnPath;
    }

    /**
     * generate a line path accelerating linearly over time
     *
     * @param pathLength
     * @param accelerationFactor if below 1 will decelerate, if <0 funny things happens (vibrate ?)
     * @param xInitialSpeed
     * @param yInitialSpeed
     * @return
     */
    public static PointF[] generateAcceleratingLinePath(int pathLength, int xInitialSpeed, int yInitialSpeed, float accelerationFactor) {
        PointF[] returnPath = new PointF[pathLength];
        float accelerator = 1;
        for (int i = 0; i < pathLength; i++) {
            returnPath[i] = new PointF(xInitialSpeed * accelerator, yInitialSpeed * accelerator);
            accelerator = (accelerator * accelerationFactor);
        }

        return returnPath;
    }


    public static PointF[] generateLineToDest(PointF startingPosition, PointF destination, int pathLength) {
        PointF[] result = new PointF[pathLength];
        float xSpeed = (destination.x - startingPosition.x) / (float) pathLength;
        float ySpeed = (destination.y - startingPosition.y) / (float) pathLength;

        for (int i = 0; i < pathLength; i++) {
            result[i] = new PointF(xSpeed, ySpeed);
        }
        return result;
    }


    /**
     * generate a curved path
     *
     * @param pathLength
     * @param xSpeed
     * @param ySpeed
     * @param xCurve
     * @param yCurve
     * @return
     */
    public static PointF[] generateCurvedPath(int pathLength, int xSpeed, int ySpeed, int xCurve, int yCurve) {
        PointF[] returnPath = new PointF[pathLength];
        float xCurrentCurve = 0;
        float yCurrentCurve = 0;
        for (int i = 0; i < pathLength; i++) {
            xCurrentCurve = (float) (xCurve * (Math.cos(Math.PI * ((double) i / (pathLength - 1)))));
            yCurrentCurve = (float) (yCurve * (Math.cos(Math.PI * ((double) i / (pathLength - 1)))));

            returnPath[i] = new PointF(xSpeed + xCurrentCurve, ySpeed + yCurrentCurve);

        }

        return returnPath;
    }

    /**
     * @param pathLength
     * @param startingPos
     * @param destPos
     * @param curve
     * @param inversed
     * @return
     */
    public static PointF[] generateCurvedPath(PointF startingPos, PointF destPos, int curve, boolean inversed, int pathLength) {
        PointF[] returnPath = generateLineToDest(startingPos, destPos, pathLength);
        float distx = Math.abs(destPos.x - startingPos.x);
        float disty = Math.abs(destPos.y - startingPos.y);
        float yCurveRatio = distx / (distx + disty);//les courbes sont perpendiculaire a la droite non courbé
        float xCurveRatio = disty / (distx + disty);
        int inversedInt = (inversed) ? -1 : 1;

        for (int i = 1; i < pathLength; i++) {
            float avancement = i / (float) pathLength;
            float curveAvancement = (float) Math.cos(Math.PI * avancement);

            float xCurrentCurve = (inversedInt * curve * xCurveRatio * curveAvancement) / (pathLength / 2);
            float yCurrentCurve = (inversedInt * curve * yCurveRatio * curveAvancement) / (pathLength / 2);

            returnPath[i].x += xCurrentCurve;
            returnPath[i].y += yCurrentCurve;
        }

        return returnPath;
    }


    /**
     * clarification needed
     *
     * @param pathLength
     * @param secondHalfSpeedCompensation
     * @param FirstHalfSpeedXY
     * @param xCurve
     * @param yCurve
     * @return
     */
    public static PointF[] generateLoopedPath(int pathLength, Point secondHalfSpeedCompensation, Point FirstHalfSpeedXY, int xCurve, int yCurve) {
        ArrayList<PointF> returnPath = new ArrayList<PointF>();

        int pathLength1half = pathLength / 2;
        int pathLength2half = pathLength - (pathLength / 2);

        returnPath.addAll(Arrays.asList(generateCurvedPath(pathLength1half, FirstHalfSpeedXY.x, FirstHalfSpeedXY.y, xCurve, yCurve)));
        returnPath.addAll(Arrays.asList(generateCurvedPath(pathLength2half, (secondHalfSpeedCompensation.x - FirstHalfSpeedXY.x), (secondHalfSpeedCompensation.y - FirstHalfSpeedXY.y), -xCurve, -yCurve)));

        return returnPath.toArray(new PointF[pathLength]);
    }


    /**
     * create a circle
     *
     * @param pathLength
     * @param center
     * @param rayon
     * @param startingPositionDegres
     * @return
     */
    public static Pair<PointF, PointF[]> generateCirclePath(int pathLength, PointF center, int rayon, int startingPositionDegres) {
        PointF[] result = new PointF[pathLength];
        double progress = (Math.PI*2) * (startingPositionDegres/360);
        double progressLeap = (Math.PI*2) / pathLength;

        float xpos = (float) (center.x + (rayon * Math.cos(progress)));
        float ypos = (float) (center.y + (rayon * Math.sin(progress)));
        PointF initialPos = new PointF(xpos, ypos);

        for (int postion = 0; postion < pathLength; postion++) {
            progress += progressLeap;
            float newXpos = (float) (center.x + (rayon * Math.cos(progress)));
            float newYpos = (float) (center.y + (rayon * Math.sin(progress)));

            result[postion] = new PointF(newXpos - xpos , newYpos - ypos );
            xpos = newXpos;
            ypos = newYpos;
        }
        return new Pair<>(initialPos, result);
    }

    public static Pair<PointF, PointF[]> generateSpiralePath(int pathLength, PointF center, int rayon, int startingPositionDegres, int iteration) {
        PointF[] result = new PointF[pathLength];
        double progress = (Math.PI*2) * (startingPositionDegres/360);
        double progressLeap = (Math.PI*2) / (pathLength/(float)iteration);

        float xpos = (float) (center.x + (rayon * Math.cos(progress)));
        float ypos = (float) (center.y + (rayon * Math.sin(progress)));
        PointF initialPos = new PointF(xpos, ypos);

        for (int postion = 0; postion < pathLength; postion++) {
            progress += progressLeap;
            float newXpos = (float) (center.x + ((rayon*(1-(postion/(float)pathLength))) * Math.cos(progress)));
            float newYpos = (float) (center.y + ((rayon*(1-(postion/(float)pathLength)))* Math.sin(progress)));

            result[postion] = new PointF(newXpos - xpos , newYpos - ypos );
            xpos = newXpos;
            ypos = newYpos;
        }
        return new Pair<>(initialPos, result);
    }



    /**
     * generate a succession of line/curve/(geometric figure) as a path
     *
     * @param pathLength         the resulting path lenght
     * @param variationSpeedXMax how much subPaths can vary horizontaly (from -variationXMax to +variationXMax)
     * @param variationSpeedYMax how much subpaths can vary verticaly (from -variationYMax to +variationYMax)
     * @param xCurveMax          how much a curved subPath can be horizontaly curved (from -xCurveMax to +xCurveMax)
     * @param yCurveMax          how much a curved subPath can be verticamy curved (from -yCurveMax to +yCurveMax)
     * @param subPathLengthMax   the max size a of each subPath
     * @return
     */
    public static PointF[] generateRandomPath(int pathLength, int variationSpeedXMax, int variationSpeedYMax, int xCurveMax, int yCurveMax, int subPathLengthMax) {
        ArrayList<PointF> returnPath = new ArrayList<PointF>();
        int filledPath = 0;
        while (filledPath < pathLength) {

            int currentSubPathLenght = (int) (Math.random() * subPathLengthMax) + 1;
            if (filledPath + currentSubPathLenght > pathLength) {
                currentSubPathLenght = pathLength - filledPath;
            }

            // -0.49 pour avoir une valeur random entre -0.49 et 0.50
            int directionX = (int) ((Math.random() - 0.49) * variationSpeedXMax * 2);
            int directionY = (int) ((Math.random() - 0.49) * variationSpeedYMax * 2);
            //  choix entre chemin line ou courbe (ou rien ?)
            switch ((int) (Math.random() * 2)) {
                case 0:
                    returnPath.addAll(Arrays.asList(generateLinePath(currentSubPathLenght, directionX, directionY)));
                    break;
                case 1:
                    int curveX = (int) ((Math.random() - 0.49) * xCurveMax * 2);
                    int curveY = (int) ((Math.random() - 0.49) * yCurveMax * 2);
                    returnPath.addAll(Arrays.asList(generateCurvedPath(currentSubPathLenght, directionX, directionY, curveX, curveY)));
                    break;
                default:
                    break;
            }
            filledPath += currentSubPathLenght;
        }
        return returnPath.toArray(new PointF[pathLength]);
    }

    /**
     * make a portion of the path faster
     *
     * @param originPath   path to speed up
     * @param startSpeedUp when the path should begin the speed up
     * @param endSpeedUp   when the path should end the speed up
     * @param factorSpeed  how much faster the portion will be (must be an int > 0)
     * @return a shorter/faster path than the original
     */
    public static PointF[] speedUpPortionOfPath(PointF[] originPath, int startSpeedUp, int endSpeedUp, int factorSpeed) {
        ArrayList<PointF> returnPath = new ArrayList<PointF>();

        for (int i = 0; i < originPath.length; i++) {
            // clone du point originale
            PointF mergedPoint = new PointF(originPath[i].x, originPath[i].y);

            if (i >= startSpeedUp && i < endSpeedUp) {
                // merge de plusieur points en un seul (nb de points lié au facteur vitesse)
                for (int j = 1; (j < factorSpeed && (i + 1) < originPath.length); j++) {
                    i++;
                    mergedPoint.offset(originPath[i].x, originPath[i].y);
                }
            }

            returnPath.add(mergedPoint);
        }
        return returnPath.toArray(new PointF[returnPath.size()]);
    }

    /**
     * make a portion of the path slower
     *
     * @param originPath     path to speed up
     * @param startSpeedDown when the path should begin the speed down
     * @param endSpeedDown   when the path should end the speed down
     * @param slowingFactor  how much faster the portion will be (must be an int > 0)
     * @return a longer/slower path than the original
     */
    public static PointF[] speedDownPortionOfPath(PointF[] originPath, int startSpeedDown, int endSpeedDown, int slowingFactor) {
        ArrayList<PointF> returnPath = new ArrayList<PointF>();

        for (int i = 0; i < originPath.length; i++) {
            // clone du point originale
            PointF mergedPoint = new PointF(originPath[i].x, originPath[i].y);

            if (i >= startSpeedDown && i < endSpeedDown) {
                // division d'un point en plusieur(nb de points lié au facteur vitesse)
                for (int j = 0; j < slowingFactor; j++) {
                    mergedPoint = new PointF((originPath[i].x / slowingFactor), (originPath[i].y / slowingFactor));
                    returnPath.add(mergedPoint);
                }
            } else {
                returnPath.add(mergedPoint);
            }
        }
        return returnPath.toArray(new PointF[returnPath.size()]);
    }

    /**
     * merge multiple path on a single one
     * !2 loop depth!
     *
     * @param pathsToMerge
     * @return
     */
    public static PointF[] mergePaths(PointF[]... pathsToMerge) {
        ArrayList<PointF> returnPath = new ArrayList<PointF>();
        for (PointF[] singlePath : pathsToMerge) {
            for (PointF point : singlePath) {
                returnPath.add(point);
            }
        }
        return returnPath.toArray(new PointF[returnPath.size()]);

    }

    /**
     * make average from 3 consecutive point(previous, current,next) for each point
     *
     * @param roughtPath
     * @return
     */
    public static PointF[] softenPath(PointF[] roughtPath) {
        PointF[] result = new PointF[roughtPath.length];

        PointF previousPoint;
        PointF currentPoint;
        PointF nextPoint;

        for (int i = 1; i < (roughtPath.length - 1); i++) {
            previousPoint = roughtPath[i - 1];
            currentPoint = roughtPath[i];
            nextPoint = roughtPath[i + 1];
            float smoothX = (previousPoint.x + currentPoint.x + nextPoint.x) / 3f;
            float smoothY = (previousPoint.y + currentPoint.y + nextPoint.y) / 3f;
            result[i] = new PointF(smoothX, smoothY);
        }
        result[0] = roughtPath[0];
        result[roughtPath.length - 1] = roughtPath[roughtPath.length - 1];
        return result;
    }
}
