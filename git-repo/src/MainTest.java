import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.rules.Timeout;

import java.awt.geom.Point2D;

// Test Class!
public class MainTest {
    @Rule
    // Global variables.
    public Timeout globalTimeout = Timeout.seconds(60);
    public int example;

    @Before
    public void setUp() {
        // If something "global" is needed, ex, set up parameters or something
        example = 1;
    }

    /**
     * Example test
     * all test should be named:
     * testSomethingThatDescribesTheTest()
     */

    @Test
    public void testLIC0() {
        Point2D[] points1 = {
                new Point2D.Double(0, 0),
                new Point2D.Double(0, 1),
                new Point2D.Double(0, 5)};

        Point2D[] points2 = {new Point2D.Double(0, 0)};

        Point2D[] points3 = {
                new Point2D.Double(0, 0),
                new Point2D.Double(0, 1),
                new Point2D.Double(0, 2),
                new Point2D.Double(0, 10),
                new Point2D.Double(15, 1),
                new Point2D.Double(100, 100)};

        // Positive tests
        assertFalse(LICs.lic0holds(points1, 4));    // False as there are no two consecutive points more than 4 away from each other (larger is exactly 4)
        assertTrue(LICs.lic0holds(points1, 3));     // True as with length 3 both pair of points in points1 could match
        assertTrue(LICs.lic0holds(points3, 60));    // True, the last two points in points3 match

        // Negative tests
        assertThrows(IllegalArgumentException.class, () -> { // The function should not accept less than two points
            LICs.lic0holds(points2, 1000);
        });
        assertThrows(IllegalArgumentException.class, () -> { // The function should not accept a negative length
            LICs.lic0holds(points1, -19);
        });
    }
  
