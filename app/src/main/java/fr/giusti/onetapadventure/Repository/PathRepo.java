package fr.giusti.onetapadventure.Repository;

import java.util.ArrayList;
import java.util.Arrays;

import android.graphics.Point;

public class PathRepo {

    // TODO methode stairs ?
    // others ? ameliorer le random ?


    /**
     * @param originalPath the original path
     * @param start        beginning of the new path from the original
     * @param end          end of the new path from the original
     * @param Xalteration  mulpicator for X move
     * @param Yalteration  multiplicator for Y move
     * @return a new altered subPath
     */
    public Point[] generateSubPath(Point[] originalPath, int start, int end, int Xalteration, int Yalteration) {
        if (start < 0) {
            return null;
        }

        Point[] result = (end < originalPath.length) ? new Point[end - start] : new Point[originalPath.length - start];
        for (int i = start; i < end && i < originalPath.length; i++) {
            result[i] = new Point(originalPath[i].x * Xalteration, originalPath[i].y * Yalteration);
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
    public Point[] createScalePath(double definitiveRatio, Point[] unscaledPath) {
        Point[] scaledPath = new Point[unscaledPath.length];
        for (int i = 0; i < unscaledPath.length; i++) {
            scaledPath[i] = new Point((int) (unscaledPath[i].x * definitiveRatio), (int) (unscaledPath[i].y * definitiveRatio));
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
    public Point[] generateLinePath(int pathLength, int xSpeed, int ySpeed) {
        Point[] returnPath = new Point[pathLength];

        for (int i = 0; i < pathLength; i++) {
            returnPath[i] = new Point(xSpeed, ySpeed);
        }

        return returnPath;
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
    public Point[] generateCurvedPath(int pathLength, int xSpeed, int ySpeed, int xCurve, int yCurve) {
        Point[] returnPath = new Point[pathLength];
        int xCurrentCurve = 0;
        int yCurrentCurve = 0;
        for (int i = 0; i < pathLength; i++) {
            xCurrentCurve = (int) (xCurve * (Math.cos(Math.PI * ((double) i / (pathLength - 1)))));
            yCurrentCurve = (int) (yCurve * (Math.cos(Math.PI * ((double) i / (pathLength - 1)))));

            returnPath[i] = new Point(xSpeed + xCurrentCurve, ySpeed + yCurrentCurve);

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
    public Point[] generateLoopedPath(int pathLength, Point secondHalfSpeedCompensation, Point FirstHalfSpeedXY, int xCurve, int yCurve) {
        ArrayList<Point> returnPath = new ArrayList<Point>();

        int pathLength1half = pathLength / 2;
        int pathLength2half = pathLength - (pathLength / 2);

        returnPath.addAll(Arrays.asList(generateCurvedPath(pathLength1half, FirstHalfSpeedXY.x, FirstHalfSpeedXY.y, xCurve, yCurve)));
        returnPath.addAll(Arrays.asList(generateCurvedPath(pathLength2half, (secondHalfSpeedCompensation.x - FirstHalfSpeedXY.x), (secondHalfSpeedCompensation.y - FirstHalfSpeedXY.y), -xCurve, -yCurve)));

        return returnPath.toArray(new Point[pathLength]);
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
    public Point[] generateRandomPath(int pathLength, int variationSpeedXMax, int variationSpeedYMax, int xCurveMax, int yCurveMax, int subPathLengthMax) {
        ArrayList<Point> returnPath = new ArrayList<Point>();
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
        return returnPath.toArray(new Point[pathLength]);
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
    public Point[] speedUpPortionOfPath(Point[] originPath, int startSpeedUp, int endSpeedUp, int factorSpeed) {
        ArrayList<Point> returnPath = new ArrayList<Point>();

        for (int i = 0; i < originPath.length; i++) {
            // clone du point originale
            Point mergedPoint = new Point(originPath[i].x, originPath[i].y);

            if (i >= startSpeedUp && i < endSpeedUp) {
                // merge de plusieur points en un seul (nb de points lié au facteur vitesse)
                for (int j = 1; (j < factorSpeed && (i + 1) < originPath.length); j++) {
                    i++;
                    mergedPoint.offset(originPath[i].x, originPath[i].y);
                }
            }

            returnPath.add(mergedPoint);
        }
        return returnPath.toArray(new Point[returnPath.size()]);
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
    public Point[] speedDownPortionOfPath(Point[] originPath, int startSpeedDown, int endSpeedDown, int slowingFactor) {
        ArrayList<Point> returnPath = new ArrayList<Point>();

        for (int i = 0; i < originPath.length; i++) {
            // clone du point originale
            Point mergedPoint = new Point(originPath[i].x, originPath[i].y);

            if (i >= startSpeedDown && i < endSpeedDown) {
                // division d'un point en plusieur(nb de points lié au facteur vitesse)
                for (int j = 0; j < slowingFactor; j++) {
                    mergedPoint = new Point((originPath[i].x / slowingFactor), (originPath[i].y / slowingFactor));
                    returnPath.add(mergedPoint);
                }
            } else {
                returnPath.add(mergedPoint);
            }
        }
        return returnPath.toArray(new Point[returnPath.size()]);
    }

    /**
     * merge multiple path on a single one
     * !2 loop depth!
     *
     * @param pathsToMerge
     * @return
     */
    public Point[] mergePaths(Point[]... pathsToMerge) {
        ArrayList<Point> returnPath = new ArrayList<Point>();
        for (Point[] singlePath : pathsToMerge) {
            for (Point point : singlePath) {
                returnPath.add(point);
            }
        }
        return returnPath.toArray(new Point[returnPath.size()]);

    }

}
