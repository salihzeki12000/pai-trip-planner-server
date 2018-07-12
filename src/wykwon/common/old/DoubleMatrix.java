package wykwon.common.old;

/**
 * DoubleMatrix 
 *
 * @author wykwon
 *
 * @created 2012-08-01
 *
 * @modified 2012-10-31
 *
 * add function 
 * 	int getRowDimension();
 *  int getColumnDimension();
 */

import Jama.Matrix;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class DoubleMatrix {
    private static Logger logger = Logger.getLogger(DoubleMatrix.class);
    protected double array[][];
    protected int rowSize;
    protected int colSize;

    public DoubleMatrix(double array[][]) {
        colSize = array[0].length;
        rowSize = array.length;
        this.array = array.clone();
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            this.array[rowIdx] = array[rowIdx].clone();
        }
    }

    public DoubleMatrix(Matrix matrix) {
        double argArray[][] = matrix.getArray();
        colSize = argArray[0].length;
        rowSize = argArray.length;
        this.array = argArray.clone();
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            this.array[rowIdx] = argArray[rowIdx].clone();
        }
    }

    public Matrix getJamaMatrix() {
        return new Matrix(array);
    }

    public DoubleMatrix(int rowSize, int colSize) {
        this.rowSize = rowSize;
        this.colSize = colSize;
        array = new double[rowSize][colSize];
    }

    public double[][] getArray() {
        return array;
    }

    public int getRowDimension() {
        return rowSize;
    }

    public int getColumnDimension() {
        return colSize;
    }

    public void setData(int rowIdx, int colIdx, double value) {
        array[rowIdx][colIdx] = value;
    }

    public void addRowVector(int rowIdx, double... rowVector) {
        if (rowVector.length != colSize) {
            throw new RuntimeException(" column size mismatch");
        }
        array[rowIdx] = rowVector.clone();
    }

    public double[][] referArray() {
        return array;
    }

    public void addColVector(int colIdx, double... colVector) {
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            array[rowIdx][colIdx] = colVector[rowIdx];
        }
    }

    public double getData(int rowIdx, int colIdx) {
        return array[rowIdx][colIdx];
    }

    public double[] getRowVector(int rowIdx) {
        return array[rowIdx].clone();
    }

    public double[] getColVector(int colIdx) {
        double colVector[] = new double[colSize];
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            colVector[rowIdx] = array[rowIdx][colIdx];
        }
        return colVector;
    }

    public String toString() {
        NumberFormat nf = new DecimalFormat("###.###");
        StringBuffer strBuf = new StringBuffer();
        for (int rowIdx = 0; rowIdx < rowSize; rowIdx++) {
            for (int colIdx = 0; colIdx < colSize; colIdx++) {
                strBuf.append(nf.format(array[rowIdx][colIdx]));
                if (colIdx < colSize - 1) {
                    strBuf.append('\t');
                }
            }
            if (rowIdx < rowSize - 1) {
                strBuf.append('\n');
            }
        }
        return strBuf.toString();
    }

    public void writeFile(String filename) {

        try {
            FileWriter fw = new FileWriter(filename);
            fw.append(this.toString());
            fw.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        DoubleMatrix matrix = new DoubleMatrix(3, 4);
        System.out.println(matrix);
        matrix.writeFile("out.txt");
        // String header[] = { "A", "B", "C" };
        // DoubleMatrix matrix = new DoubleMatrix(header);
        // // System.out.println(matrix);
        //
        // Double row2[] = { 1.1, 2.2, 3.3 };
        // Double row3[] = { 2.1, 3.2, 4.3 };
        // Double row4[] = { 5.1, 6.2, 7.3 };
        //
        // int rowIndices[] = { 2, 0 };
        // int colIndices[] = { 2, 0 };
        // // matrix.addRowVector(header);
        // matrix.addRowVector(row2);
        // matrix.addRowVector(row3);
        // matrix.addRowVector(row4);
        // System.out.println(matrix);
        // // System.out.println(matrix.subMatrix(1, 2, 1, 2));
        // System.out.println(matrix.subMatrixByRow(rowIndices));
        // logger.info("");
        // System.out.println(matrix.subMatrixByColumn(colIndices));
    }

}
