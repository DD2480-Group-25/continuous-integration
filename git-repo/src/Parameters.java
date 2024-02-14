public class Parameters {
    private final double length1; //length in LICs 0, 7, 12
    private final double radius1; //radius in LICs 1, 8, 13
    private final double epsilon; //deviation from PI in LICs 2, 9
    private final double area1; //area in LICs 3, 10, 14
    private final int q_pts; //no. of consecutive points in LIC 4
    private final int quads; //no. of quadrants in LIC 4
    private final double dist; //distance in LIC 6
    private final int n_pts; //no. of consecutive pts. in LIC 6
    private final int k_pts; //no. of int. pts. in LICs 7, 12
    private final int a_pts; //no. of int. pts. in LICs 8, 13
    private final int b_pts; //no. of int. pts. in LICs 8, 13
    private final int c_pts; //no. of int. pts. in LIC 9
    private final int d_pts; //no. of int. pts. in LIC 9
    private final int e_pts; //no. of int. pts. in LICs 10, 14
    private final int f_pts; //no. of int. pts. in LICs 10, 14
    private final int g_pts; //no. of int. pts. in LIC 11

    private final double length2; //maximum length in LIC 12
    private final double radius2; //maximum radius in LIC 13
    private final double area2; //maximum area in LIC 14

    public Parameters(double length1, double radius1, double epsilon, double area1,  int q_pts, int quads, double dist,
                      int n_pts, int k_pts, int a_pts, int b_pts, int c_pts, int d_pts, int e_pts, int f_pts, int g_pts,
                      double length2, double radius2, double area2) {
        this.length1 = length1;
        this.radius1 = radius1;
        this.epsilon = epsilon;
        this.area1 = area1;
        this.q_pts = q_pts;
        this.quads = quads;
        this.dist = dist;
        this.n_pts = n_pts;
        this.k_pts = k_pts;
        this.a_pts = a_pts;
        this.b_pts = b_pts;
        this.c_pts = c_pts;
        this.d_pts = d_pts;
        this.e_pts = e_pts;
        this.f_pts = f_pts;
        this.g_pts = g_pts;
        this.length2 = length2;
        this.radius2 = radius2;
        this.area2 = area2;
    }

    public Parameters(double[] doubles, int[] ints){
        length1 = doubles[0];
        radius1 = doubles[1];
        epsilon = doubles[2];
        area1 = doubles[3];
        q_pts = ints[0];
        quads = ints[1];
        dist = doubles[4];
        n_pts = ints[2];
        k_pts = ints[3];
        a_pts = ints[4];
        b_pts = ints[5];
        c_pts = ints[6];
        d_pts = ints[7];
        e_pts = ints[8];
        f_pts = ints[9];
        g_pts = ints[10];
        length2 = doubles[5];
        radius2 = doubles[6];
        area2 = doubles[7];



    }

    public double getLength1(){
        return length1;
    }


    public double getRadius1() {
        return radius1;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public double getArea1() {
        return area1;
    }

    public int getQ_pts() {
        return q_pts;
    }

    public int getQuads() {
        return quads;
    }

    public double getDist() {
        return dist;
    }

    public int getN_pts() {
        return n_pts;
    }

    public int getK_pts() {
        return k_pts;
    }

    public int getA_pts() {
        return a_pts;
    }

    public int getB_pts() {
        return b_pts;
    }

    public int getC_pts() {
        return c_pts;
    }

    public int getD_pts() {
        return d_pts;
    }

    public int getE_pts() {
        return e_pts;
    }

    public int getF_pts() {
        return f_pts;
    }

    public int getG_pts() {
        return g_pts;
    }

    public double getLength2() {
        return length2;
    }

    public double getRadius2() {
        return radius2;
    }

    public double getArea2() {
        return area2;
    }
}
