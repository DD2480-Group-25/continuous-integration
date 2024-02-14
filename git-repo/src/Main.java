import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    final static int VECTOR_SIZE = 15;

    public enum LogicalConnectors {
        ANDD,
        ORR,
        NOTUSED
    }

    // --- Inputs ---
    public static int numPoints;
    public static Point2D[] points;
    public static Parameters p;
    public static LogicalConnectors[][] logConMatrix = new LogicalConnectors[VECTOR_SIZE][VECTOR_SIZE];
    public static boolean[] prelimUnlockVector = new boolean[VECTOR_SIZE];

    // --- Outputs ---
    public static boolean launch;
    public static boolean[] conditionsMetVector = new boolean[VECTOR_SIZE];
    public static boolean[][] prelimUnlockMatrix = new boolean[VECTOR_SIZE][VECTOR_SIZE];
    public static boolean[] finalUnlockVector = new boolean[VECTOR_SIZE];

    public static void getInput(String filename) {
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(filename));

            numPoints = Integer.parseInt(br.readLine());

            points = new Point2D[numPoints];
            
            // Putting the points into array
            for (int i = 0; i < numPoints; i++) {
                String[] coordinates = br.readLine().split(" ");
                points[i] = new Point2D.Double(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
            }
            
            //Filling in the parameters, there are 8 doubles and 11 ints
            double[] paramDoubles = new double[8];
            int[] paramInts = new int[11];

            for (int i = 0; i < 8; i++) {
                paramDoubles[i] = Double.parseDouble(br.readLine());
            }

            for (int i = 0; i < 11; i++) {
                paramInts[i] = Integer.parseInt(br.readLine());
            }

            p = new Parameters(paramDoubles, paramInts);
            
            // Putting LCM values into the martix
            for (int i = 0; i < VECTOR_SIZE; i++) {
                String row = br.readLine();
                String[] column = row.split(" ");

                for(int j = 0; j < VECTOR_SIZE; j++) {
                    logConMatrix[i][j] = LogicalConnectors.valueOf(column[j]);
                }
            }

            for (int i = 0; i < VECTOR_SIZE; i++) {
                String value = br.readLine();
                prelimUnlockVector[i] = Boolean.parseBoolean(value);
            }
        } catch(IOException e) {
            System.out.println(e);
            System.exit(-1);
        }
    }

    public static boolean isIthRowAllTrue(int rowIndex, boolean[][] prelimUnlockMatrix) {
        for (int j = 0; j < VECTOR_SIZE; j++) {
            if (j != rowIndex) {
                if (!prelimUnlockMatrix[rowIndex][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean[] getConditionsMetVector(Point2D[] points, Parameters p) {
        return new boolean[]{
                LICs.lic0holds(points, p.getLength1()),
                LICs.lic1holds(points, p.getRadius1()),
                LICs.lic2holds(points, p.getEpsilon()),
                LICs.lic3holds(points, p.getArea1()),
                LICs.lic4holds(points, p.getQ_pts(), p.getQuads()),
                LICs.lic5holds(points),
                LICs.lic6holds(points, points.length, p.getN_pts(), p.getDist()),
                LICs.lic7holds(points, p.getK_pts(), p.getLength1()),
                LICs.lic8holds(points, points.length, p.getA_pts(), p.getB_pts(), p.getRadius1()),
                LICs.lic9holds(points, p.getC_pts(), p.getD_pts(), p.getEpsilon()),
                LICs.lic10holds(points, p.getArea1(), p.getE_pts(), p.getF_pts()),
                LICs.lic11holds(points, p.getG_pts()),
                LICs.lic12holds(points, p.getK_pts(), p.getLength1(), p.getLength2()),
                LICs.lic13holds(points, points.length, p.getA_pts(), p.getB_pts(), p.getRadius1(), p.getRadius2()),
                LICs.lic14holds(points, p.getArea1(), p.getArea2(), p.getE_pts(), p.getF_pts())
        };
    }

    // Combining LCM and CMV to calculate PUM
    // Outerloop: go through row. Innerloop: go through column.
    private static boolean[][] getPrelimUnlockMatrix(LogicalConnectors[][] logConMatrix, boolean[] conditionsMetVector) {
        boolean[][] prelimUnlockMatrix = new boolean[VECTOR_SIZE][VECTOR_SIZE];
        for (int rowId = 0; rowId < VECTOR_SIZE; rowId++) {
            for (int colId = 0; colId < VECTOR_SIZE; colId++) {
                if (logConMatrix[rowId][colId].equals(LogicalConnectors.ANDD)) { // If it's a AND
                    prelimUnlockMatrix[rowId][colId] = conditionsMetVector[rowId] && conditionsMetVector[colId];
                } else if (logConMatrix[rowId][colId].equals(LogicalConnectors.ORR)) { // If it's a OR
                    prelimUnlockMatrix[rowId][colId] = conditionsMetVector[rowId] || conditionsMetVector[colId];
                } else if (logConMatrix[rowId][colId].equals(LogicalConnectors.NOTUSED)) { // If it's a NOTUSED
                    prelimUnlockMatrix[rowId][colId] = true;
                } else {
                    throw new IllegalArgumentException("There is something wrong in the LCM");
                }
            }
        }

        return prelimUnlockMatrix;
    }

    public static boolean[] getfinalUnlockVector(boolean[][] prelimUnlockMatrix, boolean[] prelimUnlockVector) {
        boolean[] result = new boolean[VECTOR_SIZE];

        // Iterating on the PUM matrix rows
        for (int row = 0; row < VECTOR_SIZE; row++) {
            // Thanks to lazy evaluation, isIthRowAllTrue is only called when necessary
            result[row] = (!prelimUnlockVector[row]) || isIthRowAllTrue(row, prelimUnlockMatrix);
        }

        return result;
    }

    private static String test(String testFilePath){
        String result;
        getInput(testFilePath);
        conditionsMetVector = getConditionsMetVector(points, p);
        prelimUnlockMatrix = getPrelimUnlockMatrix(logConMatrix, conditionsMetVector);
        finalUnlockVector = getfinalUnlockVector(prelimUnlockMatrix, prelimUnlockVector);

        launch = true;
        for (int i = 0; i < VECTOR_SIZE; i++) {
            if (!finalUnlockVector[i]) {
                launch = false;
                break;
            }
        }

        if (launch) {
            result = "YES";
        } else {
            result = "NO";
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] testFiles = null;

        System.out.println("How many testfiles would you like to test?");
        try {
            String numOfTestFiles = sc.nextLine();
            testFiles = new String[Integer.parseInt(numOfTestFiles)];
        }catch(Exception e) {
            System.out.println("Invalid input");
            System.exit(-1);
        }
        
        for (int i = 0; i < testFiles.length; i++) {
            System.out.println("Insert the testfile's name on by one");
            String fileName = sc.nextLine();
            testFiles[i] = "testfiles/" + fileName + ".txt";
        }

        String result = "";

        for (int i = 0; i < testFiles.length; i++) {
            result = test(testFiles[i]);
            System.out.println("Result of testfile " + testFiles[i] + " is: " + result);
        }
        sc.close();
    }
}