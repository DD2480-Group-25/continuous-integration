import java.awt.geom.Point2D;
import java.util.stream.IntStream;

public class LICs {

    // LICs check
    public static boolean lic0holds(Point2D[] points, double length1) {
        if (!(0 <= length1 && points.length >= 2)) {
            throw new IllegalArgumentException("At least 2 points are required, and length should be positive or zero.");
        }

        double length1Squared = length1 * length1;
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].distanceSq(points[i+1]) > length1Squared) {
                return true;
            }
        }
        return false;
    }

    public static boolean lic1holds(Point2D[] points, double radius1) {
        if (radius1 < 0) {
            throw new IllegalArgumentException("Radius1 must be at least 0.");
        }

        Point2D p1, p2, p3;
        double[] angles = new double[3];
        for (int i = 0; i < points.length - 2; i++) {

            p1 = points[i];
            p2 = points[i + 1];
            p3 = points[i + 2];

            angles[0] = Utilities.getAngle(p1, p2, p3);
            angles[1] = Utilities.getAngle(p2, p1, p3);
            angles[2] = Utilities.getAngle(p3, p2, p1);

            double radius1SquaredEquivalent = radius1 * radius1 * 4;
            for (int j = 0; j < 3; j++) {
                if (angles[j] > Utilities.pi) {
                    angles[j] = 2 * Utilities.pi - angles[j];
                }

                if (angles[j] > Utilities.pi / 2) {

                    if (j == 0) { // Angle when p1 is vertex
                        if (p2.distanceSq(p3) > radius1SquaredEquivalent) {
                            return true;
                        }
                    } else if (j == 1) { // Angle when p2 is vertex
                        if (p1.distanceSq(p3) > radius1SquaredEquivalent) {
                            return true;
                        }
                    } else { // Angle when p3 is vertex
                        if (p1.distanceSq(p2) > radius1SquaredEquivalent) {
                            return true;
                        }
                    }
                }
            }

            if (Utilities.getCircumradius(p1, p2, p3) > radius1) {
                return true;
            }

        }
        return false;
    }

    public static boolean lic2holds(Point2D[] points, double epsilon) {
        if (epsilon < 0 || epsilon >= Utilities.pi) {
            throw new IllegalArgumentException("At least three points are required, and 0 <= epsilon < pi");
        }
        if(points.length < 3){
            return false;
        }
        for (int i = 1; i <(points.length - 1); i++) {
            if (!(points[i].equals(points[i-1])) && !(points[i].equals(points[i+1]))) {
                double angle = Utilities.getAngle(points[i], points[i-1], points[i+1]);
                if (angle > (Utilities.pi + epsilon) || angle < (Utilities.pi - epsilon)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean lic3holds(Point2D[] points, double area1) {
        if (points == null || points.length < 3 || area1 < 0) {
            throw new IllegalArgumentException("At least three points are required.");
        }

        Point2D p1, p2, p3;

        for (int i = 0; i < points.length - 2; i++) {
            p1 = points[i];
            p2 = points[i + 1];
            p3 = points[i + 2];

            if (Utilities.calculateTriangleArea(p1, p2, p3) > area1) {
                return true;
            }
        }

        return false;
    }

    public static boolean lic4holds(Point2D[] points, int qPoints, int quads) {
        if (qPoints < 2 || qPoints > points.length || quads < 1 || quads > 3) {
            throw new IllegalArgumentException("q_pts must be equal to or greater than 2, equal to or smaller than the number of points. quad must be equal to or greater than 1, equal to or greater than 3");
        }

        int[] quadCount = new int[4];

        for (int i = 0; i <= (points.length - qPoints); i++) {

            for (int j = 0; j < 4; j++){
                quadCount[j] = 0;
            }

            for (int j = 0; j < qPoints; j++) {
                double x = points[i + j].getX();
                double y = points[i + j].getY();

                if (x >= 0 && y >= 0) {
                    quadCount[0] = 1; // Quadrant I
                } else if (x < 0 && y >= 0) {
                    quadCount[1] = 1; // Quadrant II
                } else if (x >= 0 && y < 0) {
                    quadCount[2] = 1; // Quadrant III
                } else {
                    quadCount[3] = 1; // Quadrant IV
                }
            }

            if (IntStream.of(quadCount).sum() > quads) {
                return true;
            }
        }

        return false;
    }

    public static boolean lic5holds(Point2D[] points) {
        if (points == null || !(points.length >= 2)) {
            throw new IllegalArgumentException("At least 2 points are required.");
        }

        for (int i = 0; i < points.length - 1; i++) {
            if (points[i + 1].getX() - points[i].getX() < 0) {
                return true;
            }
        }
        return false;
    }

    public static boolean lic6holds(Point2D[] points, int numPoints, int nPoints, double dist) {
        if (nPoints < 3 || numPoints < nPoints) {
            throw new IllegalArgumentException("nPoints must be at least 3 but smaller than or equal to numPoints");
        }

        if (dist < 0) {
            throw new IllegalArgumentException("dist must be at least 0 ");
        }

        if (numPoints < 3) {
            return false;
        }
        Point2D pStart, pEnd;

        double distSquared = dist * dist;
        for (int i = 0; i < points.length - (nPoints - 1); i++) {
            pStart = points[i];
            pEnd = points[i + (nPoints - 1)];

            // If first and last points are identical, check distance between point and all
            // other points
            if (pStart.equals(pEnd)) {
                for (int j = i + 1; j < i + nPoints; j++) {
                    if (points[j].distanceSq(pStart) > distSquared) {
                        return true;
                    }
                }
                // Otherwise, compare distance from point to line that crosses start and end
                // point
            } else {
                for (int j = i + 1; j < i + nPoints; j++) {
                    if (Utilities.calculateDistanceToLine(points[j], pStart, pEnd) > dist) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    public static boolean lic7holds(Point2D[] points, int kPoints, double length1) {
        if (points == null) {
            throw new IllegalArgumentException("points is null.");
        }

        if (points.length < Math.max(3, (kPoints + 2))) {
            return false;
        }

        if (!(1 <= kPoints && kPoints <= points.length - 2) || !(length1 >= 0)) {
            throw new IllegalArgumentException("kPoints is invalid.");
        }

        double length1Squared = length1 * length1;
        for (int i = 0; i < points.length - kPoints - 1; i++) {
            if (points[i].distanceSq(points[i + kPoints + 1]) > length1Squared) {
                return true;
            }
        }

        return false;
    }

    public static boolean lic8holds(Point2D[] points, int numPoints, int aPoints, int bPoints, double radius1) {

        if (1 > aPoints) {
            throw new IllegalArgumentException("aPoints must be at least 1.");
        }

        if (1 > bPoints) {
            throw new IllegalArgumentException("bPoints must be at least 1.");
        }

        if (aPoints + bPoints > numPoints - 3) {
            throw new IllegalArgumentException("Sum of aPoints and bPoints must be smaller than numPoints - 3.");
        }

        if (numPoints < 5) {
            return false;
        }
        Point2D p1, p2, p3;
        double[] angles = new double[3];
        for (int i = 0; i < points.length - (2 + aPoints + bPoints); i++) {

            p1 = points[i];
            p2 = points[i + aPoints + 1];
            p3 = points[i + aPoints + bPoints + 2];

            angles[0] = Utilities.getAngle(p1, p2, p3);
            angles[1] = Utilities.getAngle(p2, p1, p3);
            angles[2] = Utilities.getAngle(p3, p2, p1);

            double radius1SquaredEquivalent = radius1 * radius1 * 4;
            for (int j = 0; j < 3; j++) {
                if (angles[j] > Utilities.pi) {
                    angles[j] = 2 * Utilities.pi - angles[j];
                }

                if (angles[j] > Utilities.pi / 2) {

                    if (j == 0) { // Angle when p1 is vertex
                        if (p2.distanceSq(p3) > radius1SquaredEquivalent) {
                            return true;
                        }
                    } else if (j == 1) { // Angle when p2 is vertex
                        if (p1.distanceSq(p3) > radius1SquaredEquivalent) {
                            return true;
                        }
                    } else { // Angle when p3 is vertex
                        if (p1.distanceSq(p2) > radius1SquaredEquivalent) {
                            return true;
                        }
                    }
                }
            }

            if (Utilities.getCircumradius(p1, p2, p3) > radius1) {
                return true;
            }

        }
        return false;
    }

    public static boolean lic9holds(Point2D[] points, int cPoints, int dPoints, double epsilon) {
        if (1 > cPoints || 1 > dPoints || (cPoints + dPoints) > (points.length-3)) {
            throw new IllegalArgumentException("At least 5 points are required, c_pts and d_pts must be eqaul to or great than zero, their sum must be equal to or smaller than the number or point minus 3");
        }

        if (points.length < 5) {
            return false;
        }

        int totalset = cPoints + dPoints + 2;
        for (int i = 0; i < points.length - totalset; i++) {
            if (!(points[i + cPoints + 1].equals(points[i]) && points[i + cPoints + dPoints + 2].equals(points[i]))) {
                double angleR = Math.abs(Utilities.getAngle(points[i + cPoints + 1], points[i], points[i + cPoints + dPoints + 2]));
                if(angleR <(Utilities.pi - epsilon) || angleR > (Utilities.pi + epsilon)){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean lic10holds(Point2D[] points, double area1, int ePoints, int fPoints) {
        if (points.length < 5 || ePoints < 1 || fPoints < 1 || ePoints + fPoints > points.length - 3) {
            return false;
        }

        Point2D p1;
        Point2D p2;
        Point2D p3;

        for (int i = 0; i < points.length - ePoints - fPoints - 2; i++) {
            p1 = points[i];
            p2 = points[i + ePoints + 1];
            p3 = points[i + ePoints + fPoints + 2];

            if (Utilities.calculateTriangleArea(p1, p2, p3) > area1) {
                return true;
            }
        }

        return false;
    }

    public static boolean lic11holds(Point2D[] points, int gPoints) {
        if (1 > gPoints || (points.length - 2) < gPoints) {
            throw new IllegalArgumentException("At least 3 points are required, g_pts must be equal to or smaller than the number or point minus 2\"");
        }

        for (int i = 0; i < points.length - gPoints - 1; i++) {
            int j = i + gPoints + 1;
            if (points[i].getX() > points[j].getX()) {
                return true;
            }
        }

        return false;
    }

    public static boolean lic12holds(Point2D[] points, int kPoints, double length1, double length2) {
        if (points == null) {
            throw new IllegalArgumentException("points must not be null.");
        }

        if (!(length2 >= 0)) {
            throw new IllegalArgumentException("length1 and length2 must be positive.");
        }

        if (points.length < Math.max(3, (kPoints + 2))) {
            return false;
        }

        double length1Squared = length1 * length1;
        for (int i = 0; i < points.length - kPoints - 1; i++) {
            if (points[i].distanceSq(points[i + kPoints + 1]) > length1Squared) {
                for (int j = 0; j < points.length - kPoints - 1; j++) {
                    if (points[j].distanceSq(points[j + kPoints + 1]) < length1Squared) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }

    public static boolean lic13holds(Point2D[] points, int numPoints, int aPoints, int bPoints, double radius1,
                                     double radius2) {
        if (!lic8holds(points, numPoints, aPoints, bPoints, radius1)) {
            return false;
        }
        if (radius2 < 0) {
            throw new IllegalArgumentException("Radius2 must be at least 0.");
        }
        Point2D p1, p2, p3;
        double[] angles = new double[3];
        for (int i = 0; i < points.length - (2 + aPoints + bPoints); i++) {

            p1 = points[i];
            p2 = points[i + aPoints + 1];
            p3 = points[i + aPoints + bPoints + 2];

            angles[0] = Utilities.getAngle(p1, p2, p3);
            angles[1] = Utilities.getAngle(p2, p1, p3);
            angles[2] = Utilities.getAngle(p3, p2, p1);

            double radius2SquaredEquivalent = radius2 * radius2 * 4;
            for (int j = 0; j < 3; j++) {
                if (angles[j] > Utilities.pi) {
                    angles[j] = 2 * Utilities.pi - angles[j];
                }

                if (angles[j] > Utilities.pi / 2) {

                    if (j == 0) { // Angle when p1 is vertex
                        if (p2.distanceSq(p3) <= radius2SquaredEquivalent) {
                            return true;
                        }
                    } else if (j == 1) { // Angle when p2 is vertex
                        if (p1.distanceSq(p3) <= radius2SquaredEquivalent) {
                            return true;
                        }
                    } else { // Angle when p3 is vertex
                        if (p1.distanceSq(p2) <= radius2SquaredEquivalent) {
                            return true;
                        }
                    }
                }
            }

            if (Utilities.getCircumradius(p1, p2, p3) <= radius2) {
                return true;
            }

        }
        return false;
    }

    public static boolean lic14holds(Point2D[] points, double area1, double area2, int ePts, int fPts) {
        if (area2 < 0 || ePts < 1 || fPts < 1) {
            throw new IllegalArgumentException("Invalid input parameters.");
        }

        if (points.length < 5) {
            return false;
        }

        Point2D p1;
        Point2D p2;
        Point2D p3;
        Point2D p4;
        Point2D p5;
        Point2D p6;

        // In order to get correct steps between the points.
        ePts++;
        fPts++;

        for (int i = 0; i < points.length - ePts - fPts; i++) {
            p1 = points[i];
            p2 = points[i + ePts];
            p3 = points[i + ePts + fPts];

            if (Utilities.calculateTriangleArea(p1, p2, p3) > area1) {
                for (int j = 0; j < points.length - ePts - fPts; j++) {
                    p4 = points[j];
                    p5 = points[j + ePts];
                    p6 = points[j + ePts + fPts];

                    if (Utilities.calculateTriangleArea(p4, p5, p6) < area2) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