    @Test
    public void testLIC1(){
        // Positive test, the points are not contained by the circle of the given radius
        Point2D[] points = {new Point2D.Double(0,1), new Point2D.Double(1,0), new Point2D.Double(1,1)};
        assertTrue(LICs.lic1holds(points,0.5));
        // Negative test, the points are contained by the circle of the given radius
        assertFalse(LICs.lic1holds(points,1));
        // Params check, radius1 < 0
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic2holds(points, -1);
        });
    }

    @Test
    public void testLIC2() {
        Point2D[] points1 = {new Point2D.Double(0, 0),
                            new Point2D.Double(1, 1),
                            new Point2D.Double(0, 5)};

        Point2D[] points3 = {new Point2D.Double(0, 1),
                new Point2D.Double(0, 1),
                new Point2D.Double(0, 1),
                new Point2D.Double(100, 100)};
        
        // Positive test   
        assertTrue(LICs.lic2holds(points1, 1)); // Return true because angle is 2.11, satisfies the condition angle < pi - 1
        assertFalse(LICs.lic2holds(points3, 2)); // Return false because there are no 3 unique points in row
        assertFalse(LICs.lic2holds(points1, 2)); // Return false because angle is 2.11, its bigger than pi - 2 and smaller than pi + 2
        assertFalse(LICs.lic2holds(points1, 3.1)); // Return false because angle is 2.11, its bigger than pi - 3.1 and smaller than pi + 3.1

        // Negative tests
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic2holds(points3, 1000); // Epsilon bigger than pi, invalid input
        });
    }

    @Test
    public void testLIC3() {
        /*
         * This test builts up a triangle with the points (0,0), (0,2), (2,0) and
         * results in an area of 2.
          
         * The first test checks if the area is less than the given area, which it is, so the
         * test should return true.
         
         * The second test also checks if the area is less than the given area, which it is
         * not, so the test should return false.
         
         * The last test also checks if the given area is negative, which it is, so the test
         * should throw an IllegalArgumentException.
         */
        Point2D[] points1 = {
            new Point2D.Double(0, 0),
            new Point2D.Double(0, 2),
            new Point2D.Double(2, 0)
        };

        double area1 = 1;
        double area2 = 3;
        
        assertTrue(LICs.lic3holds(points1, area1));
        assertFalse(LICs.lic3holds(points1, area2));
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic3holds(points1, -1);
        });
    }

    @Test
    public void testLIC4() {
        Point2D[] points1 = {new Point2D.Double(0, 0),
                            new Point2D.Double(0, -1),
                            new Point2D.Double(-1, 0)};

        Point2D[] points2 = {new Point2D.Double(0, 0),
                            new Point2D.Double(0, 1),
                            new Point2D.Double(2, 4)};

        // Positive tests
        assertTrue(LICs.lic4holds(points1, 3, 2)); // Return true because there are 3 consecutive points in more than 2 quads, 3 > 2
        assertFalse(LICs.lic4holds(points1, 2, 3)); // Return false because there are no 2 consecutive points in more than 3 quads
        assertFalse(LICs.lic4holds(points2, 2, 2)); // Return false because there are no 2 consecutive points in more than 2 quads

        // Negative test
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic4holds(points2, 1, 2); // q_pts less than 2, invalid input
        });
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic4holds(points1, 2, 4); // quads bigger than 3, invalid input
        });
    }

    @Test
    public void testLIC5() {
        Point2D[] points1 = {new Point2D.Double(0, 0),
                new Point2D.Double(0, 1),
                new Point2D.Double(0, 5)};

        Point2D[] points2 = {new Point2D.Double(0, 0),
                new Point2D.Double(65, 1),
                new Point2D.Double(5, 5)};

        Point2D[] points3 = {new Point2D.Double(1000, 1000)};

        Point2D[] points4 = {new Point2D.Double(0, 0),
                new Point2D.Double(5, 1),
                new Point2D.Double(45, 5)};


        // Positive tests
        assertFalse(LICs.lic5holds(points1)); // False because no two consecutive points have a negative x displacement
        assertFalse(LICs.lic5holds(points4)); // False because no two consecutive points have a negative x displacement
        assertTrue(LICs.lic5holds(points2));  // True because the last two points have a negative x displacement

        // Negative tests
        assertThrows(IllegalArgumentException.class, () -> { // The function should no accept less than two points
            LICs.lic5holds(points3);
        });
    }

    @Test
    public void testLIC6() {
        Point2D[] points = {new Point2D.Double(0,0),
            new Point2D.Double(0,1),
            new Point2D.Double(1,0),
            new Point2D.Double(2,0),
            new Point2D.Double(5,0),
            new Point2D.Double(0,10),
            new Point2D.Double(0,10),
            new Point2D.Double(0,0)};
        
        // Positive Test, one of the points lies a greater distance than dist from the line joining the first and last of the nPoints
        assertTrue(LICs.lic6holds(points, 8, 4, 3));
        // Positve Test, for when start and end are identical
        assertTrue(LICs.lic6holds(points, 8, 8, 3));
        // Negative Test, no point lies further from the line than 100
        assertFalse(LICs.lic6holds(points, 8, 4, 100));
        // Throws exception test, nPoints is larger than numPoints
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic6holds(points, 8, 9, 10);
        });
    }

    @Test
    public void testLIC7() {
        Point2D[] points1 = {new Point2D.Double(0,0),
                new Point2D.Double(0,1),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,0),
                new Point2D.Double(0,10),
                new Point2D.Double(0,0)};

        // Positive tests
        assertTrue(LICs.lic7holds(points1, 3, 8));  // True because the second and the 6th point are more than 8 away and have 3 points in between
        assertTrue(LICs.lic7holds(points1, 4, 8));  // True because the first and the 6th point are more than 8 away and have 4 points in between

        assertFalse(LICs.lic7holds(points1, 3, 9)); // False because no two points should match, the length is too big as the largest distance is exactly 9
        assertFalse(LICs.lic7holds(points1, 5, 8)); // False because no two points should match, the kPoints is too large for point 2 and 6 to match
        assertFalse(LICs.lic7holds(points1, 7, 0)); // With kPoints = 7, it should be false, even with length1 = 0

        Point2D[] points2 = {new Point2D.Double(0,0), new Point2D.Double(1,0)};
        // With two points, it should return false whatever the other parameters are
        assertFalse(LICs.lic7holds(points2, 1, 0.5));
        assertFalse(LICs.lic7holds(points2, 1, -6));
        assertFalse(LICs.lic7holds(points2, 8, 0.5));

        assertThrows(IllegalArgumentException.class, () -> { // The function should not accept null as points
            LICs.lic7holds(null, 1000, 1000);
        });
    }

    @Test
    public void testLIC8() {
        Point2D[] points = {new Point2D.Double(0,1),
            new Point2D.Double(5,5),
            new Point2D.Double(6,1),
            new Point2D.Double(1,0),
            new Point2D.Double(3,4),
            new Point2D.Double(8,1),
            new Point2D.Double(1,1)};
        // Positive Test, there exists a set of 3 data points that cannot be contained by a circle of radius1
        assertTrue(LICs.lic8holds(points, 7, 2, 2,0.5));
        // Negative Test, there does not exists a set of 3 data points that cannot be contained by a circle of radius1
        assertFalse(LICs.lic8holds(points, 7, 2, 2,1));
        // Params Check, aPoints smaller than 1, throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic8holds(points, 7, 0, 2, 0.5);
        });
        // Params Check, bPoints smaller than 1, throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic8holds(points, 7, 2, 0, 0.5);
        });
        // Params Check, aPoints + bPoints > numPoints - 3, throws exception
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic8holds(points, 5, 2, 2, 0.5);
        });
    }


    @Test
    public void testLIC9() {
        Point2D[] points1 = {new Point2D.Double(0, 0),
            new Point2D.Double(0, 1),
            new Point2D.Double(0, 2),
            new Point2D.Double(0, 3),
            new Point2D.Double(1, 4),
            new Point2D.Double(2, 5)};

        Point2D[] points2 = {new Point2D.Double(0, 0),
            new Point2D.Double(0, 1),
            new Point2D.Double(0, 2),
            new Point2D.Double(0, 3),
            new Point2D.Double(1, 4),
            new Point2D.Double(2, 5)};
        
        // Positive tests
        assertTrue(LICs.lic9holds(points1, 1, 1, 0.1)); // Return true because for points (0,0),(0,2),(1,4) the angle is 2.68, satisfies the condition angle < pi - 0.1
        assertFalse(LICs.lic9holds(points1, 1, 1, 3)); // Return false because there both 2.68 and angle 2.36 build by(0,1),(0,3),(2,5) does not satisfy the conditions
        
        // Negative test
        assertThrows(IllegalArgumentException.class, () -> { // c_pts < 1, invalid input 
            LICs.lic9holds(points2, 0, 1, 0.1);
        });
        assertThrows(IllegalArgumentException.class, () -> { // d_pts < 1, invalid input 
            LICs.lic9holds(points1, 1, 0, 0.1);
        });
        assertThrows(IllegalArgumentException.class, () -> { // episilon > pi, invalid input 
            LICs.lic9holds(points1, 1, 0, 1000);
        });
    }

    @Test
    public void testLIC10() {
        /*
         * This test sets up 6 points and also sets the area1 of and the set steps between the points
         * in variables e_pts and f_pts.
         * 
         * First test checks when the area provided by the points given e_pts and f_pts is greater than
         * area1, which it is, so the test should return true.
         * 
         * The second test is almost the same as the first one, but the area1 is greater than the area
         * created by the points, so the test should return false. 
         */
        Point2D[] p1 = {
            new Point2D.Double(0, 0),
            new Point2D.Double(0, 2),
            new Point2D.Double(2, 0),
            new Point2D.Double(0, 4),
            new Point2D.Double(0, 3),
            new Point2D.Double(1, 4),
        };

        double area1 = 1;
        int e_pts = 1;
        int f_pts = 2;


        assertTrue(LICs.lic10holds(p1, area1, e_pts, f_pts));
        assertFalse(LICs.lic10holds(p1, area1 * 5, e_pts, f_pts));
    }

    @Test
    public void testLIC11() {
        Point2D[] points1 = {new Point2D.Double(0, 0),
            new Point2D.Double(0, 1),
            new Point2D.Double(1, 2),
            new Point2D.Double(-1, 3)};

        Point2D[] points2 = {new Point2D.Double(0, 0),
            new Point2D.Double(0, 1),
            new Point2D.Double(1, 2),
            new Point2D.Double(2, 3)};
        
        // Positive tests
        assertTrue(LICs.lic11holds(points1, 1)); // Return true because (-1, 3) and (0, 1) satisfies the condition.
        assertFalse(LICs.lic11holds(points2, 1)); // Return false the x value of the points are increasing, does not satisfies the condition.
        
        // Negative tests
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic11holds(points2, 0); // g_pts can't be less than 1, invalid input
        });
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic11holds(points1, 3); // g_pts can't be more than the number of point minus 2, invalid input
        });
    }

    @Test
    public void testLIC12() {
        Point2D[] points1 = {new Point2D.Double(0,0),
                new Point2D.Double(0,1),
                new Point2D.Double(0,0),
                new Point2D.Double(0,4),
                new Point2D.Double(0,0),
                new Point2D.Double(0,10)};

        // Positive tests
        assertTrue(LICs.lic12holds(points1, 1, 5, 4));  // We make the parameters vary until we cannot find the two matching set of points
        assertTrue(LICs.lic12holds(points1, 2, 5, 4));  // that are respectively more than length1 away and less than length2
        assertTrue(LICs.lic12holds(points1, 3, 5, 4));
        assertFalse(LICs.lic12holds(points1, 4, 5, 4));
        assertFalse(LICs.lic12holds(points1, 5, 3, 10));    // Should be false if the number of points is smaller than kPoints + 2
                                                                                  // It is the case here as we have 6 points , and 5 + 2 = 7
        // Negative test
        assertThrows(IllegalArgumentException.class, () -> { // length2 cannot be negative
            LICs.lic12holds(points1, 5, 3, -3);
        });
    }
  
    @Test
    public void testLIC13() {
        Point2D[] points = {new Point2D.Double(0,1),
            new Point2D.Double(5,5),
            new Point2D.Double(6,1),
            new Point2D.Double(1,0),
            new Point2D.Double(3,4),
            new Point2D.Double(8,1),
            new Point2D.Double(1,1)};
        // Positive Test, LIC8 returns true and there exists a set of 3 data points that can be contained by a circle of radius2
        assertTrue(LICs.lic13holds(points, 7, 2, 2,0.5, 1));
        // Negative Test, LIC8 returns true but there does not exist a set of 3 data points that can be contained by a circle of radius2
        assertFalse(LICs.lic13holds(points, 7, 2, 2,0.5, 0.5));
        // Params Check, radius2 < 0
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic13holds(points, 7, 2, 2, 0.5, -1);
        });
    }

    @Test
    public void testLIC14() {
        /*
         * This test sets up 2 different sets of points and also sets the area1 and area2 of and the set steps
         * between the points with e_pts and f_pts.
         * The different test then check if the area given by the points is greater than area1 and less than
         * area2, which it is, so the test should return true.
         * 
         * Test 2 checks if the area given it return false if the above requirement is not fulfilled.
         * Test 3 checks if we handle when area is less than 0.
         * Test 4 checks if we handle when the number of points is less than 5.
         */
        Point2D[] points1 = {
                new Point2D.Double(0, 0),
                new Point2D.Double(0, 2),
                new Point2D.Double(2, 0),
                new Point2D.Double(0, 4),
                new Point2D.Double(0, 3),
                new Point2D.Double(1, 4),
        };

        Point2D[] points3 = {
                new Point2D.Double(0, 0),
                new Point2D.Double(0, 2),
                new Point2D.Double(2, 0),
                new Point2D.Double(0, 4),
        };

        int e_pts = 1;
        int f_pts = 2;
        double area1 = 3.9;
        double area2 = 4.1;

        assertTrue(LICs.lic14holds(points1, area1, area2, e_pts, f_pts));
        assertFalse(LICs.lic14holds(points1, area1, area1, e_pts, f_pts));
        assertThrows(IllegalArgumentException.class, () -> {
            LICs.lic14holds(points1, area1, area1 - area2, e_pts, f_pts);
        });
        assertFalse(LICs.lic14holds(points3, area1, area2, e_pts, f_pts));
    }
}
