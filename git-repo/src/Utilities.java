import java.awt.geom.Point2D;

public class Utilities {
    
    final static double pi = 3.1415626535;
    public static double calculateTriangleArea(Point2D p1, Point2D p2, Point2D p3) {
        return 0.5 * Math.abs((p1.getX() * (p2.getY() - p3.getY()) +
                p2.getX() * (p3.getY() - p1.getY()) +
                p3.getX() * (p1.getY() - p2.getY())));
    }

    public static double calculateDistanceToLine(Point2D p, Point2D start, Point2D end) {
        double pToStart = p.distance(start);
        double pToEnd = p.distance(end);
        double startToEnd = start.distance(end);

        // Find area using Heron's formula
        double s = (pToStart + pToEnd + startToEnd) / 2;
        double area = Math.sqrt(s * (s - pToStart) * (s - pToEnd) * (s - startToEnd));

        return (2 * area) / startToEnd;
    }

    public static double getCircumradius(Point2D p1, Point2D p2, Point2D p3) {
        double lengthA = p1.distance(p2);
        double lengthB = p1.distance(p3);
        double lengthC = p2.distance(p3);

        // Find area using Heron's formula
        double s = (lengthA + lengthB + lengthC) / 2;
        double area = Math.sqrt(s * (s - lengthA) * (s - lengthB) * (s - lengthC));

        return (lengthA * lengthB * lengthC) / (4 * area);
    }

    public static double getAngle(Point2D vertex, Point2D before, Point2D after){

        //vector 1
        double vec1X = before.getX() - vertex.getX();
        double vec1Y = before.getY() - vertex.getY();
        //vector 2
        double vec2X = after.getX() - vertex.getX();
        double vec2Y = after.getY() - vertex.getY();

        //Dot product
        double dotProduct = vec1X*vec2X + vec1Y*vec2Y;

        //Magnitude of vectors
        double mag1 = Math.sqrt(vec1X*vec1X + vec1Y*vec1Y);
        double mag2 = Math.sqrt(vec2X*vec2X + vec2Y*vec2Y);

        double angleInRadians = Math.acos(dotProduct / (mag1*mag2));

        return angleInRadians;
    }
}
