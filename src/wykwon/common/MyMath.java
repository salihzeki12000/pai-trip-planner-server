package wykwon.common;

import org.math.array.DoubleArray;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: wykwon
 * Date: 15. 4. 20
 * Time: 오후 2:56
 * To change this template use File | Settings | File Templates.
 */
public class MyMath {
    public static double linearInterpolate(double x1, double y1, double x2, double y2, double x) {
        return y2 - (y2 - y1) * (x2 - x) / (x2 - x1);
    }


    public static double[] derivatives(double array[], double timeStep) {
        double der[] = new double[array.length - 1];
        for (int i = 0; i < der.length; i++) {
            der[i] = (array[i + 1] - array[i]) / timeStep;
        }
        return der;
    }

    public static double[] derivatives(double array[]) {
        double der[] = new double[array.length - 1];
        for (int i = 0; i < der.length; i++) {
            der[i] = (array[i + 1] - array[i]);
        }
        return der;
    }

    public static double[][] derivatives(double matrix[][], double timeStep) {
        int colSize = matrix[0].length;
        double der[][] = new double[matrix.length - 1][colSize];
        for (int c = 0; c < colSize; c++) {
            for (int r = 0; r < der.length; r++) {
                der[r][c] = (matrix[r + 1][c] - matrix[r][c]) / timeStep;
            }
        }
        return der;

    }

    public static double[][] derivatives(double matrix[][]) {
        int colSize = matrix[0].length;
        double der[][] = new double[matrix.length - 1][colSize];
        for (int c = 0; c < colSize; c++) {
            for (int r = 0; r < der.length; r++) {
                der[r][c] = (matrix[r + 1][c] - matrix[r][c]);
            }
        }
        return der;

    }

    public static void main(String[] args) {
        System.out.println("MyMath.main");
        System.out.println(linearInterpolate(1, 1, 3, 2, 2));
        double x[] = {1, 2, 2, 3, 1, 2, 2};
        System.out.println(Arrays.toString(derivatives(x, 1)));
        double xy[][] = {{1, 1}, {2, 2}, {2, 2}, {3, 3}, {1, 1}, {2, 2}, {2, 2}};
        System.out.println(DoubleArray.toString(derivatives(xy, 1)));
    }
}

